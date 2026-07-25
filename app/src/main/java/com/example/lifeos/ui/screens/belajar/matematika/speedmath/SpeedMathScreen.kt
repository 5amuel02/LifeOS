package com.example.lifeos.ui.screens.belajar.matematika.speedmath

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.matematika.MathGame
import com.example.lifeos.ui.screens.belajar.matematika.label
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedMathScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: SpeedMathViewModel = hiltViewModel()
    val bestScore by viewModel.bestScore.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(MathGame.HITUNG_CEPAT.label) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                .padding(24.dp)
        ) {
            AnimatedContent(
                targetState = viewModel.gameState,
                transitionSpec = {
                    (fadeIn(tween(300)) + scaleIn(initialScale = 0.92f, animationSpec = tween(300))) togetherWith
                        (fadeOut(tween(150)) + scaleOut(targetScale = 1.05f, animationSpec = tween(150)))
                },
                label = "speedMathGameState"
            ) { state ->
                when (state) {
                    SpeedMathState.IDLE -> SpeedMathIdleContent(
                        bestScore = bestScore,
                        onStart = viewModel::startGame,
                        strings = strings
                    )
                    SpeedMathState.PLAYING -> SpeedMathPlayingContent(viewModel = viewModel, strings = strings)
                    SpeedMathState.GAME_OVER -> SpeedMathGameOverContent(
                        viewModel = viewModel,
                        bestScore = bestScore,
                        onPlayAgain = viewModel::startGame,
                        strings = strings
                    )
                }
            }
        }
    }
}

@Composable
private fun SpeedMathIdleContent(bestScore: Int, onStart: () -> Unit, strings: LifeOSStrings) {
    val infiniteTransition = rememberInfiniteTransition(label = "idlePulse")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconScale"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = MathGame.HITUNG_CEPAT.icon,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .scale(iconScale)
        )
        Spacer(Modifier.height(16.dp))
        Text(text = MathGame.HITUNG_CEPAT.label, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            text = strings.speedMath.speedMathDescription,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = strings.speedMath.speedMathBestScoreTemplate.format(bestScore),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onStart) { Text(strings.speedMath.speedMathStartButton) }
    }
}

