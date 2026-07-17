package com.example.lifeos.ui.screens.belajar.matematika.logika

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

enum class LogicAnswerStatus { UNANSWERED, CORRECT, INCORRECT }

class LogikaViewModel : ViewModel() {
    private val statuses = mutableStateMapOf<Int, LogicAnswerStatus>()
    private val selectedChoices = mutableStateMapOf<Int, Int>()

    val total: Int = logicPuzzleBank.size

    val score: Int
        get() = statuses.values.count { it == LogicAnswerStatus.CORRECT }

    fun statusOf(puzzleId: Int): LogicAnswerStatus = statuses[puzzleId] ?: LogicAnswerStatus.UNANSWERED

    fun selectedChoiceOf(puzzleId: Int): Int? = selectedChoices[puzzleId]

    fun submitAnswer(puzzleId: Int, choiceIndex: Int, correctIndex: Int) {
        if (statuses[puzzleId] != null) return
        selectedChoices[puzzleId] = choiceIndex
        statuses[puzzleId] = if (choiceIndex == correctIndex) LogicAnswerStatus.CORRECT else LogicAnswerStatus.INCORRECT
    }
}
