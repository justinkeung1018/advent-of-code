import java.io.File

class Monkey(val job: String, var number: Long = -1) {
    override fun toString(): String = "$job, $number"
}

fun dfs(monkey: Monkey, monkeys: Map<String, Monkey>) {
    if (monkey.number != -1L) {
        return
    }
    if (monkey.job.toIntOrNull() != null) {
        monkey.number = monkey.job.toLong()
        return
    }
    val info = monkey.job.split(" ")
    val monkey1 = info[0]
    val operator = info[1]
    val monkey2 = info[2]
    dfs(monkeys[monkey1]!!, monkeys)
    dfs(monkeys[monkey2]!!, monkeys)
    val num1 = monkeys[monkey1]!!.number
    val num2 = monkeys[monkey2]!!.number
    monkey.number = when (operator) {
        "+" -> num1 + num2
        "-" -> num1 - num2
        "*" -> num1 * num2
        "/" -> num1 / num2
        else -> throw Exception("Something went wrong")
    }
}

fun equality(monkeys: Map<String, Monkey>): Long {
    var humn = 1L
    val name1 = monkeys["root"]!!.job.split(" ")[0]
    val name2 = monkeys["root"]!!.job.split(" ")[2]
    while (true) {
        val reset = monkeys.mapValues { Monkey(it.value.job, -1) }
        reset["humn"]!!.number = humn
        val monkey1 = reset[name1]!!
        val monkey2 = reset[name2]!!
        dfs(monkey1, reset)
        dfs(monkey2, reset)
        if (monkey1.number == monkey2.number) {
            return humn
        } else {
            humn++
        }
    }
}

fun main() {
    val input = File("./input.txt").readLines()
    val monkeys = mutableMapOf<String, Monkey>()
    for (line in input) {
        val info = line.split(": ")
        val name = info[0]
        val job = info[1]
        monkeys[name] = Monkey(job)
    }
    dfs(monkeys["root"]!!, monkeys)
    println("Part 1: ${monkeys["root"]!!.number}")
    println("Part 2: ${equality(monkeys)}")
}