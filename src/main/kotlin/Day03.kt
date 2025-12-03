import java.io.File

fun main() {
    Day03().puzzle1()
    Day03().puzzle2()
}

class Day03 {
    private val file = File("inputs/day03.txt")
    private val input = file.readLines()

    fun List<Char>.toNum() = this.joinToString("").toLong()

    fun puzzle1() {
        input.sumOf {
            it.drop(2).fold(it.take(2).toList()) { acc, ch ->
                val opt1 = acc.drop(1) + ch
                val opt2 = acc.dropLast(1) + ch

                if (acc.toNum() > opt1.toNum() && acc.toNum() > opt2.toNum()) acc
                else if (opt1.toNum() > opt2.toNum()) opt1
                else opt2
            }.toNum()
        }.printAnswer()
    }

    fun puzzle2() {
        input.sumOf {
            it.drop(12).fold(it.take(12).toList()) { acc, ch ->
                val thirteen = acc + ch
                thirteen.mapIndexed { index, _ ->
                    thirteen.subList(0, index) + thirteen.subList(index + 1, thirteen.size)
                }.maxBy { it.toNum() }
            }.toNum()
        }.printAnswer()
    }
}