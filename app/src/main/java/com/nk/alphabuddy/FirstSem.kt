package com.nk.alphabuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton

class FirstSem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_sem)

        // Hide the ActionBar
        supportActionBar?.hide()

     //enggchembook image button
        val enggchembutton: ImageButton = findViewById(R.id.enggchemistry)
        enggchembutton.setOnClickListener {
            val intent = Intent(this@FirstSem, ChemistryUnits::class.java)
            startActivity(intent)
        }


    }
}