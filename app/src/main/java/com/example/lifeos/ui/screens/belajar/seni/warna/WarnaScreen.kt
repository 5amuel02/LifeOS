package com.example.lifeos.ui.screens.belajar.seni.warna

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun WarnaScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.seniGame.seniGameWarna,
            description = strings.seniWarna.seniWarnaDescription,
            scoreTemplate = strings.seniWarna.seniWarnaScoreTemplate,
            questionTitleTemplate = strings.seniWarna.seniWarnaQuestionTitleTemplate,
            allDoneMessage = strings.seniWarna.seniWarnaAllDoneMessage,
            correctFeedback = strings.seniWarna.seniWarnaCorrectFeedback,
            incorrectFeedback = strings.seniWarna.seniWarnaIncorrectFeedback,
            nextLabel = strings.seniWarna.seniWarnaNextQuestion,
            backToListLabel = strings.seniWarna.seniWarnaBackToList,
        ),
        questionBank = warnaQuestionBank,
        onBack = onBack
    )
}
