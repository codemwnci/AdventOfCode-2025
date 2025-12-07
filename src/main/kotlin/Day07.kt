import java.io.File

fun main() {
    Day07().puzzle1()
    Day07().puzzle2()
}

class Day07 {
    private data class YX(val y:Int, val x:Int)

    private val file = File("inputs/day07.txt")
    private val grid = file.readLines().map { it.map { it } }
    private val start = grid[0].indexOf('S').let { YX(0,it) }

    fun puzzle1() {
        // This commented code is the "mutable" version. this is definitely more readable than the immutable version below
        // but the results are the same (and my goals is to maximise immutability where possible.
//        var splitCount = 0
//        grid.indices.fold(setOf(start)) { beams, y ->
//            if (y == grid.lastIndex) beams
//            else {
//                beams.flatMap { beam ->
//                    if (grid[y+1][beam.x] == '^') {
//                        splitCount++
//                        listOf(YX(y+1, beam.x-1), YX(y+1, beam.x+1))
//                    }
//                    else listOf(YX(y+1, beam.x))
//                }.toSet()
//            }
//        }
//        splitCount.printAnswer()
        grid.indices.fold(setOf(start) to 0) { (beams, count), y ->
            if (y == grid.lastIndex) beams to count
            else {
                beams.flatMap { beam ->
                    if (grid[y+1][beam.x] == '^') listOf(YX(y+1, beam.x-1), YX(y+1, beam.x+1))
                    else listOf(YX(y+1, beam.x))
                }.toSet() to (count + beams.count { grid[y+1][it.x] == '^' })
            }
        }
        .second.printAnswer()

    }

    fun puzzle2() {
        grid.indices.fold(mapOf(start to 1L)) { beamCounts, y ->
            if (y == grid.lastIndex) beamCounts
            else buildMap {
                beamCounts.forEach { (beam, count) ->
                    if (grid[y + 1][beam.x] == '^') {
                        YX(y + 1, beam.x - 1).let { left -> put(left, getOrDefault(left, 0L) + count) }
                        YX(y + 1, beam.x + 1).let { right -> put(right, getOrDefault(right, 0L) + count) }
                    } else {
                        YX(y + 1, beam.x).let { next -> put(next, getOrDefault(next, 0L) + count) }
                    }
                }
            }
        }.values.sum().printAnswer()
    }
}