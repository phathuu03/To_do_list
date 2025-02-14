package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.entity.AttachmentNoteEntity
import com.example.myapplication.entity.AudioRecordEntity
import com.example.myapplication.entity.CategoryEntity
import com.example.myapplication.entity.CategoryStringEntity
import com.example.myapplication.entity.CustomCanvasEntity
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.entity.NoteWithDetails
import com.example.myapplication.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // LiveData để theo dõi danh sách ghi chú
    private val _allNotes = MutableLiveData<List<NoteEntity>>()
    val allNotes: LiveData<List<NoteEntity>> get() = _allNotes

    private val _getAllNoteWithDetails = MutableLiveData<List<NoteWithDetails>>()
    val getAllNoteWithDetails : LiveData<List<NoteWithDetails>> = _getAllNoteWithDetails

    private val _idNote = MutableLiveData<Long>()
    val idNote: LiveData<Long> = _idNote

    private val _listCategory = MutableLiveData<List<CategoryStringEntity>>()
    val listCategory: LiveData<List<CategoryStringEntity>> = _listCategory


    // Lấy tất cả ghi chú từ Repository và cập nhật LiveData
    init {
        fetchAllNotes()
        fetchAllNoteDetails()
        fetchAllNameCategory()
    }

    private fun fetchAllNotes() {
        viewModelScope.launch {
            val notes = repository.getAllNotes()
            _allNotes.postValue(notes)
        }
    }
    private fun fetchAllNoteDetails(){
        viewModelScope.launch {
            val noteDetails = repository.getAllNotesWithDetails()
            _getAllNoteWithDetails.postValue(noteDetails)
        }
    }

    fun insertOrUpdateNote(note: NoteEntity) {
        viewModelScope.launch {
            // Thực hiện async nhưng đảm bảo kết quả trả về ngay lập tức
            val id = withContext(Dispatchers.IO) {
                repository.insertOrUpdateNote(note)
            }

            // Cập nhật ID note sau khi thực hiện xong
            _idNote.postValue(id)
            fetchAllNotes()
            fetchAllNoteDetails()

        }
    }


    fun insertOrUpdateAttachment(attachmentNoteEntity: AttachmentNoteEntity){
        viewModelScope.launch {
            repository.insertAttachment(attachmentNoteEntity)
            fetchAllNotes()
            fetchAllNoteDetails()
        }
    }



    // Xóa ghi chú
    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
            fetchAllNotes()
        }
    }

    suspend fun getAllNoteWithDetails() : List<NoteWithDetails>{
        return repository.getAllNotesWithDetails()
    }

    private fun fetchAllNameCategory(){
        viewModelScope.launch{
            _listCategory.postValue(repository.getAllCategoryString())
        }
    }

    fun insertNameCategory(categoryStringEntity: CategoryStringEntity){
        viewModelScope.launch {
            repository.insertNameCategory(categoryStringEntity)
            fetchAllNameCategory()
        }
    }

    fun deleteCategoryName(categoryStringEntity: CategoryStringEntity){
        viewModelScope.launch {
            repository.deleteCategoryString(categoryStringEntity)
            fetchAllNameCategory()
        }
    }
    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            repository.insertCategory(categoryEntity)
        }
    }
    fun insertAudioRecorder(audioRecordEntity: AudioRecordEntity){
        viewModelScope.launch{
            repository.insertAudioRecorder(audioRecordEntity)
        }
    }

    fun insertCustomerCanvas(customCanvasEntity: CustomCanvasEntity){
        viewModelScope.launch {
            repository.insertCustomCanvas(customCanvasEntity)
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
