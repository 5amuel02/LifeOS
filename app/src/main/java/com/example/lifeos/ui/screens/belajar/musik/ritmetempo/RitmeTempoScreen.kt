package com.example.lifeos.ui.screens.belajar.musik.ritmetempo

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun RitmeTempoScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.musikGame.musikGameRitme,
            description = strings.musikRitme.musikRitmeDescription,
            scoreTemplate = strings.musikRitme.musikRitmeScoreTemplate,
            questionTitleTemplate = strings.musikRitme.musikRitmeQuestionTitleTemplate,
            allDoneMessage = strings.musikRitme.musikRitmeAllDoneMessage,
            correctFeedback = strings.musikRitme.musikRitmeCorrectFeedback,
            incorrectFeedback = strings.musikRitme.musikRitmeIncorrectFeedback,
            nextLabel = strings.musikRitme.musikRitmeNextQuestion,
            backToListLabel = strings.musikRitme.musikRitmeBackToList,
        ),
        questionBank = ritmeTempoQuestionBank,
        onBack = onBack
    )
}
