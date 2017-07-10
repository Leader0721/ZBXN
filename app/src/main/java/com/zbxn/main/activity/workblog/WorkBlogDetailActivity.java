package com.zbxn.main.activity.workblog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.memberCenter.KeyValue;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.entity.Comment;
import com.zbxn.main.entity.WorkBlog;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * @author: ysj
 * @date: 2016-12-12 13:05
 */
public class WorkBlogDetailActivity extends BaseActivity implements CommentFragment.CallBackComment {

    @BindView(R.id.mContent)
    WebView mContent;
    @BindView(R.id.mContentLength)
    TextView mContentLength;
    @BindView(R.id.mIcon)
    ImageView mIcon;
    @BindView(R.id.mOrigin)
    TextView mOrigin;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R.id.mComment)
    EditText mComment;
    @BindView(R.id.mPublish)
    TextView mPublish;

    private WorkBlog mWorkBlog;
    private CommentFragment mCommentFragment;
    private boolean isReply = false;//是否是'回复'
    private int mCommentId;//当前评论的id
    private int mReplyToId;//回复给谁
    private int mWorkBlogId;//日志id
    private String mReplyContent; //回复内容

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workblog_detail);
        ButterKnife.bind(this);
        setTitle("日志详情");
        mCommentFragment = (CommentFragment) getSupportFragmentManager().findFragmentById(R.id.mCommentFragment);
        int workBlogID = getIntent().getIntExtra("id", 0);
        getWorkBlogDetail(workBlogID);


        mContent.getSettings().setDefaultFontSize(14);
        mContent.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mContent.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mContent.getSettings().setBuiltInZoomControls(true);
        // 不显示webview缩放按钮
        mContent.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放
        mContent.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        mContent.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mContent.getSettings().setLoadWithOverviewMode(true);
        mContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // return super.shouldOverrideUrlLoading(view, url);
                return false;
            }
        });
    }

    /**
     * 获取日志详情
     *
     * @param id
     */
    public void getWorkBlogDetail(int id) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getWorkBlogDetail(ssid, id);
        callRequest(call, new HttpCallBack(WorkBlog.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    mWorkBlog = (WorkBlog) mResult.getData();
                    initWorkDetail();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    private void initWorkDetail() {
        mWorkBlogId = mWorkBlog.getId();
        mCommentFragment.reloadData(mWorkBlog.getId(), 2);

        String content = "<head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />" +
                "<style >img{max-width:100%;height:auto;}</style>" + "</head>\n<body><div>" +
                mWorkBlog.getWorkblogcontent() + "</div></body>";
//            mWebViewFragment.reloadDataWithContent(content);

        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        String mMimType = "text/html";
        String mEncoding = "utf-8";
        String mFailUrl = null;
        mContent.loadDataWithBaseURL(mBaseUrl, content, mMimType, mEncoding, mFailUrl);

//        mContentLength.setText(mWorkBlog.getWorkblogcontent().trim().length() + "字");
        mContentLength.setText(mWorkBlog.getBlognumber() + " 字 ");
        if (mWorkBlog.isFromMobile()) {// 来自移动端
            mOrigin.setText("来自移动端");
            mIcon.setImageResource(R.mipmap.bg_mobile);
        } else {
            mOrigin.setText("来自PC端");
            mIcon.setImageResource(R.mipmap.bg_pc);
        }
    }

    /**
     * 回复评论回调
     *
     * @param name      回复的谁
     * @param commentId 当前评论id
     * @param replyToId 被回复人的id
     */
    @Override
    public void sendComment(String name, int commentId, int replyToId) {
        mComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) mComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

        mReplyContent = "回复 " + name + ":";
        mComment.setText(mReplyContent);
        this.mCommentId = commentId;
        this.mReplyToId = replyToId;
        isReply = true;

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
                if (mComment.getText().length() < mReplyContent.length()) {
                    mComment.getText().clear();
                    mReplyContent = "";
                    isReply = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.mPublish)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mPublish:
                if (TextUtils.isEmpty(mComment.getText().toString().trim())) {
                    MyToast.showToast("评论内容不能为空");
                } else {
                    if (isReply) {
                        int size = mReplyContent.length();
                        if (mComment.getText().length() < size) {
                            mComment.setText("");
                            isReply = false;
                        }
                        String str = mComment.getText().toString().substring(size);

                        if (StringUtils.isEmpty(str.trim())) {
                            MyToast.showToast("评论内容不能为空");
                        } else {
                            postReplyTo(str);
                            isReply = false;
                        }

                    } else {
                        postComment();
                    }

                }
                break;
        }
    }

    /**
     * 评论日志
     */
    public void postComment() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().postCommentList(ssid, 2, mWorkBlogId, mComment.getText().toString());
        callRequest(call, new HttpCallBack(Comment.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("评论成功");
                } else {
                    MyToast.showToast(mResult.getMsg());
                    Toast.makeText(getBaseContext(), mResult.getMsg(), Toast.LENGTH_SHORT).show();
                }
                mComment.getText().clear();
                mCommentFragment.refreshUI();
                EventCustom eventCustom = new EventCustom();
                eventCustom.setTag(KeyEvent.UPDATEWORKBLOG);
                EventBus.getDefault().post(eventCustom);


            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
                mComment.getText().clear();
            }
        });
    }

    /**
     * 回复评论
     */
    public void postReplyTo(String content) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().postReplyTo(ssid, mCommentId, mReplyToId, content);
        callRequest(call, new HttpCallBack(Comment.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("回复成功");
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
                mComment.getText().clear();
                mCommentFragment.refreshUI();

                EventCustom eventCustom = new EventCustom();
                eventCustom.setTag(KeyEvent.UPDATEWORKBLOG);
                EventBus.getDefault().post(eventCustom);


            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
                mComment.getText().clear();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            EventCustom eventCustom = new EventCustom();
            eventCustom.setTag(KeyEvent.UPDATEWORKBLOG);
            EventBus.getDefault().post(eventCustom);
        }
        return super.onKeyDown(keyCode, event);
    }

}
