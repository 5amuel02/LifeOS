package com.example.lifeos.data.notes

enum class NoteType {
    TEXT,
    DRAWING;

    companion object {
        fun fromName(name: String): NoteType = entries.find { it.name == name } ?: TEXT
    }
}
