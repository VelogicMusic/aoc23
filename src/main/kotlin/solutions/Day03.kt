package solutions

import kotlin.math.absoluteValue

sealed class SchematicEntry(val row: Int, val column: Int) {
    fun overlaps(other: SchematicEntry): Boolean = (row - other.row).absoluteValue <= 1 && (column - other.column).absoluteValue <= 1
}

class Number(val value: Int, row: Int, column: Int) : SchematicEntry(row, column) {
    override fun equals(other: Any?): Boolean =
        when (other) {
            is Number -> value == other.value && row == other.row && (other.column - column).absoluteValue < value.toString().length
            else -> false
        }

    override fun hashCode(): Int = value * row
}

class Symbol(val value: Char, row: Int, column: Int) : SchematicEntry(row, column)

class Day03(day: Int) : Day(day) {
    override fun solvePart1(input: String): String {
        val parsedInput = parse(input)
        val symbols = parsedInput.filterIsInstance<Symbol>()
        val numbers = parsedInput.filterIsInstance<Number>()
        return numbers
            .filter { number -> symbols.any { it.overlaps(number) } }
            .toSet()
            .map { number -> number.value }
            .sum()
            .toString()
    }

    override fun solvePart2(input: String): String {
        val parsedInput = parse(input)
        val gears = parsedInput.filterIsInstance<Symbol>().filter { it.value == '*' }
        val numbers = parsedInput.filterIsInstance<Number>()
        return gears
            .associateWith { gear -> numbers.filter { it.overlaps(gear) }.toSet() }
            .filter { (_, numbers) -> numbers.size == 2 }
            .map { (_, numbers) -> numbers.first().value * numbers.last().value }
            .sum()
            .toString()
    }

    private fun parse(input: String): List<SchematicEntry> {
        val schematics = mutableListOf<SchematicEntry>()
        for ((rowIndex, line) in input.lines().withIndex()) {
            var currentNum: Int? = null
            for (columnIndex in line.indices) {
                val intRegex = Regex("^\\d+").find(line.substring(columnIndex, line.length))
                if (intRegex != null) {
                    currentNum = currentNum ?: intRegex.value.toInt()
                    schematics.add(Number(currentNum, rowIndex, columnIndex))
                    continue
                }
                currentNum = null
                if (line.substring(columnIndex, columnIndex + 1) == ".") {
                    continue
                }
                schematics.add(Symbol(line[columnIndex], rowIndex, columnIndex))
            }
        }
        return schematics.toList()
    }
}
