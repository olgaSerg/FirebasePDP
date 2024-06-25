package com.example.firebasepdp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasepdp.R
import com.example.firebasepdp.model.Note
import com.example.firebasepdp.utils.DateFormatter

class NoteAdapter(
    private val notes: List<Note>,
    private val onNoteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_summary, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note, onNoteClick)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        private val noteDate: TextView = itemView.findViewById(R.id.noteDate)

        fun bind(note: Note, onNoteClick: (Note) -> Unit) {
            noteTitle.text = note.title
            noteDate.text = DateFormatter.formatDate(note.date)
            itemView.setOnClickListener { onNoteClick(note) }
        }
    }
}