package com.example.firebasepdp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasepdp.presentation.adapter.CommentAdapter
import com.example.firebasepdp.presentation.base.BaseFragment
import com.example.firebasepdp.databinding.FragmentNoteDetailsBinding
import com.example.firebasepdp.model.Comment
import com.example.firebasepdp.model.Note
import com.example.firebasepdp.presentation.base.App
import com.example.firebasepdp.utils.DateFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

private const val COMMENTS_PATH = "comments"
private const val NOTES_PATH = "notes"
private const val ARGUMENTS_KEY = "noteId"

class NoteDetailsFragment : BaseFragment<FragmentNoteDetailsBinding>() {

    private lateinit var commentAdapter: CommentAdapter
    private val commentList = mutableListOf<Comment>()
    private var commentsListener: ValueEventListener? = null
    private var commentsRef: DatabaseReference? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNoteDetailsBinding {
        return FragmentNoteDetailsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        commentAdapter = CommentAdapter(commentList)
        binding.commentsRecyclerView.adapter = commentAdapter

        val noteId = arguments?.getString(ARGUMENTS_KEY)
        if (noteId != null) {
            fetchNoteDetails(noteId)
            fetchComments(noteId)
        }

        binding.postCommentButton.setOnClickListener {
            val commentText = binding.commentEditText.text.toString()
            if (commentText.isNotEmpty()) {
                postComment(noteId, commentText)
            }
        }
    }

    private fun fetchNoteDetails(noteId: String) {
        val db = App.getAppInstance().firestore
        db.collection(NOTES_PATH).document(noteId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val note = document.toObject(Note::class.java)
                    if (note != null) {
                        binding.noteTitle.text = note.title
                        binding.noteContent.text = note.content
                        binding.noteTimestamp.text = DateFormatter.formatDate(note.date)
                    }
                }
            }
            .addOnFailureListener {}
    }

    private fun fetchComments(noteId: String) {
        val db = App.getAppInstance().firebaseDatabase.getReference(COMMENTS_PATH).child(noteId)
        commentsRef = db
        commentsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                commentList.clear()
                for (snapshot in dataSnapshot.children) {
                    val comment = snapshot.getValue(Comment::class.java)
                    if (comment != null) {
                        commentList.add(comment)
                    }
                }
                commentAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        db.addValueEventListener(commentsListener as ValueEventListener)
    }

    private fun postComment(noteId: String?, commentText: String) {
        if (noteId != null) {
            val db = App.getAppInstance().firebaseDatabase.getReference(COMMENTS_PATH).child(noteId)
            val commentId = db.push().key
            if (commentId != null) {
                val comment = Comment(
                    content = commentText
                )
                db.child(commentId).setValue(comment)
                    .addOnSuccessListener {
                        fetchComments(noteId)
                        binding.commentEditText.text.clear()
                    }
                    .addOnFailureListener {}
            }
        }
    }

    override fun onDestroy() {
        commentsListener?.let { commentsRef?.removeEventListener(it) }
        super.onDestroy()
    }
}