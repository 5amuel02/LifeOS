package com.example.lifeos.data.notes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

object DrawingFileStore {
    private fun directory(context: Context): File =
        File(context.filesDir, "drawings").apply { mkdirs() }

    private fun fileFor(context: Context, noteId: Long): File =
        File(directory(context), "note_$noteId.png")

    fun save(context: Context, noteId: Long, bitmap: Bitmap) {
        fileFor(context, noteId).outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        }
    }

    fun load(context: Context, noteId: Long): Bitmap? {
        val file = fileFor(context, noteId)
        return if (file.exists()) BitmapFactory.decodeFile(file.path) else null
    }

    fun delete(context: Context, noteId: Long) {
        fileFor(context, noteId).delete()
    }
}
