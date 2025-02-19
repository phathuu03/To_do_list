package com.example.myapplication.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.adapter.ReminderAdapter
import com.example.myapplication.databinding.FragmentReminderBinding
import com.example.myapplication.entity.NoteWithDetails
import com.example.myapplication.viewmodel.NoteViewModel


class ReminderFragment : Fragment() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReminderAdapter
    private lateinit var editSearch: EditText
    private val binding by lazy {
        FragmentReminderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recyclerView = binding.recyclerViewReminder
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReminderAdapter(mutableListOf())
        recyclerView.adapter = adapter
        editSearch = binding.editSearch
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as MainActivity
        noteViewModel = activity.noteViewModel



        noteViewModel.getAllNoteWithDetails.observe(viewLifecycleOwner) { noteWithDetails ->

            noteWithDetails?.let { noteWithDetais ->
                val listNoteWithDetailsReminder = noteWithDetais.filter {
                    it.note.calendar != null
                }
                if (listNoteWithDetailsReminder.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.imgAdd.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.imgAdd.visibility = View.GONE
                    binding.recyclerViewReminder.visibility = View.VISIBLE
                    adapter.updateChanged(listNoteWithDetailsReminder.toMutableList())
                    searchReminder(listNoteWithDetailsReminder)

                }


            }

        }


    }

    private fun searchReminder(listNoteWithDetailsReminder: List<NoteWithDetails>) {
        editSearch.addTextChangedListener(object  : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val listAfterSearch = listNoteWithDetailsReminder.filter {
                    it.note.title.contains(s.toString() , true) ||
                            it.note.content.contains(s.toString() , true)
                }
                adapter.updateChanged(listAfterSearch.toMutableList())
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isBlank()){
                    adapter.updateChanged(listNoteWithDetailsReminder.toMutableList())
                }
            }

        })
    }

}
