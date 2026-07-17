package com.example.lifeos.ui.screens.belajar.kimia.ikatan

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun IkatanScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.kimiaGame.kimiaGameIkatan,
            description = strings.kimiaIkatan.kimiaIkatanDescription,
            scoreTemplate = strings.kimiaIkatan.kimiaIkatanScoreTemplate,
            questionTitleTemplate = strings.kimiaIkatan.kimiaIkatanQuestionTitleTemplate,
            allDoneMessage = strings.kimiaIkatan.kimiaIkatanAllDoneMessage,
            correctFeedback = strings.kimiaIkatan.kimiaIkatanCorrectFeedback,
            incorrectFeedback = strings.kimiaIkatan.kimiaIkatanIncorrectFeedback,
            nextLabel = strings.kimiaIkatan.kimiaIkatanNextQuestion,
            backToListLabel = strings.kimiaIkatan.kimiaIkatanBackToList,
        ),
        questionBank = ikatanQuestionBank,
        onBack = onBack
    )
}
