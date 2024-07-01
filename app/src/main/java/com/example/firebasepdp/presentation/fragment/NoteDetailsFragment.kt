package com.example.firebasepdp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasepdp.presentation.adapter.CommentAdapter
import com.example.firebasepdp.presentation.base.BaseFragment
import com.example.firebasepdp.databinding.FragmentNoteDetailsBinding
import com.example.firebasepdp.model.Comment
import com.example.firebasepdp.repository.CommentRepository
import com.example.firebasepdp.repository.NoteRepository
import com.example.firebasepdp.utils.DateFormatter.formatTimestamp

private const val ARGUMENTS_KEY = "noteId"

class NoteDetailsFragment : BaseFragment<FragmentNoteDetailsBinding>() {

    private val commentList = mutableListOf<Comment>()
    private var commentAdapter: CommentAdapter? = null
    private var noteRepository: NoteRepository? = null
    private var commentRepository: CommentRepository? = null

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

        noteRepository = NoteRepository(requireContext())
        commentRepository = CommentRepository()

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
        noteRepository?.fetchNoteDetails(noteId, { note ->
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            binding.noteTimestamp.text = formatTimestamp(note.date)
            fetchImage(noteId)
        }, {
        })
    }

    private fun fetchImage(noteId: String) {
        noteRepository?.fetchImage(noteId, { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.noteImage)
        }, { })
    }

    private fun fetchComments(noteId: String) {
        commentRepository?.fetchComments(noteId) { comments ->
            commentList.clear()
            commentList.addAll(comments)
            commentAdapter?.notifyDataSetChanged()
        }
    }

    private fun postComment(noteId: String?, commentText: String) {
        if (noteId != null) {
            commentRepository?.postComment(noteId, commentText, {
                fetchComments(noteId)
                binding.commentEditText.text.clear()
            }, {
            })
        }
    }

    override fun onDestroy() {
        commentRepository?.removeCommentsListener()
        commentRepository = null
        noteRepository = null
        commentAdapter = null
        super.onDestroy()
    }
}