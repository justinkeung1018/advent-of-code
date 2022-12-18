import java.io.File

fun totalSurfaceArea(cubes: Set<Triple<Int, Int, Int>>): Int {
    var connected = 0
    for (cube in cubes) {
        val x = cube.first
        val y = cube.second
        val z = cube.third
        val adjacent = setOf(
            Triple(x - 1, y, z),
            Triple(x + 1, y, z),
            Triple(x, y - 1, z),
            Triple(x, y + 1, z),
            Triple(x, y, z - 1),
            Triple(x, y, z + 1)
        )
        connected += adjacent.filter { cubes.contains(it) }.size
    }
    return 6 * cubes.size - connected
}

fun exteriorSurfaceArea(cubes: Set<Triple<Int, Int, Int>>): Int {
    val min = Triple(cubes.minOf { it.first } - 1, cubes.minOf { it.second } - 1, cubes.minOf { it.third } - 1)
    val max = Triple(cubes.maxOf { it.first } + 1, cubes.maxOf { it.second } + 1, cubes.maxOf { it.third } + 1)
    val water = mutableSetOf<Triple<Int, Int, Int>>()
    val blocked = cubes.toMutableSet()
    fun bfs(cube: Triple<Int, Int, Int>) {
        val queue = mutableListOf<Triple<Int, Int, Int>>()
        if (!blocked.contains(cube)) {
            queue.add(cube)
        }
        while (queue.isNotEmpty()) {
            val currCube = queue.removeFirst()
            val x = currCube.first
            val y = currCube.second
            val z = currCube.third
            val adjacent = setOf(
                Triple(x - 1, y, z),
                Triple(x + 1, y, z),
                Triple(x, y - 1, z),
                Triple(x, y + 1, z),
                Triple(x, y, z - 1),
                Triple(x, y, z + 1)
            ).filter {
                it.first in min.first..max.first
                        && it.second in min.second..max.second
                        && it.third in min.third..max.third
            }.filter { !blocked.contains(it) }
            queue.addAll(adjacent)
            blocked.addAll(adjacent)
            water.addAll(adjacent)
        }
    }

    bfs(min)

    var exteriorSurfaceArea = 0
    for (cube in cubes) {
        val x = cube.first
        val y = cube.second
        val z = cube.third
        val adjacent = setOf(
            Triple(x - 1, y, z),
            Triple(x + 1, y, z),
            Triple(x, y - 1, z),
            Triple(x, y + 1, z),
            Triple(x, y, z - 1),
            Triple(x, y, z + 1)
        )
        exteriorSurfaceArea += adjacent.filter { water.contains(it) }.size
    }
    return exteriorSurfaceArea
}

fun main() {
    val input = File("./input.txt").readLines()
    val cubes = input.map { it.split(",") }.map { Triple(it[0].toInt(), it[1].toInt(), it[2].toInt()) }.toMutableSet()
    println("Part 1: ${totalSurfaceArea(cubes)}")
    println("Part 2: ${exteriorSurfaceArea(cubes)}")
}