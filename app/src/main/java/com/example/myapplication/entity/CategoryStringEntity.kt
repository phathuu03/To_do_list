package com.example.myapplication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_string"
)
data class CategoryStringEntity(
    @PrimaryKey(autoGenerate = true) val id:Int =0 ,
val nameCategory: String
)