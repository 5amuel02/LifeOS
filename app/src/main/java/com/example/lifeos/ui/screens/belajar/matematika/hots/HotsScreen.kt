package com.example.lifeos.ui.screens.belajar.matematika.hots

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.audio.FeedbackSounds
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.matematika.MathGame
import com.example.lifeos.ui.screens.belajar.matematika.label

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotsScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: HotsViewModel = viewModel()
    val sounds = rememberFeedbackSounds()
    var selectedQuestionId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        selectedQuestionId?.let { strings.hots.hotsQuestionTitleTemplate.format(it) } ?: MathGame.HOTS.label
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
                label = "hotsContent"
            ) { questionId ->
                if (questionId == null) {
                    HotsQuestionListContent(
                        viewModel = viewModel,
                        strings = strings,
                        sounds = sounds,
                        onSelectQuestion = { selectedQuestionId = it }
                    )
                } else {
                    val question = hotsQuestionBank.first { it.id == questionId }
                    HotsQuestionDetailContent(
                        question = question,
                        status = viewModel.statusOf(questionId),
                        onSubmit = { input ->
                            viewModel.submitAnswer(questionId, input)
                            if (viewModel.statusOf(questionId) == HotsAnswerStatus.CORRECT) {
                                sounds.playCorrect()
                            } else {
                                sounds.playWrong()
                            }
                        },
                        onNext = {
                            val currentIndex = hotsQuestionBank.indexOfFirst { it.id == questionId }
                            val nextIndex = currentIndex + 1
                            selectedQuestionId = hotsQuestionBank.getOrNull(nextIndex)?.id
                        },
                        strings = strings
                    )
                }
            }
        }
    }
}

@Composable
private fun HotsQuestionListContent(
    viewModel: HotsViewModel,
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
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strings.hots.hotsScoreTemplate.format(viewModel.score, viewModel.total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = strings.hots.hotsDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(
                    visible = isAllDone,
                    enter = fadeIn() + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                ) {
                    Text(
                        text = strings.hots.hotsAllDoneMessage,
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
            items(hotsQuestionBank, key = { it.id }) { question ->
                val status = viewModel.statusOf(question.id)
                HotsQuestionRow(
                    question = question,
                    status = status,
                    strings = strings,
                    onClick = { onSelectQuestion(question.id) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun HotsQuestionRow(
    question: HotsQuestion,
    status: HotsAnswerStatus,
    strings: LifeOSStrings,
    onClick: () -> Unit,
) {
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
                Text(
                    text = strings.hots.hotsQuestionTitleTemplate.format(question.id),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = question.prompt,
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
private fun StatusIcon(status: HotsAnswerStatus) {
    val icon = when (status) {
        HotsAnswerStatus.CORRECT -> Icons.Filled.CheckCircle
        HotsAnswerStatus.INCORRECT -> Icons.Filled.Cancel
        HotsAnswerStatus.UNANSWERED -> Icons.Filled.RadioButtonUnchecked
    }
    val tint: Color = when (status) {
        HotsAnswerStatus.CORRECT -> MaterialTheme.colorScheme.primary
        HotsAnswerStatus.INCORRECT -> MaterialTheme.colorScheme.error
        HotsAnswerStatus.UNANSWERED -> MaterialTheme.colorScheme.outline
    }
    Icon(imageVector = icon, contentDescription = null, tint = tint)
}

@Composable
private fun HotsQuestionDetailContent(
    question: HotsQuestion,
    status: HotsAnswerStatus,
    onSubmit: (String) -> Unit,
    onNext: () -> Unit,
    strings: LifeOSStrings,
) {
    var input by rememberSaveable(question.id) { mutableStateOf("") }
    val hasNext = hotsQuestionBank.indexOfFirst { it.id == question.id } < hotsQuestionBank.size - 1

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
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(strings.hots.hotsAnswerLabel) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            enabled = status == HotsAnswerStatus.UNANSWERED,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { onSubmit(input) },
            enabled = input.isNotBlank() && status == HotsAnswerStatus.UNANSWERED,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(strings.hots.hotsCheckAnswerButton)
        }
        AnimatedVisibility(visible = status != HotsAnswerStatus.UNANSWERED) {
            val isCorrect = status == HotsAnswerStatus.CORRECT
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
                                strings.hots.hotsCorrectFeedback
                            } else {
                                strings.hots.hotsIncorrectFeedbackTemplate.format(question.answer)
                            },
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
                    Text(if (hasNext) strings.hots.hotsNextQuestion else strings.hots.hotsBackToList)
                }
            }
        }
    }
}
