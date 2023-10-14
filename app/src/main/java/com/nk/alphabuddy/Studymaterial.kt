package com.nk.alphabuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class Studymaterial : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studymaterial)

        // Hide the ActionBar
        supportActionBar?.hide()

        //books image button
        val booksButton: ImageButton = findViewById(R.id.books)
        booksButton.setOnClickListener({
            val intent = Intent(this@Studymaterial, Books::class.java)
            startActivity(intent)
        })


    }
}