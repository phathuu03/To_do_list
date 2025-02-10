package com.example.myapplication.model

import com.example.myapplication.utils.Utils
import java.time.LocalDateTime

data class Note(
    val idNote: Int = 0,

    val dateStart: LocalDateTime,

    val timeStart: LocalDateTime,

    val title: String ="Title",

    val category: CategoryNote?,

    val content: String,

    val font: FontNote = Utils.defaultFont,

    val timeReminder : LocalDateTime?,

    val attachmentNotes: List<AttachmentNote>?,

    val audioRecords: List<AudioRecord>?,

    val isFavorite : Boolean = false,

    val tasks: List<Task>? ,

    val timeUpdate: LocalDateTime?



    )

