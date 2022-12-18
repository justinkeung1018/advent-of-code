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

fun height(input: String, numRocks: Long): Long {
    val tetris = Tetris()
    val tower = tetris.tower
    // From (blowIndex, rockIndex) to (rockNumber, prevHeight)
    // rockNumber refers to the nth rock (as opposed to which of the five rock types)
    // prevHeight refers to the height of the tower after the rockNumber-th rock is placed
    val map = mutableMapOf<Pair<Int, Int>, Pair<Long, Long>>()
    var blowIndex = 0
    var height = 0L
    for (i in 0 until numRocks) {
        val rockIndex = (i % 5).toInt()
        val key = Pair(blowIndex, rockIndex)
        if (map.contains(key)) {
            val rockNumber = map[key]!!.first
            val prevHeight = map[key]!!.second
            val period = i - rockNumber
            if (rockNumber % period == numRocks % period) {
                val numCycles = (numRocks - rockNumber) / period
                val heightPerCycle = height - prevHeight
                return prevHeight + heightPerCycle * numCycles
            }
        }
        map[key] = Pair(i, height)
        val rock = rocks[rockIndex]((height + 3).toInt())
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
        height = height.coerceAtLeast((newPos.maxOf { it.second } + 1).toLong())
    }
    return height
}

fun main() {
    val input = File("./input.txt").readText()
    println(height(input, 2022))
    println(height(input, 1000000000000))
}