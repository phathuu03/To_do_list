package com.example.myapplication.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.databinding.ViewHolderAttachmentBinding
import com.example.myapplication.model.AttachmentNote

class AttachmentAdapter(
    val data: List<AttachmentNote>,
    val onDelAtt: (AttachmentNote) -> Unit,
    val context: Context
) : RecyclerView.Adapter<AttachmentAdapter.ViewHolderAttment>() {
    inner class ViewHolderAttment(val binding: ViewHolderAttachmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(attachmentNoteEntity: AttachmentNote) {

            val uriImage: Uri = Uri.parse(attachmentNoteEntity.uri)

            Glide.with(context)
                .load(uriImage)
                .apply(RequestOptions().transform(RoundedCorners(10)))
                .into(binding.imageView)

            binding.btnTrashImage.setOnClickListener {
                onDelAtt(attachmentNoteEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAttment {
        val view =
            ViewHolderAttachmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderAttment((view))
    }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: ViewHolderAttment, position: Int) {
        holder.bind(data[position])
    }
}