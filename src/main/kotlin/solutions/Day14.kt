package solutions

import util.Input

data class ReflectorDish(val dish: List<String>) {
    val score: Int
        get() =
            dish.withIndex().sumOf { (index, row) ->
                (dish.size - index) * row.count { it == 'O' }
            }

    fun moveLeft(): ReflectorDish {
        val movedDish =
            dish.map { line ->
                line.split("#").map { it.toList().sorted().reversed().joinToString("") }
            }.map { substrings -> substrings.joinToString("#") }
        return ReflectorDish(movedDish)
    }

    fun getTransposition(): ReflectorDish {
        val transposedDish = mutableListOf<String>()
        val mergedDish = dish.joinToString("")
        for (i in dish.first().indices) {
            transposedDish.add(
                mergedDish.filterIndexed { index, _ -> index % dish.first().length == i },
            )
        }
        return ReflectorDish(transposedDish)
    }

    fun flip(): ReflectorDish {
        return ReflectorDish(dish.map { row -> row.reversed() })
    }
}

class Day14 : Day(14) {
    override fun solvePart1(input: Input): String {
        return ReflectorDish(input.lines)
            .getTransposition()
            .moveLeft()
            .getTransposition()
            .score
            .toString()
    }

    override fun solvePart2(input: Input): String {
        var currentDish = ReflectorDish(input.lines)

        val hashSet = HashSet<ReflectorDish>().also { it.add(currentDish) }
        val reflectorDishList = mutableListOf(currentDish)
        var index = 0

        while (true) {
            index++
            currentDish = cycle(currentDish)
            if (currentDish in hashSet) {
                val initialIndex = reflectorDishList.indexOf(currentDish)
                val cycleLength = index - initialIndex
                val cycleOffset = (1_000_000_000 - initialIndex) % cycleLength
                val score = reflectorDishList[cycleOffset + initialIndex].score
                return score.toString()
            } else {
                hashSet.add(currentDish)
                reflectorDishList.add(currentDish)
            }
        }
    }

    private fun cycle(reflectorDish: ReflectorDish): ReflectorDish {
        var dish = reflectorDish
        for (i in 0..3) {
            dish = dish.getTransposition().moveLeft().flip()
        }
        return dish
    }
}
