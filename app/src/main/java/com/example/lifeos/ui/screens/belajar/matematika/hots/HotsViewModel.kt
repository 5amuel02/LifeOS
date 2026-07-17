package com.example.lifeos.ui.screens.belajar.matematika.hots

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

enum class HotsAnswerStatus { UNANSWERED, CORRECT, INCORRECT }

class HotsViewModel : ViewModel() {
    private val statuses = mutableStateMapOf<Int, HotsAnswerStatus>()

    val total: Int = hotsQuestionBank.size

    val score: Int
        get() = statuses.values.count { it == HotsAnswerStatus.CORRECT }

    fun statusOf(questionId: Int): HotsAnswerStatus = statuses[questionId] ?: HotsAnswerStatus.UNANSWERED

    fun submitAnswer(questionId: Int, rawInput: String) {
        val question = hotsQuestionBank.first { it.id == questionId }
        statuses[questionId] = if (isHotsAnswerCorrect(rawInput, question.answer)) {
            HotsAnswerStatus.CORRECT
        } else {
            HotsAnswerStatus.INCORRECT
        }
    }
}
