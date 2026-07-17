package com.example.lifeos.ui.screens.belajar.matematika.tekateki

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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
fun TekaTekiScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: TekaTekiViewModel = viewModel()
    val sounds = rememberFeedbackSounds()
    var selectedRiddleId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        selectedRiddleId?.let { strings.tekaTeki.tekaTekiQuestionTitleTemplate.format(it) } ?: MathGame.TEKA_TEKI.label
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedRiddleId != null) selectedRiddleId = null else onBack()
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
                targetState = selectedRiddleId,
                transitionSpec = {
                    (fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 4 }) togetherWith
                        (fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 4 })
                },
                label = "tekaTekiContent"
            ) { riddleId ->
                if (riddleId == null) {
                    RiddleListContent(
                        viewModel = viewModel,
                        strings = strings,
                        sounds = sounds,
                        onSelectRiddle = { selectedRiddleId = it }
                    )
                } else {
                    val riddle = riddleBank.first { it.id == riddleId }
                    RiddleDetailContent(
                        riddle = riddle,
                        status = viewModel.statusOf(riddleId),
                        onSubmit = { input ->
                            viewModel.submitAnswer(riddleId, input)
                            if (viewModel.statusOf(riddleId) == RiddleAnswerStatus.CORRECT) {
                                sounds.playCorrect()
                            } else {
                                sounds.playWrong()
                            }
                        },
                        onNext = {
                            val currentIndex = riddleBank.indexOfFirst { it.id == riddleId }
                            selectedRiddleId = riddleBank.getOrNull(currentIndex + 1)?.id
                        },
                        strings = strings
                    )
                }
            }
        }
    }
}

@Composable
private fun RiddleListContent(
    viewModel: TekaTekiViewModel,
    strings: LifeOSStrings,
    sounds: FeedbackSounds,
    onSelectRiddle: (Int) -> Unit,
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
                    text = strings.tekaTeki.tekaTekiScoreTemplate.format(viewModel.score, viewModel.total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = strings.tekaTeki.tekaTekiDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(
                    visible = isAllDone,
                    enter = fadeIn() + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                ) {
                    Text(
                        text = strings.tekaTeki.tekaTekiAllDoneMessage,
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
            items(riddleBank, key = { it.id }) { riddle ->
                val status = viewModel.statusOf(riddle.id)
                RiddleRow(
                    riddle = riddle,
                    status = status,
                    strings = strings,
                    onClick = { onSelectRiddle(riddle.id) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun RiddleRow(
    riddle: RiddleQuestion,
    status: RiddleAnswerStatus,
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
                    text = strings.tekaTeki.tekaTekiQuestionTitleTemplate.format(riddle.id),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = riddle.prompt,
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
private fun StatusIcon(status: RiddleAnswerStatus) {
    val icon = when (status) {
        RiddleAnswerStatus.CORRECT -> Icons.Filled.CheckCircle
        RiddleAnswerStatus.INCORRECT -> Icons.Filled.Cancel
        RiddleAnswerStatus.UNANSWERED -> Icons.Filled.RadioButtonUnchecked
    }
    val tint = when (status) {
        RiddleAnswerStatus.CORRECT -> MaterialTheme.colorScheme.primary
        RiddleAnswerStatus.INCORRECT -> MaterialTheme.colorScheme.error
        RiddleAnswerStatus.UNANSWERED -> MaterialTheme.colorScheme.outline
    }
    Icon(imageVector = icon, contentDescription = null, tint = tint)
}

@Composable
private fun RiddleDetailContent(
    riddle: RiddleQuestion,
    status: RiddleAnswerStatus,
    onSubmit: (String) -> Unit,
    onNext: () -> Unit,
    strings: LifeOSStrings,
) {
    var input by rememberSaveable(riddle.id) { mutableStateOf("") }
    var hintRevealed by rememberSaveable(riddle.id) { mutableStateOf(false) }
    val hasNext = riddleBank.indexOfFirst { it.id == riddle.id } < riddleBank.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = riddle.prompt,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(20.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        if (hintRevealed) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = riddle.hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        } else if (status == RiddleAnswerStatus.UNANSWERED) {
            OutlinedButton(onClick = { hintRevealed = true }) {
                Icon(Icons.Filled.Lightbulb, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text(strings.tekaTeki.tekaTekiShowHintButton)
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(strings.tekaTeki.tekaTekiAnswerLabel) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = status == RiddleAnswerStatus.UNANSWERED,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { onSubmit(input) },
            enabled = input.isNotBlank() && status == RiddleAnswerStatus.UNANSWERED,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(strings.tekaTeki.tekaTekiCheckAnswerButton)
        }
        AnimatedVisibility(visible = status != RiddleAnswerStatus.UNANSWERED) {
            val isCorrect = status == RiddleAnswerStatus.CORRECT
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
                                strings.tekaTeki.tekaTekiCorrectFeedback
                            } else {
                                strings.tekaTeki.tekaTekiIncorrectFeedbackTemplate.format(riddle.displayAnswer)
                            },
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            text = riddle.explanation,
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
                    Text(if (hasNext) strings.tekaTeki.tekaTekiNextQuestion else strings.tekaTeki.tekaTekiBackToList)
                }
            }
        }
    }
}
