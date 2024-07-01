package com.example.firebasepdp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasepdp.databinding.ItemImageBinding
import com.example.firebasepdp.model.NoteImage

class ImageAdapter(private val onClick: (NoteImage) -> Unit) :
    ListAdapter<NoteImage, ImageAdapter.ImageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val noteImage = getItem(position)
        holder.bind(noteImage)
        Glide.with(holder.itemView.context)
            .load(noteImage.imageUrl)
            .into(holder.binding.imageView)
    }

    class ImageViewHolder(val binding: ItemImageBinding, private val onClick: (NoteImage) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentNoteImage: NoteImage? = null

        init {
            itemView.setOnClickListener {
                currentNoteImage?.let {
                    onClick(it)
                }
            }
        }

        fun bind(noteImage: NoteImage) {
            currentNoteImage = noteImage
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<NoteImage>() {
        override fun areItemsTheSame(oldItem: NoteImage, newItem: NoteImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteImage, newItem: NoteImage): Boolean {
            return oldItem == newItem
        }
    }
}