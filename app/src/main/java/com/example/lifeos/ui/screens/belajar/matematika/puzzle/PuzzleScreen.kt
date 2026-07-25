package com.example.lifeos.ui.screens.belajar.matematika.puzzle

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.audio.rememberFeedbackSounds
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.ui.screens.belajar.matematika.MathGame
import com.example.lifeos.ui.screens.belajar.matematika.label

private val BOARD_SIZE = 320.dp
private val TILE_GAP = 4.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: PuzzleGameViewModel = hiltViewModel()
    val sounds = rememberFeedbackSounds()
    val bestMoves by viewModel.bestMoves.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.isSolved) {
        if (viewModel.isSolved) {
            sounds.playComplete()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(MathGame.PUZZLE.label) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.startNewGame() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = strings.puzzle.puzzleShuffleContentDesc)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = strings.puzzle.puzzleDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = strings.puzzle.puzzleMovesTemplate.format(viewModel.moves),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = strings.puzzle.puzzleTimeTemplate.format(formatElapsed(viewModel.elapsedMillis)),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(24.dp))
            SlidingBoard(
                board = viewModel.board,
                onTileClick = { index ->
                    val movesBefore = viewModel.moves
                    viewModel.onTileClick(index)
                    if (viewModel.moves != movesBefore) {
                        sounds.playCheck()
                    }
                },
                modifier = Modifier.size(BOARD_SIZE)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = bestMoves?.let { strings.puzzle.puzzleBestMovesTemplate.format(it) } ?: strings.puzzle.puzzleNoBestYet,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    if (viewModel.isSolved) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(strings.puzzle.puzzleSolvedTitle) },
            text = {
                Column {
                    Text(strings.puzzle.puzzleResultTemplate.format(viewModel.lastMoves, formatElapsed(viewModel.lastElapsedMillis)))
                    if (viewModel.isNewBest) {
                        Text(
                            text = strings.puzzle.puzzleNewBestMessage,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.startNewGame() }) { Text(strings.puzzle.puzzlePlayAgain) }
            }
        )
    }
}

@Composable
private fun SlidingBoard(board: List<Int>, onTileClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    val tileSize = BOARD_SIZE / GRID_SIZE
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        for (value in 1..15) {
            key(value) {
                val index = board.indexOf(value)
                val row = index / GRID_SIZE
                val col = index % GRID_SIZE
                val offsetX by animateDpAsState(targetValue = tileSize * col, label = "tileX$value")
                val offsetY by animateDpAsState(targetValue = tileSize * row, label = "tileY$value")

                Box(
                    modifier = Modifier
                        .offset(x = offsetX, y = offsetY)
                        .size(tileSize)
                        .padding(TILE_GAP / 2)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { onTileClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

private fun formatElapsed(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
