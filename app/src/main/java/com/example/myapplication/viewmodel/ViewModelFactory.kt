package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val canvasViewModel: CanvasViewModel,
    private val attachmentViewModel: AttachmentNoteViewModel,
    private val audioViewModel: RecorderViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MergedViewModel::class.java)) {
            return MergedViewModel(canvasViewModel, attachmentViewModel, audioViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
