package solutions

import util.Input

data class Node(val x: Int, val y: Int, val symbol: Char = '.') {
    override fun equals(other: Any?): Boolean =
        when (other) {
            is Node -> other.x == x && other.y == y
            else -> false
        }

    override fun hashCode() = 1337 * y + 181 + x
}

data class Graph(val nodes: Set<Node>, val edges: HashMap<Node, List<Node>>) {
    fun calcDistancesBFS(source: Node): HashMap<Node, Int> {
        val nodes = ArrayDeque<Node>()
        val distances = HashMap<Node, Int>()
        nodes.add(source)
        distances[source] = 0
        while (nodes.isNotEmpty()) {
            val currentVertex = nodes.removeFirst()
            for (adjVertex in edges.getOrDefault(currentVertex, emptySet())) {
                if (distances.containsKey(adjVertex)) continue
                if (!edges.getOrDefault(adjVertex, emptySet()).contains(currentVertex)) continue
                nodes.add(adjVertex)
                distances[adjVertex] = distances[currentVertex]!! + 1
            }
        }
        return distances
    }

    fun getOddEvenSum(loop: List<Node>): Int {
        val pipeGroups =
            loop.groupBy { node -> node.y }
                .map { (_, nodes) -> nodes.sortedBy { node -> node.x } }
                .map { nodes ->
                    nodes.takeLast(nodes.size - 1).fold(listOf(nodes.first())) { nodeList, node ->
                        when (node.symbol) {
                            '|', 'F', 'L' -> nodeList + listOf(node)
                            '7' -> if (nodeList.last().symbol == 'F') nodeList.take(nodeList.size - 1) else nodeList
                            'J' -> if (nodeList.last().symbol == 'L') nodeList.take(nodeList.size - 1) else nodeList
                            else -> nodeList
                        }
                    }.toSet()
                }.flatten().toList()
        return nodes
            .filter { node -> node !in loop }
            .groupBy { node -> node.y }
            .map { (y, nodes) ->
                nodes.count { node ->
                    (
                        pipeGroups.count { n ->
                            n.y == y && n.x > node.x
                        } % 2 == 1
                    ) && (
                        pipeGroups.count { n ->
                            n.y == y && n.x < node.x
                        } % 2 == 1
                    )
                }
            }.sum()
    }
}

class Day10 : Day(10) {
    override fun solvePart1(input: Input): String {
        val (startingNode, graph) = parse(input)
        val distances = graph.calcDistancesBFS(startingNode)
        return distances.values.max().toString()
    }

    override fun solvePart2(input: Input): String {
        val (startingNode, graph) = parse(input)
        val loop =
            graph.calcDistancesBFS(startingNode).keys
                .map { node -> graph.nodes.first { n -> n == node }.let { n -> Node(n.x, n.y, n.symbol) } }
        return graph.getOddEvenSum(loop).toString()
    }

    private fun parse(input: Input): Pair<Node, Graph> {
        val nodes = mutableSetOf<Node>()
        val edges = HashMap<Node, List<Node>>()
        var startingNode = Node(-1, -1)
        for ((y, line) in input.lines.withIndex()) {
            for ((x, symbol) in line.withIndex()) {
                val currentNode = Node(x, y, symbol)
                nodes.add(currentNode)
                edges[currentNode] =
                    when (symbol) {
                        '|' -> listOf(Node(x, y - 1), Node(x, y + 1))
                        '-' -> listOf(Node(x - 1, y), Node(x + 1, y))
                        'L' -> listOf(Node(x, y - 1), Node(x + 1, y))
                        'J' -> listOf(Node(x, y - 1), Node(x - 1, y))
                        '7' -> listOf(Node(x - 1, y), Node(x, y + 1))
                        'F' -> listOf(Node(x + 1, y), Node(x, y + 1))
                        'S' -> listOf(Node(x, y + 1), Node(x, y - 1), Node(x + 1, y), Node(x - 1, y)).also { startingNode = currentNode }
                        else -> emptyList()
                    }
            }
        }
        return startingNode to Graph(nodes.toSet(), edges)
    }
}
