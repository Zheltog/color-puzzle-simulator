package org.belog.cps

enum class Reaction(val localization: String, val shortName: String) {
    DIRECT("ПРЯМАЯ", "D"),
    MIXED("СМЕШЕНИЕ", "M");

    companion object {
        fun byShortName(shortName: String): Reaction {
            for (entry in entries) {
                if (entry.shortName.lowercase() == shortName.lowercase()) {
                    return entry
                }
            }
            throw IllegalArgumentException("Короткое имя цвета не распознано")
        }
    }
}