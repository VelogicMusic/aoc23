package solutions

data class HandPart1(val cards: String, val bid: Int) : Comparable<HandPart1> {
    private val strengthMap = "AKQJT98765432".reversed().mapIndexed { index, char -> char to index }.toMap()

    private fun getRank(): Int {
        val freq =
            strengthMap.map { (card, _) -> cards.count { it == card } }
                .filter { it != 0 }
                .sortedDescending()
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

    override fun compareTo(other: HandPart1): Int {
        val thisRank = getRank()
        val otherRank = other.getRank()
        return if (thisRank != otherRank) {
            thisRank - otherRank
        } else {
            cards.zip(other.cards).firstOrNull { (c1, c2) -> c1 != c2 }?.let { (c1, c2) ->
                strengthMap[c1]!!.compareTo(strengthMap[c2]!!)
            } ?: 0
        }
    }
}

data class HandPart2(val cards: String, val bid: Int) : Comparable<HandPart2> {
    private val strengthMap = "AKQT98765432J".reversed().mapIndexed { index, char -> char to index }.toMap()

    private fun getRank(): Int {
        val jokerAmount = cards.count { card -> card == 'J' }
        val freq =
            strengthMap.map { (card, _) -> cards.count { it == card } }
                .filter { it != 0 }
                .sortedDescending()
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
            listOf(2, 1, 1, 1) ->
                when (jokerAmount) {
                    1, 2 -> 3
                    else -> 1
                }
            else -> jokerAmount
        }
    }

    override fun compareTo(other: HandPart2): Int {
        val thisRank = getRank()
        val otherRank = other.getRank()
        return if (thisRank != otherRank) {
            thisRank - otherRank
        } else {
            cards.zip(other.cards).firstOrNull { (c1, c2) -> c1 != c2 }?.let { (c1, c2) ->
                strengthMap[c1]!!.compareTo(strengthMap[c2]!!)
            } ?: 0
        }
    }
}

class Day07(day: Int) : Day(day) {
    override fun solvePart1(input: String): String {
        val hands =
            input.lines()
                .map { line -> line.split(" ").let { (hand, bid) -> HandPart1(hand, bid.toInt()) } }
        return hands.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }

    override fun solvePart2(input: String): String {
        val hands =
            input.lines()
                .map { line -> line.split(" ").let { (hand, bid) -> HandPart2(hand, bid.toInt()) } }

        return hands.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }
}
