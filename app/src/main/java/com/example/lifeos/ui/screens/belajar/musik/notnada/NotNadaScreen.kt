package com.example.lifeos.ui.screens.belajar.musik.notnada

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun NotNadaScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.musikGame.musikGameNotNada,
            description = strings.musikNotNada.musikNotNadaDescription,
            scoreTemplate = strings.musikNotNada.musikNotNadaScoreTemplate,
            questionTitleTemplate = strings.musikNotNada.musikNotNadaQuestionTitleTemplate,
            allDoneMessage = strings.musikNotNada.musikNotNadaAllDoneMessage,
            correctFeedback = strings.musikNotNada.musikNotNadaCorrectFeedback,
            incorrectFeedback = strings.musikNotNada.musikNotNadaIncorrectFeedback,
            nextLabel = strings.musikNotNada.musikNotNadaNextQuestion,
            backToListLabel = strings.musikNotNada.musikNotNadaBackToList,
        ),
        questionBank = notNadaQuestionBank,
        onBack = onBack
    )
}
