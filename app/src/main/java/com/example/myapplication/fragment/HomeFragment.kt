package com.example.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.AddNoteActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.adapter.TabLayoutAdapter
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.viewmodel.NoteViewModel


class HomeFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var noteViewModel: NoteViewModel
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        noteViewModel = activity.noteViewModel
        recyclerView = binding.recyclerViewNote
        initRecyclerViewCategory()
        initRecyclerViewNotes()



        noteViewModel.getAllNoteWithDetails.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                adapter.updateChange(it.toMutableList())
                binding.layoutDataNull.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                binding.layoutDataNull.visibility = View.GONE
                adapter.updateChange(it.toMutableList())
            }

        }




        binding.btnAddSmall.setOnClickListener {
            val intent = Intent(requireActivity(), AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerViewCategory() {
        val recyclerViewCategory = binding.recyclerViewCategory
        recyclerViewCategory.layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
        val adapter = TabLayoutAdapter(
            mutableListOf(),
            onClickItem = {  }
        )

        recyclerViewCategory.adapter = adapter
        val dataCategory = mutableListOf<String>("All")

       noteViewModel.listCategory.observe(viewLifecycleOwner){
           it?.forEach { cate ->
               dataCategory.add(cate.nameCategory)
           }
           adapter.updateDataChanged(dataCategory)
       }


    }

    private fun initRecyclerViewNotes() {

        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = NoteAdapter(
            mutableListOf(),
            onClickItem = { idNote ->
                val intent = Intent(requireContext(), AddNoteActivity::class.java)
                intent.putExtra("id_note", idNote)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            },
            onLongClickItem = { view, noteDetail ->

                val popupMenu = PopupMenu(view.context, view)

                popupMenu.inflate(R.menu.popup_note)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.trash_note -> {
                            val noteDetailArchive = noteDetail.note.copy(isTrash = true)
                            noteViewModel.insertOrUpdateNote(noteDetailArchive)
                            return@setOnMenuItemClickListener true
                        }

                        R.id.archive_note -> {
                            val noteDetailArchive = noteDetail.note.copy(isArchive = true)
                            noteViewModel.insertOrUpdateNote(noteDetailArchive)

                            return@setOnMenuItemClickListener true
                        }



                        else -> {
                            return@setOnMenuItemClickListener false
                        }
                    }

                }
                popupMenu.show()
            },
            requireContext()
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}