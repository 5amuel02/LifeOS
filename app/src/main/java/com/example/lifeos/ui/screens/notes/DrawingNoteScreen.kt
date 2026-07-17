package com.example.lifeos.ui.screens.notes

import android.app.Application
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingNoteScreen(noteId: Long?, onBack: () -> Unit) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    val viewModel: DrawingNoteViewModel = viewModel(
        factory = DrawingNoteViewModelFactory(context.applicationContext as Application, noteId)
    )
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var showClearConfirm by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (viewModel.isEditing) strings.drawing.drawingEditorTitleEditing else strings.drawing.drawingEditorTitleNew)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = strings.drawing.drawingUndoContentDesc)
                    }
                    IconButton(onClick = { showClearConfirm = true }) {
                        Icon(Icons.Filled.DeleteSweep, contentDescription = strings.drawing.drawingClearContentDesc)
                    }
                    if (viewModel.isEditing) {
                        IconButton(onClick = { viewModel.delete(onBack) }) {
                            Icon(Icons.Filled.Delete, contentDescription = strings.noteEditor.noteEditorDeleteContentDesc)
                        }
                    }
                    IconButton(onClick = { viewModel.save(canvasSize, onBack) }) {
                        Icon(Icons.Filled.Check, contentDescription = strings.noteEditor.noteEditorSaveContentDesc)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TextField(
                value = viewModel.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = { Text(strings.drawing.drawingTitlePlaceholder) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            DrawingToolbar(
                selectedColor = viewModel.selectedColor,
                selectedWidth = viewModel.selectedWidth,
                onColorSelected = viewModel::onColorSelected,
                onWidthSelected = viewModel::onWidthSelected
            )

            DrawingCanvas(
                strokes = viewModel.strokes,
                backgroundBitmap = viewModel.backgroundBitmap,
                currentColor = viewModel.selectedColor,
                currentWidth = viewModel.selectedWidth,
                onStrokeFinished = viewModel::addStroke,
                onSizeChanged = { canvasSize = it },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text(strings.drawing.drawingClearConfirmTitle) },
            text = { Text(strings.drawing.drawingClearConfirmText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clear()
                    showClearConfirm = false
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun DrawingToolbar(
    selectedColor: Color,
    selectedWidth: Float,
    onColorSelected: (Color) -> Unit,
    onWidthSelected: (Float) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            drawingColors.forEach { color ->
                val isSelected = color == selectedColor
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            drawingWidths.forEach { width ->
                val isSelected = width == selectedWidth
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                        )
                        .clickable { onWidthSelected(width) },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size((width / 1.2f).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawingCanvas(
    strokes: List<DrawStroke>,
    backgroundBitmap: androidx.compose.ui.graphics.ImageBitmap?,
    currentColor: Color,
    currentWidth: Float,
    onStrokeFinished: (DrawStroke) -> Unit,
    onSizeChanged: (IntSize) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }
    val latestColor by rememberUpdatedState(currentColor)
    val latestWidth by rememberUpdatedState(currentWidth)
    val latestOnStrokeFinished by rememberUpdatedState(onStrokeFinished)

    Canvas(
        modifier = modifier
            .background(Color.White)
            .border(1.dp, MaterialTheme.colorScheme.outline)
            .onSizeChanged(onSizeChanged)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> currentPoints = listOf(offset) },
                    onDrag = { change, _ ->
                        change.consume()
                        currentPoints = currentPoints + change.position
                    },
                    onDragEnd = {
                        if (currentPoints.isNotEmpty()) {
                            latestOnStrokeFinished(DrawStroke(currentPoints, latestColor, latestWidth))
                        }
                        currentPoints = emptyList()
                    }
                )
            }
    ) {
        backgroundBitmap?.let { drawImage(it) }
        strokes.forEach { stroke -> drawStroke(stroke) }
        if (currentPoints.size > 1) {
            drawStroke(DrawStroke(currentPoints, currentColor, currentWidth))
        }
    }
}
