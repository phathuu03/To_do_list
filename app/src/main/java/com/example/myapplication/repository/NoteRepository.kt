package com.example.myapplication.repository

import com.example.myapplication.dao.NoteDao
import com.example.myapplication.entity.AttachmentNoteEntity
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
        noteDao.insertNote(note)
    }

    // Xóa ghi chú
    suspend fun deleteNote(note: NoteEntity) = withContext(Dispatchers.IO) {
        noteDao.deleteNote(note)
    }

    // Lấy ghi chú theo ID kèm thông tin chi tiết (NoteWithDetails)
    suspend fun getNoteWithDetails(noteId: Int): NoteWithDetails? = withContext(Dispatchers.IO) {
        noteDao.getNoteWithDetails(noteId)
    }

    // Thêm tệp đính kèm
    suspend fun insertAttachment(attachment: AttachmentNoteEntity) = withContext(Dispatchers.IO) {
        noteDao.insertAttachment(attachment)
    }

    // Lấy danh sách tệp đính kèm theo noteId
    suspend fun getAttachmentsByNoteId(noteId: Int): List<AttachmentNoteEntity> = withContext(Dispatchers.IO) {
        noteDao.getAttachmentsByNoteId(noteId)
    }

    // Tương tự, bạn có thể thêm các phương thức cho AudioRecord, CustomCanvas, và Task.
}
