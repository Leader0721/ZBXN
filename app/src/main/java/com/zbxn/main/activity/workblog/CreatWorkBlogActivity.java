package com.zbxn.main.activity.workblog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.DateUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Member;
import com.zbxn.main.entity.WorkBlog;

import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.pub.utils.MyToast.showToast;

/**
 * @author: ysj
 * @date: 2016-12-13 11:52
 */
public class CreatWorkBlogActivity extends BaseActivity {

    /**
     * 日志的本地缓存
     */
    private static final String Flag_SharedPreference_Blog = "blog";
    /**
     * 输入内容_日志{@link WorkBlog}
     */
    public static final String Flag_Input_Blog = "blog";
    /**
     * 日志长度范围
     */
    private static final int[] Flag_RangeLengthOfBlog = {20, 1000};

    @BindView(R.id.mContent)
    WebView mContent;
    //    @BindView(R.id.mContentLength)
//    TextView mContentLength;
    // 已完成日志提交，清空缓存
    private boolean mSubmitFinish = false;
    //从那个页面跳入 创建  0--日志列表 1--主界面快捷入口
    private int type;

    private String mContentStr = "";

    //0--没开始加载  1--加载中  2--加载完成
    private int mWebviewState = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workblog_creat);
        ButterKnife.bind(this);
        String date = DateUtils.getDate("MM月dd日");
        setTitle(date);
        init();
        loadHistory();
    }

    @Override
    public void initRight() {
        super.initRight();
        super.initRight();
        setRight1("完成");
        setRight1Icon(R.mipmap.complete2);
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        mWebviewState = 1;
        //获取内容
        mContent.loadUrl("javascript:getHTML()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //0--没开始加载  1--加载中  2--加载完成
                while (mWebviewState == 1) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mWebviewState = 1;
                handler.sendEmptyMessage(1);
            }
        }).start();
        /*int length = mContent.getText().toString().trim().length();
        if (length < Flag_RangeLengthOfBlog[0]) {
            Toast.makeText(this, "日志内容不能少于" + Flag_RangeLengthOfBlog[0] + "个字", Toast.LENGTH_SHORT).show();
            return;
        } else if (length > Flag_RangeLengthOfBlog[1]) {
            Toast.makeText(this, "日志内容不能多余" + Flag_RangeLengthOfBlog[1] + "个字", Toast.LENGTH_SHORT).show();
            return;
        }*/

    }

    private String cache = "";

    /**
     * 如果当天写了日志，加载传入的日志
     */
    private void loadHistory() {
        Parcelable parcelable = getIntent().getParcelableExtra(Flag_Input_Blog);
        if (parcelable != null) {
            cache = PreferencesUtils.getString(this, Flag_SharedPreference_Blog);
            if (cache == null) {
                return;
            }
            if (Member.get().getBlogStateToday() == 1) {
//                mContent.setText(cache);
//                mContent.setSelection(cache.length());

                //等待h5加载完成
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //0--没开始加载  1--加载中  2--加载完成
                        while (mWebviewState == 1) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mWebviewState = 1;
                        handler.sendEmptyMessage(2);
                    }
                }).start();
            }
        }
    }

    //初始化EditText
    private void init() {
        type = getIntent().getIntExtra("type", 0);

        mContent.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        mWebviewState = 1;
        mContent.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                mWebviewState = 2;
                if (url.startsWith("gethtml:")) {
                    try {
                        mContentStr = URLDecoder.decode(url.substring(8).toString(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return true;
                }
                // return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebviewState = 2;
                super.onPageFinished(view, url);
            }
        });
        mContent.loadUrl("file:///android_asset/Demo.html");
        /*// 最大输入长度
        mContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                Flag_RangeLengthOfBlog[1])});
        //监听EditText输入的字符数
        mContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = 0;
                if (s == null)
                    length = 0;
                String content = s.toString().trim();
                length = content.length();
                mContentLength.setText(String.valueOf(length));
            }
        });*/
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mCreateBlog:
                /*int length = mContent.getText().toString().trim().length();
                if (length < Flag_RangeLengthOfBlog[0]) {
                    Toast.makeText(this, "日志内容不能少于" + Flag_RangeLengthOfBlog[0] + "个字", Toast.LENGTH_SHORT).show();
                    return;
                } else if (length > Flag_RangeLengthOfBlog[1]) {
                    Toast.makeText(this, "日志内容不能多余" + Flag_RangeLengthOfBlog[1] + "个字", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                mWebviewState = 1;
                //获取内容
                mContent.loadUrl("javascript:getHTML()");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //0--没开始加载  1--加载中  2--加载完成
                        while (mWebviewState == 1) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        mWebviewState = 1;
                        handler.sendEmptyMessage(1);
                    }
                }).start();
                break;
        }
    }

    /**
     * 创建日志(或修改)
     */
    public void postCreatWorkBlog() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().postCreatWorkBlog(ssid, mContentStr);
        callRequest(call, new HttpCallBack(WorkBlog.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    if (type == 1) {
                        Intent intent = new Intent(getBaseContext(), WorkBlogCenterActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "提交失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String string) {
                showToast(R.string.NETWORKERROR);
            }
        });
    }

    @Override
    protected void onDestroy() {
        // 清空缓存
        if (mSubmitFinish) {
            PreferencesUtils.putString(this, Flag_SharedPreference_Blog, "");
        } else {
            mWebviewState = 1;
            //获取内容
            mContent.loadUrl("javascript:getHTML()");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //0--没开始加载  1--加载中  2--加载完成
                    while (mWebviewState == 1) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mWebviewState = 1;
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://
                    if (!StringUtils.isEmpty(mContentStr)) {
                        PreferencesUtils.putString(CreatWorkBlogActivity.this, Flag_SharedPreference_Blog, mContentStr);
                    }
                    break;
                case 1://
                    if (StringUtils.isEmpty(mContentStr)) {
                        MyToast.showToast("请输入日志");
                        break;
                    }
                    MessageDialog messageDialog = new MessageDialog(CreatWorkBlogActivity.this);
                    messageDialog.setTitle("提示");
                    messageDialog.setMessage("确认提交?");
                    messageDialog.setNegativeButton("返回");
                    messageDialog.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            postCreatWorkBlog();
                        }
                    });
                    messageDialog.show();
                    break;
                case 2://
                    //设置内容
                    mContent.loadUrl("javascript:setHTML('" + cache + "')");
                    break;
            }
        }
    };
}
