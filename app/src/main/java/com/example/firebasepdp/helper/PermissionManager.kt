package com.example.firebasepdp.helper

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.firebasepdp.R

private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001

class PermissionManager(
    private val fragment: Fragment,
    private val onPermissionGranted: () -> Unit
) {

    private val requestPermissionLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
            onPermissionGranted()
        } else {
            Toast.makeText(
                fragment.requireContext(),
                fragment.getString(R.string.permission_denied_toast), Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
                    onPermissionGranted()
                }
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                    Toast.makeText(
                        fragment.requireContext(),
                        fragment.getString(R.string.storage_permission_is_needed_to_pick_images_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO
                        )
                    )
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                fragment.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            } else {
                onPermissionGranted()
            }
        }
    }
}