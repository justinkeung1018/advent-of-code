import java.io.File

class Tetris {
    val tower = Array(1000000) { Array(7) { '.' } }

    override fun toString(): String = tower.reversed().joinToString("\n", transform = { it.joinToString("") })
}

val rocks = listOf<(Int) -> List<Pair<Int, Int>>>(
    { lowest -> listOf(Pair(2, lowest), Pair(3, lowest), Pair(4, lowest), Pair(5, lowest)) },
    { lowest -> listOf(Pair(3, lowest), Pair(2, lowest + 1), Pair(3, lowest + 1), Pair(4, lowest + 1), Pair(3, lowest + 2)) },
    { lowest -> listOf(Pair(2, lowest), Pair(3, lowest), Pair(4, lowest), Pair(4, lowest + 1), Pair(4, lowest + 2)) },
    { lowest -> listOf(Pair(2, lowest), Pair(2, lowest + 1), Pair(2, lowest + 2), Pair(2, lowest + 3)) },
    { lowest -> listOf(Pair(2, lowest), Pair(3, lowest), Pair(2, lowest + 1), Pair(3, lowest + 1)) }
)

fun height(input: String, n: Long): Long {
    val tetris = Tetris()
    val surfaces = mutableMapOf<Set<Pair<Int, Int>>, MutableSet<Pair<Int, Int>>>()
    fun surface(highest: Int): Set<Pair<Int, Int>> {
        val queue = (0..6).map { Pair(it, highest) }.toMutableList()
        val surface = mutableSetOf<Pair<Int, Int>>()
            while (queue.isNotEmpty()) {
                val cell = queue.removeFirst()
                val x = cell.first
                val y = cell.second
                val offsetCell = Pair(x, y - highest)
                if (surface.contains(offsetCell)) continue
                if (tetris.tower[y][x] != '.') continue
                surface.add(offsetCell)
                if (x > 0) queue.add(Pair(x - 1, y))
                if (x < 6) queue.add(Pair(x + 1, y))
                if (y > 0) queue.add(Pair(x, y - 1))
            }
        return surface
    }
    // Output = (Surface when cycle is reached, [height, period, blowIndex, rockIndex])
    fun cycle(numRocks: Int, tower: Array<Array<Char>> = tetris.tower, offset: Int = 0, blowIndexInit: Int = 0, rockIndexInit: Int = 0): Pair<Set<Pair<Int, Int>>, List<Int>> {
        var blowIndex = blowIndexInit
        var height = offset
        for (i in rockIndexInit until numRocks) {
            val rockIndex = (i % 5).toInt()
            val surface = surface(height)
            val value = Pair(blowIndex, rockIndex)
            if (!surfaces.contains(surface)) {
                surfaces[surface] = mutableSetOf()
            }
            if (surfaces[surface]!!.contains(value)) {
                return Pair(surface, listOf(height - offset, i + 1, blowIndex, rockIndex))
            }
            surfaces[surface]!!.add(value)
            val rock = rocks[rockIndex](height + 3)
            var newPos = rock
            while (true) {
                val afterBlow = newPos.map {
                    when (input[blowIndex]) {
                        '>' -> Pair(it.first + 1, it.second)
                        '<' -> Pair(it.first - 1, it.second)
                        else -> throw IllegalArgumentException("Something wrong with the input")
                    }
                }
                if (afterBlow.all { it.first in 0 until 7 && tower[it.second][it.first] != '#' }) {
                    newPos = afterBlow
                }
                blowIndex = (blowIndex + 1) % input.length
                val afterFall = newPos.map { Pair(it.first, it.second - 1) }
                if (afterFall.all { it.second >= 0 && tower[it.second][it.first] != '#' }) {
                    newPos = afterFall
                } else {
                    break
                }
            }
            for (pos in newPos) {
                tower[pos.second][pos.first] = '#'
            }
            height = height.coerceAtLeast(newPos.maxOf { it.second } + 1)
        }
        return Pair(emptySet(), listOf(height - offset, numRocks, 0, 0)) // There are no cycles
    }
    val cycleReached = cycle(n.coerceAtMost(1000000).toInt())
    val surface = cycleReached.first
    val heightWhenCycleReached = cycleReached.second[0]
    val blowIndex = cycleReached.second[2]
    val rockIndex = cycleReached.second[3]

    val offset = surface.maxOf { it.second } - surface.minOf{ it.second }

    fun newTetris(): Tetris {
        val newTetris = Tetris()
        for (y in 0..offset) {
            for (x in 0..6) {
                if (!surface.contains(Pair(x, y - offset))) {
                    newTetris.tower[y][x] = '#'
                }
            }
        }
        return newTetris
    }

    surfaces.clear()
    val cycle = cycle(1000000, newTetris().tower, offset, blowIndex, rockIndex)
    val height = cycle.second[0]
    val period = cycle.second[1]
    val initial = heightWhenCycleReached - height
    val numCycles = n / period
    val rocksRemaining = (n % period).toInt()

    val remainder = cycle(rocksRemaining, newTetris().tower, offset, blowIndex, rockIndex)
    val remainderHeight = remainder.second[0]

    return initial + height * numCycles + remainderHeight
}

fun main() {
    val input = File("./input.txt").readText()
    println(height(input, 2022))
//    println(height(input, 1000000000000))
}