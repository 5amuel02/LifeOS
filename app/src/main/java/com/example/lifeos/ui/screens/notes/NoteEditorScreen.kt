package com.example.lifeos.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.notes.ChecklistItemUi
import com.example.lifeos.data.notes.presetNoteColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(onBack: () -> Unit) {
    val strings = LocalStrings.current
    val viewModel: NoteEditorViewModel = hiltViewModel()
    var showCustomColorDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (viewModel.isEditing) strings.noteEditor.noteEditorTitleEditing else strings.noteEditor.noteEditorTitleNew)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.shared.back)
                    }
                },
                actions = {
                    if (viewModel.isEditing) {
                        IconButton(onClick = { viewModel.delete(onBack) }) {
                            Icon(Icons.Filled.Delete, contentDescription = strings.noteEditor.noteEditorDeleteContentDesc)
                        }
                    }
                    IconButton(onClick = { viewModel.save(onBack) }) {
                        Icon(Icons.Filled.Check, contentDescription = strings.noteEditor.noteEditorSaveContentDesc)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text(strings.noteEditor.noteEditorTitleLabel) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = viewModel.content,
                    onValueChange = viewModel::onContentChange,
                    label = { Text(strings.noteEditor.noteEditorContentLabel) },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text(text = strings.noteEditor.noteEditorColorLabel, style = MaterialTheme.typography.titleMedium)
            }
            item {
                ColorPickerRow(
                    selectedColor = viewModel.color,
                    defaultContentDescription = strings.noteEditor.noteEditorColorDefaultContentDesc,
                    customContentDescription = strings.noteEditor.noteEditorColorCustomContentDesc,
                    onColorSelected = viewModel::onColorSelected,
                    onCustomClick = { showCustomColorDialog = true }
                )
            }
            item {
                Text(text = strings.noteEditor.noteEditorChecklistHeader, style = MaterialTheme.typography.titleMedium)
            }
            items(viewModel.checklistItems, key = { it.key }) { item ->
                ChecklistItemRow(
                    item = item,
                    placeholder = strings.noteEditor.noteEditorChecklistPlaceholder,
                    removeContentDescription = strings.noteEditor.noteEditorChecklistRemoveContentDesc,
                    onToggle = { viewModel.onChecklistItemToggle(item.key) },
                    onTextChange = { viewModel.onChecklistItemTextChange(item.key, it) },
                    onRemove = { viewModel.removeChecklistItem(item.key) }
                )
            }
            item {
                TextButton(onClick = { viewModel.addChecklistItem() }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text(text = strings.noteEditor.noteEditorAddChecklistItem)
                }
            }
        }
    }

    if (showCustomColorDialog) {
        CustomColorDialog(
            initialColor = viewModel.color ?: Color(0xFFFFFFFF),
            title = strings.noteEditor.noteEditorCustomColorTitle,
            confirmText = strings.noteEditor.noteEditorCustomColorConfirm,
            cancelText = strings.shared.cancel,
            redLabel = strings.noteEditor.noteEditorCustomColorRed,
            greenLabel = strings.noteEditor.noteEditorCustomColorGreen,
            blueLabel = strings.noteEditor.noteEditorCustomColorBlue,
            onConfirm = { picked ->
                viewModel.onColorSelected(picked)
                showCustomColorDialog = false
            },
            onDismiss = { showCustomColorDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChecklistItemRow(
    item: ChecklistItemUi,
    placeholder: String,
    removeContentDescription: String,
    onToggle: () -> Unit,
    onTextChange: (String) -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.isChecked, onCheckedChange = { onToggle() })
        TextField(
            value = item.text,
            onValueChange = onTextChange,
            placeholder = { Text(placeholder) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Close, contentDescription = removeContentDescription)
        }
    }
}

@Composable
private fun ColorPickerRow(
    selectedColor: Color?,
    defaultContentDescription: String,
    customContentDescription: String,
    onColorSelected: (Color?) -> Unit,
    onCustomClick: () -> Unit,
) {
    val isCustomSelected = selectedColor != null && selectedColor !in presetNoteColors

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ColorSwatch(
            fillColor = MaterialTheme.colorScheme.surfaceVariant,
            isSelected = selectedColor == null,
            onClick = { onColorSelected(null) }
        ) {
            Icon(
                imageVector = Icons.Filled.Block,
                contentDescription = defaultContentDescription,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        presetNoteColors.forEach { presetColor ->
            ColorSwatch(
                fillColor = presetColor,
                isSelected = selectedColor == presetColor,
                onClick = { onColorSelected(presetColor) }
            )
        }

        ColorSwatch(
            fillColor = if (isCustomSelected) selectedColor!! else MaterialTheme.colorScheme.surfaceVariant,
            isSelected = isCustomSelected,
            onClick = onCustomClick
        ) {
            if (!isCustomSelected) {
                Icon(
                    imageVector = Icons.Filled.Palette,
                    contentDescription = customContentDescription,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    fillColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {},
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(fillColor)
            .border(width = if (isSelected) 2.dp else 1.dp, color = borderColor, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CustomColorDialog(
    initialColor: Color,
    title: String,
    confirmText: String,
    cancelText: String,
    redLabel: String,
    greenLabel: String,
    blueLabel: String,
    onConfirm: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    var red by remember { mutableFloatStateOf(initialColor.red * 255f) }
    var green by remember { mutableFloatStateOf(initialColor.green * 255f) }
    var blue by remember { mutableFloatStateOf(initialColor.blue * 255f) }
    val previewColor = Color(red / 255f, green / 255f, blue / 255f)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(previewColor)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                RgbSliderRow(label = redLabel, value = red, onValueChange = { red = it })
                RgbSliderRow(label = greenLabel, value = green, onValueChange = { green = it })
                RgbSliderRow(label = blueLabel, value = blue, onValueChange = { blue = it })
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(previewColor) }) { Text(confirmText) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(cancelText) }
        }
    )
}

@Composable
private fun RgbSliderRow(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Text(text = "$label: ${value.toInt()}", style = MaterialTheme.typography.labelMedium)
        Slider(value = value, onValueChange = onValueChange, valueRange = 0f..255f)
    }
}
