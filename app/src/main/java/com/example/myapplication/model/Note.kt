package com.example.myapplication.model

import android.text.SpannableStringBuilder
import com.example.myapplication.utils.Utils
import java.time.LocalDateTime

data class Note(
    val idNote: Int = generateId(),

    val dateStart: LocalDateTime,

    val timeStart: LocalDateTime,

    val title: String ="Title",

    val category: CategoryNote?,

    val content: SpannableStringBuilder,

    val font: FontNote = Utils.defaultFont,

    val timeReminder : LocalDateTime?,

    val attachmentNotes: List<AttachmentNote>?,

    val audioRecords: List<AudioRecord>?,

    val isFavorite : Boolean = false,

    val tasks: List<Task>?

    ){
    companion object {
        private var currentId = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}