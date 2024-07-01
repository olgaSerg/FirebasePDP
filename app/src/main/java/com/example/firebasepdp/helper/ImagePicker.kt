package com.example.firebasepdp.helper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.firebasepdp.R

class ImagePicker(
    private val fragment: Fragment,
    private val onImagePicked: (Uri) -> Unit
) {

    private val pickImageLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                onImagePicked(uri)
            }
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = fragment.getString(R.string.intent_type)
        pickImageLauncher.launch(intent)
    }
}