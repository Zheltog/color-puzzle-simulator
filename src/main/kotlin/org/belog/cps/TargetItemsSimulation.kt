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

        val bannedSequences = mutableSetOf<SequenceWithResult>()
        val possibleSequences = mutableSetOf<SequenceWithResult>()
        for (itemsSequence in totalItemsSequences) {
            val circle = ColorCircle()
            for (i in itemsSequence!!.indices) {
                val itemI = itemsSequence[i]
                if (!circle.checkAndApplyItem(possibleItems[itemI])) {
                    break
                }
                if (circle.isBlocked()) {
                    val items = if (i < itemsSequence.size - 1) itemsSequence.subList(0, i + 1) else itemsSequence
                    bannedSequences.add(SequenceWithResult(
                        items = items,
                        finalInnerColor = circle.getCurrentInnerColor(),
                        finalOuterColor = circle.getCurrentOuterColor()
                    ))
                    break
                }
                if (i == itemsSequence.size - 1) {
                    if (!outerShouldMatch || circle.getCurrentOuterColor() == circle.getCurrentInnerColor()) {
                        possibleSequences.add(SequenceWithResult(
                            items = itemsSequence,
                            finalInnerColor = circle.getCurrentInnerColor(),
                            finalOuterColor = circle.getCurrentOuterColor()
                        ))
                        break
                    }
                }
            }
        }

        localize(possibleSequences, possibleItems).forEach {
            println("Возможная последовательность: $it")
        }

        if (printBanned) {
            localize(bannedSequences, possibleItems).forEach {
                println("\"Заблокированная\" последовательность: $it")
            }
        }
    }

    private fun localize(sequences: Set<SequenceWithResult>, possibleItems: List<Item>) = sequences.map {
        val items = it.items.map {
            itemIndex -> possibleItems[itemIndex]
        }.toList()
        "$items (итого бант=${it.finalInnerColor.localization}, тело=${it.finalOuterColor.localization})"
    }.toList()
}