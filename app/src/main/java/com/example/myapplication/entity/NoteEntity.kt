package com.example.myapplication.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.model.FontNote
import java.time.LocalDateTime

@Entity(tableName = "notes")

data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val idNote: Int = 0,

    val dateStart: String,

    val timeStart: String,

    val title: String ="Title",

    @Embedded val category: CategoryNote?,

    val content: String,

    @Embedded val font: FontNote = FontNote(),

    val timeReminder : String?,

    val isFavorite : Boolean = false,

    val timeUpdate: LocalDateTime?,
    )

