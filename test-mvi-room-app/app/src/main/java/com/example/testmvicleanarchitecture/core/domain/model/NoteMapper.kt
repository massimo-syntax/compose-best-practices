package com.example.testmvicleanarchitecture.core.domain.model

import com.example.testmvicleanarchitecture.core.data.local.entity.NoteEntity

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        colorHex = colorHex,
        isPinned = isPinned
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        colorHex = colorHex,
        isPinned = isPinned
    )
}

fun List<NoteEntity>.toNoteList(): List<Note> {
    return map { it.toNote() }
}
