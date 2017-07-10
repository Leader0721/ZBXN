package com.zbxn.main.activity.message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.bean.Bulletin;
import com.pub.dialog.ProgressDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DateUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.ProgressWebView;
import com.zbxn.R;
import com.zbxn.main.activity.memberCenter.CollectCenterActivity;
import com.zbxn.main.activity.mission.PhotoDetailActivity;
import com.zbxn.main.activity.workblog.CommentFragment;
import com.zbxn.main.entity.Member;
import com.zbxn.main.entity.MissionEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/27.
 */
@SuppressLint("SetJavaScriptEnabled")
public class MessageDetailActivity extends BaseActivity implements CommentFragment.CallBackComment {
    /**
     * 从其它页面传来的参数{@link Bulletin}
     */
    public static final String Flag_Input_Bulletin = "bulletin";

    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mCreateTime)
    TextView mCreateTime;
    @BindView(R.id.mCreateUserName)
    TextView mCreateUserName;
    @BindView(R.id.mAlertMessage)
    TextView mAlertMessage;
    @BindView(R.id.message_scroll)
    ScrollView mScrollView;
    @BindView(R.id.mProgressWebView)
    ProgressWebView mProgressWebView;
    @BindView(R.id.mComment)
    EditText mComment;
    @BindView(R.id.mPublish)
    TextView mPublish;
    @BindView(R.id.comment_layout)
    LinearLayout commentLayout;

    private MenuItem mCollect;
    private MenuItem mStick;
    private MenuItem mDelete;
    private Bulletin mBulletin = new Bulletin();

    //    private WebViewFragment mWebViewFragment;
    private CommentFragment mCommentFragment;

    private int msgId;
    private int dataId;
    private int position;

    //private LoadingControllerImp mLoadingController;

    private boolean isReply = false;
    private int commentId;
    private int replyToId;
    private String reply;
    private ProgressDialog mProgressDialog;

    private int mType;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagedetail);
        setTitle("消息详情");
        ButterKnife.bind(this);
        String id = getIntent().getStringExtra("id");
        mType = getIntent().getIntExtra("type", 0);
        if (!StringUtils.isEmpty(id)) {
            dataId = Integer.parseInt(id);
        } else {
            mBulletin = getIntent().getParcelableExtra(Flag_Input_Bulletin);
            dataId = mBulletin.getRelatedid();
            msgId = mBulletin.getId();
            position = getIntent().getIntExtra("position", 0);
            mProgressDialog = new ProgressDialog(this);
            if (mBulletin.getAllowComment() == 1) {
                commentLayout.setVisibility(View.VISIBLE);
            } else if (mBulletin.getAllowComment() == 0) {
                commentLayout.setVisibility(View.GONE);
            }
        }

        initView();

        loadData();

//        WebSettings webSettings = mProgressWebView.getSettings();
//        webSettings.getJavaScriptEnabled();
        // 启用javascript
        mProgressWebView.getSettings().setJavaScriptEnabled(true);
        // 随便找了个带图片的网站
