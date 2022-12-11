import java.io.File

class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: (Long) -> Boolean,
    val ifTrue: Int,
    val ifFalse: Int,
    var numInspects: Int = 0
)

fun main(args: Array<String>) {
    val input = File("./input.txt").readLines()

    val monkeys = mutableMapOf<Int, Monkey>()

    var num: Int? = null
    var items: MutableList<Long>? = null
    var operation: ((Long) -> Long)? = null
    var test: ((Long) -> Boolean)? = null
    var ifTrue: Int? = null
    var ifFalse: Int? = null

    var lcm = 1

    for (line in input) {
        val s = line.trim()
        if (s.startsWith("Monkey")) {
            num = s.substringAfter("Monkey ").substringBefore(":").toInt()
        } else if (s.startsWith("Starting items: ")) {
            items = s.substringAfter("Starting items: ").split(", ").map { it.toLong() }.toMutableList()
        } else if (s.startsWith("Operation: ")) {
            val components = s.substringAfter("Operation: new = old ").split(" ")
            operation = fun(old: Long): Long {
                val n = when (components.last()) {
                    "old" -> old
                    else -> components.last().toLong()
                }
                return when (components.first()) {
                    "+" -> old + n
                    "-" -> old - n
                    "*" -> old * n
                    "/" -> old * n
                    else -> old
                }
            }
        } else if (s.startsWith("Test: ")) {
            val divisor = s.split(" ").last().toInt()
            test = { it % divisor == 0L }
            lcm *= divisor
        } else if (s.startsWith("If true: ")) {
            ifTrue = s.split(" ").last().toInt()
        } else if (s.startsWith("If false: ")) {
            ifFalse = s.split(" ").last().toInt()
        }
        try {
            monkeys[num!!] = Monkey(items!!, operation!!, test!!, ifTrue!!, ifFalse!!)
            num = null
            items = null
            operation = null
            test = null
            ifTrue = null
            ifFalse = null
        } catch (e: NullPointerException) {
            continue
        }
    }

    val part = args[0].toInt()
    val rounds = if (part == 1) 20 else 10000
    for (i in 0 until rounds) {
        for (monkey in monkeys.values) {
            monkey.numInspects += monkey.items.size
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.removeFirst()
                val worryLevel = (if (part == 1) monkey.operation(item) / 3L else monkey.operation(item)) % lcm
                if (monkey.test(worryLevel)) {
                    monkeys[monkey.ifTrue]?.items?.add(worryLevel)
                } else {
                    monkeys[monkey.ifFalse]?.items?.add(worryLevel)
                }
            }
        }
    }

    val monkeyList = monkeys.values.sortedWith(compareByDescending { it.numInspects })
    val first = monkeyList[0].numInspects
    val second = monkeyList[1].numInspects
    println("Result: ${first.toLong() * second.toLong()}")
}