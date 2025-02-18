package com.example.myapplication.repository

import com.example.myapplication.dao.NoteDao
import com.example.myapplication.entity.AttachmentNoteEntity
import com.example.myapplication.entity.AudioRecordEntity
import com.example.myapplication.entity.CategoryEntity
import com.example.myapplication.entity.CategoryStringEntity
import com.example.myapplication.entity.CustomCanvasEntity
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.entity.NoteWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {

    // Lấy tất cả ghi chú
    suspend fun getAllNotes(): List<NoteEntity> = withContext(Dispatchers.IO) {
        noteDao.getAllNotes()
    }

    // Thêm hoặc cập nhật ghi chú
    suspend fun insertOrUpdateNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        if (note.idNote == 0L) {
            noteDao.insertNote(note)
        } else {
            noteDao.updateNote(note)
            -1
        }
    }

    // Xóa ghi chú
    suspend fun deleteNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(note)
    }

    // Lấy ghi chú theo ID kèm thông tin chi tiết (NoteWithDetails)
    suspend fun getNoteWithDetails(noteId: Long): NoteWithDetails? = withContext(Dispatchers.IO) {
        noteDao.getNoteWithDetails(noteId)
    }

    // Thêm tệp đính kèm
    suspend fun insertAttachment(attachment: AttachmentNoteEntity) = withContext(Dispatchers.IO) {
        noteDao.insertAttachment(attachment)
    }

    // Lấy danh sách tệp đính kèm theo noteId
    suspend fun getAttachmentsByNoteId(noteId: Int): List<AttachmentNoteEntity> =
        withContext(Dispatchers.IO) {
            noteDao.getAttachmentsByNoteId(noteId)
        }

    suspend fun getAllCategories(): List<CategoryEntity> = withContext(Dispatchers.IO) {
        noteDao.getAllCategories()

    }

    suspend fun getCategoriesByNoteId(noteId: Int): List<CategoryEntity> =
        withContext(Dispatchers.IO) {
            noteDao.getCategoriesByNoteId(noteId)
        }

    suspend fun insertCategory(category: CategoryEntity): Long = withContext(Dispatchers.IO) {
        noteDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        noteDao.deleteCategory(category)
    }

    suspend fun getAllNotesWithDetails(): List<NoteWithDetails> {
        return noteDao.getAllNotesWithDetails()
    }

    suspend fun getAllCategoryString(): List<CategoryStringEntity> = withContext(Dispatchers.IO) {
        noteDao.getAllCategoryName()
    }

    suspend fun insertNameCategory(categoryName: CategoryStringEntity) =
        withContext(Dispatchers.IO) {
            noteDao.insertCategoryString(categoryName)
        }

    suspend fun deleteCategoryString(categoryName: CategoryStringEntity) = withContext(Dispatchers.IO){
        noteDao.deleteCategoryString(categoryName)
    }


    // Tương tự, bạn có thể thêm các phương thức cho AudioRecord, CustomCanvas, và Task.

    suspend fun insertAudioRecorder(audioRecordEntity: AudioRecordEntity) =
        withContext(Dispatchers.IO) {
            noteDao.insertAudioRecord(audioRecordEntity)
        }

    suspend fun insertCustomCanvas(customCanvasEntity: CustomCanvasEntity) = withContext(Dispatchers.IO){
        noteDao.insertCustomCanvas(customCanvasEntity)
    }


    // show note archive
    suspend fun getAddArchiverNoteWithDetail() = withContext(Dispatchers.IO){
        noteDao.getAllArchiveNotesWithDetails()
    }

    // show note trash
    suspend fun getAddTrashNoteWithDetail() = withContext(Dispatchers.IO){
        noteDao.getAllTrashNotesWithDetails()
    }





}
