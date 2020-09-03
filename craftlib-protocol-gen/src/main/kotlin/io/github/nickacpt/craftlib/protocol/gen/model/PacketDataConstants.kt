package io.github.nickacpt.craftlib.protocol.gen.model

import java.util.regex.Pattern

const val getterAccess = "(?:this\\.)?([^.()]+)\\.(?:get)?([^(]+)(?:\\(\\))?"
val getterAccessRegex = Regex(getterAccess)
val getterAccessPattern = Pattern.compile(getterAccess)

const val getterAccessReplacer = "\$1_\$2"
val skipFieldsWithSuffixes = listOf(".length")
val unwantedSuffixes = listOf(".copy()")

val complexGetterAccess = "(?:\\(([^.()]+)\\))\$"
val complexGetterAccessRegex = Regex(complexGetterAccess)
val complexGetterAccessPattern = Pattern.compile(complexGetterAccess)

