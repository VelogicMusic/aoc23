package solutions

class Day02(day: Int) : Day(day) {
    override fun solvePart1(input: String): String =
        parseInput(input)
            .withIndex()
            .filter { (_, gameSets) ->
                gameSets.all { colorMap ->
                    colorMap.getOrDefault("red", 0) <= 12 &&
                        colorMap.getOrDefault("green", 0) <= 13 &&
                        colorMap.getOrDefault("blue", 0) <= 14
                }
            }
            .sumOf { (index) -> index + 1 }
            .toString()

    override fun solvePart2(input: String): String =
        parseInput(input)
            .sumOf { gameSets ->
                listOf("red", "green", "blue").fold(1) { accumulator, color ->
                    accumulator * gameSets.flatMap { listOf(it.getOrDefault(color, 0)) }.max()
                }.toInt()
            }
            .toString()

    private fun parseInput(input: String): List<List<Map<String, Int>>> =
        input.lines()
            .map { game ->
                game.substringAfter(": ")
                    .split("; ")
                    .map { gameSet ->
                        gameSet
                            .split(", ")
                            .associate { colorCount ->
                                colorCount.split(" ").let { (count, color) -> color to count.toInt() }
                            }
                    }
            }
}
