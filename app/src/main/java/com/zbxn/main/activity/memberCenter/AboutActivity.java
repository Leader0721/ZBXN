package com.zbxn.main.activity.memberCenter;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pub.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/22.
 */
public class AboutActivity extends BaseActivity {
    @BindView(R.id.web_about)
    WebView webAbout;

    String url = "http://www.zbzbx.com/zms_app_intro/ZBX_introduce.aspx";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setTitle("关于智博星");
        webAbout.loadUrl(url);
        WebSettings settings = webAbout.getSettings();
        settings.setJavaScriptEnabled(true);
        webAbout.setWebViewClient(new MyWebViewClient());

    }

    @Override
    public void initRight() {
        setRight1Show(false);
        setRight2Show(false);
        super.initRight();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
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
