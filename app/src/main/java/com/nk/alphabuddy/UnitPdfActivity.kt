package com.nk.alphabuddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UnitPdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pdfUrl = intent.getStringExtra("pdfUrl")

        if (!pdfUrl.isNullOrEmpty()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl))
            startActivity(browserIntent)
            finish() // Close this activity after opening the browser
        } else {
            Toast.makeText(this, "PDF URL is invalid!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
