import java.io.File

fun main() {
    Day06().puzzle1()
    Day06().puzzle2()
}

class Day06 {
    private val file = File("inputs/day06.txt")

    fun rotateClockwise(matrix: List<List<String>>): List<List<String>> {
        val rows = matrix.size
        val cols = matrix[0].size
        val rotated = Array(cols) { Array(rows) { "" } }

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                rotated[j][rows - 1 - i] = matrix[i][j]
            }
        }
        return rotated.toList().map { it.toList() }
    }

    fun puzzle1() {
        val input = file.readLines().map { it.split(" ").filter { it.isNotBlank() } }
        val sums = rotateClockwise(input)

        sums.sumOf {
            val operator = it.first()
            val nums = it.drop(1).map { it.toLong() }
            nums.reduce { acc, s ->
                when (operator) {
                    "*" -> acc * s
                    "+" -> acc + s
                    else -> throw Exception("Unknown operator")
                }
            }
        }.printAnswer()
    }

    // initially solved this by reading the whole string from right to left, and "resetting" the calculation when I encountered
    // an operator. Then rewrote the solution to split the input into "problems" by splitting on blank columns so that each
    // problem could be solved individually. Makes this a lot more "functional" with far less mutability
    fun puzzle2() {
        val lines = file.readLines()

        val eachSum = splitByBlankColumns(lines)
        eachSum.sumOf { problem ->
            val operator = problem.last().trim()
            val nums = (problem[0].length - 1 downTo 0 ).map { i -> // read Right to Left
                problem.fold("") { acc, string -> if (string.getOrNull(i)?.isDigit() ?: false) acc + string[i] else acc }.toLong()
            }

            nums.reduce { acc, s ->
                when (operator) {
                    "*" -> acc * s
                    "+" -> acc + s
                    else -> throw Exception("Unknown operator")
                }
            }
        }.printAnswer()
    }

    fun splitByBlankColumns(input: List<String>): List<List<String>> {
        if (input.isEmpty()) return emptyList()

        val maxLength = input.maxOf { it.length }
        val blankColumns = (0 until maxLength).filter { col -> input.all {
            line -> col >= line.length || line[col] == ' '
        } }

        if (blankColumns.isEmpty()) return listOf(input)

        val ranges = mutableListOf<IntRange>()
        var start = 0
        for (blankCol in blankColumns) {
            if (blankCol > start) {
                ranges.add(start until blankCol)
            }
            start = blankCol + 1
        }
        if (start < maxLength) { ranges.add(start until maxLength) }

        return ranges.map { range ->
            input.map { line ->
                val endIndex = minOf(range.last + 1, line.length)
                if (range.first < line.length) { line.substring(range.first, endIndex) }
                else { "" }
            }
        }
    }
}