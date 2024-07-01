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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query

private const val ARGUMENTS_KEY = "noteId"
private const val NOTES_PATH = "notes"

class NoteListFragment : BaseFragment<FragmentNoteListBinding>() {

    private var noteAdapter: NoteAdapter? = null
    private val noteList = mutableListOf<Note>()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNoteListBinding {
        return FragmentNoteListBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = noteAdapter

        fetchNotes()
    }

    private fun fetchNotes() {
        showLoading(true)
        val db = App.getAppInstance().firestore
        db.collection(NOTES_PATH)
            .orderBy(getString(R.string.title), Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                noteList.clear()
                for (document in result) {
                    val id = document.id
                    val title = document.getString(getString(R.string.title)) ?: ""
                    val content = document.getString(getString(R.string.content)) ?: ""
                    val date = document.getTimestamp(getString(R.string.date)) ?: Timestamp.now()

                    val note = Note(
                        id = id,
                        title = title,
                        content = content,
                        date = date
                    )
                    noteList.add(note)
                }
                noteAdapter?.notifyDataSetChanged()
                showLoading(false)
            }
            .addOnFailureListener {
                showLoading(false)
            }
    }

    private fun setOnClickListeners() {
        noteAdapter = NoteAdapter(noteList) { note ->
            val args = Bundle().apply {
                putString(ARGUMENTS_KEY, note.id)
            }
            findNavController().navigate(R.id.action_noteListFragment_to_noteDetailsFragment, args)
        }

        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addNoteFragment)
        }

        binding.fabViewImages.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_imageGalleryFragment)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.fabAddNote.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.fabViewImages.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        noteAdapter = null
        super.onDestroyView()
    }
}