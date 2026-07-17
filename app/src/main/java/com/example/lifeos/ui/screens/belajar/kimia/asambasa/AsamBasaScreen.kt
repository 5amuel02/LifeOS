package com.example.lifeos.ui.screens.belajar.kimia.asambasa

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun AsamBasaScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.kimiaGame.kimiaGameAsamBasa,
            description = strings.kimiaAsamBasa.kimiaAsamBasaDescription,
            scoreTemplate = strings.kimiaAsamBasa.kimiaAsamBasaScoreTemplate,
            questionTitleTemplate = strings.kimiaAsamBasa.kimiaAsamBasaQuestionTitleTemplate,
            allDoneMessage = strings.kimiaAsamBasa.kimiaAsamBasaAllDoneMessage,
            correctFeedback = strings.kimiaAsamBasa.kimiaAsamBasaCorrectFeedback,
            incorrectFeedback = strings.kimiaAsamBasa.kimiaAsamBasaIncorrectFeedback,
            nextLabel = strings.kimiaAsamBasa.kimiaAsamBasaNextQuestion,
            backToListLabel = strings.kimiaAsamBasa.kimiaAsamBasaBackToList,
        ),
        questionBank = asamBasaQuestionBank,
        onBack = onBack
    )
}
