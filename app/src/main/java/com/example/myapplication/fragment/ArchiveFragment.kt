package com.example.myapplication.fragment

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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.AddNoteActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.NoteAdapter
import com.example.myapplication.databinding.FragmentArchiveBinding
import com.example.myapplication.entity.NoteWithDetails


class ArchiveFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var editSearch: EditText


    private val binding by lazy {
        FragmentArchiveBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        editSearch = binding.editSearch

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
        val viewModel = activity.noteViewModel
        adapter = NoteAdapter(
            data = mutableListOf(),
            onClickItem = { idNote ->
                val intent = Intent(requireContext(), AddNoteActivity::class.java)
                intent.putExtra("id_note", idNote)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            },
            onLongClickItem = { view, noteDetail ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.popup_archive_note)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.trash_note -> {
                            val noteDetailArchive = noteDetail.note.copy(isTrash = true)
                            viewModel.insertOrUpdateNote(noteDetailArchive)
                            return@setOnMenuItemClickListener true
                        }

                        R.id.archive_note -> {
                            val noteDetailArchive = noteDetail.note.copy(isArchive = false)
                            viewModel.insertOrUpdateNote(noteDetailArchive)

                            return@setOnMenuItemClickListener true
                        }

                        else -> return@setOnMenuItemClickListener false
                    }

                }
                popupMenu.show()
            },
            context = requireContext()
        )
        recyclerView.adapter = adapter

        viewModel.listNoteWithDetailsArchive.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                adapter.updateChange(it.toMutableList())
                binding.imgAdd.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                binding.imgAdd.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateChange(it.toMutableList())
                searchArchive(it.toMutableList())
            }
        }


    }

    private fun searchArchive(toMutableList: MutableList<NoteWithDetails>) {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val listSearch = toMutableList.filter {
                    it.note.title.contains(s.toString(), true) ||
                            it.note.content.contains(s.toString(), true)
                }
                adapter.updateChange(listSearch.toMutableList())
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isBlank()){
                    adapter.updateChange(toMutableList)
                }
            }

        })


    }


}