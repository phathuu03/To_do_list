package com.example.myapplication.model

import android.net.Uri
import com.example.myapplication.utils.enums.MediaType

data class AttachmentNote(
    val idAttachment: Int = generateId(),

    val uri: Uri,

    val type: MediaType,

    ) {
    companion object {
        private var current: Int = 0
        private fun generateId(): Int {
            return ++current
        }
    }
}