package com.example.myapplication.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithDetails(
    @Embedded val note: NoteEntity,

    @Relation(
        parentColumn = "idNote",
        entityColumn = "noteId"
    )
    val audioRecords: List<AudioRecordEntity> = emptyList(),

    @Relation(
        parentColumn = "idNote",
        entityColumn = "noteId"
    )
    val attachmentNotes: List<AttachmentNoteEntity> = emptyList(),

    @Relation(
        parentColumn = "idNote",
        entityColumn = "noteId"
    )
    val customCanvasList: List<CustomCanvasEntity> = emptyList(),

    @Relation(
        parentColumn = "idNote",
        entityColumn = "noteId"
    )
    val tasks: List<TaskEntity> = emptyList(),




    )
