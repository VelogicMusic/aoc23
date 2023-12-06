package solutions

class Day06(day: Int) : Day(day) {
    override fun solvePart1(input: String): String {
        val races =
            input.split("\n")
                .map { inp -> Regex("\\d+").findAll(inp) }
                .map { regex -> regex.map { r -> r.value.toInt() }.toList() }
                .let { it.first() zip it.last() }
        return races
            .map { (time, distance) ->
                (0..time).filter { (time - it) * it > distance }.size
            }
            .reduce { acc, i -> acc * i}
            .toString()
    }

    override fun solvePart2(input: String): String {
        return input.split("\n")
            .map { inp -> inp.substringAfter(":") }
            .map { inp -> inp.replace("\\s".toRegex(), "") }
            .let { it.first().toInt() to it.last().toLong() }
            .let { (time, distance) -> (0..time).count { (time - it) * it > distance } }
            .toString()
    }
}
