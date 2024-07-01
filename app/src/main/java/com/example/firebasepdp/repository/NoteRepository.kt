package com.example.firebasepdp.repository

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.firebasepdp.R
import com.example.firebasepdp.model.Note
import com.example.firebasepdp.presentation.base.App
import com.google.firebase.storage.FirebaseStorage
import java.lang.ref.WeakReference
import java.util.Date
import java.util.UUID

private const val NOTES_PATH = "notes"
private const val TITLE_KEY = "title"
private const val CONTENT_KEY = "content"
private const val DATE_KEY = "date"
private const val IMAGES_PATH = "images"
private const val URL_KEY = "url"

class NoteRepository(context: Context) {

    private val contextRef = WeakReference(context)
    private val firestore by lazy { App.getAppInstance().firestore }

    fun uploadImageToStorage(uri: Uri, title: String, content: String) {
        val context = contextRef.get() ?: return
        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child(
            context.getString(
                R.string.images_reference_path,
                UUID.randomUUID()
            )
        )

        imageReference.putFile(uri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    saveNoteAndImageToFirestore(title, content, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context,
                    context.getString(R.string.failed_to_upload_image_toast), Toast.LENGTH_SHORT).show()
            }
    }

    fun saveNoteAndImageToFirestore(title: String, content: String, imageUrl: String?) {
        val context = contextRef.get() ?: return
        val note = hashMapOf(
            TITLE_KEY to title,
            CONTENT_KEY to content,
            DATE_KEY to Date()
        )

        firestore.collection(NOTES_PATH).add(note)
            .addOnSuccessListener { documentReference ->
                val noteId = documentReference.id
                if (imageUrl != null) {
                    saveImageToFirestore(noteId, imageUrl)
                } else {
                    Toast.makeText(context,
                        context.getString(R.string.note_saved_successfully_toast), Toast.LENGTH_SHORT).show()
                    (context as? FragmentActivity)?.supportFragmentManager?.popBackStack()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context,
                    context.getString(R.string.failed_to_save_note_toast), Toast.LENGTH_SHORT).show()
            }
    }

    fun fetchNoteDetails(noteId: String, onSuccess: (Note) -> Unit, onFailure: () -> Unit) {
        firestore.collection(NOTES_PATH).document(noteId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val note = document.toObject(Note::class.java)
                    if (note != null) {
                        onSuccess(note)
                    } else {
                        onFailure()
                    }
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener { onFailure() }
    }

    fun fetchImage(noteId: String, onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        firestore.collection(IMAGES_PATH).document(noteId)
            .get()
            .addOnSuccessListener { document ->
                val imageUrl = document.getString(URL_KEY)
                if (!imageUrl.isNullOrEmpty()) {
                    onSuccess(imageUrl)
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener { onFailure() }
    }

    private fun saveImageToFirestore(noteId: String, imageUrl: String) {
        val context = contextRef.get() ?: return
        val image = hashMapOf(
            URL_KEY to imageUrl
        )

        firestore.collection(IMAGES_PATH).document(noteId).set(image)
            .addOnSuccessListener {
                Toast.makeText(context,
                    context.getString(R.string.note_and_image_saved_successfully_toast), Toast.LENGTH_SHORT).show()
                (context as? FragmentActivity)?.supportFragmentManager?.popBackStack()
            }
            .addOnFailureListener {
                Toast.makeText(context,
                    context.getString(R.string.failed_to_save_image_info_toast), Toast.LENGTH_SHORT).show()
            }
    }
}