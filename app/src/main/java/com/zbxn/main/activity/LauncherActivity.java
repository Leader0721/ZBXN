package com.zbxn.main.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.utils.DeviceUtils;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.IOnLoginCallBack;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Member;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动页
 */
public class LauncherActivity extends Activity implements IOnLoginCallBack {

    /**
     * 启动页停留时间
     */
    private static final int DURATION = 2000;

    /**
     * 记录是否第一次启动
     */
    private static final String FIRST_APP_TAG = "launcher";
    @BindView(R.id.mLogo)
    ImageView mLogo;
    @BindView(R.id.mVersion)
    TextView mVersion;
    @BindView(R.id.launch_layout)
    RelativeLayout launchLayout;

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mLogo, "alpha", 0f, 1.0f);
        animator.setDuration(1600);
        animator.start();

        mVersion.setText("版本 " + DeviceUtils.getInstance(this).getAppVersionName());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, DURATION);
    }

    /**
     * 跳转到下一步
     */
    private void next() {
        SharedPreferences setting = getSharedPreferences(FIRST_APP_TAG, 0);
        boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {
            setting.edit().putBoolean("FIRST", false).commit();
            startActivity(new Intent(this, GuideActivity.class));
            finish();
            return;
        }
        Member member = Member.getLocalCache();
        if (member == null) {// 本地无缓存，直接登录
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {// 本地有缓存，用缓存登录
            String username = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME, null);
            String password = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_PASSWORD, null);
            member.setPassword(password);
            LoginActivity login = new LoginActivity(this);
            //登录请求
            login.login(this, username, password, false);
        }
    }

    /**
     * 自动登录请求回调
     *
     * @param result
     */
    @Override
    public void onResult(int result) {
        switch (result) {
            case 0:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case 1:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
