package com.bcaf.asuransi_akses

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.CookieManager
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import com.bcaf.asuransi_akses.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webview)

        // Mengaktifkan JavaScript pada WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true // Mengizinkan akses ke file lokal
        webView.settings.allowContentAccess = true // Mengizinkan akses konten lokal
        webView.settings.domStorageEnabled = true // Mengaktifkan DOM storage (localStorage)
        webView.settings.databaseEnabled = true // Mengaktifkan akses ke database HTML5

        // Mengaktifkan cache mode
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT // Menggunakan cache default

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.clearCache(true)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                println("Page started loading: $url") // Debugging untuk melihat URL yang dimuat
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: android.webkit.WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                println("Error loading page: ${error?.description}")
            }
        }

        webView.loadUrl("http://10.10.13.97:4200")
    }
}
