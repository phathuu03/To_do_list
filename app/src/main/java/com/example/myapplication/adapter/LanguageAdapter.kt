package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ViewHolderSelectLanguageBinding
import com.example.myapplication.model.LanguageApplication
import com.example.myapplication.utils.Utils.languages

class LanguageAdapter(private val listener: OnItemSelectedListener, private val currentLanguage: String , private val context: Context) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolderLanguage>() {
    var selectedPosition = when(currentLanguage){
        "" -> 0
        "vi" -> 1
        "kr" ->2
        else -> 0
    }


    inner class ViewHolderLanguage(private val binding: ViewHolderSelectLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rdBtn = binding.rdSelectedCountry
        val item = binding.layoutChooseLanguage

        @SuppressLint("SuspiciousIndentation")
        fun bind(language: LanguageApplication, position: Int) {
            val drawable = ContextCompat.getDrawable(context , language.flag)
            drawable?.let {
                Glide.with(context)
                    .load(drawable)
                    .circleCrop()
                    .error(R.drawable.ic_country)
                    .into(binding.imgFlag)


            }

            binding.tvSelectedCountry.text = language.language
            rdBtn.isChecked = position == selectedPosition

            item.setOnClickListener {
                listener.onItemSelected(language, position)
            }
        }

    }

    interface OnItemSelectedListener {
        fun onItemSelected(
            language: LanguageApplication,
            position: Int
        )  // Định nghĩa sự kiện item được chọn
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