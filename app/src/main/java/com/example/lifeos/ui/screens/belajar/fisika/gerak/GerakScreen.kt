package com.example.lifeos.ui.screens.belajar.fisika.gerak

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun GerakScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.fisikaGame.fisikaGameGerak,
            description = strings.fisikaGerak.fisikaGerakDescription,
            scoreTemplate = strings.fisikaGerak.fisikaGerakScoreTemplate,
            questionTitleTemplate = strings.fisikaGerak.fisikaGerakQuestionTitleTemplate,
            allDoneMessage = strings.fisikaGerak.fisikaGerakAllDoneMessage,
            correctFeedback = strings.fisikaGerak.fisikaGerakCorrectFeedback,
            incorrectFeedback = strings.fisikaGerak.fisikaGerakIncorrectFeedback,
            nextLabel = strings.fisikaGerak.fisikaGerakNextQuestion,
            backToListLabel = strings.fisikaGerak.fisikaGerakBackToList,
        ),
        questionBank = gerakQuestionBank,
        onBack = onBack
    )
}
