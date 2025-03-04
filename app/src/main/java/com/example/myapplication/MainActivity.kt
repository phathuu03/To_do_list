package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.dao.NoteDao
import com.example.myapplication.database.DatabaseBuilder
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodelfactory.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var btnAddNote: ImageButton
    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepository
     lateinit var noteViewModel: NoteViewModel

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        btnAddClicked()
        binding.navHostFragment.post {
            navController = binding.navHostFragment.findNavController()
            NavigationUI.setupWithNavController(binding.menuNav, navController)
        }



    }

    private fun btnAddClicked() {
        btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }


    private fun initView() {
        btnAddNote = binding.btnAdd
        noteDao = DatabaseBuilder.getInstance(applicationContext).noteDao()
        repository = NoteRepository(noteDao)
        noteViewModel =
            ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]

    }


}