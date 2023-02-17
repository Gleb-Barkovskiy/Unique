package com.kigya.unique.utils.extensions.string

@Suppress("SameReturnValue")
fun String.Companion.empty(): String = ""

fun String.fastReplace(from: String, to: String): String {
    val sb = StringBuilder()
    var i = 0
    while (i < this.length) {
        when {
            this[i] == from[0] -> {
                var j = 0
                while (j < from.length && i + j < this.length) {
                    if (this[i + j] != from[j]) break
                    j++
                }
                if (j == from.length) {
                    sb.append(to)
                    i += j
                } else {
                    sb.append(this[i])
                    i++
                }
            }

            else -> {
                sb.append(this[i])
                i++
            }
        }
    }
    return sb.toString()
}

fun String.findMatchInOrNull(list: List<String>): String? = list.firstOrNull {
    it.lowercase().contains(this.lowercase())
}
