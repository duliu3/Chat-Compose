package cn.gowild.robotlife.eve;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;

/**
 * Created by zwj on 2018/3/21.
 * <p>
 * 公共html 加载
 */

public class GowildHtmlActivity extends Activity  {

    private static final String TAG = GowildHtmlActivity.class.getSimpleName();
    protected String mUrl;
    protected WebView mWebView;
    protected String mInfos;
    protected boolean mIsOver;
    @BindView(R.id.common_web_ll)
    LinearLayout mCommonWebLl;


    private void goBack() {
        if (mWebView.canGoBack()) {
            Log.d(TAG,"menu back");
            mWebView.goBack();
            return;
        }
        finish();
        Log.d(TAG,"html finish");
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    protected void onDestroy() {
//        webview的onDetachedFromWindow前先执行了webview的destroy方法，就可能存在泄露
        if( mWebView!=null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
        mIsOver = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        init(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init(intent);
    }

    /**
     * 初始化参数
     */
    private void init(Intent intent) {
        mWebView = new WebView(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(layoutParams);
        LinearLayout linearLayout = findViewById(R.id.common_web_ll);
        ImageView back = findViewById(R.id.web_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        linearLayout.addView(mWebView);

        if (intent != null && intent.hasExtra(Constants.LOAD_URL)) {
            mUrl = intent.getStringExtra(Constants.LOAD_URL);
            Log.i("INIT", "mUrl = " + mUrl);
            initWebViewSettings();
        }

//        if (intent != null && intent.hasExtra(Constants.SETTING.TITTLE)) {
//            String tittle = intent.getStringExtra(Constants.SETTING.TITTLE);
//            setTitleBarTitle(tittle);
//        }
    }


    @SuppressLint("JavascriptInterface")
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    private void initWebViewSettings() {
        initSetting();
        loadWeb();
//        debugMode();
    }

    /**
     * 兼容老的用户信息接口
     */
    @JavascriptInterface
    public void userInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null && !TextUtils.isEmpty(mInfos)) {
                    mWebView.loadUrl("javascript:sendInfo(" + mInfos + ")");
                }
            }
        });
    }

    private void initSetting() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setLoadsImagesAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient());
        //        mWebView.setDefaultHandler(new DefaultHandler());
    }


//
//    private void debugMode() {
//        if (Constants.debug()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Logger.d("html", "initWebViewSettings isDebug");
//                WebView.setWebContentsDebuggingEnabled(true);
//            }
//        }
//    }

    private void setChromeClient() {

        /*mWebView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                return true;
            }

        });*/
    }


    private void loadWeb() {
        String reg = ".+(jpg|gif|bmp|png|jpeg)$";
        if (mUrl.matches(reg)) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<html>")
                    .append("<body style='margin: 0; padding: 0'>")
                    .append("<img style='max-width:100%;height:auto;' src=\"")
                    .append(mUrl)
                    .append("\"/>")
                    .append("</body>")
                    .append("</html");
            mWebView.loadDataWithBaseURL(null, buffer.toString(), "text/html", "UTF-8", null);
        } else {
            mWebView.loadUrl(mUrl);
        }
    }


    @JavascriptInterface
    public void close() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
