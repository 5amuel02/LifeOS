package com.example.lifeos.ui.screens.belajar.common

data class QuizQuestion(
    val id: Int,
    val prompt: String,
    val choices: List<String>,
    val correctIndex: Int,
    val explanation: String,
)

enum class QuizAnswerStatus { UNANSWERED, CORRECT, INCORRECT }
