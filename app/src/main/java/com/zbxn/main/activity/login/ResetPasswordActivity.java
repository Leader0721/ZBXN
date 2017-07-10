package com.zbxn.main.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.ToolbarParams;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.main.entity.ResetPasswordEntity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 项目名称：找回密码
 * 创建人：LiangHanXin
 * 创建时间：2016/10/28 14:11
 */
public class ResetPasswordActivity extends BaseActivity {


    @BindView(R.id.mMobilePhone)
    EditText mMobilePhone;//手机号
    @BindView(R.id.mVerificationCode)
    EditText mVerificationCode;//验证码
    @BindView(R.id.mVerification)

    TextView mVerification;
    @BindView(R.id.mNewPassword)
    EditText mNewPassword;//输入密码
    @BindView(R.id.mPasswordAgain)
    EditText mPasswordAgain;//再次输入
    @BindView(R.id.mComplete)
    Button mComplete;//确定
    private List<ResetPasswordEntity> mList;
    private ResetPasswordEntity sre;
    private int mi = 60;
    private Timer switchTimer;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        //绑定控件
        ButterKnife.bind(this);
        //设置标题
        setTitle("忘记密码");
        mComplete.setEnabled(false);
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("忘记密码");
        return super.onToolbarConfiguration(toolbar, params);
    }



   /* *//**
     * 滑动关闭当前Activitiv
     *//*
    @Override
    public boolean getSwipeBackEnable() {
//        return super.getSwipeBackEnable();
        return true;
    }*/

    @OnClick({R.id.mVerification, R.id.mComplete})
    public void onClick(View view) {
        String mmobile = mMobilePhone.getText().toString();//手机号
        String mverificationCode = mVerificationCode.getText().toString();//手机号
        switch (view.getId()) {

            case R.id.mVerification://验证码

                verification();
                if (!StringUtils.isEmpty(mmobile)) {

                    if (ValidationUtil.isMobile(mmobile)) {//验证手机号的格式
//                        mMobilePhone.setEnabled(false);
                        mComplete.setEnabled(true);
                        send();
                    } else {
                        MyToast.showToast("你输入的手机号格式不对");
                    }
                } else {
                    MyToast.showToast("手机号不能为空");
                }
                break;
            case R.id.mComplete://完成

                if (ValidationUtil.isMobile(mmobile)) {//验证手机号的格式

                    if (!StringUtils.isEmpty(mverificationCode)) {
                        if (ValidationUtil.isNumeric(mverificationCode)) {
                            mMobilePhone.setEnabled(false);
                            verificationpassword();
                        } else {
                            MyToast.showToast("验证码格式不正确");
                        }
                    } else {
                        MyToast.showToast("验证码不能为空");
                    }
                } else {
                    MyToast.showToast("手机号格式不正确");
                }

                break;
        }
    }


    /**
     * 判断手机格式获取验证码
     */


    public void verification() {
        String mmobile = mMobilePhone.getText().toString();//手机号
        if (ValidationUtil.isMobile(mmobile)) {//验证手机号的格式
           RuleTimeDataList(this, mmobile);
        } else {
            MyToast.showToast("你输入的手机号格式不对");
        }


    }

    /**
     * 倒计时
     */
    private void send() {
        mi = 60;
        if (switchTimer != null) {
            switchTimer.cancel();
            switchTimer = null;
        }
        switchTimer = new Timer();
        switchTimer.schedule(new TimerTask() {
            public void run() {
                mi -= 1;
                imageSwitcherHandler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
        mVerification.setText("(" + mi + ")重新发送");
        mVerification.setTextColor(0xffffffff);
        mVerification.setClickable(false);
        MyToast.showToast("验证码已发送到您的手机请注意查收！");
    }
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 0) {
//                MyToast.showLongToast(RegisterActivity.this, "验证码已发送到您的手机请注意查收！", Toast.LENGTH_LONG);
                MyToast.showToast("验证码已发送到您的手机请注意查收！");
            } else if (what == 1) {
                Object data = msg.obj;
//                MyToast.showLongToast(RegisterActivity.this, JsonUtil.getString(((Throwable) data).getMessage(), "description"), Toast.LENGTH_LONG);
            } else if (what == 2) {

            } else if (what == 3) {

            }
        }
    };
    private Handler imageSwitcherHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                if (mi <= 0) {
                    mVerification.setText("重新发送");
                    mVerification.setTextColor(0xffffffff);
                    mVerification.setClickable(true);
                    mMobilePhone.setEnabled(true);
                    if (switchTimer != null) {
                        switchTimer.cancel();
                        switchTimer = null;
                    }
                } else {
                    mVerification.setText("(" + mi + ")重新发送");
                }
            }
        }
    };

    /**
     * 判断手机格式以及验证码以及密码的格式
     */
    public void verificationpassword() {
        String phoneNumber = mMobilePhone.getText().toString();//手机号
        String mverificationCode = mVerificationCode.getText().toString();//验证码
        String mnew = mNewPassword.getText().toString();//密码
        String mpassword = mPasswordAgain.getText().toString();//再次输入

        if (!StringUtils.isEmpty(mverificationCode)) {
            if (!StringUtils.isEmpty(mnew)) {
                if (mnew.length() >= 8 && mnew.length() <= 20) {
                    if (!StringUtils.isEmpty(mpassword)) {
                        if (ValidationUtil.isABCNumber(mpassword)) {
                            if (mnew.equals(mpassword)) {
                                //格式认证成功后

                              Password(this, phoneNumber, mverificationCode, mnew);

                            } else {
                                MyToast.showToast("您输入的两次密码不相同");
                            }
                        } else {
                            MyToast.showToast("您输入的新密码格式有误");
                        }
                    } else {
                        MyToast.showToast("确认密码不能为空");
                    }
                } else {
                    MyToast.showToast("新密码格式有误");
                }
            } else {
                MyToast.showToast("新密码不能为空");
            }

        } else {
            MyToast.showToast("验证码不能为空");
        }
    }


    /**
     * 发送短信
     *
     */
    public void RuleTimeDataList(Context context, String phoneNumber) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        //请求网络
        Call call = HttpRequest.getIResourceOA().GetFindPassword(ssid, phoneNumber);
        callRequest(call, new HttpCallBack(ResetPasswordEntity.class, context, true) {


            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ResetPasswordEntity> list = mResult.getRows();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast( "获取网络数据失败");
            }
        });
    }
    /**
     * 重置密码
     *
     */
    public void Password(Context context,String phoneNumber,String randomNumber,String passWord) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        //请求网络
        Call call = HttpRequest.getIResourceOA().GetFindMessagePwd(ssid, phoneNumber,randomNumber,passWord);
        callRequest(call, new HttpCallBack(ResetPasswordEntity.class, context, true) {


            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("修改成功");
                    finish();

                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast( "获取网络数据失败");
            }
        });
    }

}
