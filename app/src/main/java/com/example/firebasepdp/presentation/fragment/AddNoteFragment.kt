package com.example.firebasepdp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.firebasepdp.R
import com.example.firebasepdp.presentation.base.BaseFragment
import com.example.firebasepdp.databinding.FragmentAddNoteBinding
import com.example.firebasepdp.presentation.base.App
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

private const val NOTES_PATH = "notes"
private const val TITLE_KEY = "title"
private const val CONTENT_KEY = "content"
private const val DATA_KEY = "data"

class AddNoteFragment : BaseFragment<FragmentAddNoteBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddNoteBinding {
        return FragmentAddNoteBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                saveNoteToFirestore(title, content)
            } else {
                Toast.makeText(context,
                    getString(R.string.please_fill_in_both_fields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNoteToFirestore(title: String, content: String) {
        val db = App.getAppInstance().firestore
        val note = hashMapOf(
            TITLE_KEY to title,
            CONTENT_KEY to content,
            DATA_KEY to Date()
        )

        db.collection(NOTES_PATH)
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(context, getString(R.string.note_added), Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,
                    getString(R.string.error_adding_note, e.message), Toast.LENGTH_SHORT).show()
            }
    }
}