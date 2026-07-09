package org.belog.cps

enum class Color(val localization: String, val shortName: String) {
    RED("КРАСНЫЙ", "R"),
    ORANGE("ОРАНЖЕВЫЙ", "O"),
    YELLOW("ЖЁЛТЫЙ", "Y"),
    GREEN("ЗЕЛЁНЫЙ", "G"),
    LIGHT_BLUE("ГОЛУБОЙ", "LB"),
    PINK("РОЗОВЫЙ", "P"),
    DARK_BLUE("СИНИЙ", "DB"),
    VIOLET("ФИОЛЕТОВЫЙ", "V");

    companion object {
        fun byShortName(shortName: String): Color {
            for (entry in entries) {
                if (entry.shortName.lowercase() == shortName.lowercase()) {
                    return entry
                }
            }
            throw IllegalArgumentException("Короткое имя цвета не распознано")
        }
    }
}