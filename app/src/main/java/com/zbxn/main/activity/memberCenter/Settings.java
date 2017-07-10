package com.zbxn.main.activity.memberCenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pub.base.BaseActivity;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.DeviceUtils;
import com.pub.utils.FileUtils;
import com.pub.utils.MyToast;
import com.zbxn.R;
import com.zbxn.main.service.AppUpdateUtils;
import com.zbxn.main.service.AppVersion;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/21.
 */
public class Settings extends BaseActivity {
    /**
     * 检查缓存
     */
    private static final int Flag_Message_LoadCache = 1001;

    /**
     * 清理缓存
     */
    private static final int Flag_Message_ClearCache = 1002;

    /**
     * 检查更新
     */
    private static final int Flag_Message_CheckUpdate = 1003;

    /**
     * 消息_检查更新
     */
    private static final int Flag_Message_SendCheckUpdate = 1004;

    @BindView(R.id.mCacheSum)
    TextView mCacheSum;
    @BindView(R.id.mVersion)
    TextView mVersion;
    @BindView(R.id.mModifyPassword)
    LinearLayout mModifyPassword;
    @BindView(R.id.mMessageSetting)
    LinearLayout mMessageSetting;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setTitle("设置");
        init();
    }

    @Override
    public void initRight() {
        setRight1Show(false);
        setRight2Show(false);
        super.initRight();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Flag_Message_LoadCache:
                    if (msg.obj == null) {
                        mCacheSum.setText("0 MB");
                    } else {
                        double cachesize = Double.valueOf(msg.obj + "");
                        String result = String.format("%.2f", cachesize);
                        mCacheSum.setText(result + " MB");
                    }
                    break;
                case Flag_Message_ClearCache:
                    MyToast.showToast("清理完成");
                    loadCache();
                    break;
                case Flag_Message_CheckUpdate:
                    /*boolean hasNewVersion = (msg.arg1 == 1);
                    if (!hasNewVersion) {
//                    showToast("当前已是最新版本", Toast.LENGTH_SHORT);
                        MyToast.showToast("当前已是最新版本");
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();*/
                    break;
                case Flag_Message_SendCheckUpdate://版本检查是否最新
                    break;
                default:
                    break;
            }
        }
    };


    @OnClick({R.id.mClearCache, R.id.mCheckUpdate, R.id.mModifyPassword,R.id.mMessageSetting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mClearCache:
                showClearCacheDialog();
                break;
            case R.id.mCheckUpdate:
                getNewVersion();
                break;
            case R.id.mModifyPassword://修改密码
                startActivity(new Intent(Settings.this, ModifyPasswordActivity.class));
                break;
            case R.id.mMessageSetting://消息设置
                //对当前的网络状态进行一个检查
                if (isNetworkAvailable(Settings.this)){
                startActivity(new Intent(Settings.this, MessageSettings.class));
                }else {
                    MyToast.showToast("网络无连接状态不可进行消息设置");
                }
                break;
        }
    }


    /**
     * 自动升级
     */
    public void getNewVersion() {
        Call call = HttpRequest.getIResourceOA().getNewVersion("Android");
        callRequest(call, new HttpCallBack(AppVersion.class, Settings.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    AppVersion entity = (AppVersion) mResult.getData();
                    AppUpdateUtils.init(Settings.this, entity, false, false);
                    AppUpdateUtils.upDate();
                } else {
                    //MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                //MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void init() {
        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setCancelable(false);

        mVersion.setText("版本 "
                + DeviceUtils.getInstance(this).getAppVersionName());

        // 设置缓存信息
        loadCache();
    }

    // 加载缓存大小
    private void loadCache() {
        new Thread() {
            public void run() {
                File file = Glide.getPhotoCacheDir(Settings.this);
                if (!file.exists())
                    return;
                double size = FileUtils.sizeOfDirectory(file);
                size = size / 1024 / 1024;
                Message message = Message.obtain();
                message.what = Flag_Message_LoadCache;
                message.obj = size;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    // 清理缓存
    private void clearCache() {
        new Thread() {
            public void run() {
                Glide.get(Settings.this).clearDiskCache();
                Message message = Message.obtain();
                message.what = Flag_Message_ClearCache;
                mHandler.sendMessage(message);
            }
        }.start();
    }

    private void showClearCacheDialog() {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("你确定要清除缓存吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                clearCache();
            }
        });
        dialog.setNegativeButton("取消");
        dialog.show();
    }

}
