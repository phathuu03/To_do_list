package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderEmojiBinding
import com.example.myapplication.utils.Utils

class EmojiAdapter(val onClick: (String) -> Unit) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {
    val data = Utils.emojiList
    inner class EmojiViewHolder(private val binding: ViewHolderEmojiBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(emoji : String){
            binding.tvEmoji.text = emoji
            binding.tvEmoji.setOnClickListener{
                onClick(emoji)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
       val viewBinding = ViewHolderEmojiBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return  EmojiViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(data[position])
    }
}