//        mProgressWebView.loadUrl("http://n.zbzbx.com/");
        // 添加js交互接口类，并起别名 imagelistner

        mProgressWebView.addJavascriptInterface(new JavaScriptInterface(this), "imagelistner");
        mProgressWebView.setWebViewClient(new MyWebViewClient());
    }

    private void loadData() {
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call;
        if (mType == 1) {
            call = HttpRequest.getIResourceOA().GetDetailById(ssid, dataId + "");
        } else {
            call = HttpRequest.getIResourceOA().GetSystemMsgDetailById(ssid, dataId + "");
        }
        callRequest(call, new HttpCallBack(Bulletin.class, getApplicationContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mBulletin = (Bulletin) mResult.getData();
                    if (mBulletin.getIstop() == 1 && mBulletin.getCreateuserid() == Member.get().getId()) {
                        mStick.setVisible(true);
                    }
                    if (mBulletin.getCreateuserid() == Member.get().getId()) {
                        mDelete.setVisible(true);
                    }
                    refreshUI();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        mProgressWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    public class JavaScriptInterface {

        public JavaScriptInterface(Context context) {
        }

        @JavascriptInterface
        public void openImage(String img) {
            ArrayList<String> list_Ads = new ArrayList<>();
            list_Ads.add(img);
            Intent intent = new Intent(MessageDetailActivity.this, PhotoDetailActivity.class);
            intent.putExtra("list", list_Ads);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_messagedetail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.mMenuCollect);
        mDelete = menu.findItem(R.id.mSelectMessage);
        mStick = menu.findItem(R.id.mMenuSick);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == mCollect) {
            String collect = null;
            if (mBulletin.isCollect()) {
                collect = "false";
                mBulletin.setCollect(false);
            } else {
                mBulletin.setCollect(true);
                collect = "true";
            }
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            Call call = HttpRequest.getIResourceOA().MessageCollectOrNot(ssid, msgId + "", collect);
            callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {//0成功
                        MyToast.showToast(mResult.getMsg());
                        refreshCollectState();
                    } else {
                        String message = mResult.getMsg();
                        MyToast.showToast(message);
                        //撤回对本地数据的修改
                        mBulletin.setCollect(!mBulletin.isCollect());
                        refreshCollectState();
                    }
                }

                @Override
                public void onFailure(String string) {
                    MyToast.showToast("获取网络数据失败");
                    //撤回对本地数据的修改
                    mBulletin.setCollect(!mBulletin.isCollect());
                    refreshCollectState();
                }
            });
        } else if (item == mStick) {
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            Call call = HttpRequest.getIResourceOA().MessageIsStopOrNot(ssid, mBulletin.getId() + "", "0");
            callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {//0成功
                        MyToast.showToast("取消置顶成功");
                        refreshUI();
                        mStick.setVisible(false);
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    } else {
                        MyToast.showToast("取消置顶失败");
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(String string) {
                    MyToast.showToast("获取网络数据失败");
                }
            });
        } else if (item == mDelete) {
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            Call call = HttpRequest.getIResourceOA().MessageIsDeleteOrNot(ssid, mBulletin.getId() + "");
            callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {//0成功
                        MyToast.showToast("删除成功");
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(String string) {
                    MyToast.showToast("获取网络数据失败");
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
//        mWebViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.mWebViewFragment);

        mProgressWebView.getSettings().setDefaultFontSize(14);
        mProgressWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mProgressWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mProgressWebView.getSettings().setBuiltInZoomControls(true);
        // 不显示webview缩放按钮
        mProgressWebView.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放
        mProgressWebView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        mProgressWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mProgressWebView.getSettings().setLoadWithOverviewMode(true);
        mProgressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // return super.shouldOverrideUrlLoading(view, url);
                return false;
            }
        });

        mCommentFragment = (CommentFragment) getSupportFragmentManager().findFragmentById(R.id.mCommentFragment);
    }


    public void refreshUI() {
        if (mBulletin == null) {
        } else {
            mTitle.setText(mBulletin.getTitle() + "");
            mCreateTime.setText(DateUtils.fromTodaySimple(mBulletin
                    .getCreatetime()));

            mCreateUserName.setText(mBulletin.getCreateUserName());


            String content = "<head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />" +
                    "<style >img{max-width:100%;height:auto;}</style>" + "</head>\n<body><div>" +
                    mBulletin.getContent() + "</div></body>";
//            mWebViewFragment.reloadDataWithContent(content);

            String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
            String mMimType = "text/html";
            String mEncoding = "utf-8";
            String mFailUrl = null;
            mProgressWebView.loadDataWithBaseURL(mBaseUrl, content, mMimType, mEncoding, mFailUrl);
            mCommentFragment.reloadData(dataId, 1);
            refreshCollectState();
        }
    }


    /**
     * 刷新收藏状态
     */
    private void refreshCollectState() {
        mCollect.setEnabled(true);
        if (mBulletin.isCollect()) {
            mCollect.setIcon(R.mipmap.bg_collect_yes);
        } else {
            mCollect.setIcon(R.mipmap.bg_collect_no);
            Intent intent = new Intent(MessageDetailActivity.this, CollectCenterActivity.class);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
        }
    }


    @Override
    public void sendComment(String name, int commentId, int replyToId) {
        isReply = true;
        mComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) mComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        reply = "回复 " + name + ":";
        mComment.setText(reply);
        this.commentId = commentId;
        this.replyToId = replyToId;

        //设置光标位置
        Editable editable = mComment.getText();
        Spannable spanText = editable;
        Selection.setSelection(spanText, editable.length());
        //EditText 监听
        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mComment.getText().length() < reply.length()) {
                    mComment.getText().clear();
                    reply = "";
                    isReply = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.mPublish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPublish:
                if (TextUtils.isEmpty(mComment.getText().toString().trim())) {
//            showToast("回复信息不能为空", Toast.LENGTH_SHORT);
                    MyToast.showToast("评论内容不能为空");
                } else {
                    if (isReply) {
                        int size = reply.length();
//                        if (mComment.getText().length() < size) {
//                            isReply = false;
//                        }
                        String str = mComment.getText().toString().substring(size);
                        if (StringUtils.isEmpty(str.trim())) {
                            MyToast.showToast("评论内容不能为空");
                        } else {
                            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                            Call call = HttpRequest.getIResourceOA().postReplyTo(ssid, commentId, replyToId, str);
                            callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
                                @Override
                                public void onSuccess(ResultData mResult) {
                                    MyToast.showToast("回复成功");
                                    //loadData();
                                    mComment.getText().clear();
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                    mCommentFragment.refreshUI();
                                }

                                @Override
                                public void onFailure(String string) {
                                    MyToast.showToast("回复失败");
                                    mComment.getText().clear();
                                    if (mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });
                            isReply = false;
                            mProgressDialog.setCancelable(false);
                            mProgressDialog.show("正在提交...");
                        }
                    } else {
                        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                        Call call = HttpRequest.getIResourceOA().postCommentList(ssid, 1, dataId, mComment.getText().toString());
                        callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
                            @Override
                            public void onSuccess(ResultData mResult) {
                                MyToast.showToast("评论成功");
                                //loadData();
                                mComment.getText().clear();
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                                mCommentFragment.refreshUI();
                            }

                            @Override
                            public void onFailure(String string) {
                                MyToast.showToast("评论失败");
                                mComment.getText().clear();
                                if (mProgressDialog.isShowing()) {
                                    mProgressDialog.dismiss();
                                }
                            }
                        });
                    }

                }
                break;
        }
    }
}
