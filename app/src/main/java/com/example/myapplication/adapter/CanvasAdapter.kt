package com.example.myapplication.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.databinding.ViewHolderCanvasBinding
import com.example.myapplication.model.CustomCanvas

class CanvasAdapter(
    val data: MutableList<CustomCanvas>,
    val onDelete: (CustomCanvas) -> Unit,
    val context: Context
) : RecyclerView.Adapter<CanvasAdapter.ViewHolderCanvas>() {
    inner class ViewHolderCanvas(val binding: ViewHolderCanvasBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(customCanvas: CustomCanvas) {

            val uriImage: Uri = Uri.parse(customCanvas.uri)

            Glide.with(context)
                .load(uriImage)
                .apply(RequestOptions().transform(RoundedCorners(10)))
                .into(binding.imageView)

            binding.btnTrashImage.setOnClickListener {
                onDelete(customCanvas)
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
}