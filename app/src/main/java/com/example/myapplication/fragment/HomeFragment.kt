package com.example.myapplication.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import com.example.myapplication.entity.NoteWithDetails
import com.example.myapplication.viewmodel.NoteViewModel


class HomeFragment() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var searchNote: EditText
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchNote = binding.editSearch
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




        noteViewModel.getAllNoteWithDetails.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.layoutDataNull.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
                binding.layoutDataNull.visibility = View.GONE
                initRecyclerViewNotes()
                adapter.updateChange(it.toMutableList())
                initRecyclerViewCategory(it.toMutableList())
                searchNote(it.toMutableList())


            }
        }




        binding.btnAddSmall.setOnClickListener {
            val intent = Intent(requireActivity(), AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun searchNote(listNoteWithDetail: MutableList<NoteWithDetails>) {
        searchNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                var listNoteSearch = mutableListOf<NoteWithDetails>()

                listNoteSearch = listNoteWithDetail.filter {

                    it.note.title.contains(s.toString(), true) ||
                            it.note.content.contains(s.toString(), true)

                }.toMutableList()

                adapter.updateChange(listNoteSearch)

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isBlank()){
                    adapter.updateChange(listNoteWithDetail)
                }

            }

        })

    }

    private fun initRecyclerViewCategory(listNoteWithDetail: MutableList<NoteWithDetails>)  {
        val recyclerViewCategory = binding.recyclerViewCategory
        recyclerViewCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val adapter = TabLayoutAdapter(
            mutableListOf(),
            onClickItem = { cate ->
                searchNote.setText("")
                noteViewModel.listCategory.observe(viewLifecycleOwner) { listNameCate ->
                    var listFilter = mutableListOf<NoteWithDetails>()
                    if (cate.equals("all", true)) {
                        listFilter.addAll(listNoteWithDetail)
                        adapter.updateChange(listFilter)
                    } else {
                        listFilter = listNoteWithDetail.filter {
                            it.categories.any { categoryDt ->
                                categoryDt.nameCategory == cate
                            }
                        }.toMutableList()
                        adapter.updateChange(listFilter)

                    }
                    searchNote(listFilter)

                }


            }
        )

        recyclerViewCategory.adapter = adapter
        val dataCategory = mutableListOf<String>("All")

        noteViewModel.listCategory.observe(viewLifecycleOwner) {
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