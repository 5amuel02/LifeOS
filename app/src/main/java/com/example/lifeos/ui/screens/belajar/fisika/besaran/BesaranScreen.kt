package com.example.lifeos.ui.screens.belajar.fisika.besaran

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun BesaranScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.fisikaGame.fisikaGameBesaran,
            description = strings.fisikaBesaran.fisikaBesaranDescription,
            scoreTemplate = strings.fisikaBesaran.fisikaBesaranScoreTemplate,
            questionTitleTemplate = strings.fisikaBesaran.fisikaBesaranQuestionTitleTemplate,
            allDoneMessage = strings.fisikaBesaran.fisikaBesaranAllDoneMessage,
            correctFeedback = strings.fisikaBesaran.fisikaBesaranCorrectFeedback,
            incorrectFeedback = strings.fisikaBesaran.fisikaBesaranIncorrectFeedback,
            nextLabel = strings.fisikaBesaran.fisikaBesaranNextQuestion,
            backToListLabel = strings.fisikaBesaran.fisikaBesaranBackToList,
        ),
        questionBank = besaranQuestionBank,
        onBack = onBack
    )
}
