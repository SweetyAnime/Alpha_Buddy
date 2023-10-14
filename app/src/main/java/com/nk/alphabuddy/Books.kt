@file:Suppress("RedundantSamConstructor")

package com.nk.alphabuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

@Suppress("RedundantSamConstructor")
class Books : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        // Hide the ActionBar
        supportActionBar?.hide()

        //firstsemester image button
        val firstsembutton: ImageButton = findViewById(R.id.firstsemester)
        firstsembutton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@Books, FirstSem::class.java)
            startActivity(intent)
        })





    }
}