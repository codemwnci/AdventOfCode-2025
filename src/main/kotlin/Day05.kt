import java.io.File

fun main() {
    Day05().puzzle1()
    Day05().puzzle2()
}

class Day05 {
    private val file = File("inputs/day05.txt")

    fun puzzle1() {
        val (ranges, ingredients) = file.readLines().splitOnBlank().let { (r, i) ->
            r.map { it.split("-").let { it[0].toLong()..it[1].toLong() } } to i.map { it.toLong() }
        }

        ingredients.count { i -> ranges.any { it.contains(i) } }.printAnswer()
    }

    fun puzzle2() {
        fun mergeRanges(ranges: List<LongRange>): List<LongRange> {
            val sorted = ranges.sortedBy { it.first }
            val merged = mutableListOf<LongRange>()
            var current = sorted[0]
            for (i in 1 until sorted.size) {
                val next = sorted[i]
                if (next.first <= current.last + 1) {
                    current = current.first..maxOf(current.last, next.last)
                } else {
                    merged.add(current)
                    current = next
                }
            }
            merged.add(current)
            return merged
        }

        val ranges = file.readLines().splitOnBlank().let { (r, _) ->
            r.map { it.split("-").let { it[0].toLong()..it[1].toLong() } }
        }

        mergeRanges(ranges).sumOf { it.last - it.first + 1 }.printAnswer()
    }
}