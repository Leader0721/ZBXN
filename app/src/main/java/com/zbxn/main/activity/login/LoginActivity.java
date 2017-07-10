package com.zbxn.main.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.KEY;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.main.activity.MainActivity;
import com.zbxn.main.entity.Member;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;

/**
 * Created by GISirFive on 2016/8/3.
 */
public class LoginActivity extends BaseActivity {
    public static final String FLAG_INPUT_USERNAME = "username";
    public static final String FLAG_INPUT_PASSWORD = "password";
    public static final String FLAG_INPUT_HEADURL = "headUrl";
    public static final String FLAG_INPUT_ID = "id";
    public static final String FLAG_SSID = "ssid";
    public static final String FLAG_ZMSCOMPANYID = "zmsCompanyId";
    public static final String FLAG_ZMSCOMPANYNAME = "zmsCompanyName";
    public static final String FLAG_INFO_MSG = "info_msg";
    public static final String FLAG_INFO_MSG_COUNT = "info_msg_count";
    public static final String FLAG_PERMISSIONIDS = "permissionIds";
    public static final String FLAG_DEPARMENTNAME = "deparmentName";
    private int isVisible = 1;
    @BindView(R.id.myUserName)
    TextView myUserName;
    @BindView(R.id.myPassword)
    TextView myPassword;
    @BindView(R.id.mUserName)
    EditText mUserName;
    @BindView(R.id.mPassword)
    EditText mPassword;
    @BindView(R.id.mForgetPassword)
    TextView mForgetPassword;
    @BindView(R.id.mRegister)
    TextView mRegister;
    @BindView(R.id.mContentLayout)
    CoordinatorLayout mContentLayout;

    @BindView(R.id.deleteNum)
    ImageView deleteNum;
    @BindView(R.id.deletePassword)
    ImageView deletePassword;
    @BindView(R.id.isPassWordVis)
    ImageView isPassWordVis;

    private IOnLoginCallBack loginCallBack;

    public LoginActivity() {
    }

    public LoginActivity(IOnLoginCallBack loginCallBack) {
        this.loginCallBack = loginCallBack;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定控件
        ButterKnife.bind(this);

        //隐藏返回按钮
        setBackGone();
        //设置标题
        setTitle("登录");
        init();
        in();
        initDeleteHint();

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
        JPushInterface.stopPush(LoginActivity.this);
    }

    private void initDeleteHint() {
        if (!StringUtils.isEmpty(mUserName.getText().toString())) {
            deleteNum.setVisibility(View.VISIBLE);
        }


        isPassWordVis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible == 1) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPassWordVis.setImageResource(R.mipmap.temp143);
                    isVisible = 2;
                } else {
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPassWordVis.setImageResource(R.mipmap.temp142);
                    isVisible = 1;
                }
                mPassword.setSelection(mPassword.getText().toString().length());
            }
        });


        //设置是否显示
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                deleteNum.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deleteNum.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                deleteNum.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

            }
        });

        deleteNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setText("");
            }
        });

        deletePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword.setText("");
                isPassWordVis.setVisibility(View.INVISIBLE);
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isPassWordVis.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPassWordVis.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                isPassWordVis.setVisibility(View.VISIBLE);
                boolean isEmpty = (s == null || s.length() == 0);
                deletePassword.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

            }
        });

//        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    myUserName.setTextColor(getResources().getColor(R.color.cpb_blue));
//                } else {
//                    myUserName.setTextColor(getResources().getColor(R.color.tvc3));
//                }
//            }
//        });
//        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    myPassword.setTextColor(getResources().getColor(R.color.cpb_blue));
//                } else {
//                    myPassword.setTextColor(getResources().getColor(R.color.tvc3));
//                }
//            }
//        });
    }

    private void in() {
        String username = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME);
        String password = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_PASSWORD);
        if (!StringUtils.isEmpty(username)) {
            mUserName.setText(username);
        }
        if (!StringUtils.isEmpty(password)) {
            mPassword.setText(password);
        }
    }

    @OnClick({R.id.mBtnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnLogin:
                if (StringUtils.isEmpty(mUserName)) {
                    MyToast.showToast("用户名不能为空");
                } else if (StringUtils.isEmpty(mPassword)) {
                    MyToast.showToast("密码不能为空");
                } else if (ValidationUtil.isNumeric(StringUtils.getEditText(mUserName))) {
                    if (ValidationUtil.isABCNumber(StringUtils.getEditText(mPassword))) {
                        login(this, StringUtils.getEditText(mUserName), StringUtils.getEditText(mPassword), true);
                    } else {
                        MyToast.showToast("密码输入的格式有误");
                    }

                } else {
                    MyToast.showToast("账号输入的格式有误");
                }
                break;
        }
    }

    private void init() {
        mUserName.setText("");
        mPassword.setText("");
        isPassWordVis.setVisibility(View.INVISIBLE);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));//跳转注册界面
            }
        });
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordVerifyActivity.class));
            }
        });

    }

    public void finishForResult(boolean b) {
        if (b) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    /**
     * 登录
     */
    public void login(Context context, final String userName, final String password, boolean isShowProgress) {
        //请求网络
        Call call = HttpRequest.getIResourceOA().GetLogin(userName, password);
        callRequest(call, new HttpCallBack(Member.class, context, isShowProgress) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess()) || "100".equals(mResult.getSuccess())) {//0成功   100未完善信息
                    Member entity = (Member) mResult.getData();
                    //将用户输入的密码保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(), LoginActivity.FLAG_INPUT_PASSWORD, password);
                    //将用户输入的账号保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_USERNAME, userName);
                    //登录用户头像
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_HEADURL, entity.getPortrait());
                    //登录用户id
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_INPUT_ID, entity.getId() + "");
                    //用户默认职位权限
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_PERMISSIONIDS, entity.getPermissionIds() + "");
                    //部门名称
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_DEPARMENTNAME, entity.getDepartmentName());

                    //将用户输入的账号保存起来，以便下次使用
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_SSID, entity.getSsid());

                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_ZMSCOMPANYID, entity.getZmsCompanyId());
                    PreferencesUtils.putString(BaseApp.getContext(),
                            LoginActivity.FLAG_ZMSCOMPANYNAME, entity.getZmsCompanyName());

                    if ("100".equals(mResult.getSuccess())) {
                        PreferencesUtils.putString(BaseApp.getContext(),
                                LoginActivity.FLAG_INFO_MSG, mResult.getMsg());
                    } else {
                        PreferencesUtils.putString(BaseApp.getContext(),
                                LoginActivity.FLAG_INFO_MSG, "");
                    }

                    Member.save(entity);
                    if (loginCallBack != null) {
                        loginCallBack.onResult(0);
                    } else {
                        finishForResult(true);
                    }

                } else {
                    if (loginCallBack != null) {
                        loginCallBack.onResult(1);
                    }
                    Member.clear();
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                if (loginCallBack != null) {
                    loginCallBack.onResult(1);
                }
                Member.clear();
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * ************* 设置别名 *************************
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    System.out.println("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(LoginActivity.this,
                            (String) msg.obj, null, mAliasCallback);
                    break;
                default:
                    System.out.println("Unhandled msg - " + msg.what);
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    System.out.println("alia退出:" + alias + "|");
                    System.out.println(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(LoginActivity.this, KEY.ISSETALIAS, false);

                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    System.out.println(logs);

                    // 保存是否设置别名状态
                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(LoginActivity.this, KEY.ISSETALIAS, false);

                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(
                            mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    System.out.println(logs);
            }
        }
    };


}
