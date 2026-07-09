package org.belog.cps

data class Item(
    val color: Color,
    val reaction: Reaction
) {
    override fun toString(): String {
        return "${color.localization}+${reaction.localization}"
    }

    companion object {
        val BANNED_COLORS = listOf(Color.VIOLET)
    }
}
