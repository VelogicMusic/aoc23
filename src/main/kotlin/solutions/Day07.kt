package solutions

import util.Input

sealed class Hand(val cards: String, val bid: Int) : Comparable<Hand> {
    abstract val strengthMap: Map<Char, Int>

    val freq: List<Int>
        get() =
            strengthMap.map { (card, _) -> cards.count { it == card } }
                .filter { it != 0 }
                .sortedDescending()

    abstract fun getRank(): Int

    override fun compareTo(other: Hand): Int {
        val thisRank = getRank()
        val otherRank = other.getRank()
        return if (thisRank != otherRank) {
            thisRank - otherRank
        } else {
            cards.zip(other.cards).firstOrNull { (c1, c2) -> c1 != c2 }?.let { (c1, c2) ->
                strengthMap[c1]!! compareTo strengthMap[c2]!!
            } ?: 0
        }
    }
}

class HandPart1(cards: String, bid: Int) : Hand(cards, bid) {
    override val strengthMap = "AKQJT98765432".reversed().mapIndexed { index, char -> char to index }.toMap()

    override fun getRank(): Int {
        return when (freq) {
            listOf(5) -> 6
            listOf(4, 1) -> 5
            listOf(3, 2) -> 4
            listOf(3, 1, 1) -> 3
            listOf(2, 2, 1) -> 2
            listOf(2, 1, 1, 1) -> 1
            else -> 0
        }
    }
}

class HandPart2(cards: String, bid: Int) : Hand(cards, bid) {
    override val strengthMap = "AKQT98765432J".reversed().mapIndexed { index, char -> char to index }.toMap()

    override fun getRank(): Int {
        val jokerAmount = cards.count { card -> card == 'J' }
        return when (freq) {
            listOf(5) -> 6
            listOf(4, 1) -> if (jokerAmount > 0) 6 else 5
            listOf(3, 2) -> if (jokerAmount > 0) 6 else 4
            listOf(3, 1, 1) -> if (jokerAmount > 0) 5 else 3
            listOf(2, 2, 1) ->
                when (jokerAmount) {
                    2 -> 5
                    1 -> 4
                    else -> 2
                }
            listOf(2, 1, 1, 1) -> if (jokerAmount > 0) 3 else 1
            else -> jokerAmount
        }
    }
}

class Day07 : Day(7) {
    override fun solvePart1(input: Input): String {
        val hands =
            input.lines
                .map { line -> line.split(" ").let { (hand, bid) -> HandPart1(hand, bid.toInt()) } }
        return hands.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }

    override fun solvePart2(input: Input): String {
        val hands =
            input.lines
                .map { line -> line.split(" ").let { (hand, bid) -> HandPart2(hand, bid.toInt()) } }
        return hands.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }
}
