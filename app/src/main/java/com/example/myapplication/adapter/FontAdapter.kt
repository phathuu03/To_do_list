package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemViewChooseFontBinding
import com.example.myapplication.model.Font
import com.example.myapplication.utils.Utils

class FontAdapter(val onItemClick: OnItemSelectedListener) : RecyclerView.Adapter<FontAdapter.FontViewHolder>() {
    private var selectedPosition = 0
    inner class FontViewHolder(val binding: ItemViewChooseFontBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rdIsSelected = binding.isSelectedFont


        @SuppressLint("SuspiciousIndentation")
        fun bind(font: Font, position: Int) {

            binding.tvNameFont.text = font.nameFont
                binding.itemChooseFont.setOnClickListener {
                    onItemClick.onItemSelected(font, position )
                    rdIsSelected.isSelected = position == selectedPosition
                    notifyDataSetChanged()
                }
        }


    }

    interface OnItemSelectedListener {
        fun onItemSelected(font: Font, position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontViewHolder {

        val viewBinding =
            ItemViewChooseFontBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FontViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = Utils.fonts.size

    override fun onBindViewHolder(holder: FontViewHolder, position: Int) {
        val fonts = Utils.fonts
        holder.bind(fonts[position] , position)





    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecyclerView (position : Int){
        this.selectedPosition = position
        notifyDataSetChanged()  // Cập nhật lại RecyclerView
    }
}