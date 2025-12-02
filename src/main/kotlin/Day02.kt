import java.io.File

fun main() {
    Day02().puzzle1()
    Day02().puzzle2()
}

class Day02 {
    private fun List<Long>.toRange() = this[0]..this[1]
    private val file = File("inputs/day02.txt")
    private val ranges = file.readText().trim().split(",").map { it.split("-").map { it.toLong() }.toRange() }

    fun puzzle1() {
        fun invalidProductId(id: Long) = id.toString().let {
            if (it.length % 2 == 1) false
            else {
                val mid = it.length / 2
                it.substring(0, mid) == it.substring(mid)
            }
        }

        ranges.sumOf { it.filter { invalidProductId(it) }.sum() }.printAnswer()
    }

    fun puzzle2() {
        fun Long.divisors() = this.toString().length.let { n -> (1 until n).filter { n % it == 0 } } // exclude self
        fun invalidProductId(id: Long) = id.toString().let { str ->
            id.divisors().any { div -> str.windowed(div, div).toSet().size == 1 }
        }

        ranges.sumOf { it.filter { invalidProductId(it) }.sum() }.printAnswer()
    }
}