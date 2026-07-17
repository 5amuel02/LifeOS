package com.example.lifeos.ui.screens.belajar.kimia.wujud

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun WujudScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.kimiaGame.kimiaGameWujud,
            description = strings.kimiaWujud.kimiaWujudDescription,
            scoreTemplate = strings.kimiaWujud.kimiaWujudScoreTemplate,
            questionTitleTemplate = strings.kimiaWujud.kimiaWujudQuestionTitleTemplate,
            allDoneMessage = strings.kimiaWujud.kimiaWujudAllDoneMessage,
            correctFeedback = strings.kimiaWujud.kimiaWujudCorrectFeedback,
            incorrectFeedback = strings.kimiaWujud.kimiaWujudIncorrectFeedback,
            nextLabel = strings.kimiaWujud.kimiaWujudNextQuestion,
            backToListLabel = strings.kimiaWujud.kimiaWujudBackToList,
        ),
        questionBank = wujudQuestionBank,
        onBack = onBack
    )
}
