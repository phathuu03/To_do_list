package com.example.myapplication.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.NoteItem

class MergedViewModel(
    private val canvasViewModel: CanvasViewModel,
    private val attachmentNoteViewModel: AttachmentNoteViewModel,
    private val recorderViewModel: RecorderViewModel
) : ViewModel() {
     val mergedLiveData = MediatorLiveData<List<NoteItem>>()
    
    init {
        mergedLiveData.addSource(canvasViewModel.canvas) { mergeAndUpdateData() }
        mergedLiveData.addSource(attachmentNoteViewModel.attachmentNotes) { mergeAndUpdateData() }
        mergedLiveData.addSource(recorderViewModel.recorders) { mergeAndUpdateData() }
    }

    private fun mergeAndUpdateData() {
        val data = mutableListOf<NoteItem>()
        canvasViewModel.canvas.value?.forEach { data.add((NoteItem.CanvasItem(it)) )  }
        attachmentNoteViewModel.attachmentNotes.value?.forEach{data.add(NoteItem.AttachmentItem(it))}
        recorderViewModel.recorders.value?.forEach { data.add(NoteItem.AudioItem(it)) }

        mergedLiveData.value = data


    }
}