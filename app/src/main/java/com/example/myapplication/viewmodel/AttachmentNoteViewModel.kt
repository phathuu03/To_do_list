package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.AttachmentNote

class AttachmentNoteViewModel : ViewModel() {
    private val _attachmentNotes = MutableLiveData<List<AttachmentNote>>()
    val attachmentNotes: LiveData<List<AttachmentNote>> = _attachmentNotes

    fun addAttachment (attachmentNote: AttachmentNote) {
        val currentListAtt  = _attachmentNotes.value ?: emptyList()
        _attachmentNotes.value = currentListAtt + attachmentNote
    }


}