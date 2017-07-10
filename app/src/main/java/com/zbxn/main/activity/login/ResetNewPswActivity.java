package com.zbxn.main.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


public class ResetNewPswActivity extends BaseActivity {


    @BindView(R.id.myUserName)
    TextView myUserName;
    @BindView(R.id.etPsw1)
    EditText etPsw1;
    @BindView(R.id.isPassWordVis1)
    ImageView isPassWordVis1;
    @BindView(R.id.etDeletePsw1)
    ImageView etDeletePsw1;
    @BindView(R.id.myPassword)
    TextView myPassword;
    @BindView(R.id.etPsw2)
    EditText etPsw2;
    @BindView(R.id.isPassWordVis2)
    ImageView isPassWordVis2;
    @BindView(R.id.etDeletePsw2)
    ImageView etDeletePsw2;
    @BindView(R.id.activity_reset_new_psw)
    LinearLayout activityResetNewPsw;
    @BindView(R.id.btSure)
    CircularProgressButton btSure;
    private int isVisible1 = 1;
    private int isVisible2 = 1;
    private String mPhoneNumber;
    private String mVerificationCode;
    private String mPsw1;
    private String mPsw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_new_psw);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        isPassWordVis1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible1 == 1) {
                    etPsw1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPassWordVis1.setImageResource(R.mipmap.temp143);
                    isVisible1 = 2;
                } else {
                    etPsw1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPassWordVis1.setImageResource(R.mipmap.temp142);
                    isVisible1 = 1;
                }
            }
        });
        isPassWordVis2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible2 == 1) {
                    etPsw2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPassWordVis2.setImageResource(R.mipmap.temp143);
                    isVisible2 = 2;
                } else {
                    etPsw2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPassWordVis2.setImageResource(R.mipmap.temp142);
                    isVisible2 = 1;
                }
            }
        });


        mPhoneNumber = getIntent().getStringExtra("mPhoneNumber");
        mVerificationCode = getIntent().getStringExtra("mVerificationCode");
        etPsw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                etDeletePsw1.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                etDeletePsw1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etPsw1.setText("");
                    }
                });
            }
        });
        etPsw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                etDeletePsw2.setVisibility(isEmpty ? View.INVISIBLE : View.VISIBLE);
                etDeletePsw2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etPsw2.setText("");
                    }
                });
            }
        });
    }

    @OnClick({R.id.btSure})
    public void onClick(View view) {
        if (validate()){
            verificationpassword();
        }

    }

    //验证是否是手机号码
    private boolean validate() {
        if (StringUtils.isEmpty(mPhoneNumber)){
            MyToast.showToast("请重新输入手机号码");
            return false;
        }
        if (!ValidationUtil.isMobile(mPhoneNumber.trim())) {//验证手机号的格式
            MyToast.showToast("你输入的手机号格式不对");
            return false;
        }
        return true;
    }
    /**
     * 判断手机格式以及验证码以及密码的格式
     */
    public void verificationpassword() {
        mPsw1 = StringUtils.getEditText(etPsw1);
        mPsw2 = StringUtils.getEditText(etPsw2);
        if (!StringUtils.isEmpty(mVerificationCode)) {
            if (!StringUtils.isEmpty(mPsw1)) {
                if (mPsw1.length() >= 8 && mPsw1.length() <= 20) {
                    if (!StringUtils.isEmpty(mPsw2)) {
                        if (ValidationUtil.isABCNumber(mPsw2)) {
                            if (ValidationUtil.isABCNumber(mPsw1)) {
                                if (mPsw1.equals(mPsw2)) {
                                    //格式认证成功后

                                    Password(this, mPhoneNumber, mVerificationCode, mPsw2);

                                } else {
                                    MyToast.showToast("您输入的两次密码不相同");
                                }
                            }else {
                                MyToast.showToast("您输入的新密码格式有误");
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

    public void Password(Context context, String phoneNumber, String randomNumber, String passWord) {
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

    @Override
    public void initRight() {
        setTitle("找回密码");
        super.initRight();
    }

}
