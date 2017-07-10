package com.zbxn.oa.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pub.base.BaseActivity;
import com.pub.common.ToolbarParams;
import com.zbxn.R;

import butterknife.BindView;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.web_about)
    WebView webAbout;

    String url = "http://www.zbzbx.com/zms_app_intro/ZBX_introduce.aspx";


    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("关于智博星");
        return super.onToolbarConfiguration(toolbar, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        webAbout.loadUrl(url);
        WebSettings settings = webAbout.getSettings();
        settings.setJavaScriptEnabled(true);
        webAbout.setWebViewClient(new MyWebViewClient());

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webAbout.canGoBack()) {
            webAbout.goBack();
            return true;
        }
        if (!webAbout.canGoBack()) {
            finish();
        }
        return false;
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);

        }
    }

}
