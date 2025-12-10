import java.io.File
import java.util.LinkedList
import java.util.Queue
import com.microsoft.z3.*

fun main() {
    Day10().puzzle1()
    Day10().puzzle2()
}
private data class Machine(val indicators: List<Boolean>, val wiring: List<List<Int>>, val joltage: List<Int>) {
    // fairly simple breadth first search approach to count the shortest number of button presses
    fun calcMinButtonPresses(): Int {
        val start = List<Boolean>(joltage.size) { false }
        val queue: Queue<Pair<List<Boolean>, Int>> = LinkedList()
        queue.add(start to 0)
        val visited = hashSetOf(start)
        while (queue.isNotEmpty()) {
            val (from, presses) = queue.poll()
            if (from == indicators) return presses
            for (button in wiring) {
                from.mapIndexed { idx, bool -> if (idx in button) !bool else bool }.let { to ->
                    if (to !in visited) {
                        visited.add(to)
                        queue.add(to to presses + 1)
                    }
                }
            }
        }
        error("Failed to match target - bfs failure")
    }

    // This one was way outside of my comfort zone, and had to resort to Reddit for advice.
    // My programmatic approaches were just way too brute force, and this needed a much more mathematical answer (hence reddit!)
    // Z3 (like previous years) was the suggested solution to solve.
    // This code is largely from other people's python code converted to Kotlin
    fun calcMinButtonPressesForJoltage(): Int {
        val ctx = Context()
        val optimizer = ctx.mkOptimize()

        val buttonVars = wiring.indices.map { i -> ctx.mkIntConst("button_$i") }
        for (buttonVar in buttonVars) {
            optimizer.Add(ctx.mkGe(buttonVar, ctx.mkInt(0)))
        }

        for (counterIdx in joltage.indices) {
            val affectingButtons = wiring.indices.filter { counterIdx in wiring[it] }
            if (affectingButtons.isEmpty()) continue

            val sum = ctx.mkAdd(*affectingButtons.map { buttonVars[it] }.toTypedArray())
            optimizer.Add(ctx.mkEq(sum, ctx.mkInt(joltage[counterIdx])))
        }

        val totalPresses = ctx.mkAdd(*buttonVars.toTypedArray())
        optimizer.MkMinimize(totalPresses)

        if (optimizer.Check() == Status.SATISFIABLE) {
            return buttonVars.sumOf { optimizer.model.evaluate(it, false).toString().toInt() }
        }
        error("No solution found")
    }
}

class Day10 {
    private val file = File("inputs/day10.txt")
    private val machines = file.readLines().map {
        val parts = Regex("\\[([^]]+)]\\s+(\\([^}]+\\))\\s+(\\{[^}]+})").matchEntire(it)!!.destructured.toList()
        Machine(
            parts[0].map { it == '#' },
            parts[1].split(" ").map { it.drop(1).dropLast(1).split(",").map { it.toInt() } },
            parts[2].drop(1).dropLast(1).split(",").map { it.toInt() }
        )
    }

    fun puzzle1() = machines.sumOf { it.calcMinButtonPresses() }.printAnswer()
    fun puzzle2() = machines.sumOf { it.calcMinButtonPressesForJoltage() }.printAnswer()
}
