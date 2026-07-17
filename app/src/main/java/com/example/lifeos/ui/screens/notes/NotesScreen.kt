package com.example.lifeos.ui.screens.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifeos.core.strings.LifeOSStrings
import com.example.lifeos.core.strings.LocalStrings
import com.example.lifeos.data.notes.DrawingFileStore
import com.example.lifeos.data.notes.NoteType
import com.example.lifeos.data.notes.contrastingOnColor
import com.example.lifeos.data.notes.parseNoteColorString
import com.example.lifeos.ui.screens.common.PlaceholderScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesScreen(
    onAddNote: () -> Unit = {},
    onAddDrawing: () -> Unit = {},
    onEditNote: (Long) -> Unit = {},
    onOpenDrawing: (Long) -> Unit = {},
) {
    val strings = LocalStrings.current
    val viewModel: NotesListViewModel = viewModel()
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    var noteIdPendingDelete by rememberSaveable { mutableStateOf<Long?>(null) }
    var showAddTypeDialog by rememberSaveable { mutableStateOf(false) }

    val pinnedNotes = notes.filter { it.note.isPinned }
    val otherNotes = notes.filter { !it.note.isPinned }

    fun openNote(item: NoteListItemUi) {
        if (item.note.noteType == NoteType.DRAWING.name) {
            onOpenDrawing(item.note.id)
        } else {
            onEditNote(item.note.id)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddTypeDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = strings.notes.notesAddContentDesc)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                placeholder = { Text(strings.notes.notesSearchPlaceholder) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = strings.notes.notesSearchClearContentDesc)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            )

            if (notes.isEmpty()) {
                Box(modifier = Modifier.weight(1f)) {
                    val isSearching = searchQuery.isNotBlank()
                    PlaceholderScreen(
                        title = if (isSearching) strings.notes.notesSearchEmptyTitle else strings.notes.notesEmptyTitle,
                        subtitle = if (isSearching) strings.notes.notesSearchEmptySubtitle else strings.notes.notesEmptySubtitle,
                        icon = Icons.AutoMirrored.Filled.Notes
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (pinnedNotes.isNotEmpty()) {
                        item { SectionHeader(strings.notes.notesPinnedSectionTitle) }
                        items(pinnedNotes, key = { it.note.id }) { item ->
                            NoteListItem(
                                item = item,
                                strings = strings,
                                onClick = { openNote(item) },
                                onDeleteClick = { noteIdPendingDelete = item.note.id },
                                onPinClick = { viewModel.togglePin(item.note.id, item.note.isPinned) }
                            )
                        }
                        if (otherNotes.isNotEmpty()) {
                            item { SectionHeader(strings.notes.notesOthersSectionTitle) }
                        }
                    }
                    items(otherNotes, key = { it.note.id }) { item ->
                        NoteListItem(
                            item = item,
                            strings = strings,
                            onClick = { openNote(item) },
                            onDeleteClick = { noteIdPendingDelete = item.note.id },
                            onPinClick = { viewModel.togglePin(item.note.id, item.note.isPinned) }
                        )
                    }
                }
            }
        }
    }

    val idToDelete = noteIdPendingDelete
    if (idToDelete != null) {
        AlertDialog(
            onDismissRequest = { noteIdPendingDelete = null },
            title = { Text(strings.notes.notesDeleteDialogTitle) },
            text = { Text(strings.notes.notesDeleteDialogText) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteNote(idToDelete)
                    noteIdPendingDelete = null
                }) { Text(strings.shared.delete) }
            },
            dismissButton = {
                TextButton(onClick = { noteIdPendingDelete = null }) { Text(strings.shared.cancel) }
            }
        )
    }

    if (showAddTypeDialog) {
        AlertDialog(
            onDismissRequest = { showAddTypeDialog = false },
            title = { Text(strings.notes.notesAddChooseTypeTitle) },
            text = {
                Column {
                    AddTypeRow(
                        icon = Icons.AutoMirrored.Filled.Notes,
                        label = strings.notes.notesAddTypeText,
                        onClick = {
                            showAddTypeDialog = false
                            onAddNote()
                        }
                    )
                    AddTypeRow(
                        icon = Icons.Filled.Draw,
                        label = strings.notes.notesAddTypeDrawing,
                        onClick = {
                            showAddTypeDialog = false
                            onAddDrawing()
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddTypeDialog = false }) { Text(strings.shared.cancel) }
            }
        )
    }
}

@Composable
private fun AddTypeRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun NoteListItem(
    item: NoteListItemUi,
    strings: LifeOSStrings,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onPinClick: () -> Unit,
) {
    val note = item.note
    val dateFormat = remember(strings.localeTag) {
        SimpleDateFormat("d MMM yyyy, HH:mm", Locale.forLanguageTag(strings.localeTag))
    }
    val swatch = parseNoteColorString(note.color)
    val onSwatch = swatch?.contrastingOnColor()
    val contentColor = onSwatch ?: MaterialTheme.colorScheme.onSurface
    val mutedColor = onSwatch?.copy(alpha = 0.72f) ?: MaterialTheme.colorScheme.onSurfaceVariant
    val accentColor = onSwatch ?: MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (swatch != null) {
            CardDefaults.cardColors(containerColor = swatch, contentColor = contentColor)
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onClick)
            ) {
                Text(
                    text = note.title.ifBlank { strings.shared.untitledNote },
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (note.noteType == NoteType.DRAWING.name) {
                    DrawingThumbnail(noteId = note.id, updatedAt = note.updatedAt)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Draw,
                            contentDescription = null,
                            tint = mutedColor,
                            modifier = Modifier.height(14.dp)
                        )
                        Text(
                            text = strings.notes.notesDrawingLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = mutedColor
                        )
                    }
                }
                if (note.content.isNotBlank()) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (item.totalItems > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                        Text(
                            text = strings.notes.notesChecklistDoneTemplate.format(item.checkedItems, item.totalItems),
                            style = MaterialTheme.typography.labelMedium,
                            color = accentColor
                        )
                    }
                    LinearProgressIndicator(
                        progress = { item.checkedItems.toFloat() / item.totalItems.toFloat() },
                        color = accentColor,
                        trackColor = accentColor.copy(alpha = 0.24f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
                Text(
                    text = dateFormat.format(Date(note.updatedAt)),
                    style = MaterialTheme.typography.labelSmall,
                    color = mutedColor,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onPinClick) {
                    Icon(
                        imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                        contentDescription = if (note.isPinned) strings.notes.notesUnpinContentDesc else strings.notes.notesPinContentDesc,
                        tint = if (note.isPinned) accentColor else mutedColor
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = strings.notes.notesDeleteContentDesc)
                }
            }
        }
    }
}

@Composable
private fun DrawingThumbnail(noteId: Long, updatedAt: Long) {
    val context = LocalContext.current
    val bitmap = remember(noteId, updatedAt) {
        DrawingFileStore.load(context, noteId)?.asImageBitmap()
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(top = 6.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
