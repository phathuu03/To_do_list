package com.example.myapplication.entity
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.myapplication.utils.enums.MediaType

@Entity(
    tableName = "attachment_notes",
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["idNote"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["noteId"])]
)
data class AttachmentNoteEntity(
    @PrimaryKey(autoGenerate = true) val idAttachment: Int = 0,
    val noteId: Int,
    val uri: String,
    val type: MediaType
)
