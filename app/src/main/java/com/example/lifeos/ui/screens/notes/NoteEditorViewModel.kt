package com.example.lifeos.ui.screens.notes

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.notes.ChecklistItemUi
import com.example.lifeos.data.notes.ChecklistRepository
import com.example.lifeos.data.notes.NoteRepository
import com.example.lifeos.data.notes.NoteType
import com.example.lifeos.data.notes.parseNoteColorString
import com.example.lifeos.data.notes.toNoteColorString
import kotlinx.coroutines.launch
import java.util.UUID

class NoteEditorViewModel(application: Application, private val noteId: Long?) : AndroidViewModel(application) {
    private val database = LifeOSDatabase.getInstance(application)
    private val repository = NoteRepository(database.noteDao())
    private val checklistRepository = ChecklistRepository(database.checklistItemDao())

    var title by mutableStateOf("")
        private set
    var content by mutableStateOf("")
        private set
    var color by mutableStateOf<Color?>(null)
        private set
    var checklistItems by mutableStateOf<List<ChecklistItemUi>>(emptyList())
        private set
    var isLoading by mutableStateOf(noteId != null)
        private set

    val isEditing: Boolean get() = noteId != null

    init {
        if (noteId != null) {
            viewModelScope.launch {
                repository.getNoteById(noteId)?.let { note ->
                    title = note.title
                    content = note.content
                    color = parseNoteColorString(note.color)
                }
                checklistItems = checklistRepository.getItemsForNoteOnce(noteId).map {
                    ChecklistItemUi(key = it.id.toString(), id = it.id, text = it.text, isChecked = it.isChecked)
                }
                isLoading = false
            }
        }
    }

    fun onTitleChange(value: String) {
        title = value
    }

    fun onContentChange(value: String) {
        content = value
    }

    fun onColorSelected(value: Color?) {
        color = value
    }

    fun addChecklistItem() {
        checklistItems = checklistItems + ChecklistItemUi(key = UUID.randomUUID().toString())
    }

    fun onChecklistItemTextChange(key: String, text: String) {
        checklistItems = checklistItems.map { if (it.key == key) it.copy(text = text) else it }
    }

    fun onChecklistItemToggle(key: String) {
        checklistItems = checklistItems.map { if (it.key == key) it.copy(isChecked = !it.isChecked) else it }
    }

    fun removeChecklistItem(key: String) {
        checklistItems = checklistItems.filter { it.key != key }
    }

    fun save(onSaved: () -> Unit) {
        if (title.isBlank() && content.isBlank() && checklistItems.none { it.text.isNotBlank() }) return
        viewModelScope.launch {
            val colorString = color?.toNoteColorString() ?: "DEFAULT"
            val savedNoteId = repository.saveNote(noteId, title, content, colorString, NoteType.TEXT.name)
            checklistRepository.syncItems(savedNoteId, checklistItems)
            onSaved()
        }
    }

    fun delete(onDeleted: () -> Unit) {
        val id = noteId ?: return
        viewModelScope.launch {
            repository.deleteNote(id)
            onDeleted()
        }
    }
}

class NoteEditorViewModelFactory(
    private val application: Application,
    private val noteId: Long?,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NoteEditorViewModel(application, noteId) as T
    }
}
