package com.example.myapplication.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.adapter.CategoryAdapter
import com.example.myapplication.databinding.BottomSheetChooseCategoryBinding
import com.example.myapplication.entity.CategoryStringEntity
import com.example.myapplication.listener.PasserCategory
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.viewmodel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private val categoryNotes = mutableListOf<CategoryNote>()


class ChooseCategoryBottomSheetFragment(
    private val viewModel: NoteViewModel,
    private val passerCategory: PasserCategory
) :
    BottomSheetDialogFragment() {


    private val binding by lazy {

        BottomSheetChooseCategoryBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }


    private fun initView() {


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddCategory.setOnClickListener {
            addCategory()
        }
        val recyclerView = binding.recyclerViewCategory
        val adapter = CategoryAdapter(emptyList<CategoryStringEntity>().toMutableList(),
            {
            passerCategory.passerCategory(it)
            dismiss()
        },
            {
                viewModel.deleteCategoryName(it)
            }
        )
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        viewModel.listCategory.observe(viewLifecycleOwner) {
            adapter.updateChanged(it)
        }


    }

    private fun addCategory() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.txt_category))

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString().trim()
            categoryNotes.add(CategoryNote(nameCategory = enteredText))
            viewModel.insertNameCategory(CategoryStringEntity(nameCategory = enteredText))
        }

        builder.show()
    }

}