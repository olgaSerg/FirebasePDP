package com.example.firebasepdp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasepdp.databinding.ItemNoteSummaryBinding
import com.example.firebasepdp.model.Note
import com.example.firebasepdp.utils.DateFormatter.formatTimestamp

class NoteAdapter(
    private val notes: List<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note, onNoteClick)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(private val binding: ItemNoteSummaryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, onNoteClick: (Note) -> Unit) {
            binding.noteTitle.text = note.title
            binding.noteDate.text = formatTimestamp(note.date)
            itemView.setOnClickListener { onNoteClick(note) }
        }
    }
}