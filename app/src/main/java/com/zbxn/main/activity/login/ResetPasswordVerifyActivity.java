package com.zbxn.main.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
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

public class ResetPasswordVerifyActivity extends BaseActivity {


    @BindView(R.id.myUserName)
    TextView myUserName;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.etDeleteNum)
    ImageView etDeleteNum;
    @BindView(R.id.btGainCode)
    TextView btGainCode;
    @BindView(R.id.myPassword)
    TextView myPassword;
    @BindView(R.id.etVerificationCode)
    EditText etVerificationCode;
    @BindView(R.id.isPassWordVis)
    ImageView isPassWordVis;
    @BindView(R.id.deletePassword)
    ImageView deletePassword;
    @BindView(R.id.btNext)
    CircularProgressButton btNext;
    @BindView(R.id.mContentLayout)
    CoordinatorLayout mContentLayout;
    private int mi = 60;
    private Timer switchTimer;
    private int isVisible1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_verify);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //删除按钮的操作
        etVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                deletePassword.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                deletePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etVerificationCode.setText("");
                    }
                });
            }
        });
        //删除按钮的操作
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                etDeleteNum.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                etDeleteNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etPhoneNumber.setText("");
                    }
                });
            }
        });

        isPassWordVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible1 == 1) {
                    etVerificationCode.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPassWordVis.setImageResource(R.mipmap.temp143);
                    isVisible1 = 2;
                } else {
                    etVerificationCode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPassWordVis.setImageResource(R.mipmap.temp142);
                    isVisible1 = 1;
                }
            }
        });
    }

    @Override
    public void initRight() {
        setTitle("找回密码");
        super.initRight();
    }

    @OnClick({R.id.btGainCode, R.id.btNext})
    public void onClick(View view) {
        switch (view.getId()) {
            //获取验证码
            case R.id.btGainCode:
                btGainCode.setEnabled(false);
                if (validate()) {
                    send();
                    verification();
                } else {
                    btGainCode.setEnabled(true);
                }
                break;
            //下一步
            case R.id.btNext:
                if (validate() && validate2()) {
                    if (validate1()) {
                        Intent intent = new Intent(ResetPasswordVerifyActivity.this, ResetNewPswActivity.class);
                        intent.putExtra("mPhoneNumber", StringUtils.getEditText(etPhoneNumber));
                        intent.putExtra("mVerificationCode", StringUtils.getEditText(etVerificationCode));
                        startActivity(intent);
                        finish();
                    }
                }
                break;
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
        btGainCode.setText("(" + mi + ")重新发送");
        btGainCode.setTextColor(0xffffffff);
        btGainCode.setClickable(false);
        MyToast.showToast("验证码已发送到您的手机请注意查收！");
    }

    //验证是手机号码否为空
    private boolean validate() {
        if (StringUtils.isEmpty(etPhoneNumber)) {
            MyToast.showToast("请输入手机号码");
            return false;
        }
        return true;
    }

    //验证是验证码否为空
    private boolean validate2() {
        if (StringUtils.isEmpty(etVerificationCode)) {
            MyToast.showToast("验证码不能为空");
            return false;
        }
        return true;
    }

    //验证是否是手机号码
    private boolean validate1() {
        String mmobile = StringUtils.getEditText(etPhoneNumber);//手机号
        if (ValidationUtil.isMobile(mmobile.trim())) {//验证手机号的格式
            return true;
        } else {
            MyToast.showToast("你输入的手机号格式不对");
            return false;
        }

    }

    /**
     * 判断手机格式获取验证码
     */


    public void verification() {
        String mmobile = StringUtils.getEditText(etPhoneNumber);//手机号
        if (ValidationUtil.isMobile(mmobile)) {//验证手机号的格式
            RuleTimeDataList(this, mmobile);
        } else {
            MyToast.showToast("你输入的手机号格式不对");
        }
    }

    /**
     * 发送短信
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
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    private Handler imageSwitcherHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 0) {
                if (mi <= 0) {
                    btGainCode.setText("重新发送");
                    btGainCode.setTextColor(0xffffffff);
                    btGainCode.setClickable(true);
                    btGainCode.setEnabled(true);
                    if (switchTimer != null) {
                        switchTimer.cancel();
                        switchTimer = null;
                    }
                } else {
                    btGainCode.setText("(" + mi + ")重新发送");
                }
            }
        }
    };

}
