package com.example.myapplication.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.EmojiAdapter
import com.example.myapplication.databinding.ChooseEmojiBottomSheetBinding
import com.example.myapplication.listener.PasserEmoji
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChooseEmojiBottomSheet(private val passeEmoji: PasserEmoji) : BottomSheetDialogFragment() {

    private val binding by lazy {
        ChooseEmojiBottomSheetBinding.inflate(layoutInflater)
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
        val recyclerView = binding.recyclerViewEmoji

        recyclerView.layoutManager = GridLayoutManager(requireContext() , 6, RecyclerView.HORIZONTAL , false)
        recyclerView.adapter = EmojiAdapter(onClick = {emoji ->
            passeEmoji.passEmoji(emoji)
        })

    }



}


