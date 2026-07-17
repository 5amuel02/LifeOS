package com.example.lifeos.ui.screens.belajar.matematika.logika

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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Timeline
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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
fun LogikaScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: LogikaViewModel = viewModel()
    val sounds = rememberFeedbackSounds()
    var selectedPuzzleId by rememberSaveable { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        selectedPuzzleId?.let { strings.logika.logikaPuzzleTitleTemplate.format(it) } ?: MathGame.LOGIKA.label
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (selectedPuzzleId != null) selectedPuzzleId = null else onBack()
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
                targetState = selectedPuzzleId,
                transitionSpec = {
                    (fadeIn(tween(250)) + slideInHorizontally(tween(250)) { it / 4 }) togetherWith
                        (fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 4 })
                },
                label = "logikaContent"
            ) { puzzleId ->
                if (puzzleId == null) {
                    LogikaPuzzleListContent(
                        viewModel = viewModel,
                        strings = strings,
                        sounds = sounds,
                        onSelectPuzzle = { selectedPuzzleId = it }
                    )
                } else {
                    val puzzle = logicPuzzleBank.first { it.id == puzzleId }
                    LogikaPuzzleDetailContent(
                        puzzle = puzzle,
                        status = viewModel.statusOf(puzzleId),
                        selectedChoice = viewModel.selectedChoiceOf(puzzleId),
                        onChoiceSelected = { choiceIndex, correctIndex ->
                            if (choiceIndex == correctIndex) sounds.playCorrect() else sounds.playWrong()
                            viewModel.submitAnswer(puzzleId, choiceIndex, correctIndex)
                        },
                        onNext = {
                            val currentIndex = logicPuzzleBank.indexOfFirst { it.id == puzzleId }
                            selectedPuzzleId = logicPuzzleBank.getOrNull(currentIndex + 1)?.id
                        },
                        strings = strings
                    )
                }
            }
        }
    }
}

