package com.example.lifeos.ui.screens.belajar.matematika.puzzle

import android.app.Application
import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.puzzle.PuzzleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TIMER_TICK_MILLIS = 200L

class PuzzleGameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PuzzleRepository(LifeOSDatabase.getInstance(application).puzzleResultDao())
    private var timerJob: Job? = null
    private var startedAtRealtime = 0L

    var board by mutableStateOf(shuffledBoard())
        private set
    var moves by mutableIntStateOf(0)
        private set
    var elapsedMillis by mutableLongStateOf(0L)
        private set
    var isSolved by mutableStateOf(false)
        private set
    var isNewBest by mutableStateOf(false)
        private set
    var lastMoves by mutableIntStateOf(0)
        private set
    var lastElapsedMillis by mutableLongStateOf(0L)
        private set

    val bestMoves: StateFlow<Int?> = repository.getBestResult()
        .map { it?.moves }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        startTimer()
    }

    fun startNewGame() {
        timerJob?.cancel()
        board = shuffledBoard()
        moves = 0
        elapsedMillis = 0L
        isSolved = false
        isNewBest = false
        startTimer()
    }

    fun onTileClick(index: Int) {
        if (isSolved) return
        val blankIndex = board.indexOf(0)
        if (!isAdjacent(index, blankIndex)) return

        val newBoard = board.toMutableList()
        newBoard[blankIndex] = newBoard[index]
        newBoard[index] = 0
        board = newBoard
        moves += 1

        if (board == SOLVED_BOARD) {
            onSolved()
        }
    }

    private fun startTimer() {
        startedAtRealtime = SystemClock.elapsedRealtime()
        timerJob = viewModelScope.launch {
            while (!isSolved) {
                delay(TIMER_TICK_MILLIS)
                elapsedMillis = SystemClock.elapsedRealtime() - startedAtRealtime
            }
        }
    }

    private fun onSolved() {
        isSolved = true
        timerJob?.cancel()
        lastMoves = moves
        lastElapsedMillis = elapsedMillis
        viewModelScope.launch {
            val previousBest = repository.getBestMovesOnce()
            isNewBest = previousBest == null || lastMoves < previousBest
            repository.recordResult(lastMoves, lastElapsedMillis)
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
