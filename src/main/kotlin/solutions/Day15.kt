package solutions

import util.Input

sealed class Code(val value: String) {
    fun computeHashValue(): Int {
        var result = 0
        for (c in value) {
            result += c.code
            result *= 17
            result %= 256
        }
        return result
    }
}

data class SimpleCode(val label: String) : Code(label)

data class CodeEquals(val label: String, val focalLength: Int) : Code(label)

class Day15 : Day(15) {
    override fun solvePart1(input: Input): String {
        return input.text.split(",")
            .map { charSeq -> SimpleCode(charSeq) }
            .sumOf { code -> code.computeHashValue() }
            .toString()
    }

    override fun solvePart2(input: Input): String {
        val operations =
            input.text.split(",")
                .map { charSeq ->
                    if (charSeq.endsWith("-")) {
                        SimpleCode(charSeq.substringBefore("-"))
                    } else {
                        charSeq.split("=").let { (value, focalLength) ->
                            CodeEquals(value, focalLength.toInt())
                        }
                    }
                }
        val boxes = mutableMapOf<Int, ArrayDeque<CodeEquals>>()
        for (code in operations) {
            val hashVal = code.computeHashValue()
            val slots = boxes.getOrDefault(hashVal, ArrayDeque())
            val indexInBox = slots.firstOrNull { c -> c.value == code.value }
            when (code) {
                is SimpleCode ->
                    indexInBox?.let { slots.remove(it) }
                is CodeEquals -> {
                    if (indexInBox == null) {
                        slots.addLast(code)
                    } else {
                        slots[slots.indexOf(indexInBox)] = code
                    }
                }
            }
            boxes[hashVal] = slots
        }
        return boxes.map { (boxNr, slots) ->
            slots.withIndex().sumOf { (slotNr, box) -> (boxNr + 1) * (slotNr + 1) * box.focalLength }
        }.sum().toString()
    }
}
