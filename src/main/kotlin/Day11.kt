import java.io.File

fun main() {
    Day11().puzzle1()
    Day11().puzzle2()
}

class Day11 {
    private val file = File("inputs/day11.txt")
    private val connections = file.readLines().associate {
        it.split(": ").let { it[0] to it[1].split(" ") }
    }

    fun puzzle1() {
        val target = "out"
        val valid = mutableSetOf<List<String>>()
        val queue = ArrayDeque<List<String>>()
        queue.add(listOf("you"))

        while (queue.isNotEmpty()) {
            val from = queue.removeFirst()

            if (from.last() == target) {
                valid.add(from)
                continue
            }

            connections[from.last()]?.forEach {
                if (it !in from) queue.add(from + it)
            }
        }

        valid.size.printAnswer()
    }

    fun puzzle2() {
        val memo = mutableMapOf<Triple<String, Boolean, Boolean>, Long>()
        fun dfs(node: String, hasDac: Boolean = false, hasFft: Boolean = false): Long {
            if (node == "out") return if (hasDac && hasFft) 1L else 0L

            val state = Triple(node, hasDac, hasFft)
            memo[state]?.let { return it }

            val result = connections[node]?.sumOf { next ->
                dfs(next, hasDac || next == "dac", hasFft || next == "fft")
            } ?: 0L

            memo[state] = result
            return result
        }
        dfs("svr").printAnswer()
    }

}