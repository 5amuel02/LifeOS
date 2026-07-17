package com.example.lifeos.ui.screens.belajar.bahasainggris.susunkalimat

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.audio.FeedbackSounds
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SusunKalimatScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: SusunKalimatViewModel = viewModel()
    val sounds = rememberFeedbackSounds()
    var selectedQuestionId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        selectedQuestionId?.let { strings.susunKalimat.susunKalimatQuestionTitleTemplate.format(it) }
                            ?: strings.bahasaInggrisGame.bahasaInggrisGameSusunKalimat
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
                label = "susunKalimatContent"
            ) { questionId ->
                if (questionId == null) {
                    SentenceListContent(
                        viewModel = viewModel,
                        strings = strings,
                        sounds = sounds,
                        onSelectQuestion = { selectedQuestionId = it }
                    )
                } else {
                    val question = sentenceQuestionBank.first { it.id == questionId }
                    val currentIndex = sentenceQuestionBank.indexOfFirst { it.id == questionId }
                    val hasNext = currentIndex < sentenceQuestionBank.size - 1
                    SentenceDetailContent(
                        question = question,
                        status = viewModel.statusOf(questionId),
                        placedIndices = viewModel.placedIndicesFor(questionId),
                        shuffledIndices = viewModel.shuffledIndicesFor(question),
                        strings = strings,
                        hasNext = hasNext,
                        onPlaceWord = { wordIndex -> viewModel.placeWord(questionId, wordIndex) },
                        onRemoveWord = { wordIndex -> viewModel.removeWord(questionId, wordIndex) },
                        onReset = { viewModel.resetPlacement(questionId) },
                        onCheck = {
                            viewModel.checkAnswer(question)
                            if (viewModel.statusOf(questionId) == SentenceStatus.CORRECT) {
                                sounds.playCorrect()
                            } else {
                                sounds.playWrong()
                            }
                        },
                        onNext = { selectedQuestionId = sentenceQuestionBank.getOrNull(currentIndex + 1)?.id }
                    )
                }
            }
        }
    }
}

@Composable
private fun SentenceListContent(
    viewModel: SusunKalimatViewModel,
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
                    text = strings.susunKalimat.susunKalimatScoreTemplate.format(viewModel.score, viewModel.total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = strings.susunKalimat.susunKalimatDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(
                    visible = isAllDone,
                    enter = fadeIn(tween(300)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy), initialScale = 0.7f)
                ) {
                    Text(
                        text = strings.susunKalimat.susunKalimatAllDoneMessage,
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
            items(sentenceQuestionBank, key = { it.id }) { question ->
                val status = viewModel.statusOf(question.id)
                SentenceRow(
                    title = strings.susunKalimat.susunKalimatQuestionTitleTemplate.format(question.id),
                    hint = question.translationHint,
                    status = status,
                    onClick = { onSelectQuestion(question.id) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun SentenceRow(title: String, hint: String, status: SentenceStatus, onClick: () -> Unit) {
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
                    text = hint,
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
private fun StatusIcon(status: SentenceStatus) {
    val icon = when (status) {
        SentenceStatus.CORRECT -> Icons.Filled.CheckCircle
        SentenceStatus.INCORRECT -> Icons.Filled.Cancel
        SentenceStatus.UNANSWERED -> Icons.Filled.RadioButtonUnchecked
    }
    val tint = when (status) {
        SentenceStatus.CORRECT -> MaterialTheme.colorScheme.primary
        SentenceStatus.INCORRECT -> MaterialTheme.colorScheme.error
        SentenceStatus.UNANSWERED -> MaterialTheme.colorScheme.outline
    }
    Icon(imageVector = icon, contentDescription = null, tint = tint)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SentenceDetailContent(
    question: SentenceQuestion,
    status: SentenceStatus,
    placedIndices: List<Int>,
    shuffledIndices: List<Int>,
    strings: LifeOSStrings,
    hasNext: Boolean,
    onPlaceWord: (Int) -> Unit,
    onRemoveWord: (Int) -> Unit,
    onReset: () -> Unit,
    onCheck: () -> Unit,
    onNext: () -> Unit,
) {
    val revealed = status != SentenceStatus.UNANSWERED

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = strings.susunKalimat.susunKalimatHintLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = question.translationHint,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                placedIndices.forEach { wordIndex ->
                    WordChip(
                        text = question.words[wordIndex],
                        onClick = { onRemoveWord(wordIndex) },
                        enabled = !revealed,
                        highlight = true
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            shuffledIndices.filter { it !in placedIndices }.forEach { wordIndex ->
                WordChip(
                    text = question.words[wordIndex],
                    onClick = { onPlaceWord(wordIndex) },
                    enabled = !revealed,
                    highlight = false
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = onReset,
                enabled = !revealed && placedIndices.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text(strings.susunKalimat.susunKalimatResetButton)
            }
            Button(
                onClick = onCheck,
                enabled = !revealed && placedIndices.size == question.words.size,
                modifier = Modifier.weight(1f)
            ) {
                Text(strings.susunKalimat.susunKalimatCheckButton)
            }
        }
        AnimatedVisibility(visible = revealed) {
            val isCorrect = status == SentenceStatus.CORRECT
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
                            text = if (isCorrect) {
                                strings.susunKalimat.susunKalimatCorrectFeedback
                            } else {
                                strings.susunKalimat.susunKalimatIncorrectFeedback
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            text = question.words.joinToString(" ") + ".",
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
                    Text(if (hasNext) strings.susunKalimat.susunKalimatNextQuestion else strings.susunKalimat.susunKalimatBackToList)
                }
            }
        }
    }
}

@Composable
private fun WordChip(text: String, onClick: () -> Unit, enabled: Boolean, highlight: Boolean) {
    Card(
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}
