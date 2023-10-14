package com.nk.alphabuddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class ChemSem1Unit1 : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chemistry_sem1_unit1)

        supportActionBar?.hide()

        val webView = findViewById<WebView>(R.id.chemsem1unit1pdf)

        // Enable JavaScript (optional, depending on your PDF)
        val settings = webView.settings
        settings.javaScriptEnabled = true

        val pdfUrl = "https://drive.google.com/file/d/1BcbnT1mzj9fDWm9KWlpTB1bYsY8FZW56/view?usp=sharing"

        // Load the PDF URL in the WebView
        webView.loadUrl(pdfUrl)

        // Set a WebViewClient to handle redirects and open links within the WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                // Handle redirects and open links in the WebView
                request?.url?.toString()?.let {
                    view?.loadUrl(it)
                }
                return true
            }
        }
    }
}