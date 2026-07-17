package com.example.lifeos.ui.screens.belajar.seni.aliran

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun AliranTokohScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.seniGame.seniGameAliran,
            description = strings.seniAliran.seniAliranDescription,
            scoreTemplate = strings.seniAliran.seniAliranScoreTemplate,
            questionTitleTemplate = strings.seniAliran.seniAliranQuestionTitleTemplate,
            allDoneMessage = strings.seniAliran.seniAliranAllDoneMessage,
            correctFeedback = strings.seniAliran.seniAliranCorrectFeedback,
            incorrectFeedback = strings.seniAliran.seniAliranIncorrectFeedback,
            nextLabel = strings.seniAliran.seniAliranNextQuestion,
            backToListLabel = strings.seniAliran.seniAliranBackToList,
        ),
        questionBank = aliranTokohQuestionBank,
        onBack = onBack
    )
}
