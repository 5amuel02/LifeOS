package com.example.lifeos.ui.screens.belajar.seni.teknik

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun TeknikMediaScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.seniGame.seniGameTeknik,
            description = strings.seniTeknik.seniTeknikDescription,
            scoreTemplate = strings.seniTeknik.seniTeknikScoreTemplate,
            questionTitleTemplate = strings.seniTeknik.seniTeknikQuestionTitleTemplate,
            allDoneMessage = strings.seniTeknik.seniTeknikAllDoneMessage,
            correctFeedback = strings.seniTeknik.seniTeknikCorrectFeedback,
            incorrectFeedback = strings.seniTeknik.seniTeknikIncorrectFeedback,
            nextLabel = strings.seniTeknik.seniTeknikNextQuestion,
            backToListLabel = strings.seniTeknik.seniTeknikBackToList,
        ),
        questionBank = teknikMediaQuestionBank,
        onBack = onBack
    )
}
