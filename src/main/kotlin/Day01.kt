import java.io.File
import kotlin.math.abs

fun main() {
    Day01().puzzle1()
    Day01().puzzle2()
}

class Day01 {
    private enum class Dir { L, R }
    private fun Char.toDir() = if (this == 'L') Dir.L else Dir.R

    private val file = File("inputs/day01.txt")
    private val rotations = file.readLines().map { it.first().toDir() to it.drop(1).toInt() }

    private fun Int.twoDigitRotate(dir: Dir, amount: Int) = when(dir) {
        Dir.L -> ((this - amount) % 100).let { if (it < 0) it + 100 else it }
        Dir.R -> (this + amount) % 100
    }

    private fun Int.didRotatePastZero(dir: Dir, amount: Int) = when(dir) {
        Dir.L -> (this - amount).let { if (it > 0) 0 else abs(it / 100) + 1 }
        Dir.R -> (this + amount) / 100
    }

    fun puzzle1() {
        rotations.runningFold(50) { last, (dir, amount) -> last.twoDigitRotate(dir, amount) }.count { it == 0 }.printAnswer()
    }

    fun puzzle2() {
        rotations.fold(50 to 0) { (pos, count), (dir, amount) ->
            val extraTick = if (pos == 0 && dir == Dir.L) -1 else 0 // offset that if we are ON zero, we will click negative if turning left, so we would otherwise count twice
            pos.twoDigitRotate(dir, amount) to count + extraTick + pos.didRotatePastZero(dir, amount)
        }.second.printAnswer()
    }
}