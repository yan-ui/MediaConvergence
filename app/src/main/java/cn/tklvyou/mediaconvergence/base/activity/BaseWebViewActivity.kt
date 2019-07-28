package cn.tklvyou.mediaconvergence.base.activity
import android.net.http.SslError
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import cn.tklvyou.mediaconvergence.base.BaseContract
import org.jsoup.Jsoup

abstract class BaseWebViewActivity<P:BaseContract.BasePresenter<*>> : BaseActivity<P>() {

    private lateinit var webView:WebView

    public fun initWebView(webView: WebView){
        this.webView = webView
        //支持javascript
        val settings = webView.settings
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.javaScriptEnabled = true
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                // 当WebView进度改变时更新窗口进
                if (newProgress >= 30) {
                }
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                webView.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
            }

        }

    }


    public fun loadUrl(url:String){
        webView.loadUrl(url)
    }

    public fun loadHtml(html:String){
        imageFillWidth(webView,html)
    }

    /**
     * 处理图片视频填充手机宽度
     *
     * @param webView
     */
    private fun imageFillWidth(webView: WebView, content: String) {
        val doc = Jsoup.parse(content)

        //修改视频标签
        val embeds = doc.getElementsByTag("embed")
        for (element in embeds) {
            //宽度填充手机，高度自适应
            element.attr("width", "100%").attr("height", "auto")
        }
        //webview 无法正确识别 embed 为视频，所以这里把这个标签改成 video 手机就可以识别了
        doc.select("embed").tagName("video")

        //控制图片的大小
        val imgs = doc.getElementsByTag("img")
        for (i in 0 until imgs.size) {
            //宽度填充手机，高度自适应
            imgs[i].attr("style", "width: 100%; height: auto;")
        }

        //对数据进行包装,除去WebView默认存在的一定像素的边距问题
        val data = "<html><head><style>img{width:100% !important;}</style></head><body style='margin:0;padding:0'>${doc}</body></html>"

        //加载使用 jsoup 处理过的 html 文本
//        webView.loadDataWithBaseURL(Contacts.DEV_BASE_URL, doc.toString(), "text/html", "UTF-8", null)
        webView.loadData(data, "text/html; charset=UTF-8", null)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //页面内回退
            if (webView.canGoBack()) {
                webView.goBack()
            }
        }

        return super.onKeyDown(keyCode, event)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (webView != null) {
            webView.removeAllViews()
            try {
                webView.destroy()
            } catch (t: Throwable) {
            }

        }
    }


}