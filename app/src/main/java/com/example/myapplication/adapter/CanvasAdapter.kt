package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.databinding.ViewHolderCanvasBinding
import com.example.myapplication.entity.CustomCanvasEntity
import com.example.myapplication.model.CustomCanvas

class CanvasAdapter(
    val data: MutableList<Any>,
    val onDelete: (Any) -> Unit,
    val context: Context
) : RecyclerView.Adapter<CanvasAdapter.ViewHolderCanvas>() {
    inner class ViewHolderCanvas(val binding: ViewHolderCanvasBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Any) {

            val uriImage: Uri? =when(item){
                is CustomCanvas -> Uri.parse(item.uri)
                is CustomCanvasEntity -> Uri.parse(item.uri)
                else -> null
            }

            Glide.with(context)
                .load(uriImage)
                .apply(RequestOptions().transform(RoundedCorners(10)))
                .into(binding.imageView)

            binding.btnTrashImage.setOnClickListener {
                onDelete(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCanvas {
        val view =
            ViewHolderCanvasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderCanvas(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolderCanvas, position: Int) {
        holder.bind(data[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onUpdateDataChanged(newData : MutableList<Any>){
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }
}