package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.databinding.ActivityAddNoteBinding


class AddNoteActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    private val binding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpToolBar()


    }

    private fun setUpToolBar() {
        toolbar = binding.toolbarAdd
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu , menu)
        return true
    }


}