package com.example.firebasepdp.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

private const val WELCOME_IMAGE_KEY = "welcome_message"
private const val BACKGROUND_IMAGE_URL_KEY = "background_image_url"

class RemoteConfigManager {

    private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
    }

    fun getWelcomeMessage(): String {
        return firebaseRemoteConfig.getString(WELCOME_IMAGE_KEY)
    }

    fun getBackgroundImageUrl(): String {
        return firebaseRemoteConfig.getString(BACKGROUND_IMAGE_URL_KEY)
    }
}