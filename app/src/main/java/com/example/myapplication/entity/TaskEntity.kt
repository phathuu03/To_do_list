package com.example.myapplication.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["idNote"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["noteId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val idTask: Int = 0,
    val noteId: Long,
    val nameTask: String,
    val isChecked: Boolean = false
)
