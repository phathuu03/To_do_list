package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NoteDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        val idNote = intent.getLongExtra("id_note", 0L)
        Toast.makeText(this , idNote.toString(), Toast.LENGTH_SHORT ).show()

    }
}