package org.belog.cps

import kotlin.math.pow

class TargetColorsSimulation(
    private val finalInnerColor: Color,
    private val stepsCount: Int,
    private val printBanned: Boolean,
    private val outerShouldMatch: Boolean
) {

    fun run() {
        println("Запуск смиуляции с параметрами: $finalInnerColor, $stepsCount")

//        val possibleCombinations = ((Color.entries.size * Reaction.entries.size).toDouble())
//            .pow(stepsCount.toDouble()).toInt()

        val possibleItems = mutableListOf<Item>()
        for (color in Color.entries) {
            if (!Item.BANNED_COLORS.contains(color)) {
                for (reaction in Reaction.entries) {
                    possibleItems.add(Item(color, reaction))
                }
            }
        }

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
        val successfulSequences = mutableSetOf<MutableList<Int>>()
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
                if (circle.getCurrentInnerColor() == finalInnerColor) {
                    if (!outerShouldMatch || circle.getCurrentOuterColor() == finalInnerColor) {
                        if (i < itemsSequence.size - 1) {
                            successfulSequences.add(itemsSequence.subList(0, i + 1))
                        } else {
                            successfulSequences.add(itemsSequence)
                        }
                        break
                    }
                }
            }
        }

        val successfulSteps = successfulSequences.map {
            itemIndices -> itemIndices.map {
                itemIndex -> possibleItems[itemIndex]
            }.toList()
        }.toList()

        successfulSteps.forEach {
            println("Успешная последовательность: $it")
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

    private fun walkThroughNodes(nodes: List<Node>, currentItemsSequence: MutableList<Int>?, totalItemsSequences: MutableList<MutableList<Int>?>) {
        if (nodes.isEmpty()) {
            if (currentItemsSequence != null) {
                val presentIndices = mutableSetOf<Int>()
                for (index in currentItemsSequence) {
                    if (presentIndices.contains(index)) {
                        return
                    }
                    presentIndices.add(index)
                }
            }
            totalItemsSequences.add(currentItemsSequence)
        } else {
            for (node in nodes) {
                val currentSequence = mutableListOf<Int>()
                if (currentItemsSequence != null) {
                    currentSequence.addAll(currentItemsSequence)
                }
                currentSequence.add(node.itemIndex)
                walkThroughNodes(node.nextNodes, currentSequence, totalItemsSequences)
            }
        }
    }

    private fun fillNode(node: Node, possibleItemsIndices: IntRange, currentStep: Int, totalSteps: Int) {
        for (itemI in possibleItemsIndices) {
            val nextNode = Node(itemI, mutableListOf())
            node.nextNodes.add(nextNode)
            if (currentStep < totalSteps) {
                fillNode(nextNode, possibleItemsIndices, currentStep + 1, totalSteps)
            }
        }
    }

    private class Node(val itemIndex: Int, val nextNodes: MutableList<Node>)
}