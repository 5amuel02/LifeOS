package com.example.lifeos.ui.screens.belajar.matematika.tekateki

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

enum class RiddleAnswerStatus { UNANSWERED, CORRECT, INCORRECT }

class TekaTekiViewModel : ViewModel() {
    private val statuses = mutableStateMapOf<Int, RiddleAnswerStatus>()

    val total: Int = riddleBank.size

    val score: Int
        get() = statuses.values.count { it == RiddleAnswerStatus.CORRECT }

    fun statusOf(riddleId: Int): RiddleAnswerStatus = statuses[riddleId] ?: RiddleAnswerStatus.UNANSWERED

    fun submitAnswer(riddleId: Int, rawInput: String) {
        val riddle = riddleBank.first { it.id == riddleId }
        statuses[riddleId] = if (isRiddleAnswerCorrect(rawInput, riddle.acceptableAnswers)) {
            RiddleAnswerStatus.CORRECT
        } else {
            RiddleAnswerStatus.INCORRECT
        }
    }
}
