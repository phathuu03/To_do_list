package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // LiveData để theo dõi danh sách ghi chú
    private val _allNotes = MutableLiveData<List<NoteEntity>>()
    val allNotes: LiveData<List<NoteEntity>> get() = _allNotes

    // Lấy tất cả ghi chú từ Repository và cập nhật LiveData
    init {
        fetchAllNotes()
    }

    private fun fetchAllNotes() {
        viewModelScope.launch {
            val notes = repository.getAllNotes()
            _allNotes.postValue(notes)
        }
    }

    // Thêm hoặc cập nhật ghi chú
    suspend fun insertOrUpdateNote(note: NoteEntity): Long {
        val id = repository.insertOrUpdateNote(note)
        fetchAllNotes()  // Cập nhật danh sách ghi chú sau khi thêm/sửa
        return id
    }


    // Xóa ghi chú
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            fetchAllNotes()
        }
    }

//    // Lấy ghi chú kèm chi tiết
//    fun getNoteWithDetails(noteId: Int): LiveData<NoteWithDetails> {
//        val noteDetails = MutableLiveData<NoteWithDetails>()
//        viewModelScope.launch {
//            val details = repository.getNoteWithDetails(noteId)
//            noteDetails.postValue(details)
//        }
//        return noteDetails
//    }
}
