package au.sj.sparrow.adblockwebview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setWebView()

        buttonGo.setOnClickListener { webView.loadUrl(urlEditText.text.toString()) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setWebView() {
        webView.webViewClient = object : WebViewClient() {

            private val loadedUrls = HashMap<String, Boolean>()

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                var ad: Boolean
                val url = request?.url.toString()
                if (!loadedUrls.containsKey(url)) {
                    ad = AdBlocker.isAd(url)
                    loadedUrls[url] = ad
                } else {
                    ad = loadedUrls[url] ?: false
                }

                return if (ad) AdBlocker.createEmptyResourse()
                else super.shouldInterceptRequest(view, request)
            }
        }
        webView.webChromeClient = object : WebChromeClient() {

        }
        webView.clearCache(true)
        webView.clearHistory()

        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.loadUrl("https://www.google.com")
//        webView.loadUrl("https://www.google.com/amp/s/www.macrumors.com/2017/05/22/scrolling-changes-coming-to-mobile-safari/amp/")
//        webView.loadUrl("https://pi-hole.net/pages-to-test-ad-blocking-performance/")
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
    }
}
