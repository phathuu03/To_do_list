package com.example.myapplication

import TrashAdapter
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.dao.NoteDao
import com.example.myapplication.database.DatabaseBuilder
import com.example.myapplication.databinding.ActivityTrashBinding
import com.example.myapplication.entity.NoteEntity
import com.example.myapplication.repository.NoteRepository
import com.example.myapplication.viewmodel.NoteViewModel
import com.example.myapplication.viewmodelfactory.NoteViewModelFactory

class TrashActivity : AppCompatActivity() {
    private lateinit var noteDao: NoteDao
    private lateinit var repository: NoteRepository
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrashAdapter
    private lateinit var editSearch : EditText

    // Dùng Set để lưu các ghi chú được chọn (Tránh trùng lặp)
    private val selectedItems = mutableSetOf<NoteEntity>()

    private val binding by lazy {
        ActivityTrashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        setUpToolBar()
        setUpRecyclerView()
        clickAction()
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }
    private val backPressedCallback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            isEnabled = false
            onBackPressedDispatcher.onBackPressed()
            up()
        }

    }

    private fun clickAction() {
        binding.acDelete.setOnClickListener {
            if (selectedItems.isNotEmpty()){
                showDeleteConfirmDialog()
            }
        }
        binding.acRestore.setOnClickListener {
            if (selectedItems.isNotEmpty()) {
                selectedItems.forEach {
                    it.isTrash = false
                    noteViewModel.insertOrUpdateNote(it)
                }
                selectedItems.clear()
                adapter.notifyDataSetChanged() // Cập nhật giao diện
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = TrashAdapter(
            data = mutableListOf(),
            selectedItems = selectedItems,
            onSelectedItem = { noteEntity -> selectedItems.add(noteEntity) },
            onUnSelectedItem = { noteEntity -> selectedItems.remove(noteEntity) }
        )
        recyclerView.adapter = adapter

        noteViewModel.allNotes.observe(this) {
                val listNoteTrash = it.filter { it.isTrash }
                if (listNoteTrash.isNotEmpty()){
                    recyclerView.visibility = View.VISIBLE
                    binding.imgAdd.visibility = View.GONE
                    binding.tvEmpty.visibility = View.GONE
                    adapter.updateDataChanged(listNoteTrash)
                    searchTrash(listNoteTrash.toMutableList())

                }else{
                    recyclerView.visibility = View.GONE
                    binding.imgAdd.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.VISIBLE
                }
        }
    }
    private fun showDeleteConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa ghi chú này không?")
            .setPositiveButton("Xóa") { _, _ ->
                selectedItems.forEach{
                    noteViewModel.deleteNote(it)
                }
                selectedItems.clear()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun setUpToolBar() {
        val toolbar = binding.toolbarAdd
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Trash"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                up()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun up() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        noteDao = DatabaseBuilder.getInstance(applicationContext).noteDao()
        repository = NoteRepository(noteDao)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]
        recyclerView = binding.recyclerViewTrash
        recyclerView.layoutManager = LinearLayoutManager(this)
        editSearch =binding.editSearch
    }
    private fun searchTrash(toMutableList: MutableList<NoteEntity>) {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val listSearch = toMutableList.filter {
                    it.title.contains(s.toString(), true)
                }
                adapter.updateDataChanged(listSearch.toMutableList())
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isBlank()){
                    adapter.updateDataChanged(toMutableList)
                }
            }

        })


    }
}
