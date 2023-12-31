package solutions

import util.Input

data class TranslationMap(val mappings: Set<SingleMapping>) {
    fun getMappingDest(number: Long): Long {
        return mappings.firstOrNull { mapping -> number in mapping.src..<mapping.src + mapping.range }?.let { mapping ->
            mapping.dest + (number - mapping.src)
        } ?: number
    }

    fun getMappingSrc(number: Long): Long {
        return mappings.firstOrNull { mapping -> number in mapping.dest..<mapping.dest + mapping.range }?.let { mapping ->
            mapping.src + (number - mapping.dest)
        } ?: number
    }
}

data class SingleMapping(val dest: Long, val src: Long, val range: Long)

class Day05 : Day(5) {
    override fun solvePart1(input: Input): String {
        val (seeds, translationMaps) = parse(input)
        return seeds
            .minOf { seed ->
                translationMaps.fold(seed) { prevNum, translationMap -> translationMap.getMappingDest(prevNum) }
            }
            .toString()
    }

    override fun solvePart2(input: Input): String {
        val (tmpSeeds, tmpTranslationMaps) = parse(input)
        val seeds = tmpSeeds.chunked(2).map { (src, range) -> src..<src + range }
        val translationMaps = tmpTranslationMaps.reversed()
        var seedTry = 0L
        while (true) {
            val possibleSeed =
                translationMaps
                    .fold(seedTry) { num, translationMap -> translationMap.getMappingSrc(num) }
            if (seeds.any { seed -> seed.contains(possibleSeed) }) {
                return seedTry.toString()
            }
            seedTry += 1L
        }
    }

    private fun parse(input: Input): Pair<Set<Long>, List<TranslationMap>> {
        val seeds =
            input.text.substringBefore("\n\n").substringAfter(": ").split(" ")
                .map { it.toLong() }
                .toSet()

        val translationMaps =
            input.text.substringAfter("\n\n").split("\n\n")
                .map { mapping ->
                    mapping.substringAfter(":\n").split("\n")
                        .map { line -> line.split(" ").map { it.toLong() } }
                }
                .map { numSections ->
                    numSections.map { (dest, src, range) -> SingleMapping(dest, src, range) }.toSet()
                }
                .map { singleMappings -> TranslationMap(singleMappings) }

        return seeds to translationMaps
    }
}
