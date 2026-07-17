package com.example.lifeos.ui.screens.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.LifeOSDatabase
import com.example.lifeos.data.notes.ChecklistRepository
import com.example.lifeos.data.notes.NoteEntity
import com.example.lifeos.data.notes.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NoteListItemUi(
    val note: NoteEntity,
    val totalItems: Int,
    val checkedItems: Int,
)

class NotesListViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LifeOSDatabase.getInstance(application)
    private val repository = NoteRepository(database.noteDao())
    private val checklistRepository = ChecklistRepository(database.checklistItemDao())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val notes: StateFlow<List<NoteListItemUi>> = combine(
        repository.getAllNotes(),
        checklistRepository.getChecklistSummaries(),
        _searchQuery
    ) { notes, summaries, query ->
        val filteredNotes = if (query.isBlank()) {
            notes
        } else {
            notes.filter { it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true) }
        }
        val summaryByNoteId = summaries.associateBy { it.noteId }
        filteredNotes.map { note ->
            val summary = summaryByNoteId[note.id]
            NoteListItemUi(note = note, totalItems = summary?.total ?: 0, checkedItems = summary?.checkedCount ?: 0)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch { repository.deleteNote(id) }
    }

    fun togglePin(id: Long, isCurrentlyPinned: Boolean) {
        viewModelScope.launch { repository.togglePin(id, isCurrentlyPinned) }
    }
}
