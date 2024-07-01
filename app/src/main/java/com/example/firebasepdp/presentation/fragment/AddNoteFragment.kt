package com.example.firebasepdp.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.firebasepdp.R
import com.example.firebasepdp.presentation.base.BaseFragment
import com.example.firebasepdp.databinding.FragmentAddNoteBinding
import com.example.firebasepdp.helper.ImagePicker
import com.example.firebasepdp.repository.NoteRepository
import com.example.firebasepdp.helper.PermissionManager

class AddNoteFragment : BaseFragment<FragmentAddNoteBinding>() {

    private var imageUri: Uri? = null
    private var permissionManager: PermissionManager? = null
    private var imagePicker: ImagePicker? = null
    private var noteRepository: NoteRepository? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddNoteBinding {
        return FragmentAddNoteBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionManager = PermissionManager(this) { openGallery() }
        imagePicker = ImagePicker(this) { uri -> onImagePicked(uri) }
        noteRepository = NoteRepository(requireContext())

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                if (imageUri != null) {
                    binding.buttonAddImage.isEnabled = false
                    it.isEnabled = false
                    noteRepository?.uploadImageToStorage(imageUri!!, title, content)
                } else {
                    it.isEnabled = false
                    noteRepository?.saveNoteAndImageToFirestore(title, content, null)
                }
            } else {
                Toast.makeText(context,
                    getString(R.string.please_fill_in_both_fields), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAddImage.setOnClickListener {
            permissionManager?.requestStoragePermission()
        }
    }

    private fun onImagePicked(uri: Uri) {
        imageUri = uri
        binding.imageView.setImageURI(uri)
    }

    private fun openGallery() {
        imagePicker?.openGallery()
    }

    override fun onDestroyView() {
        permissionManager = null
        imagePicker = null
        noteRepository = null
        super.onDestroyView()
    }
}