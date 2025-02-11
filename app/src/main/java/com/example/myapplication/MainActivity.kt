package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var btnAddNote: ImageButton
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.navHostFragment.post {
            navController = binding.navHostFragment.findNavController()
            NavigationUI.setupWithNavController(binding.menuNav, navController)
        }
        initView()
        btnAddClicked()


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

    }

    override fun onStart() {
        super.onStart()

    }
}