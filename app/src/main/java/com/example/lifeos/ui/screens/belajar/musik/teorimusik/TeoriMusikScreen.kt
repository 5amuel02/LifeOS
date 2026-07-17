package com.example.lifeos.ui.screens.belajar.musik.teorimusik

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun TeoriMusikScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.musikGame.musikGameTeori,
            description = strings.musikTeori.musikTeoriDescription,
            scoreTemplate = strings.musikTeori.musikTeoriScoreTemplate,
            questionTitleTemplate = strings.musikTeori.musikTeoriQuestionTitleTemplate,
            allDoneMessage = strings.musikTeori.musikTeoriAllDoneMessage,
            correctFeedback = strings.musikTeori.musikTeoriCorrectFeedback,
            incorrectFeedback = strings.musikTeori.musikTeoriIncorrectFeedback,
            nextLabel = strings.musikTeori.musikTeoriNextQuestion,
            backToListLabel = strings.musikTeori.musikTeoriBackToList,
        ),
        questionBank = teoriMusikQuestionBank,
        onBack = onBack
    )
}
