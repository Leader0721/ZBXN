package com.zbxn.main.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pub.base.BaseActivity;
import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：注册主页面
 * 创建人：LiangHanXin
 * 创建时间：2016/11/9 9:27
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R.id.web_about)
    WebView webAbout;

    String url = "http://n.zbzbx.com/Login/Login/Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setTitle("注册");
        webAbout.loadUrl(url);
        WebSettings settings = webAbout.getSettings();
        settings.setJavaScriptEnabled(true);

        //支持javascript
//        webAbout.getSettings().setJavaScriptEnabled(true);
        // 设置出现缩放工具
//        webAbout.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webAbout.getSettings().setUseWideViewPort(true);
        //设置支持缩放
//        webAbout.getSettings().setSupportZoom(true);
        //自适应
        webAbout.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webAbout.getSettings().setLoadWithOverviewMode(true);
        webAbout.setWebViewClient(new MyWebViewClient());
    }




    /**
     * 点击onToolbar返回的处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (webAbout.canGoBack()) {
                webAbout.goBack();//返回上一页
                return true;
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 点击手机返回键的处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webAbout.canGoBack()) {
                webAbout.goBack();//返回上一页
                return true;
            } else {
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("zbzbx.com/Login/Login/Register")) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            if (url.contains("zbzbx.com/Login") ||
                    url.contains("zbzbx.com/appdownload/AndroidIOSDownload.html")) {//你点击跳转的地址
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);//你要跳转的页面
                RegisterActivity.this.startActivity(intent);

                finish();
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);

        }
    }

}
