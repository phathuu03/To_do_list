package com.example.myapplication.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var instance: NoteDatabase? = null

    fun getInstance(context: Context): NoteDatabase {
        if (instance == null) {
            synchronized(NoteDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                .fallbackToDestructiveMigration()
                .build()
            }
        }
        return instance!!
    }
}
