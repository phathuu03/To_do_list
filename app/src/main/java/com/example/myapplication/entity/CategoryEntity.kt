package com.example.myapplication.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["idNote"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["noteId"])]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val idCategoryNote: Int =0,
    val noteId: Long,
    val nameCategory : String
    )