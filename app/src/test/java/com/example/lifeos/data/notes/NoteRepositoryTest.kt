package com.example.lifeos.data.notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** In-memory [NoteDao] so the repository can be tested on the JVM without Room. */
private class FakeNoteDao : NoteDao {
    val notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    private var nextId = 1L

    override fun getAllNotes(): Flow<List<NoteEntity>> = notes
    override suspend fun getNoteById(id: Long): NoteEntity? = notes.value.find { it.id == id }
    override suspend fun setPinned(id: Long, isPinned: Boolean) {
        notes.value = notes.value.map { if (it.id == id) it.copy(isPinned = isPinned) else it }
    }
    override suspend fun insert(note: NoteEntity): Long {
        val id = nextId++
        notes.value = notes.value + note.copy(id = id)
        return id
    }
    override suspend fun update(note: NoteEntity) {
        notes.value = notes.value.map { if (it.id == note.id) note else it }
    }
    override suspend fun delete(note: NoteEntity) = deleteById(note.id)
    override suspend fun deleteById(id: Long) {
        notes.value = notes.value.filterNot { it.id == id }
    }
}

class NoteRepositoryTest {

    @Test
    fun `saveNote inserts a new note and returns its id`() = runTest {
        val repo = NoteRepository(FakeNoteDao())
        val id = repo.saveNote(id = null, title = "Title", content = "Body", color = "DEFAULT", noteType = "TEXT")

        assertEquals(1L, id)
        assertEquals("Title", repo.getNoteById(id)?.title)
    }

    @Test
    fun `saveNote with an existing id updates in place without duplicating`() = runTest {
        val repo = NoteRepository(FakeNoteDao())
        val id = repo.saveNote(null, "Old", "Body", "DEFAULT", "TEXT")

        repo.saveNote(id, "New", "Body2", "RED", "TEXT")

        val stored = repo.getNoteById(id)!!
        assertEquals("New", stored.title)
        assertEquals("Body2", stored.content)
        assertEquals(1, repo.getAllNotes().first().size)
    }

    @Test
    fun `togglePin flips the pinned flag`() = runTest {
        val repo = NoteRepository(FakeNoteDao())
        val id = repo.saveNote(null, "T", "B", "DEFAULT", "TEXT")
        assertFalse(repo.getNoteById(id)!!.isPinned)

        repo.togglePin(id, isCurrentlyPinned = false)

        assertTrue(repo.getNoteById(id)!!.isPinned)
    }

    @Test
    fun `deleteNote removes the note`() = runTest {
        val repo = NoteRepository(FakeNoteDao())
        val id = repo.saveNote(null, "T", "B", "DEFAULT", "TEXT")

        repo.deleteNote(id)

        assertNull(repo.getNoteById(id))
    }
}
