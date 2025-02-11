package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemViewHolderChooseCategoryBinding
import com.example.myapplication.model.CategoryNote

class CategoryAdapter(private val data: List<CategoryNote>) : RecyclerView.Adapter<CategoryAdapter.ViewHolderCategory>() {
    inner class ViewHolderCategory(private val binding: ItemViewHolderChooseCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryNote: CategoryNote){
            binding.tvCategory.text = categoryNote.nameCategory
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategory {
        val viewHolder = ItemViewHolderChooseCategoryBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return ViewHolderCategory(viewHolder)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolderCategory, position: Int) {
        holder.bind(data[position])
    }
}