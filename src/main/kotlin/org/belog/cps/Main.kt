package org.belog.cps

fun main() {
    println("Введите желаемый финальный цвет банта в формате:")
    Color.entries.forEach {
        println("${it.localization} - ${it.shortName}")
    }
    val finalColorShortName = readln()
    println("Введите желаемое количество шагов")
    val stepsCount = readln()
    println("Печатать \"заблокированные\" последовательности? (Да = Y / Нет = N)")
    val printBanned = readln()
    println("Тело должно стать того же финального цвета? (Да = Y / Нет = N)")
    val outerShouldMatch = readln()
    val simulation = TargetColorsSimulation(
        finalInnerColor = Color.byShortName(finalColorShortName),
        stepsCount = stepsCount.toInt(),
        printBanned = "y" == printBanned.lowercase(),
        outerShouldMatch = "y" == outerShouldMatch.lowercase()
    )
    simulation.run()
}
