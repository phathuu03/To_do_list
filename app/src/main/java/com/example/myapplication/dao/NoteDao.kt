package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.myapplication.entity.AttachmentNoteEntity
import com.example.myapplication.entity.AudioRecordEntity
import com.example.myapplication.entity.CategoryEntity
import com.example.myapplication.entity.CategoryStringEntity
import com.example.myapplication.entity.CustomCanvasEntity
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.entity.NoteWithDetails
import com.example.myapplication.entity.TaskEntity

@Dao
interface NoteDao {

    // *** NoteEntity CRUD ***
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE idNote = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    @Query("SELECT * FROM notes ORDER BY timeUpdate DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    // *** com.example.myapplication.entity.AttachmentNoteEntity ***
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: AttachmentNoteEntity)

    @Query("SELECT * FROM attachment_notes WHERE noteId = :noteId")
    suspend fun getAttachmentsByNoteId(noteId: Int): List<AttachmentNoteEntity>

    // *** AudioRecordEntity ***
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudioRecord(audioRecord: AudioRecordEntity)

    @Query("SELECT * FROM audio_records WHERE noteId = :noteId")
    suspend fun getAudioRecordsByNoteId(noteId: Int): List<AudioRecordEntity>

    // *** CustomCanvasEntity ***
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomCanvas(customCanvas: CustomCanvasEntity)

    @Query("SELECT * FROM custom_canvas WHERE noteId = :noteId")
    suspend fun getCustomCanvasByNoteId(noteId: Int): List<CustomCanvasEntity>

    // *** TaskEntity ***
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE noteId = :noteId")
    suspend fun getTasksByNoteId(noteId: Int): List<TaskEntity>

    // *** Transaction: Get full details of Note with relations ***
    @Transaction
    @Query("SELECT * FROM notes WHERE idNote = :id")
    suspend fun getNoteWithDetails(id: Long): NoteWithDetails?


    // hien thi nhung muc khong bi an di
    @Transaction
    @Query("SELECT * FROM notes WHERE isTrash = 0 and isArchive = 0")
    suspend fun getAllNotesWithDetails(): List<NoteWithDetails>


    // hien thi nhung muc da xoa
    @Transaction
    @Query("SELECT * FROM notes WHERE isTrash = 1")
    suspend fun getAllTrashNotesWithDetails(): List<NoteWithDetails>


    // hien thi nhung muc da luu tru
    @Transaction
    @Query("SELECT * FROM notes WHERE isArchive = 1 ")
    suspend fun getAllArchiveNotesWithDetails(): List<NoteWithDetails>








    // Lấy tất cả các danh mục
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>

    // Lấy danh mục theo noteId
    @Query("SELECT * FROM categories WHERE noteId = :noteId")
    suspend fun getCategoriesByNoteId(noteId: Int): List<CategoryEntity>

    // Thêm hoặc cập nhật danh mục
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    // Xóa danh mục
    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    // show all category name
    @Query("SELECT * FROM category_string")
    suspend fun getAllCategoryName() : List<CategoryStringEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryString(categoryStringEntity: CategoryStringEntity)

    @Delete
    suspend fun deleteCategoryString(categoryStringEntity: CategoryStringEntity)
}
