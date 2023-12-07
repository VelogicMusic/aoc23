package solutions

data class Hand(val cards: String, val bid: Int): Comparable<Hand> {
    private val strengthMap = "AKQJT98765432".reversed().mapIndexed { index, char -> char to index }.toMap()

    override fun compareTo(other: Hand): Int {
        val handStrength1 = strengthMap.map { (card, ) -> cards.count { it == card }}.sortedDescending()
        val handStrength2 = strengthMap.map { (card, _) -> other.cards.count { it == card }}.sortedDescending()
        return when {
            handStrength1.first() != handStrength2.first() -> handStrength1.first() - handStrength2.first()
            handStrength1.first() == 3 && (handStrength1[1] + handStrength2[1] != 4) -> handStrength2[1] - handStrength1[1]

            else ->
                    cards.zip(other.cards).firstOrNull { (c1, c2) -> c1 != c2 }?.let { (c1, c2) ->
                        strengthMap[c1]!!.compareTo(strengthMap[c2]!!)
                    } ?: 0
        }
    }
}

class Day07(day: Int) : Day(day) {
    override fun solvePart1(input: String): String {
        val hands = parse(input)
        return hands.sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
            .toString()
    }

    override fun solvePart2(input: String): String {
        return ""
    }

    private fun parse(input: String): List<Hand> {
        return input.lines()
            .map { line -> line.split(" ").let { (hand, bid) -> Hand(hand, bid.toInt()) } }
    }
}
