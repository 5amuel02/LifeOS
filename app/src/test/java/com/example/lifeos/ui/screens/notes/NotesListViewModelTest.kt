package com.example.lifeos.ui.screens.notes

import com.example.lifeos.data.notes.ChecklistItemEntity
import com.example.lifeos.data.notes.ChecklistItemDao
import com.example.lifeos.data.notes.ChecklistRepository
import com.example.lifeos.data.notes.ChecklistSummary
import com.example.lifeos.data.notes.NoteDao
import com.example.lifeos.data.notes.NoteEntity
import com.example.lifeos.data.notes.NoteRepository
import com.example.lifeos.util.MainDispatcherRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private class FakeNoteDao(initial: List<NoteEntity>) : NoteDao {
    val notes = MutableStateFlow(initial)
    override fun getAllNotes(): Flow<List<NoteEntity>> = notes
    override suspend fun getNoteById(id: Long): NoteEntity? = notes.value.find { it.id == id }
    override suspend fun setPinned(id: Long, isPinned: Boolean) {
        notes.value = notes.value.map { if (it.id == id) it.copy(isPinned = isPinned) else it }
    }
    override suspend fun insert(note: NoteEntity): Long = 0
    override suspend fun update(note: NoteEntity) {}
    override suspend fun delete(note: NoteEntity) {}
    override suspend fun deleteById(id: Long) {
        notes.value = notes.value.filterNot { it.id == id }
    }
}

private class FakeChecklistItemDao(summaries: List<ChecklistSummary>) : ChecklistItemDao {
    val summaries = MutableStateFlow(summaries)
    override fun getChecklistSummaries(): Flow<List<ChecklistSummary>> = summaries
    override fun getItemsForNote(noteId: Long): Flow<List<ChecklistItemEntity>> = MutableStateFlow(emptyList())
    override suspend fun getItemsForNoteOnce(noteId: Long): List<ChecklistItemEntity> = emptyList()
    override suspend fun insert(item: ChecklistItemEntity): Long = 0
    override suspend fun update(item: ChecklistItemEntity) {}
    override suspend fun delete(item: ChecklistItemEntity) {}
    override suspend fun deleteById(id: Long) {}
}

private fun note(id: Long, title: String, content: String = "") =
    NoteEntity(id = id, title = title, content = content, createdAt = 0, updatedAt = 0)

class NotesListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun viewModel(
        notes: List<NoteEntity>,
        summaries: List<ChecklistSummary> = emptyList(),
    ) = NotesListViewModel(
        NoteRepository(FakeNoteDao(notes)),
        ChecklistRepository(FakeChecklistItemDao(summaries)),
    )

    @Test
    fun `notes are joined with their checklist summary`() = runTest {
        val vm = viewModel(
            notes = listOf(note(1, "Groceries")),
            summaries = listOf(ChecklistSummary(noteId = 1, total = 3, checkedCount = 1)),
        )

        val result = vm.notes.first { it.isNotEmpty() }

        assertEquals(1, result.size)
        assertEquals(3, result[0].totalItems)
        assertEquals(1, result[0].checkedItems)
    }

    @Test
    fun `notes without a checklist report zero items`() = runTest {
        val vm = viewModel(notes = listOf(note(1, "Plain note")))

        val result = vm.notes.first { it.isNotEmpty() }

        assertEquals(0, result[0].totalItems)
        assertEquals(0, result[0].checkedItems)
    }

    @Test
    fun `search query filters by title or content`() = runTest {
        val vm = viewModel(
            notes = listOf(
                note(1, "Work", content = "quarterly report"),
                note(2, "Groceries", content = "milk and eggs"),
            ),
        )

        vm.onSearchQueryChange("milk")
        val result = vm.notes.first { it.isNotEmpty() }

        assertEquals(1, result.size)
        assertEquals("Groceries", result[0].note.title)
    }
}
