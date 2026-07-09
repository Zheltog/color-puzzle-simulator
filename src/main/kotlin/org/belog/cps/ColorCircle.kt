package org.belog.cps

class ColorCircle(initInnerId: Int = DEFAULT_INNER_COLOR_ID, initOuterId: Int = DEFAULT_OUTER_COLOR_ID) {

    companion object {
        val DEFAULT_INNER_COLOR_ID = 7
        val DEFAULT_OUTER_COLOR_ID = 3

        val BANNED_OUTER_COLORS = listOf(Color.VIOLET)
        val BANNED_INNER_COLORS = listOf(Color.GREEN)
    }

    private val colorsOrdered = Color.entries.toTypedArray().also {
        if (it.size % 2 != 0) {
            throw IllegalStateException("Число цветов должно быть чётным!")
        }
    }
    private val colorsToIndicesMap = colorsOrdered.withIndex().associateBy(
        { it.value }, { it.index }
    ).toMap()
    private val colorsTotal = colorsOrdered.size

    private var currentInnerColorId = 0
    private var currentOuterColorId = 0

    init {
        currentInnerColorId = initInnerId
        currentOuterColorId = initOuterId
    }

    fun getCurrentInnerColor(): Color {
        return colorsOrdered[currentInnerColorId]
    }

    fun getCurrentOuterColor(): Color {
        return colorsOrdered[currentOuterColorId]
    }

    fun isBlocked() = BANNED_INNER_COLORS.contains(colorsOrdered[currentInnerColorId]) ||
            BANNED_OUTER_COLORS.contains(colorsOrdered[currentOuterColorId])

    fun checkAndApplyItem(item: Item): Boolean {
        if (currentOuterColorId == colorsToIndicesMap[item.color]) {
            return false
        }
        val oppositeOuterColorId = getOppositeColorId(currentOuterColorId)
        if (oppositeOuterColorId == colorsToIndicesMap[item.color]) {
            return false
        }
        applyItem(item)
        return true
    }

    fun applyItem(item: Item) {
        val targetColorId = colorsToIndicesMap[item.color]
        when (item.reaction) {
            Reaction.DIRECT -> {
                val oppositeColorId = getOppositeColorId(targetColorId!!)
                val delta = oppositeColorId - currentOuterColorId
                currentOuterColorId = oppositeColorId
                currentInnerColorId = (currentInnerColorId + delta) % colorsTotal
            }
            Reaction.MIXED -> {
                val delta = targetColorId!! - currentOuterColorId
                val invertedColorId = (currentOuterColorId - delta) % colorsTotal
                currentOuterColorId = invertedColorId
            }
        }
        if (currentInnerColorId < 0) {
            currentInnerColorId = colorsTotal + currentInnerColorId
        }
        if (currentOuterColorId < 0) {
            currentOuterColorId = colorsTotal + currentOuterColorId
        }

//        println("Применён предмет $item. Текущие цвета: внутренний=${colorsOrdered[currentInnerColorId].localization}, внешний=${colorsOrdered[currentOuterColorId].localization}")
    }

    private fun getOppositeColorId(id: Int) : Int {
        return (id + colorsTotal / 2) % colorsTotal
    }

    override fun toString(): String {
        return "Circle{currentInnerColorId=$currentInnerColorId,currentOuterColor=$currentOuterColorId}"
    }
}