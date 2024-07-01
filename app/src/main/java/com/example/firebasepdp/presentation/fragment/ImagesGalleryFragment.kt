package com.example.firebasepdp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.firebasepdp.R
import com.example.firebasepdp.databinding.FragmentImageGalleryBinding
import com.example.firebasepdp.model.NoteImage
import com.example.firebasepdp.presentation.adapter.ImageAdapter
import com.example.firebasepdp.presentation.base.App
import com.example.firebasepdp.presentation.base.BaseFragment

private const val IMAGES_PATH = "images"
private const val ARGUMENTS_KEY = "noteId"
private const val URL_FIELD = "url"

class ImagesGalleryFragment : BaseFragment<FragmentImageGalleryBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentImageGalleryBinding {
        return FragmentImageGalleryBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageAdapter = ImageAdapter { noteImage ->
            val args = Bundle().apply {
                putString(ARGUMENTS_KEY, noteImage.id)
            }
            findNavController().navigate(R.id.action_imageGalleryFragment_to_noteDetailFragment, args)
        }

        binding.recyclerViewImages.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewImages.adapter = imageAdapter

        loadImagesFromFirestore(imageAdapter)
    }

    private fun loadImagesFromFirestore(adapter: ImageAdapter) {
        val db = App.getAppInstance().firestore
        db.collection(IMAGES_PATH).get()
            .addOnSuccessListener { result ->
                val noteImages = result.documents.mapNotNull { document ->
                    val id = document.id
                    val imageUrl = document.getString(URL_FIELD)
                    if (imageUrl != null) {
                        NoteImage(id, imageUrl)
                    } else {
                        null
                    }
                }
                adapter.submitList(noteImages)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,
                    getString(R.string.error_loading_images_toast, e.message), Toast.LENGTH_SHORT).show()
            }
    }
}