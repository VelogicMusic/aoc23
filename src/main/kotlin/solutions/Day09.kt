package solutions

import util.Input

data class Sequence(val numbers: List<Int>) {
    fun getNextSequence(): Sequence {
        val differences = numbers.zipWithNext().map { (first, second) -> second - first }
        return Sequence(differences)
    }
}

class Day09 : Day(9) {
    override fun solvePart1(input: Input): String {
        val sequences = parse(input)
        val seqList = calculateSequences(sequences)
        return seqList.sumOf { seqs ->
            seqs.reversed().fold(listOf(0)) { numbers, seq ->
                numbers + listOf(numbers.last() + seq.numbers.last())
            }.last()
        }.toString()
    }

    override fun solvePart2(input: Input): String {
        val sequences = parse(input)
        val seqList = calculateSequences(sequences)
        return seqList.sumOf { seqs ->
            seqs.reversed().fold(listOf(0)) { numbers, seq ->
                listOf(seq.numbers.first() - numbers.first()) + numbers
            }.first()
        }.toString()
    }

    private fun calculateSequences(sequences: List<Sequence>): List<List<Sequence>> {
        val resultSequences = mutableListOf<List<Sequence>>()
        for (seq in sequences) {
            var sequence = seq
            val seqList = mutableListOf(sequence)
            while (!sequence.numbers.all { it == 0 }) {
                sequence = sequence.getNextSequence()
                seqList.add(sequence)
            }
            resultSequences.add(seqList.toList())
        }
        return resultSequences.toList()
    }

    private fun parse(input: Input): List<Sequence> {
        return input.lines
            .map { line -> line.split(" ").map { num -> num.toInt() } }
            .map { nums -> Sequence(nums) }
    }
}
