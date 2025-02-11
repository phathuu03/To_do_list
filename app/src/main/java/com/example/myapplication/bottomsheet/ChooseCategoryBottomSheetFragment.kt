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
import com.example.myapplication.model.CategoryNote
import com.example.myapplication.viewmodel.CategoryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private val categoryNotes =  mutableListOf<CategoryNote>()


class ChooseCategoryBottomSheetFragment(private val viewModel: CategoryViewModel) :
    BottomSheetDialogFragment() {
    private val binding by lazy {

        BottomSheetChooseCategoryBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }


    private fun initView() {
      viewModel.listCategory.observe(requireActivity()){

      }

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
        recyclerView.layoutManager  =  GridLayoutManager(requireContext() , 2)

        viewModel.listCategory.observe(requireActivity()){
            recyclerView.adapter = CategoryAdapter(it)
        }



    }

    private fun addCategory() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.txt_category))

        val input = EditText(requireContext())
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val enteredText = input.text.toString()
            categoryNotes.add(CategoryNote(nameCategory = enteredText))

            viewModel.addCategory(CategoryNote(nameCategory = enteredText))
        }
        builder.show()
    }

}