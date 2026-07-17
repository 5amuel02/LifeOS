package com.example.lifeos.ui.screens.belajar.fisika.energi

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun EnergiScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.fisikaGame.fisikaGameEnergi,
            description = strings.fisikaEnergi.fisikaEnergiDescription,
            scoreTemplate = strings.fisikaEnergi.fisikaEnergiScoreTemplate,
            questionTitleTemplate = strings.fisikaEnergi.fisikaEnergiQuestionTitleTemplate,
            allDoneMessage = strings.fisikaEnergi.fisikaEnergiAllDoneMessage,
            correctFeedback = strings.fisikaEnergi.fisikaEnergiCorrectFeedback,
            incorrectFeedback = strings.fisikaEnergi.fisikaEnergiIncorrectFeedback,
            nextLabel = strings.fisikaEnergi.fisikaEnergiNextQuestion,
            backToListLabel = strings.fisikaEnergi.fisikaEnergiBackToList,
        ),
        questionBank = energiQuestionBank,
        onBack = onBack
    )
}
