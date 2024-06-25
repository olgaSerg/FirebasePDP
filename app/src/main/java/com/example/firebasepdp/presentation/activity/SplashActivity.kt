package com.example.firebasepdp.presentation.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.firebasepdp.R
import com.example.firebasepdp.presentation.base.BaseActivity
import com.example.firebasepdp.databinding.ActivitySplashBinding
import com.example.firebasepdp.firebase.RemoteConfigManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_DISPLAY_LENGTH = 3000L
private const val DEFAULT_IMAGE_URL =
    "https://firebasestorage.googleapis.com/v0/b/fir-pdp-7ac1c.appspot.com/o/tree-736885_960_720.jpg?alt=media&token=6a2a129b-d874-4a67-bca6-a6cd343bfafd"


class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private val remoteConfigManager: RemoteConfigManager by lazy { RemoteConfigManager() }
    private val splashScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchAndApplyConfig()
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    private fun fetchAndApplyConfig() {
        remoteConfigManager.fetchAndActivate { applyConfig() }
    }

    private fun applyConfig() {
        val welcomeMessage = remoteConfigManager.getWelcomeMessage()
        val backgroundImageUrl = remoteConfigManager.getBackgroundImageUrl()

        val defaultWelcomeMessage = getString(R.string.default_welcome_message)

        val finalWelcomeMessage = welcomeMessage.ifEmpty { defaultWelcomeMessage }
        val finalBackgroundImageUrl = backgroundImageUrl.ifEmpty { DEFAULT_IMAGE_URL }

        binding.welcomeTextView.text = finalWelcomeMessage

        loadImage(finalBackgroundImageUrl)
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    showContent()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    showContent()
                    return false
                }
            })
            .into(binding.backgroundImageView)
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.splashContent.visibility = View.VISIBLE

        splashScope.launch {
            delay(SPLASH_DISPLAY_LENGTH)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        splashScope.cancel()
    }
}