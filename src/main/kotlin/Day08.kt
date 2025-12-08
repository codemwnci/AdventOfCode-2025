import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    Day08().puzzle1()
    Day08().puzzle2()
}

private fun Int.sq():Double = this.toDouble().pow(2)
private data class XYZ(val x: Int, val y: Int, val z: Int) {
    fun euqlidean(other: XYZ) = sqrt((x - other.x).sq() + (y - other.y).sq() + (z - other.z).sq())
}

class Day08 {
    private val file = File("inputs/day08.txt")
    private val coords = file.readLines().map { it.split(",").map { it.toInt() } }.map { XYZ(it[0], it[1], it[2]) }

    fun puzzle1() {
        coords.combinations(2).sortedBy { (a, b) -> a.euqlidean(b) }.take(1000).fold(listOf<Set<XYZ>>()) { circuits, (a,b) ->
            doConnect(circuits, a, b)
        }
        .map { it.size }.sortedDescending().take(3).reduce { acc, item -> acc * item }.printAnswer()
    }

    // connect the two 3d coordinates together into a new circuit, and if they (either/or) are already in other circuits
    // join them together.
    private fun doConnect(circuits: List<Set<XYZ>>, a: XYZ, b: XYZ): List<Set<XYZ>> {
        val updatedCircuits = circuits.toMutableList()
        val circuitA = updatedCircuits.removeFirstAndGet { a in it } ?: mutableSetOf(a, b)
        val circuitB = updatedCircuits.removeFirstAndGet { b in it } ?: mutableSetOf(a, b)
        updatedCircuits.addAll(setOf(circuitA + circuitB + b + a))
        return updatedCircuits
    }

    private fun MutableList<Set<XYZ>>.removeFirstAndGet(predicate: (Set<XYZ>) -> Boolean): Set<XYZ>? {
        val iterator = iterator()
        for (value in iterator) {
            if (predicate(value)) {
                iterator.remove()
                return value
            }
        }
        return null
    }

    fun puzzle2() {
        val separatedCircuits = coords.map{ setOf(it) }
        coords.combinations(2).sortedBy { (a, b) -> a.euqlidean(b) }.fold(separatedCircuits) { circuits, (a,b) ->
            val c = doConnect(circuits, a, b)
            if (c.size == 1) {
                // this is the point when the last pair are joined up. So stop here and print the answer
                (a.x.toLong() * b.x.toLong()).printAnswer()
                return
            }
            else c
        }
    }
}