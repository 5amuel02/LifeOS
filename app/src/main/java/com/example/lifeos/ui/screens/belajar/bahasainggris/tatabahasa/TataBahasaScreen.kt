package com.example.lifeos.ui.screens.belajar.bahasainggris.tatabahasa

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun TataBahasaScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.bahasaInggrisGame.bahasaInggrisGameTataBahasa,
            description = strings.tataBahasa.tataBahasaDescription,
            scoreTemplate = strings.tataBahasa.tataBahasaScoreTemplate,
            questionTitleTemplate = strings.tataBahasa.tataBahasaQuestionTitleTemplate,
            allDoneMessage = strings.tataBahasa.tataBahasaAllDoneMessage,
            correctFeedback = strings.tataBahasa.tataBahasaCorrectFeedback,
            incorrectFeedback = strings.tataBahasa.tataBahasaIncorrectFeedback,
            nextLabel = strings.tataBahasa.tataBahasaNextQuestion,
            backToListLabel = strings.tataBahasa.tataBahasaBackToList,
        ),
        questionBank = tataBahasaQuestionBank,
        onBack = onBack
    )
}
