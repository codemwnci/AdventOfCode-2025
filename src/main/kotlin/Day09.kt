import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day09().puzzle1()
    Day09().puzzle2()
}

private data class XY(val x: Int, val y: Int) {
    fun area(other: XY) = (abs(other.x - x).toLong()+1) * (abs(other.y - y).toLong()+1)
}

private data class Rect(val x1: Int, val x2: Int, val y1: Int, val y2: Int) {
    val area: Long get() = (x2 - x1 + 1L) * (y2 - y1 + 1L)

    constructor(p1: XY, p2: XY) : this(
        min(p1.x, p2.x), max(p1.x, p2.x),
        min(p1.y, p2.y), max(p1.y, p2.y)
    )

    fun isValid(polygon: List<XY>): Boolean {
        val edges = polygon.zipWithNext() + (polygon.last() to polygon.first())
        return edges.none { (pA, pB) -> intersectsEdge(pA, pB) }
    }

    private fun intersectsEdge(pA: XY, pB: XY): Boolean {
        return if (pA.x == pB.x) {
            val edgeX = pA.x
            val (minEy, maxEy) = min(pA.y, pB.y) to max(pA.y, pB.y)
            (edgeX in (x1 + 1)..<x2) && (max(minEy, y1) < min(maxEy, y2))
        } else {
            val edgeY = pA.y
            val (minEx, maxEx) = min(pA.x, pB.x) to max(pA.x, pB.x)
            (edgeY in (y1 + 1)..<y2) && (max(minEx, x1) < min(maxEx, x2))
        }
    }
}

class Day09 {
    private val file = File("inputs/day09.txt")
    private val coords = file.readLines().map { it.split(",").map { it.toInt() } }.map { XY(it[0], it[1]) }

    fun puzzle1() {
        coords.combinations(2).maxOf { it.first().area(it.last()) }.printAnswer()
    }

    fun puzzle2() {
        coords.combinations(2).map { (p1, p2) -> Rect(p1, p2) }.filter { it.isValid(coords) }.maxOf { it.area }.printAnswer()
    }
}