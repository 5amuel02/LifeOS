package com.example.lifeos.ui.screens.belajar.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lifeos.core.audio.FeedbackSounds
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LocalStrings

data class QuizChrome(
    val screenTitle: String,
    val description: String,
    val scoreTemplate: String,
    val questionTitleTemplate: String,
    val allDoneMessage: String,
    val correctFeedback: String,
    val incorrectFeedback: String,
    val nextLabel: String,
    val backToListLabel: String,
)

@Composable
private fun rememberQuizViewModel(bank: List<QuizQuestion>): QuizViewModel {
    return viewModel(
        factory = viewModelFactory {
            initializer { QuizViewModel(bank) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(chrome: QuizChrome, questionBank: List<QuizQuestion>, onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel = rememberQuizViewModel(questionBank)
    val sounds = rememberFeedbackSounds()
    var selectedQuestionId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(selectedQuestionId?.let { chrome.questionTitleTemplate.format(it) } ?: chrome.screenTitle)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedQuestionId != null) selectedQuestionId = null else onBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedQuestionId,
                transitionSpec = {
                    (fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 4 }) togetherWith
                        (fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 4 })
                },
                label = "quizContent"
            ) { questionId ->
                if (questionId == null) {
                    QuizListContent(
                        chrome = chrome,
                        questionBank = questionBank,
                        viewModel = viewModel,
                        sounds = sounds,
                        onSelectQuestion = { selectedQuestionId = it }
                    )
                } else {
                    val question = questionBank.first { it.id == questionId }
                    val currentIndex = questionBank.indexOfFirst { it.id == questionId }
                    val hasNext = currentIndex < questionBank.size - 1
                    QuizDetailContent(
                        chrome = chrome,
                        question = question,
                        status = viewModel.statusOf(questionId),
                        selectedChoice = viewModel.selectedChoiceOf(questionId),
                        hasNext = hasNext,
                        onChoiceSelected = { choiceIndex ->
                            if (choiceIndex == question.correctIndex) sounds.playCorrect() else sounds.playWrong()
                            viewModel.submitAnswer(questionId, choiceIndex, question.correctIndex)
                        },
                        onNext = { selectedQuestionId = questionBank.getOrNull(currentIndex + 1)?.id }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizListContent(
    chrome: QuizChrome,
    questionBank: List<QuizQuestion>,
    viewModel: QuizViewModel,
    sounds: FeedbackSounds,
    onSelectQuestion: (Int) -> Unit,
) {
    val isAllDone = viewModel.score == viewModel.total
    var celebrated by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isAllDone) {
        if (isAllDone && !celebrated) {
            sounds.playComplete()
            celebrated = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = chrome.scoreTemplate.format(viewModel.score, viewModel.total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = chrome.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(
                    visible = isAllDone,
                    enter = fadeIn(tween(300)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy), initialScale = 0.7f)
                ) {
                    Text(
                        text = chrome.allDoneMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(questionBank, key = { it.id }) { question ->
                val status = viewModel.statusOf(question.id)
                QuizRow(
                    title = chrome.questionTitleTemplate.format(question.id),
                    prompt = question.prompt,
                    status = status,
                    onClick = { onSelectQuestion(question.id) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun QuizRow(title: String, prompt: String, status: QuizAnswerStatus, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusIcon(status)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text(
                    text = prompt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun StatusIcon(status: QuizAnswerStatus) {
    val icon = when (status) {
        QuizAnswerStatus.CORRECT -> Icons.Filled.CheckCircle
        QuizAnswerStatus.INCORRECT -> Icons.Filled.Cancel
        QuizAnswerStatus.UNANSWERED -> Icons.Filled.RadioButtonUnchecked
    }
    val tint = when (status) {
        QuizAnswerStatus.CORRECT -> MaterialTheme.colorScheme.primary
        QuizAnswerStatus.INCORRECT -> MaterialTheme.colorScheme.error
        QuizAnswerStatus.UNANSWERED -> MaterialTheme.colorScheme.outline
    }
    Icon(imageVector = icon, contentDescription = null, tint = tint)
}

@Composable
private fun QuizDetailContent(
    chrome: QuizChrome,
    question: QuizQuestion,
    status: QuizAnswerStatus,
    selectedChoice: Int?,
    hasNext: Boolean,
    onChoiceSelected: (Int) -> Unit,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = question.prompt,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(20.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            question.choices.forEachIndexed { index, choiceText ->
                QuizChoiceRow(
                    text = choiceText,
                    isCorrect = index == question.correctIndex,
                    isSelected = index == selectedChoice,
                    revealed = status != QuizAnswerStatus.UNANSWERED,
                    onClick = { onChoiceSelected(index) }
                )
            }
        }
        AnimatedVisibility(visible = status != QuizAnswerStatus.UNANSWERED) {
            val isCorrect = status == QuizAnswerStatus.CORRECT
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCorrect) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isCorrect) chrome.correctFeedback else chrome.incorrectFeedback,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick = onNext, modifier = Modifier.fillMaxWidth()) {
                    Text(if (hasNext) chrome.nextLabel else chrome.backToListLabel)
                }
            }
        }
    }
}

@Composable
private fun QuizChoiceRow(
    text: String,
    isCorrect: Boolean,
    isSelected: Boolean,
    revealed: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            !revealed -> MaterialTheme.colorScheme.outlineVariant
            isCorrect -> MaterialTheme.colorScheme.primary
            isSelected -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        label = "choiceBorderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = when {
            !revealed -> MaterialTheme.colorScheme.surface
            isCorrect -> MaterialTheme.colorScheme.primaryContainer
            isSelected -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surface
        },
        label = "choiceContainerColor"
    )

    val scale = remember { Animatable(1f) }
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(revealed) {
        if (revealed && isCorrect) {
            scale.animateTo(1.05f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        } else if (revealed && isSelected) {
            listOf(-8f, 8f, -6f, 6f, -3f, 3f, 0f).forEach { offset ->
                shakeOffset.animateTo(offset, animationSpec = tween(45))
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = shakeOffset.value.dp)
            .scale(scale.value)
            .clickable(enabled = !revealed, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp))
    }
}
