import solutions.Day

fun main(args: String) {
    val arguments = argparse(args)
    val solutions: List<Day> = emptyList()
}

fun argparse(args: Array<String>) =
        args
                .fold(Pair(emptyMap<String, List<String>>(), "")) { (map, lastKey), s ->
                    if (s.startsWith("-"))
                            Pair(
                                    map + (s.split(Regex("-"))[1] to emptyList()),
                                    s.split(Regex("-"))[0]
                            )
                    else
                            Pair(
                                    map + (lastKey to map.getOrDefault(lastKey, emptyList()) + s),
                                    lastKey
                            )
                }
                .first
