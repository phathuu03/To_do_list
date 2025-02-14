package com.example.myapplication.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.NoteViewModel

@Suppress("UNCHECKED_CAST")
class NoteViewModelFactory(private  val repository: NoteRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)){
            return NoteViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}