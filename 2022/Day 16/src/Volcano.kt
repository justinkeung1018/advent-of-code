import java.io.File
import java.util.*

class Node(val valve: String, val flowrate: Int, val tunnels: List<String>) {
//    override fun toString(): String = "$valve, $flowrate, $tunnels"
    override fun toString(): String = valve

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Node) return false
        return valve == other.valve
    }

    override fun hashCode(): Int {
        var result = valve.hashCode()
        result = 31 * result + flowrate
        result = 31 * result + tunnels.hashCode()
        return result
    }
}

val nodes = mutableMapOf<String, Node>()
val distances = mutableMapOf<Node, Map<String, Int>>()

fun minDistance(start: Node, end: Node): Int {
    fun helper(start: Node, end: Node, visited: Set<Node>): Int {
        if (start == end) {
            return 0
        }
        var minDistance = Int.MAX_VALUE
        for (tunnel in start.tunnels.filter { !visited.contains(nodes[it]) }) {
            val remaining = helper(nodes[tunnel]!!, end, visited + start)
            if (remaining < 0) {
                // Overflow
                continue
            }
            minDistance = minDistance.coerceAtMost(remaining)
        }
        return 1 + minDistance
    }
    return helper(start, end, emptySet())
}

fun pressureReleased(): Int {
    fun search(start: Node, mins: Int, opened: Set<Node>): Int {
        if (mins <= 0) {
            return 0
        }
        val open = (mins - 1) * start.flowrate
        val unopened = nodes.filter { it.value != start && !opened.contains(it.value) }
        val pressures = mutableListOf(open)
        for (entry in unopened) {
            val valve = entry.key
            val node = entry.value
            val distance = distances[start]!![valve]!!
            pressures.add(open + search(node, mins - distance - 1,opened + node))
        }
        return pressures.max()
    }
    val aa = nodes["AA"]!!
    val open = 29 * aa.flowrate
    val pressures = mutableListOf(open)
    for (entry in nodes.filter { it.value != aa }) {
        val valve = entry.key
        val node = entry.value
        val distance = distances[aa]!![valve]!!
        pressures.add(search(node, 30 - distance, setOf(aa)))
        pressures.add(open + search(node, 29 - distance, setOf(aa)))
    }
    return pressures.max()
}

fun main() {
    val input = File("./input.txt").readLines()
    for (line in input) {
        val info = line.split("Valve", "has flow rate=", "; tunnels lead to valves ", ",", "; tunnel leads to valve ").map { it.trim() }.filter { it.isNotEmpty() }
        val valve = info[0]
        val flowrate = info[1].toInt()
        val tunnels = info.slice(2 until info.size)
        nodes[valve] = Node(valve, flowrate, tunnels)
    }
    println(nodes)
    for (node in nodes.values) {
        distances[node] = nodes.mapValues { minDistance(node, it.value) }
    }
    println(distances)
    println("Part 1: ${pressureReleased()}")
}