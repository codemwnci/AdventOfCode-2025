import java.io.File

fun main() {
    Day04().puzzle1()
    Day04().puzzle2()
}

class Day04 {
    private val file = File("inputs/day04.txt")
    private val grid = file.readLines().map { it.toCharArray() }.toTypedArray()
    data class YX(val y: Int, val x: Int)

    private fun neighbours(xStart: Int, yStart: Int): List<YX> {
        val yRange = yStart-1 .. yStart+1
        val xRange = xStart-1 .. xStart+1

        return yRange.flatMap { y ->  xRange.map { x -> YX(y, x)  } }.filter {
            it.y in grid.indices && it.x in grid[it.y].indices && it != YX(yStart, xStart) // exclude out of bounds, and self
        }
    }

    fun puzzle1() {
        grid.flatMapIndexed { y, row ->
            row.mapIndexed { x, _ ->
                grid[y][x] == '@' && neighbours(x, y).filter { grid[it.y][it.x] == '@' }.size < 4
            }
        }.count { it } .printAnswer()
    }

    fun puzzle2() {
        fun toRemove(_g: Array<CharArray>) = _g.flatMapIndexed { y, row -> row.mapIndexed { x, _ -> YX(y, x) } }.filter { (y, x) ->
            _g[y][x] == '@' && neighbours(x, y).filter { _g[it.y][it.x] == '@' }.size < 4
        }

        fun doRemove(g: Array<CharArray>, count: Int = 0): Int {
            val rem = toRemove(g)
            if (rem.isEmpty()) return count

            val newGrid = g.mapIndexed { y, row -> row.mapIndexed { x, _ -> if (YX(y,x) in rem) '.' else g[y][x] }.toCharArray() }.toTypedArray()
            return doRemove(newGrid, rem.size + count)
        }

        doRemove(grid).printAnswer()
    }
}