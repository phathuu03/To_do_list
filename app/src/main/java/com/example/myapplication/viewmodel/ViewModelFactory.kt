package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val canvasViewModel: CanvasViewModel,
    private val attachmentViewModel: AttachmentNoteViewModel,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MergedViewModel::class.java)) {
            return MergedViewModel(canvasViewModel, attachmentViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
