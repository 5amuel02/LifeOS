package com.example.lifeos.ui.screens.belajar.bahasainggris.listening

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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.audio.FeedbackSounds
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeningScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: ListeningViewModel = hiltViewModel()
    val sounds = rememberFeedbackSounds()
    var selectedQuestionId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        selectedQuestionId?.let { strings.listening.listeningQuestionTitleTemplate.format(it) }
                            ?: strings.bahasaInggrisGame.bahasaInggrisGameListening
                    )
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
                label = "listeningContent"
            ) { questionId ->
                if (questionId == null) {
                    ListeningListContent(
                        viewModel = viewModel,
                        strings = strings,
                        sounds = sounds,
                        onSelectQuestion = { selectedQuestionId = it }
                    )
                } else {
                    val question = listeningQuestionBank.first { it.id == questionId }
                    val currentIndex = listeningQuestionBank.indexOfFirst { it.id == questionId }
                    val hasNext = currentIndex < listeningQuestionBank.size - 1
                    ListeningDetailContent(
                        question = question,
                        status = viewModel.statusOf(questionId),
                        selectedChoice = viewModel.selectedChoiceOf(questionId),
                        isTtsReady = viewModel.isTtsReady,
                        hasNext = hasNext,
                        strings = strings,
                        onPlay = { viewModel.speak(question.spokenText) },
                        onChoiceSelected = { choiceIndex ->
                            if (choiceIndex == question.correctIndex) sounds.playCorrect() else sounds.playWrong()
                            viewModel.submitAnswer(questionId, choiceIndex, question.correctIndex)
                        },
                        onNext = { selectedQuestionId = listeningQuestionBank.getOrNull(currentIndex + 1)?.id }
                    )
                }
            }
        }
    }
}

@Composable
private fun ListeningListContent(
    viewModel: ListeningViewModel,
    strings: LifeOSStrings,
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
                    text = strings.listening.listeningScoreTemplate.format(viewModel.score, viewModel.total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = strings.listening.listeningDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(
                    visible = isAllDone,
                    enter = fadeIn(tween(300)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy), initialScale = 0.7f)
                ) {
                    Text(
                        text = strings.listening.listeningAllDoneMessage,
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
            items(listeningQuestionBank, key = { it.id }) { question ->
                val status = viewModel.statusOf(question.id)
                ListeningRow(
                    title = strings.listening.listeningQuestionTitleTemplate.format(question.id),
                    status = status,
                    onClick = { onSelectQuestion(question.id) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun ListeningRow(title: String, status: ListeningAnswerStatus, onClick: () -> Unit) {
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
            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatusIcon(status: ListeningAnswerStatus) {
    val icon = when (status) {
        ListeningAnswerStatus.CORRECT -> Icons.Filled.CheckCircle
        ListeningAnswerStatus.INCORRECT -> Icons.Filled.Cancel
        ListeningAnswerStatus.UNANSWERED -> Icons.Filled.RadioButtonUnchecked
    }
    val tint = when (status) {
        ListeningAnswerStatus.CORRECT -> MaterialTheme.colorScheme.primary
        ListeningAnswerStatus.INCORRECT -> MaterialTheme.colorScheme.error
        ListeningAnswerStatus.UNANSWERED -> MaterialTheme.colorScheme.outline
    }
    Icon(imageVector = icon, contentDescription = null, tint = tint)
}

@Composable
private fun ListeningDetailContent(
    question: ListeningQuestion,
    status: ListeningAnswerStatus,
    selectedChoice: Int?,
    isTtsReady: Boolean,
    hasNext: Boolean,
    strings: LifeOSStrings,
    onPlay: () -> Unit,
    onChoiceSelected: (Int) -> Unit,
    onNext: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = strings.listening.listeningPrompt,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onPlay, enabled = isTtsReady) {
            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
            Text(strings.listening.listeningPlayButton)
        }
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            question.choices.forEachIndexed { index, choiceText ->
                ListeningChoiceRow(
                    text = choiceText,
                    isCorrect = index == question.correctIndex,
                    isSelected = index == selectedChoice,
                    revealed = status != ListeningAnswerStatus.UNANSWERED,
                    onClick = { onChoiceSelected(index) }
                )
            }
        }
        AnimatedVisibility(visible = status != ListeningAnswerStatus.UNANSWERED) {
            val isCorrect = status == ListeningAnswerStatus.CORRECT
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
                            text = if (isCorrect) strings.listening.listeningCorrectFeedback else strings.listening.listeningIncorrectFeedback,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            text = question.note,
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
                    Text(if (hasNext) strings.listening.listeningNextQuestion else strings.listening.listeningBackToList)
                }
            }
        }
    }
}

@Composable
private fun ListeningChoiceRow(
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
        label = "listeningChoiceBorderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = when {
            !revealed -> MaterialTheme.colorScheme.surface
            isCorrect -> MaterialTheme.colorScheme.primaryContainer
            isSelected -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surface
        },
        label = "listeningChoiceContainerColor"
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
