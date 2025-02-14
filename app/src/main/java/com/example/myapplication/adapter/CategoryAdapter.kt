package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemViewHolderChooseCategoryBinding
import com.example.myapplication.entity.CategoryStringEntity

class CategoryAdapter(private val data: MutableList<CategoryStringEntity>,
   private val onClickItem: (CategoryStringEntity) -> Unit,
    private val onLongClickItemItem: (CategoryStringEntity) -> Unit
    ) : RecyclerView.Adapter<CategoryAdapter.ViewHolderCategory>() {
    inner class ViewHolderCategory(private val binding: ItemViewHolderChooseCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryNote: CategoryStringEntity){
            binding.tvCategory.text = categoryNote.nameCategory
            itemView.setOnClickListener {
                onClickItem(categoryNote)
            }
           itemView.setOnLongClickListener {
             onLongClickItemItem(categoryNote)
               return@setOnLongClickListener true
           }
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
    fun updateChanged(newData : List<CategoryStringEntity>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}