@Composable
private fun SpeedMathPlayingContent(viewModel: SpeedMathViewModel, strings: LifeOSStrings) {
    val progress = if (viewModel.totalMillis == 0L) {
        0f
    } else {
        (viewModel.remainingMillis.toFloat() / viewModel.totalMillis.toFloat()).coerceIn(0f, 1f)
    }
    val isUrgent = progress < 0.25f
    val timerColor by animateColorAsState(
        targetValue = when {
            progress < 0.25f -> MaterialTheme.colorScheme.error
            progress < 0.5f -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(300),
        label = "timerColor"
    )
    val infiniteTransition = rememberInfiniteTransition(label = "timerPulse")
    val timerAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isUrgent) 0.35f else 1f,
        animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse),
        label = "timerAlpha"
    )

    var previousScore by remember { mutableIntStateOf(viewModel.score) }
    val scoreScale = remember { Animatable(1f) }
    LaunchedEffect(viewModel.score) {
        if (viewModel.score != previousScore) {
            previousScore = viewModel.score
            scoreScale.snapTo(1.4f)
            scoreScale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = strings.speedMath.speedMathScoreTemplate.format(viewModel.score),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.scale(scoreScale.value)
        )
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .graphicsLayer { alpha = timerAlpha },
            color = timerColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(Modifier.weight(1f))
        AnimatedContent(
            targetState = viewModel.question,
            transitionSpec = {
                (slideInHorizontally(tween(250)) { it / 3 } + fadeIn(tween(250))) togetherWith
                    (slideOutHorizontally(tween(200)) { -it / 3 } + fadeOut(tween(200)))
            },
            label = "questionTransition"
        ) { question ->
            Text(
                text = question.text,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.weight(1f))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            viewModel.question.choices.chunked(2).forEach { rowChoices ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    rowChoices.forEach { choice ->
                        key(choice) {
                            AnswerButton(
                                value = choice,
                                selected = viewModel.selectedAnswer,
                                correctAnswer = viewModel.question.correctAnswer,
                                onClick = { viewModel.submitAnswer(choice) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerButton(
    value: Int,
    selected: Int?,
    correctAnswer: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isRevealed = selected != null
    val isCorrectChoice = isRevealed && value == correctAnswer
    val isWrongSelected = isRevealed && value == selected && value != correctAnswer

    val containerColor by animateColorAsState(
        targetValue = when {
            isCorrectChoice -> MaterialTheme.colorScheme.primary
            isWrongSelected -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(200),
        label = "answerContainerColor"
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            isCorrectChoice -> MaterialTheme.colorScheme.onPrimary
            isWrongSelected -> MaterialTheme.colorScheme.onError
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(200),
        label = "answerContentColor"
    )

    val scale = remember { Animatable(1f) }
    val shakeOffset = remember { Animatable(0f) }
    val density = LocalDensity.current
    val shakeAmplitudePx = with(density) { 8.dp.toPx() }

    LaunchedEffect(isCorrectChoice) {
        if (isCorrectChoice) {
            scale.snapTo(0.85f)
            scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
        }
    }
    LaunchedEffect(isWrongSelected) {
        if (isWrongSelected) {
            repeat(3) {
                shakeOffset.animateTo(shakeAmplitudePx, animationSpec = tween(45))
                shakeOffset.animateTo(-shakeAmplitudePx, animationSpec = tween(45))
            }
            shakeOffset.animateTo(0f, animationSpec = tween(45))
        }
    }

    Button(
        onClick = onClick,
        enabled = !isRevealed,
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                translationX = shakeOffset.value
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        )
    ) {
        Text(text = value.toString(), style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun SpeedMathGameOverContent(
    viewModel: SpeedMathViewModel,
    bestScore: Int,
    onPlayAgain: () -> Unit,
    strings: LifeOSStrings,
) {
    val animatedScore = remember { Animatable(0f) }
    LaunchedEffect(viewModel.lastScore) {
        animatedScore.snapTo(0f)
        animatedScore.animateTo(
            targetValue = viewModel.lastScore.toFloat(),
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (viewModel.isNewHighScore) {
            ConfettiBurst(modifier = Modifier.fillMaxSize())
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (viewModel.isNewHighScore) {
                val badgeScale = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    badgeScale.animateTo(
                        1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
                    )
                }
                Text(
                    text = strings.speedMath.speedMathNewHighScore,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.scale(badgeScale.value)
                )
                Spacer(Modifier.height(8.dp))
            }
            Text(text = strings.speedMath.speedMathGameOverTitle, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Text(
                text = strings.speedMath.speedMathFinalScoreTemplate.format(animatedScore.value.toInt()),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = strings.speedMath.speedMathBestScoreTemplate.format(bestScore.coerceAtLeast(viewModel.lastScore)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = strings.speedMath.speedMathAverageTimeTemplate.format(formatSeconds(viewModel.lastAverageAnswerMillis)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = onPlayAgain) { Text(strings.speedMath.speedMathPlayAgain) }
        }
    }
}

private data class ConfettiParticle(
    val angleDegrees: Float,
    val distance: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    val startDelay: Float,
)

@Composable
private fun ConfettiBurst(modifier: Modifier = Modifier) {
    val colors = remember {
        listOf(Color(0xFF2A78D6), Color(0xFF1BAF7A), Color(0xFFEDA100), Color(0xFFE34948), Color(0xFFE87BA4))
    }
    val particles = remember {
        List(28) { index ->
            ConfettiParticle(
                angleDegrees = Random.nextFloat() * 360f,
                distance = 100f + Random.nextFloat() * 220f,
                color = colors[index % colors.size],
                size = 5f + Random.nextFloat() * 6f,
                rotationSpeed = Random.nextFloat() * 720f - 360f,
                startDelay = Random.nextFloat() * 0.2f
            )
        }
    }
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(1100, easing = LinearOutSlowInEasing))
    }

    Canvas(modifier = modifier) {
        val originX = size.width / 2f
        val originY = size.height * 0.35f
        particles.forEach { particle ->
            val t = ((progress.value - particle.startDelay) / (1f - particle.startDelay)).coerceIn(0f, 1f)
            if (t <= 0f) return@forEach
            val eased = 1f - (1f - t) * (1f - t)
            val radians = Math.toRadians(particle.angleDegrees.toDouble())
            val x = originX + (cos(radians) * particle.distance * eased).toFloat()
            val y = originY + (sin(radians) * particle.distance * eased).toFloat() + (t * t * 140f)
            val alpha = (1f - t).coerceIn(0f, 1f)
            rotate(degrees = particle.rotationSpeed * t, pivot = Offset(x, y)) {
                drawRect(
                    color = particle.color.copy(alpha = alpha),
                    topLeft = Offset(x - particle.size / 2f, y - particle.size),
                    size = Size(particle.size, particle.size * 2f)
                )
            }
        }
    }
}

private fun formatSeconds(millis: Long): String = "%.1f".format(millis / 1000f)
