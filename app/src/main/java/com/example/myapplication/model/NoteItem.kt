package com.example.myapplication.model

sealed class NoteItem {
    data class AttachmentItem(val attachment: AttachmentNote) : NoteItem()
    data class CanvasItem(val canvas: CustomCanvas) : NoteItem()
    data class AudioItem(val audio: AudioRecord) : NoteItem()
    data class TaskItem(val task: Task) : NoteItem()
}
