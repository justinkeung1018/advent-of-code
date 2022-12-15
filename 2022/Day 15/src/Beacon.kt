import java.io.File
import kotlin.math.absoluteValue

class Sensor(val pos: Position, val beacon: Position) {
    fun distance(): Int = manhattan(pos, beacon)
}

class Position(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return true
        if (other !is Position) return false
        return x == other.x && y == other.y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String = "($x, $y)"
}

fun manhattan(p1: Position, p2: Position): Int = (p1.x - p2.x).absoluteValue + (p1.y - p2.y).absoluteValue

fun part1(sensors: List<Sensor>, target: Int): Int {
    val impossible = mutableSetOf<Int>()
    for (sensor in sensors) {
        val distance = sensor.distance()
        for (i in 0..distance - (sensor.pos.y - target).absoluteValue) {
            impossible.add(sensor.pos.x + i)
            impossible.add(sensor.pos.x - i)
        }
    }
    return impossible.filter { x -> sensors.all { s -> s.beacon != Position(x, target) } }.size
}

fun part2(sensors: List<Sensor>): Long {
    val candidates = mutableSetOf<Position>()
    for (sensor in sensors) {
        val distance = sensor.distance() + 1 // Just outside the distance of the closest beacon
        val x = sensor.pos.x
        val y = sensor.pos.y
        for (i in 0..distance) {
            candidates.addAll(
                setOf(
                    Position(x + distance - i, y + i),
                    Position(x - distance + i, y - i),
                    Position(x + i, y + distance - i),
                    Position(x - i, y - distance + i)
                )
            )
        }
    }
    candidates.removeAll { it.x < 0 || it.x > 4000000 || it.y < 0 || it.y > 4000000 }
    for (candidate in candidates) {
        if (sensors.all { manhattan(candidate, it.pos) > it.distance() }) {
            return candidate.x * 4000000L + candidate.y
        }
    }
    return -1
}

fun main() {
    val input = File("./input.txt").readLines()
    val sensors = mutableListOf<Sensor>()
    for (line in input) {
        val info = line.split("Sensor at x=", ", y=", ": closest beacon is at x=").filter { it.isNotEmpty() }
            .map { it.toInt() }
        sensors.add(Sensor(Position(info[0], info[1]), Position(info[2], info[3])))
    }
    println("Part 1: ${part1(sensors, 2000000)}")
    println("Part 2: ${part2(sensors)}")
}