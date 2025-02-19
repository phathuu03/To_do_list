package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ViewHolderTabLayoutBinding

class TabLayoutAdapter(
    val data: MutableList<String>, val onClickItem: (String) -> Unit
) : RecyclerView.Adapter<TabLayoutAdapter.ViewHolderTabLayout>() {
    private var selectedPosition= 0

    inner class ViewHolderTabLayout(private val binding: ViewHolderTabLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String , position: Int) {
            binding.tvCategory.text = data

            if (position == selectedPosition){
                binding.line.visibility = View.VISIBLE
                binding.tvCategory.setTextColor(ContextCompat.getColor(itemView.context , R.color.variant))
                binding.tvCategory.textSize = 18F
            }else{
                binding.line.visibility = View.GONE
            }


            itemView.setOnClickListener {
                onClickItem(data)
                selectedPosition = position
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTabLayout {
        val view = ViewHolderTabLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderTabLayout(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolderTabLayout, position: Int) {
        holder.bind(data[position], position)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataChanged(newDada : List<String>){
        this.data.clear()
        this.data.addAll(newDada)
        notifyDataSetChanged()
    }
}