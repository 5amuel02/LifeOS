package com.example.lifeos.ui.screens.belajar.seni.unsur

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun UnsurSeniScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.seniGame.seniGameUnsur,
            description = strings.seniUnsur.seniUnsurDescription,
            scoreTemplate = strings.seniUnsur.seniUnsurScoreTemplate,
            questionTitleTemplate = strings.seniUnsur.seniUnsurQuestionTitleTemplate,
            allDoneMessage = strings.seniUnsur.seniUnsurAllDoneMessage,
            correctFeedback = strings.seniUnsur.seniUnsurCorrectFeedback,
            incorrectFeedback = strings.seniUnsur.seniUnsurIncorrectFeedback,
            nextLabel = strings.seniUnsur.seniUnsurNextQuestion,
            backToListLabel = strings.seniUnsur.seniUnsurBackToList,
        ),
        questionBank = unsurSeniQuestionBank,
        onBack = onBack
    )
}
