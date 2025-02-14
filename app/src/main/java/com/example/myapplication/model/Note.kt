package com.example.myapplication.model


data class Note(
    val idNote: Int = 0,

    val dateStart: String,

    val timeStart: String,

    val title: String ="Title",

    val category: CategoryNote?,

    val content: String,

    val font: FontNote = FontNote(),

    val timeReminder : String?,

    val attachmentNotes: List<AttachmentNote>?,

    val audioRecords: List<AudioRecord>?,

    val isFavorite : Boolean = false,

    val tasks: List<Task>? ,

    val timeUpdate: String?,

    val noteCanvas : List<CustomCanvas>?,






    )

