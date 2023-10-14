package com.nk.alphabuddy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Hide the ActionBar
        supportActionBar?.hide()

        val gifImageView: GifImageView = findViewById(R.id.splash)
        val gifDrawable = GifDrawable(resources, R.drawable.splashgif)
        gifImageView.setImageDrawable(gifDrawable)

        // Delay opening the new activity for 4 seconds (4000 milliseconds)
        @Suppress("DEPRECATION")
        Handler().postDelayed({
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }, 4300)
    }
}