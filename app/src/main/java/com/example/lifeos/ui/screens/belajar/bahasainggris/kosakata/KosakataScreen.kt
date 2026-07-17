package com.example.lifeos.ui.screens.belajar.bahasainggris.kosakata

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun KosakataScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.bahasaInggrisGame.bahasaInggrisGameKosakata,
            description = strings.kosakata.kosakataDescription,
            scoreTemplate = strings.kosakata.kosakataScoreTemplate,
            questionTitleTemplate = strings.kosakata.kosakataQuestionTitleTemplate,
            allDoneMessage = strings.kosakata.kosakataAllDoneMessage,
            correctFeedback = strings.kosakata.kosakataCorrectFeedback,
            incorrectFeedback = strings.kosakata.kosakataIncorrectFeedback,
            nextLabel = strings.kosakata.kosakataNextQuestion,
            backToListLabel = strings.kosakata.kosakataBackToList,
        ),
        questionBank = kosakataQuestionBank,
        onBack = onBack
    )
}
