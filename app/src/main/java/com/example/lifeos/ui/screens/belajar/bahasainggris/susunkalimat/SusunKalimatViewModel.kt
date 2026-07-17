package com.example.lifeos.ui.screens.belajar.bahasainggris.susunkalimat

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

enum class SentenceStatus { UNANSWERED, CORRECT, INCORRECT }

class SusunKalimatViewModel : ViewModel() {
    private val statuses = mutableStateMapOf<Int, SentenceStatus>()
    private val placedIndices = mutableStateMapOf<Int, List<Int>>()
    private val shuffledOrders = mutableStateMapOf<Int, List<Int>>()

    val total: Int = sentenceQuestionBank.size

    val score: Int
        get() = statuses.values.count { it == SentenceStatus.CORRECT }

    fun statusOf(questionId: Int): SentenceStatus = statuses[questionId] ?: SentenceStatus.UNANSWERED

    fun placedIndicesFor(questionId: Int): List<Int> = placedIndices[questionId] ?: emptyList()

    fun shuffledIndicesFor(question: SentenceQuestion): List<Int> =
        shuffledOrders.getOrPut(question.id) { question.words.indices.shuffled() }

    fun placeWord(questionId: Int, wordIndex: Int) {
        if (statuses[questionId] != null) return
        val current = placedIndices[questionId] ?: emptyList()
        if (wordIndex in current) return
        placedIndices[questionId] = current + wordIndex
    }

    fun removeWord(questionId: Int, wordIndex: Int) {
        if (statuses[questionId] != null) return
        val current = placedIndices[questionId] ?: emptyList()
        placedIndices[questionId] = current.filter { it != wordIndex }
    }

    fun resetPlacement(questionId: Int) {
        if (statuses[questionId] != null) return
        placedIndices[questionId] = emptyList()
    }

    fun checkAnswer(question: SentenceQuestion) {
        val current = placedIndices[question.id] ?: emptyList()
        if (current.size != question.words.size) return
        statuses[question.id] = if (current == question.words.indices.toList()) {
            SentenceStatus.CORRECT
        } else {
            SentenceStatus.INCORRECT
        }
    }
}
