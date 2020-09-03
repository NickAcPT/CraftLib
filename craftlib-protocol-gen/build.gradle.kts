import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("kapt")
}

group = "io.github.nickacpt"
version = "0.1.5-SNAPSHOT"
val protocolVersion = "1.16.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly ("com.google.auto.service:auto-service:1.0-rc7")
    kapt ("com.google.auto.service:auto-service:1.0-rc7")
    implementation("com.squareup.moshi:moshi:1.9.3")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.3")

    implementation("com.squareup:kotlinpoet:1.6.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
}