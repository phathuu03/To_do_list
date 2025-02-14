package com.example.myapplication.model

data class AttachmentNote(
    val idAttachment: Int = generateId(),

    val uri: String,

    val type: String,

    ) {
    companion object {
        private var current: Int = 0
        private fun generateId(): Int {
            return ++current
        }
    }
}