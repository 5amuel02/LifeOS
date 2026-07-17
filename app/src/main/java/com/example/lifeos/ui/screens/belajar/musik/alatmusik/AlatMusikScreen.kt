package com.example.lifeos.ui.screens.belajar.musik.alatmusik

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun AlatMusikScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.musikGame.musikGameAlat,
            description = strings.musikAlat.musikAlatDescription,
            scoreTemplate = strings.musikAlat.musikAlatScoreTemplate,
            questionTitleTemplate = strings.musikAlat.musikAlatQuestionTitleTemplate,
            allDoneMessage = strings.musikAlat.musikAlatAllDoneMessage,
            correctFeedback = strings.musikAlat.musikAlatCorrectFeedback,
            incorrectFeedback = strings.musikAlat.musikAlatIncorrectFeedback,
            nextLabel = strings.musikAlat.musikAlatNextQuestion,
            backToListLabel = strings.musikAlat.musikAlatBackToList,
        ),
        questionBank = alatMusikQuestionBank,
        onBack = onBack
    )
}
