package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderSelectLanguageBinding
import com.example.myapplication.model.LanguageApplication
import com.example.myapplication.utils.Utils.languages

class LanguageAdapter(private val listener: OnItemSelectedListener) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolderLanguage>() {
    private var selectedPosition = 0



    inner class ViewHolderLanguage(private val binding: ViewHolderSelectLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val rdBtn = binding.rdSelectedCountry
        val item = binding.layoutChooseLanguage

        @SuppressLint("SuspiciousIndentation")
        fun bind(language: LanguageApplication, position: Int) {
            binding.tvSelectedCountry.text = language.language
            rdBtn.isChecked = position == selectedPosition


                item.setOnClickListener {
                listener.onItemSelected(language, position)
            }
        }

    }

    interface OnItemSelectedListener {
        fun onItemSelected(language: LanguageApplication, position: Int)  // Định nghĩa sự kiện item được chọn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderLanguage {
        val viewBinding = ViewHolderSelectLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolderLanguage(viewBinding)
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ViewHolderLanguage, position: Int) {
        holder.bind(languages[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelection(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        notifyDataSetChanged()  // Cập nhật lại RecyclerView
    }

}