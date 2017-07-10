package com.zbxn.main.activity.approvalmodule;

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
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.ProgressWebView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.ApplyEntity;
import com.zbxn.main.entity.ApprovalInfoEntity;

import java.net.URLDecoder;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 表单详情
 *
 * @author: ysj
 * @date: 2016-10-11 14:08
 */
public class ApplyDetailActivity extends BaseActivity {

    private static final int Flag_ApplyForm_Approval = 1000;// 同意并转审
    private static final int Flag_ApplyForm_Inquire = 1001;// 同意并结束
    private static final int Flag_ApplyForm_Rejected = 1003;// 驳回
    private static final int Flag_ApplyForm_Stop = 1004;// 终止

    private static final int Flag_State_4 = 4; //撤回
    private static final int Flag_State_5 = 5; //催办

    @BindView(R.id.mWebView)
    ProgressWebView mWebView;
    @BindView(R.id.applyEnd)
    TextView applyEnd;
    @BindView(R.id.applyNext)
    TextView applyNext;
    @BindView(R.id.applyStop)
    TextView applyStop;
    @BindView(R.id.view_next)
    View viewNext;
    @BindView(R.id.view_stop)
    View viewStop;
    @BindView(R.id.bottom)
    LinearLayout bottom;

    private int flag = -1;
    private String approvalID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applydetail);
        ButterKnife.bind(this);
        setTitle("申请单详情");
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        approvalID = intent.getStringExtra("approvalID");
        flag = intent.getIntExtra("flag", -1);
        getInfoType();
        switch (flag) {
            case Flag_ApplyForm_Approval:
                applyStop.setVisibility(View.GONE);
                viewStop.setVisibility(View.GONE);
                applyEnd.setText("撤回");
                applyNext.setText("催办");
                break;
            case Flag_ApplyForm_Inquire:
                applyNext.setVisibility(View.GONE);
                viewNext.setVisibility(View.GONE);
                applyStop.setVisibility(View.GONE);
                viewStop.setVisibility(View.GONE);
                applyEnd.setText("终止");
                break;
        }

        mWebView.getSettings().setDefaultFontSize(14);
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        // 不显示webview缩放按钮
        mWebView.getSettings().setDisplayZoomControls(false);
        // 扩大比例的缩放
        mWebView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        mWebView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setLoadWithOverviewMode(true);
//        1、手机向html 选完通讯录人员传  javascript:getMessageFromApp('{'userid':'','name':''}')
//        2、html向手机 fromJsMessage:1    1--跳转到通讯录选人   2--提交成功关闭页面
        mWebView.setWebViewClient(new WebViewClient() {

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


        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        mWebView.loadUrl(server + "/oaApproveInfoController/selectApproveByid.do?tokenid=" + ssid + "&infoid=" + approvalID);
    }

    @OnClick({R.id.applyEnd, R.id.applyNext, R.id.applyStop})
    public void onClick(View view) {
        Intent intent = new Intent(this, ApprovalOpinionActivity.class);
        switch (view.getId()) {
            case R.id.applyEnd:
                if (flag == Flag_ApplyForm_Approval) { // 撤回
                    postApprovalOpinion(Flag_State_4);
                    return;
                } else if (flag == Flag_ApplyForm_Inquire) { // 终止
                    intent.putExtra("flag", Flag_ApplyForm_Stop);
                    intent.putExtra("approvalID", approvalID);
                } else if (flag == -1) { // 同意并结束
                    intent.putExtra("flag", Flag_ApplyForm_Inquire);
                    intent.putExtra("approvalID", approvalID);
                    intent.putExtra("state", 1);
                    intent.putExtra("isAgree", true);
                }
                break;
            case R.id.applyNext:
                if (flag == Flag_ApplyForm_Approval) { // 催办
                    postApprovalOpinion(Flag_State_5);
                    return;
                } else if (flag == -1) { // 同意并转审批
                    intent.putExtra("flag", Flag_ApplyForm_Approval);
                    intent.putExtra("approvalID", approvalID);
                    intent.putExtra("isAgree", true);
                }
                break;
            case R.id.applyStop:
                if (flag == -1) { // 驳回
                    intent.putExtra("flag", Flag_ApplyForm_Rejected);
                    intent.putExtra("approvalID", approvalID);
                }
                break;
        }
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取当前用户身份（申请人或审批人）
     */
    public void getInfoType() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getInfoController(ssid, approvalID);
        callRequest(call, new HttpCallBack(ApprovalInfoEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    List<ApprovalInfoEntity> list = mResult.getRows();
                    ApprovalInfoEntity infoEntity = list.get(0);
                    if (infoEntity.getState() == 0 && infoEntity.getTheApply() != 2) {
                        bottom.setVisibility(View.VISIBLE);
                    }
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

    /**
     * 修改审批状态
     *
     * @param state
     */
    public void postApprovalOpinion(int state) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().postApprovalOpinion(ssid, approvalID, state, null, null);
        callRequest(call, new HttpCallBack(ApplyEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("修改成功");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
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

}
