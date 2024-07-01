package com.example.firebasepdp.repository

import com.example.firebasepdp.model.Comment
import com.example.firebasepdp.presentation.base.App
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

private const val COMMENTS_PATH = "comments"

class CommentRepository {

    private val firebaseDatabase by lazy { App.getAppInstance().firebaseDatabase }
    private var commentsListener: ValueEventListener? = null
    private var commentsRef: DatabaseReference? = null

    fun fetchComments(noteId: String, onCommentsFetched: (List<Comment>) -> Unit) {
        val db = firebaseDatabase.getReference(COMMENTS_PATH).child(noteId)
        commentsRef = db
        commentsListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val commentList = mutableListOf<Comment>()
                for (snapshot in dataSnapshot.children) {
                    val comment = snapshot.getValue(Comment::class.java)
                    if (comment != null) {
                        commentList.add(comment)
                    }
                }
                onCommentsFetched(commentList)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        db.addValueEventListener(commentsListener as ValueEventListener)
    }

    fun postComment(noteId: String, commentText: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val db = firebaseDatabase.getReference(COMMENTS_PATH).child(noteId)
        val commentId = db.push().key
        if (commentId != null) {
            val comment = Comment(content = commentText)
            db.child(commentId).setValue(comment)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure() }
        }
    }

    fun removeCommentsListener() {
        commentsListener?.let { commentsRef?.removeEventListener(it) }
    }
}