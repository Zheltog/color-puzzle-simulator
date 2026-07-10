package org.belog.cps

class TargetItemsSimulation(
    private val possibleItemsSet: Set<Item>,
    private val stepsCount: Int,
    private val printBanned: Boolean,
    private val outerShouldMatch: Boolean
) : AbstractSimulation() {

    override fun run() {
        println("Запуск симуляции с параметрами: $possibleItemsSet, $stepsCount, $printBanned, $outerShouldMatch")

        val possibleItems = possibleItemsSet.toList()

        val nodes = mutableListOf<Node>()
        for (itemI in possibleItems.indices) {
            nodes.add(Node(itemI, mutableListOf()))
        }
        if (stepsCount > 1) {
            nodes.forEach { it -> fillNode(
                node = it,
                possibleItemsIndices = possibleItems.indices,
                currentStep = 2,
                totalSteps = stepsCount
            )}
        }

        val totalItemsSequences = mutableListOf<MutableList<Int>?>()
        walkThroughNodes(nodes, null, totalItemsSequences)

        println("Есть ${totalItemsSequences.size} возможных комбинаций")

        val bannedSequences = mutableSetOf<MutableList<Int>>()
        val possibleColors = mutableSetOf<Pair<Color, Color>>()
        for (itemsSequence in totalItemsSequences) {
            val circle = ColorCircle()
            for (i in itemsSequence!!.indices) {
                val itemI = itemsSequence[i]
                if (!circle.checkAndApplyItem(possibleItems[itemI])) {
                    break
                }
                if (circle.isBlocked()) {
                    if (i < itemsSequence.size - 1) {
                        bannedSequences.add(itemsSequence.subList(0, i + 1))
                    } else {
                        bannedSequences.add(itemsSequence)
                    }
                    break
                }
                if (i == itemsSequence.size - 1) {
                    if (!outerShouldMatch || circle.getCurrentOuterColor() == circle.getCurrentInnerColor()) {
                        possibleColors.add(Pair(circle.getCurrentInnerColor(), circle.getCurrentOuterColor()))
                        break
                    }
                }
            }
        }

        possibleColors.forEach {
            println("Возможное состояние круга: внутренний=${it.first.localization}, внешний=${it.second.localization}")
        }

        if (printBanned) {
            val bannedSteps = bannedSequences.map {
                itemIndices -> itemIndices.map {
                    itemIndex -> possibleItems[itemIndex]
                }.toList()
            }.toList()

            bannedSteps.forEach {
                println("\"Заблокированная\" последовательность: $it")
            }
        }
    }
}