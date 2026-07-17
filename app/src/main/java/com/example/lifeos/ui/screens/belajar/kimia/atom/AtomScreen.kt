package com.example.lifeos.ui.screens.belajar.kimia.atom

import androidx.compose.runtime.Composable
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.common.QuizChrome
import com.example.lifeos.ui.screens.belajar.common.QuizScreen

@Composable
fun AtomScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    QuizScreen(
        chrome = QuizChrome(
            screenTitle = strings.kimiaGame.kimiaGameAtom,
            description = strings.kimiaAtom.kimiaAtomDescription,
            scoreTemplate = strings.kimiaAtom.kimiaAtomScoreTemplate,
            questionTitleTemplate = strings.kimiaAtom.kimiaAtomQuestionTitleTemplate,
            allDoneMessage = strings.kimiaAtom.kimiaAtomAllDoneMessage,
            correctFeedback = strings.kimiaAtom.kimiaAtomCorrectFeedback,
            incorrectFeedback = strings.kimiaAtom.kimiaAtomIncorrectFeedback,
            nextLabel = strings.kimiaAtom.kimiaAtomNextQuestion,
            backToListLabel = strings.kimiaAtom.kimiaAtomBackToList,
        ),
        questionBank = atomQuestionBank,
        onBack = onBack
    )
}
