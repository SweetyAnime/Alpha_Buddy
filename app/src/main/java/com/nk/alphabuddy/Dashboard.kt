package com.nk.alphabuddy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Hide the ActionBar
        supportActionBar?.hide()

       //studymaterials image button
        val studyMaterialsButton: ImageButton = findViewById(R.id.studymaterials)
        studyMaterialsButton.setOnClickListener {
            val intent = Intent(this@Dashboard, Studymaterial::class.java)
            startActivity(intent)
        }

        //studymaterials image button
        val timetableButton: ImageButton = findViewById(R.id.timetable)
        timetableButton.setOnClickListener {
            val intent = Intent(this@Dashboard, Timetable::class.java)
            startActivity(intent)
        }


    }
}