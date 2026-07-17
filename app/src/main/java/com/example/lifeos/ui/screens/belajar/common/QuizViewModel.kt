package com.example.lifeos.ui.screens.belajar.common

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

/** Generic scored multiple-choice quiz state, reused by any subject's quiz-style game. */
class QuizViewModel(private val bank: List<QuizQuestion>) : ViewModel() {
    private val statuses = mutableStateMapOf<Int, QuizAnswerStatus>()
    private val selectedChoices = mutableStateMapOf<Int, Int>()

    val total: Int = bank.size

    val score: Int
        get() = statuses.values.count { it == QuizAnswerStatus.CORRECT }

    fun statusOf(questionId: Int): QuizAnswerStatus = statuses[questionId] ?: QuizAnswerStatus.UNANSWERED

    fun selectedChoiceOf(questionId: Int): Int? = selectedChoices[questionId]

    fun submitAnswer(questionId: Int, choiceIndex: Int, correctIndex: Int) {
        if (statuses[questionId] != null) return
        selectedChoices[questionId] = choiceIndex
        statuses[questionId] = if (choiceIndex == correctIndex) QuizAnswerStatus.CORRECT else QuizAnswerStatus.INCORRECT
    }
}
