package com.example.lifeos.ui.screens.belajar.matematika.puzzle

import kotlin.math.abs
import kotlin.random.Random

const val GRID_SIZE = 4
private const val SHUFFLE_STEPS = 200

/** 0 represents the empty slot; 1..15 are the numbered tiles. */
val SOLVED_BOARD: List<Int> = (1..15).toList() + 0

/** Shuffles by performing random legal slides from the solved state, so the result is always solvable. */
fun shuffledBoard(random: Random = Random(System.nanoTime())): List<Int> {
    val state = SOLVED_BOARD.toMutableList()
    var blankIndex = state.indexOf(0)
    var lastBlankIndex = -1
    repeat(SHUFFLE_STEPS) {
        val candidates = neighborsOf(blankIndex).filter { it != lastBlankIndex }
        val next = candidates.random(random)
        state[blankIndex] = state[next]
        state[next] = 0
        lastBlankIndex = blankIndex
        blankIndex = next
    }
    return state
}

fun neighborsOf(index: Int): List<Int> {
    val row = index / GRID_SIZE
    val col = index % GRID_SIZE
    val result = mutableListOf<Int>()
    if (row > 0) result.add(index - GRID_SIZE)
    if (row < GRID_SIZE - 1) result.add(index + GRID_SIZE)
    if (col > 0) result.add(index - 1)
    if (col < GRID_SIZE - 1) result.add(index + 1)
    return result
}

fun isAdjacent(indexA: Int, indexB: Int): Boolean {
    val rowA = indexA / GRID_SIZE
    val colA = indexA % GRID_SIZE
    val rowB = indexB / GRID_SIZE
    val colB = indexB % GRID_SIZE
    return (rowA == rowB && abs(colA - colB) == 1) || (colA == colB && abs(rowA - rowB) == 1)
}
