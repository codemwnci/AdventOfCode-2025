import java.io.File

fun main() {
    Day12().puzzle1()
}

// Definitely needed help with this one.
// Usual issue - solution would have worked, but not well optimised and would have taken too long to run
class Day12 {
    private val file = File("inputs/day12.txt")
    private val groups = file.readLines().splitOnBlank()
    private val shapes = groups.dropLast(1).map { Shape(it.drop(1).map { it.toCharArray() }) }
    private val regions = groups.last().map {
        val (size, quantities) = it.split(": ")
        val (w, h) = size.split("x").map { it.toInt() }
        Region(w, h, quantities.split(" ").map { it.toInt() }.toIntArray())
    }

    fun puzzle1() { regions.count { region -> region.canFit(shapes) }.printAnswer() }
    fun puzzle2() { } // No puzzle 2 for last day of AOC
}

private data class Shape(val grid: List<CharArray>) {
    val size = grid.sumOf { line ->  line.count { it == '#'} }
    fun rotate(): Shape {
        val rotatedGrid = List(grid.first().size) { CharArray(grid.size) { '.' } }
        for (i in 0..<grid.size) {
            for (j in 0..<grid.first().size) {
                rotatedGrid[j][grid.size - i - 1] = grid[i][j]
            }
        }
        return Shape(rotatedGrid)
    }
}


private fun Array<CharArray>.canPlace(rowIndex: Int, colIndex: Int, shape: Shape): Boolean {
    for (i in rowIndex..<(rowIndex + shape.grid.size)) {
        for (j in colIndex..<(colIndex + shape.grid.first().size)) {
            if (shape.grid[i - rowIndex][j - colIndex] == '#') {
                if (this[i][j] == '#') {
                    return false
                }
            }
        }
    }
    return true
}

private fun Array<CharArray>.place(rowIndex: Int, colIndex: Int, shape: Shape, char: Char = '#') {
    for (i in rowIndex..<(rowIndex + shape.grid.size)) {
        for (j in colIndex..<(colIndex + shape.grid.first().size)) {
            if (shape.grid[i - rowIndex][j - colIndex] == '#') {
                this[i][j] = char
            }
        }
    }
}

private data class Region(val n: Int, val m: Int, val quantities: IntArray) {
    fun canFit(shapes: List<Shape>): Boolean {
        if (n * m < quantities.withIndex().sumOf { (index, count) -> shapes[index].size * count }) return false

        val grid = Array(n) { CharArray(m) { '.' } }
        val rotatedShapes = shapes.map { shape -> (1..3).fold(listOf(shape)) { acc, _ -> acc + acc.last().rotate() } }

        fun backtrack(shapeIndex: Int): Boolean {
            if (shapeIndex == quantities.size) return true
            if (quantities[shapeIndex] == 0) return backtrack(shapeIndex + 1)
            val rotations = rotatedShapes[shapeIndex]
            for (shape in rotations) {
                for (i in 0..<(n - shape.grid.size + 1)) {
                    for (j in 0..<(m - shape.grid.first().size + 1)) {
                        if (grid.canPlace(i, j, shape)) {
                            grid.place(i, j, shape)
                            quantities[shapeIndex]--
                            if (backtrack(shapeIndex)) return true
                            grid.place(i, j, shape, '.')
                            quantities[shapeIndex]++
                        }
                    }
                }
            }
            return false
        }
        return backtrack(0)
    }
}