package org.belog.cps

abstract class AbstractSimulation {

    abstract fun run()

    protected fun walkThroughNodes(nodes: List<Node>, currentItemsSequence: MutableList<Int>?, totalItemsSequences: MutableList<MutableList<Int>?>) {
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

    protected fun fillNode(node: Node, possibleItemsIndices: IntRange, currentStep: Int, totalSteps: Int) {
        for (itemI in possibleItemsIndices) {
            val nextNode = Node(itemI, mutableListOf())
            node.nextNodes.add(nextNode)
            if (currentStep < totalSteps) {
                fillNode(nextNode, possibleItemsIndices, currentStep + 1, totalSteps)
            }
        }
    }

    protected class Node(val itemIndex: Int, val nextNodes: MutableList<Node>)
}