package com.example.myapplication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.viewmodel.AttachmentNoteViewModel
import com.example.myapplication.viewmodel.CanvasViewModel
import com.example.myapplication.viewmodel.MergedViewModel
import com.example.myapplication.viewmodel.RecorderViewModel

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
