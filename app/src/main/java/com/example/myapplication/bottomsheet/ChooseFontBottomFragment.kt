package com.example.myapplication.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.FontAdapter
import com.example.myapplication.databinding.BottomSheetFontBinding
import com.example.myapplication.model.Font
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseFontBottomFragment(
    private val onClickFont: OnClickFont

) : BottomSheetDialogFragment(), FontAdapter.OnItemSelectedListener {
    private lateinit var adapter: FontAdapter
    private val binding by lazy {
        BottomSheetFontBinding.inflate(layoutInflater)
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

        val recyclerView = binding.recyclerViewFont
        adapter = FontAdapter(this@ChooseFontBottomFragment)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ChooseFontBottomFragment.adapter
        }


        return binding.root
    }

    override fun onItemSelected(font: Font, position: Int) {
        adapter.updateRecyclerView(position)
        onClickFont.sendFont(font.nameFont, font.resId)
        dismiss()


    }

    interface OnClickFont {
        fun sendFont(name: String, resId: Int)
    }


}