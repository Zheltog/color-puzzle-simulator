package org.belog.cps

fun main() {
    println("Введите режим симуляции:")
    println("1 - симуляция по целевому цвету;")
    println("2 - симуляция по целевым предметам")
    val mode = readln()
    when (mode) {
        "1" -> processTargetColors()
        "2" -> processTargetItems()
        else -> throw IllegalArgumentException("Режим симуляции не распознан")
    }
}

private fun processTargetColors() {
    println("Введите желаемый финальный цвет банта в формате:")
    printColors()
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

private fun processTargetItems() {
    println("Введите через пробел предметы в формате <ЦВЕТ>+<РЕАКЦИЯ>")
    println("Где цвет в формате:")
    printColors()
    println("И реакция в формате:")
    Reaction.entries.forEach {
        println("${it.localization} - ${it.shortName}")
    }
    println("Пример: DB+D o+m")
    val items = readln()
    val itemsDescriptionsSplit = items.split(" ")
    if (itemsDescriptionsSplit.isEmpty()) {
        throw IllegalArgumentException("Не удалось распознать описание предметов")
    }
    val itemsParsed = itemsDescriptionsSplit.map { parseItem(it) }.toSet()
    println("Введите желаемое количество шагов")
    val stepsCount = readln()
    val stepsCountInt = stepsCount.toInt()
    if (itemsParsed.size < stepsCountInt) {
        throw IllegalArgumentException("Число предметов должно быть не меньше, чем количество шагов")
    }
    println("Печатать \"заблокированные\" последовательности? (Да = Y / Нет = N)")
    val printBanned = readln()
    println("Тело должно стать того же финального цвета? (Да = Y / Нет = N)")
    val outerShouldMatch = readln()
    val simulation = TargetItemsSimulation(
        possibleItemsSet = itemsParsed,
        stepsCount = stepsCountInt,
        printBanned = "y" == printBanned.lowercase(),
        outerShouldMatch = "y" == outerShouldMatch.lowercase()
    )
    simulation.run()
}

private fun printColors() {
    Color.entries.forEach {
        println("${it.localization} - ${it.shortName}")
    }
}

private fun parseItem(description: String): Item {
    val split = description.split("+")
    if (split.size != 2) {
        throw IllegalArgumentException("Не удалось распознать $description как описание предмета")
    }
    val colorShortName = split[0]
    val reactionShortName = split[1]
    return Item(Color.byShortName(colorShortName), Reaction.byShortName(reactionShortName))
}