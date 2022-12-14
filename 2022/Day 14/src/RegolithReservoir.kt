import java.io.File

class RegolithReservoir(private val lines: List<List<List<Int>>>) {
    private val shift = 0 // Reduce the number of columns to make the printed grid look cleaner
    private val rows = 170
    private val cols = 700 - shift
    private var grid: Array<Array<Char>> = Array(rows) { Array(cols) { '.' } }

    private fun drawGrid() {
        grid = Array(rows) { Array(cols) { '.' } }
        for (line in lines) {
            var currCol = line[0][0] - shift
            var currRow = line[0][1]
            for (i in 1 until line.size) {
                val point = line[i]
                val col = point[0] - shift
                val row = point[1]

                val minRow = row.coerceAtMost(currRow)
                val maxRow = row.coerceAtLeast(currRow)
                val minCol = col.coerceAtMost(currCol)
                val maxCol = col.coerceAtLeast(currCol)
                for (r in minRow..maxRow) {
                    for (c in minCol..maxCol) {
                        grid[r][c] = '#'
                    }
                }
                currCol = col
                currRow = row
            }
        }
    }

    fun part1(): Int {
        drawGrid()
        var numSand = 0
        var row = 0
        var col = 500 - shift
        while (row != rows - 1) {
            if (grid[row + 1][col] == '.') {
                row++
            } else if (grid[row + 1][col - 1] == '.') {
                row++
                col--
            } else if (grid[row + 1][col + 1] == '.') {
                row++
                col++
            } else {
                numSand++
                grid[row][col] = 'o'
                row = 0
                col = 500 - shift
            }
        }
        return numSand
    }

    fun part2(): Int {
        drawGrid()
        for (i in rows - 1 downTo 0 step 1) {
            if (grid[i].contains('#')) {
                grid[i + 2] = Array(cols) { '#' }
                break
            }
        }
        var numSand = 0
        var row = 0
        var col = 500 - shift
        while (grid[0][500 - shift] != 'o') {
            if (grid[row + 1][col] == '.') {
                row++
            } else if (grid[row + 1][col - 1] == '.') {
                row++
                col--
            } else if (grid[row + 1][col + 1] == '.') {
                row++
                col++
            } else {
                numSand++
                grid[row][col] = 'o'
                row = 0
                col = 500 - shift
            }
        }
        return numSand
    }

    override fun toString(): String = grid.joinToString("\n") { it.joinToString("") }
}

fun main() {
    val input = File("./input.txt").readLines()
    val lines = input.map { line -> line.split("->").map { pt -> pt.trim().split(",").map { str -> str.toInt() } } }

    val reservoir = RegolithReservoir(lines)

    println(reservoir.part1())
    println(reservoir.part2())
    println(reservoir)
}