package com.example.firebasepdp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasepdp.presentation.adapter.NoteAdapter
import com.example.firebasepdp.R
import com.example.firebasepdp.presentation.base.BaseFragment
import com.example.firebasepdp.databinding.FragmentNoteListBinding
import com.example.firebasepdp.model.Note
import com.example.firebasepdp.presentation.base.App
import com.google.firebase.firestore.FirebaseFirestore

private const val ARGUMENTS_KEY = "noteId"
private const val NOTES_PATH = "notes"

class NoteListFragment : BaseFragment<FragmentNoteListBinding>() {

    private lateinit var noteAdapter: NoteAdapter
    private val noteList = mutableListOf<Note>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNoteListBinding {
        return FragmentNoteListBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        noteAdapter = NoteAdapter(noteList) { note ->
            val args = Bundle().apply {
                putString(ARGUMENTS_KEY, note.id)
            }
            findNavController().navigate(R.id.action_noteListFragment_to_noteDetailsFragment, args)
        }
        binding.recyclerView.adapter = noteAdapter

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        showProgressBar()
        fetchNotes()
    }

    private fun fetchNotes() {
        val db = App.getAppInstance().firestore
        db.collection(NOTES_PATH)
            .get()
            .addOnSuccessListener { result ->
                noteList.clear()
                for (document in result) {
                    val note = document.toObject(Note::class.java)
                    note.id = document.id
                    noteList.add(note)
                }
                noteAdapter.notifyDataSetChanged()
                hideProgressBar()
            }
            .addOnFailureListener {
                hideProgressBar()
            }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}