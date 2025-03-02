package com.nk.alphabuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView


class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Hide the ActionBar
        supportActionBar?.hide()

        // Initialize Lottie
        val lottieProfile = findViewById<LottieAnimationView>(R.id.lottieProfile)

        // Manually set animation from assets
        lottieProfile.setAnimation("profileicon.json")

        // Play animation
        lottieProfile.playAnimation()

        // Play animation on click for testing
        lottieProfile.setOnClickListener {
            lottieProfile.playAnimation()
        }
    }
}