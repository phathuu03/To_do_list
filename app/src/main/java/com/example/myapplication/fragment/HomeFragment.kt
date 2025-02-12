package com.example.myapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.AddNoteActivity
import com.example.myapplication.dao.NoteDao
import com.example.myapplication.database.DatabaseBuilder
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodelfactory.NoteViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepository
    private lateinit var noteViewModel: NoteViewModel
private val binding by lazy {
    FragmentHomeBinding.inflate(layoutInflater)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteDao = DatabaseBuilder.getInstance(requireContext().applicationContext).noteDao()
        repository = NoteRepository(noteDao)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        noteViewModel.allNotes.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                binding.layoutDataNull.visibility = View.VISIBLE
            }else{
                binding.layoutDataNull.visibility = View.GONE
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.btnAddSmall.setOnClickListener{
            val intent = Intent(requireActivity(), AddNoteActivity::class.java)
            startActivity(intent)
        }
    }
}