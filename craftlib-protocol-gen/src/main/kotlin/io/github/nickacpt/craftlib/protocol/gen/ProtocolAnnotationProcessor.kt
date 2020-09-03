package io.github.nickacpt.craftlib.protocol.gen

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.nickacpt.craftlib.protocol.gen.codegen.KlassGenerator
import io.github.nickacpt.craftlib.protocol.gen.codegen.abstractProtocolType
import io.github.nickacpt.craftlib.protocol.gen.codegen.protocolStateType
import io.github.nickacpt.craftlib.protocol.gen.codegen.protocolVersionType
import io.github.nickacpt.craftlib.protocol.gen.model.*
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


@AutoService(Processor::class)
class ProtocolAnnotationProcessor : AbstractProcessor() {
    private lateinit var env: ProcessingEnvironment

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(MinecraftProtocolAnnotation::class.java.name)
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        env = processingEnv
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val kaptKotlinGeneratedDir = env.options["kapt.kotlin.generated"] ?: return false

        val foundAnnotations = roundEnv.getElementsAnnotatedWith(MinecraftProtocolAnnotation::class.java).map {
            it.getAnnotation(MinecraftProtocolAnnotation::class.java)
        }
        for (it in foundAnnotations) {

            val moshi = Moshi.Builder()
                .add(ProtocolDataAdapter)
                .add(InstructionOperationAdapter)
                .add(InstructionFieldTypeAdapter)
                .add(KotlinJsonAdapterFactory()).build()
            val json: String

            val version = it.version
            try {
                json = javaClass.getResourceAsStream("/$version.json")
                    .readAllBytes().decodeToString()

                env.messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "Loaded data for version $version.json into memory\n"
                )


            } catch (e: Exception) {
                env.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Unable to load version $version data to memory.\nVersion specified is not supported.\n"
                )
                return false
            }


            try {

                val adapter = moshi.adapter(Array<ProtocolData>::class.java)
                val protocolData = adapter.fromJson(json)?.first() ?: throw Throwable("Unable to parse version JSON")

                val packetsToGenerate = protocolData.packets.filter { it.state != ProtocolState.PLAY }
                val protocolGenerated = TypeSpec.objectBuilder("MinecraftProtocolGenerated")

                packetsToGenerate.groupBy { it.state }.forEach { (protocolState, packetList) ->
                    val state = protocolState.name.toLowerCase()

                    val protocolParam = ParameterSpec("protocol", abstractProtocolType)
                    val packetCodeGen = CodeBlock.builder().addStatement(
                        "%L %N.protocol(%S, %L) {", "return",
                        protocolParam,
                        protocolState.actualName.toLowerCase().capitalize(),
                        protocolState.id
                    ).indent()

                    packetList.groupBy { it.direction }.forEach { (sideDirection, packetList) ->
                        val side = sideDirection.asPackage
                        packetCodeGen.addStatement("${sideDirection.name.toLowerCase()} {")
                        packetCodeGen.indent()

                        val generator = KlassGenerator(
                            "dev.zerite.craftlib.protocol.$state.$side",
                            File(kaptKotlinGeneratedDir)
                        )

                        packetList.forEach {
                            val generatedTypeName = generator.generate(it)

                            packetCodeGen.addStatement("%T {", generatedTypeName).indent()
                                .addStatement("%T.MC${version.replace('.', '_')} to %L", protocolVersionType, it.id)
                                .unindent().addStatement("}")

                        }
                        packetCodeGen.unindent().addStatement("}")
                    }
                    packetCodeGen.unindent().addStatement("}")

                    val funSpec = FunSpec.builder(protocolState.actualName)
                        .returns(protocolStateType)
                        .addParameter(protocolParam)
                        .addCode(packetCodeGen.build())
                        .build()
                    protocolGenerated.addFunction(funSpec)
                }

                FileSpec.builder("dev.zerite.craftlib.protocol.version", "MinecraftProtocolGenerated")
                    .addType(protocolGenerated.build()).build().writeTo(File(kaptKotlinGeneratedDir))
                println()

            } catch (e: Exception) {
                env.messager.printMessage(Diagnostic.Kind.ERROR, e.message)
                e.printStackTrace()
                return false
            }

        }
        return true
    }
}