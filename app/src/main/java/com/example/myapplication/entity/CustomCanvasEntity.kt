package com.example.myapplication.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "custom_canvas",
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["idNote"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["noteId"])]
)
data class CustomCanvasEntity(
    @PrimaryKey(autoGenerate = true) val idCanvas: Int = 0,
    val noteId: Long,
    val fileName: String,
    val uri: String
)
