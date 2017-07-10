package com.zbxn.main.activity.workblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBackDownload;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.MyToast;
import com.pub.utils.ZipUtils;
import com.pub.widget.ProgressWebView;
import com.zbxn.R;

import java.io.File;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 浏览器Hybrid页面
 *
 * @author: ysj
 * @date: 2016-10-11 14:08
 */
public class WorkBlogWebviewActivity extends BaseActivity {


    @BindView(R.id.webview_progress)
    ProgressWebView webviewProgress;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workblog_webview);
        ButterKnife.bind(this);

        setTitle("日志浏览器壳");

        initView();

        downloadFile();
    }

    private void initView() {
        webviewProgress.getSettings().setDefaultFontSize(14);
        webviewProgress.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webviewProgress.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webviewProgress.getSettings().setBuiltInZoomControls(true);
        // 不显示webview缩放按钮
        webviewProgress.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放
        webviewProgress.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        webviewProgress.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webviewProgress.getSettings().setLoadWithOverviewMode(true);
        /*webviewProgress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });*/
//        1、手机向html 选完通讯录人员传  javascript:getMessageFromApp('{'userid':'','name':''}')
//        2、html向手机 fromJsMessage:1    1--跳转到通讯录选人   2--提交成功关闭页面
        webviewProgress.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                if ("fromjsmessage:1".equals(url)) {//1--跳转到通讯录选人
                    return true;
                } else if ("fromjsmessage:2".equals(url)) {//2--提交成功关闭页面
                    return true;
                } else if (url.startsWith("alertfromhtml:")) {//弹出消息
                    try {
                        String msg = URLDecoder.decode(url.substring(14).toString(), "UTF-8");
                        MyToast.showToast(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyToast.showToast("请输入正确格式");
                    }
                    return true;
                } else if (url.contains("jsp_error")) {//弹出消息
                    MyToast.showToast("获取数据异常");
                    finish();
                    return true;
                } else {
                    return true;
                }

                // return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @OnClick({R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                break;
        }
    }

    private void downloadFile() {
        Call<ResponseBody> call = HttpRequest.getIResourceOA().downloadFileWorkBlogZip();
        callRequest(call, new HttpCallBackDownload(ResponseBody.class, this, ConfigUtils.HYBRID_DOWNLOAD_DIR, "workblog.zip", true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("下载成功：" + ConfigUtils.HYBRID_DOWNLOAD_DIR + "workblog.zip");
                    try {
                        ZipUtils.UnZipFolder(ConfigUtils.HYBRID_DOWNLOAD_DIR + "workblog.zip", ConfigUtils.HYBRID_DIR);
                        MyToast.showToast("解压成功");
                        File file = new File(ConfigUtils.HYBRID_DIR + "set/set1.html");
                        if (file.exists()) {
                            MyToast.showToast("文件存在");
                        } else {
                            MyToast.showToast("文件不存在");
                        }
                        webviewProgress.loadUrl("file:///" + ConfigUtils.HYBRID_DIR + "set/set1.html");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(string);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1000) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
