import java.io.File

class HillClimbing(private val input: List<String>) {
    private val rows = input.size
    private val cols = input[0].length
    private val directions = setOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
    private val dp: Array<Array<Int>> = Array(rows) { Array(cols) { Integer.MAX_VALUE } }

    fun part1(): Int {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (input[i][j] == 'S') {
                    return bfs(mutableListOf(Pair(i, j)), mutableSetOf(Pair(i, j)))
                }
            }
        }
        return -1
    }

    fun part2(): Int {
        var numSteps = Integer.MAX_VALUE
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (height(input[i][j]) == 'a') {
                    numSteps = numSteps.coerceAtMost(bfs(mutableListOf(Pair(i, j)), mutableSetOf(Pair(i, j))))
                }
            }
        }
        return numSteps
    }

    private fun height(square: Char) =
        when (square) {
            'S' -> 'a'
            'E' -> 'z'
            else -> square
        }

    private fun bfs(queue: MutableList<Pair<Int, Int>>, visited: MutableSet<Pair<Int, Int>>): Int {
        var numSteps = 0;

        while (queue.isNotEmpty()) {
            val size = queue.size
            for (i in 0 until size) {
                val square = queue.removeFirst()
                val row = square.first
                val col = square.second
                if (input[row][col] == 'E') {
                    return numSteps
                }
                for (direction in directions) {
                    val dr = direction.first
                    val dc = direction.second
                    val newRow = row + dr
                    val newCol = col + dc
                    if (newRow < 0 || newRow >= rows) {
                        continue
                    }
                    if (newCol < 0 || newCol >= cols) {
                        continue
                    }
                    if (visited.contains(Pair(newRow, newCol))) {
                        continue
                    }
                    if (height(input[newRow][newCol]) - height(input[row][col]) <= 1) {
                        visited.add(Pair(newRow, newCol))
                        queue.add(Pair(newRow, newCol))
                    }
                }
            }
            numSteps++
        }
        return Integer.MAX_VALUE
    }

    // Doesn't work
    private fun dfs(row: Int, col: Int, visited: MutableSet<Pair<Int, Int>>) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return
        }

        if (visited.contains(Pair(row, col))) {
            return
        }

        visited.add(Pair(row, col))

        val square = input[row][col]
        if (square == 'E') {
            dp[row][col] = 0
            return
        }

        var steps = Integer.MAX_VALUE
        for (direction in directions) {
            val dr = direction.first
            val dc = direction.second
            val newRow = row + dr
            val newCol = col + dc
            if (newRow < 0 || newRow >= rows) {
                continue
            }
            if (newCol < 0 || newCol >= cols) {
                continue
            }
            if (height(input[newRow][newCol]) - height(square) <= 1) {
                dfs(newRow, newCol, visited)
                steps = steps.coerceAtMost(dp[newRow][newCol])
            }
        }
        if (steps != Integer.MAX_VALUE) {
            dp[row][col] = 1 + steps
        }
    }
}

fun main() {
    val input = File("./input.txt").readLines()
    val hillClimbing = HillClimbing(input)
    println("Part 1: ${hillClimbing.part1()}")
    println("Part 2: ${hillClimbing.part2()}")
}