@Composable
private fun LogikaPuzzleListContent(
    viewModel: LogikaViewModel,
    strings: LifeOSStrings,
    sounds: FeedbackSounds,
    onSelectPuzzle: (Int) -> Unit,
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
                    text = strings.logika.logikaScoreTemplate.format(viewModel.score, viewModel.total),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = strings.logika.logikaDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
                AnimatedVisibility(
                    visible = isAllDone,
                    enter = fadeIn() + scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                ) {
                    Text(
                        text = strings.logika.logikaAllDoneMessage,
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
            items(logicPuzzleBank, key = { it.id }) { puzzle ->
                val status = viewModel.statusOf(puzzle.id)
                LogikaPuzzleRow(
                    puzzle = puzzle,
                    status = status,
                    strings = strings,
                    onClick = { onSelectPuzzle(puzzle.id) }
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun LogikaPuzzleRow(
    puzzle: LogicPuzzle,
    status: LogicAnswerStatus,
    strings: LifeOSStrings,
    onClick: () -> Unit,
) {
    val (typeIcon, previewText) = when (puzzle) {
        is LogicPuzzle.ShapePattern -> Icons.Filled.Timeline to puzzle.instruction
        is LogicPuzzle.OddOneOut -> Icons.Filled.FilterAlt to puzzle.instruction
        is LogicPuzzle.TextLogic -> Icons.Filled.Psychology to puzzle.prompt
    }
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
            Icon(imageVector = typeIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = strings.logika.logikaPuzzleTitleTemplate.format(puzzle.id),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = previewText,
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
private fun StatusIcon(status: LogicAnswerStatus) {
    val icon = when (status) {
        LogicAnswerStatus.CORRECT -> Icons.Filled.CheckCircle
        LogicAnswerStatus.INCORRECT -> Icons.Filled.Cancel
        LogicAnswerStatus.UNANSWERED -> Icons.Filled.RadioButtonUnchecked
    }
    val tint = when (status) {
        LogicAnswerStatus.CORRECT -> MaterialTheme.colorScheme.primary
        LogicAnswerStatus.INCORRECT -> MaterialTheme.colorScheme.error
        LogicAnswerStatus.UNANSWERED -> MaterialTheme.colorScheme.outline
    }
    Icon(imageVector = icon, contentDescription = null, tint = tint)
}

@Composable
private fun LogikaPuzzleDetailContent(
    puzzle: LogicPuzzle,
    status: LogicAnswerStatus,
    selectedChoice: Int?,
    onChoiceSelected: (choiceIndex: Int, correctIndex: Int) -> Unit,
    onNext: () -> Unit,
    strings: LifeOSStrings,
) {
    val hasNext = logicPuzzleBank.indexOfFirst { it.id == puzzle.id } < logicPuzzleBank.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        when (puzzle) {
            is LogicPuzzle.ShapePattern -> ShapePatternContent(puzzle, status, selectedChoice, strings, onChoiceSelected)
            is LogicPuzzle.OddOneOut -> OddOneOutContent(puzzle, status, selectedChoice, onChoiceSelected)
            is LogicPuzzle.TextLogic -> TextLogicContent(puzzle, status, selectedChoice, onChoiceSelected)
        }

        AnimatedVisibility(visible = status != LogicAnswerStatus.UNANSWERED) {
            val isCorrect = status == LogicAnswerStatus.CORRECT
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
                            text = if (isCorrect) strings.logika.logikaCorrectFeedback else strings.logika.logikaIncorrectFeedback,
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }
                        )
                        Text(
                            text = puzzle.explanation,
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
                    Text(if (hasNext) strings.logika.logikaNextPuzzle else strings.logika.logikaBackToList)
                }
            }
        }
    }
}

@Composable
private fun ShapePatternContent(
    puzzle: LogicPuzzle.ShapePattern,
    status: LogicAnswerStatus,
    selectedChoice: Int?,
    strings: LifeOSStrings,
    onChoiceSelected: (Int, Int) -> Unit,
) {
    Text(text = puzzle.instruction, style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        puzzle.sequence.forEach { token -> PuzzleTokenGlyph(token = token, unitSize = 36.dp) }
        QuestionMarkBox(size = 36.dp)
    }
    Spacer(Modifier.height(24.dp))
    Text(text = strings.logika.logikaChooseNextLabel, style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(8.dp))
    ChoiceTokenGrid(
        choices = puzzle.choices,
        correctIndex = puzzle.correctIndex,
        status = status,
        selectedChoice = selectedChoice,
        onChoiceSelected = onChoiceSelected
    )
}

@Composable
private fun OddOneOutContent(
    puzzle: LogicPuzzle.OddOneOut,
    status: LogicAnswerStatus,
    selectedChoice: Int?,
    onChoiceSelected: (Int, Int) -> Unit,
) {
    Text(text = puzzle.instruction, style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(20.dp))
    ChoiceTokenGrid(
        choices = puzzle.options,
        correctIndex = puzzle.correctIndex,
        status = status,
        selectedChoice = selectedChoice,
        onChoiceSelected = onChoiceSelected
    )
}

@Composable
private fun TextLogicContent(
    puzzle: LogicPuzzle.TextLogic,
    status: LogicAnswerStatus,
    selectedChoice: Int?,
    onChoiceSelected: (Int, Int) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(text = puzzle.prompt, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(20.dp))
    }
    Spacer(Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        puzzle.choices.forEachIndexed { index, choiceText ->
            TextChoiceRow(
                text = choiceText,
                isCorrect = index == puzzle.correctIndex,
                isSelected = index == selectedChoice,
                revealed = status != LogicAnswerStatus.UNANSWERED,
                onClick = { onChoiceSelected(index, puzzle.correctIndex) }
            )
        }
    }
}

@Composable
private fun TextChoiceRow(
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
        label = "textChoiceBorderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = when {
            !revealed -> MaterialTheme.colorScheme.surface
            isCorrect -> MaterialTheme.colorScheme.primaryContainer
            isSelected -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surface
        },
        label = "textChoiceContainerColor"
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

@Composable
private fun ChoiceTokenGrid(
    choices: List<PuzzleToken>,
    correctIndex: Int,
    status: LogicAnswerStatus,
    selectedChoice: Int?,
    onChoiceSelected: (Int, Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        choices.indices.chunked(2).forEach { rowIndices ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                rowIndices.forEach { index ->
                    TokenChoiceCard(
                        token = choices[index],
                        isCorrect = index == correctIndex,
                        isSelected = index == selectedChoice,
                        revealed = status != LogicAnswerStatus.UNANSWERED,
                        onClick = { onChoiceSelected(index, correctIndex) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TokenChoiceCard(
    token: PuzzleToken,
    isCorrect: Boolean,
    isSelected: Boolean,
    revealed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            revealed && isCorrect -> MaterialTheme.colorScheme.primary
            revealed && isSelected -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        label = "tokenChoiceBorderColor"
    )
    val containerColor by animateColorAsState(
        targetValue = when {
            revealed && isCorrect -> MaterialTheme.colorScheme.primaryContainer
            revealed && isSelected -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        label = "tokenChoiceContainerColor"
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
        modifier = modifier
            .aspectRatio(1f)
            .offset(x = shakeOffset.value.dp)
            .scale(scale.value)
            .clickable(enabled = !revealed, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            PuzzleTokenGlyph(token = token, unitSize = 44.dp)
        }
    }
}

@Composable
private fun QuestionMarkBox(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "?",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
