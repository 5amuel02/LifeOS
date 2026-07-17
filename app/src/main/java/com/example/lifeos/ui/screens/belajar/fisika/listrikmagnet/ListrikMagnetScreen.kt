package com.example.lifeos.ui.screens.belajar.fisika.listrikmagnet

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun ListrikMagnetScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.fisikaGame.fisikaGameListrik,
            description = strings.fisikaListrik.fisikaListrikDescription,
            scoreTemplate = strings.fisikaListrik.fisikaListrikScoreTemplate,
            questionTitleTemplate = strings.fisikaListrik.fisikaListrikQuestionTitleTemplate,
            allDoneMessage = strings.fisikaListrik.fisikaListrikAllDoneMessage,
            correctFeedback = strings.fisikaListrik.fisikaListrikCorrectFeedback,
            incorrectFeedback = strings.fisikaListrik.fisikaListrikIncorrectFeedback,
            nextLabel = strings.fisikaListrik.fisikaListrikNextQuestion,
            backToListLabel = strings.fisikaListrik.fisikaListrikBackToList,
        ),
        questionBank = listrikMagnetQuestionBank,
        onBack = onBack
    )
}
