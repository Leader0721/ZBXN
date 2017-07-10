package com.zbxn.main.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.lidroid.xutils.exception.DbException;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.KEY;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.dbutils.DBUtils;
import com.umeng.analytics.MobclickAgent;
import com.zbxn.R;
import com.zbxn.main.activity.approvalmodule.ApplyDetailActivity;
import com.zbxn.main.activity.contacts.ContactsSearchActivity;
import com.zbxn.main.activity.login.IOnLoginCallBack;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.message.MessageDetailActivity;
import com.zbxn.main.activity.message.MessageSearchActivity;
import com.zbxn.main.activity.mission.MissionDetailsActivity;
import com.zbxn.main.activity.schedule.ScheduleInfoActivity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.service.AppUpdateUtils;
import com.zbxn.main.service.AppVersion;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements IOnLoginCallBack {

    /**
     * 两次按返回键的间隔
     */
    public static final long BACK_DURATION = 3000;

    @BindView(R.id.mShortCut)
    ImageView mShortCut;

    private MainFragmentManager mPagerManager;
    private FragmentManager mFragmentManager;
    private ShortCutFragment mShortCutFragment;

    /**
     * 第一次按返回键的时间
     */
    private long mFistPressBackTime = System.currentTimeMillis() - BACK_DURATION;

    private DBUtils<Contacts> mDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MobclickAgent.openActivityDurationTrack(false);

        //隐藏返回按钮
        setBackGone();
        //设置标题
        setTitle("首页");

        mPagerManager = new MainFragmentManager(this);

        init();

        if (mDBUtils == null) {
            mDBUtils = new DBUtils<>(Contacts.class);
        }
        findDepartmentContacts();

        String id = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, id));
        //如果停止了resume
        if (JPushInterface.isPushStopped(this)) {
            JPushInterface.resumePush(this);
        }
        String info_msg = PreferencesUtils.getString(BaseApp.getContext(),
                LoginActivity.FLAG_INFO_MSG, "");
        int info_msg_count = PreferencesUtils.getInt(BaseApp.getContext(),
                LoginActivity.FLAG_INFO_MSG_COUNT, 0);
        if (!StringUtils.isEmpty(info_msg) && info_msg_count < 3) {
            info_msg_count++;
            PreferencesUtils.putInt(BaseApp.getContext(), LoginActivity.FLAG_INFO_MSG_COUNT, info_msg_count);
            MessageDialog dialog = new MessageDialog(this);
            dialog.setTitle("提示");
            dialog.setMessage(info_msg);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {

                }
            });
            dialog.show();
        }

        setData(getIntent());

        getNewVersion();
    }

    @Override
    public void initRight() {
        setRight2Show(false);
        switch (mPagerManager.getCurrentPageIndex()) {
            case 0:
            case 2:
                setRight1Show(true);
                break;
            default:
                setRight1Show(false);
                break;
        }
        setRight1Icon(R.mipmap.nav_search);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        switch (mPagerManager.getCurrentPageIndex()) {
            case 0:
                Intent intent1 = new Intent(this, MessageSearchActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent = new Intent(this, ContactsSearchActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            default:
                setRight1Show(false);
                break;
        }

        super.actionRight1(menuItem);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mShortCutFragment == null || mShortCutFragment.isHidden()) {
                long duration = System.currentTimeMillis() - mFistPressBackTime;
                if (duration < BACK_DURATION) {
                    return super.onKeyDown(keyCode, event);
                } else {
                    mFistPressBackTime = System.currentTimeMillis();
                    MyToast.showToast("再按一次退出应用");
                    return false;
                }
            } else {
                hideShortCutView();
                return false;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @OnClick(R.id.mShortCut)
    public void onClick() {
        if (mShortCutFragment == null || mShortCutFragment.isHidden()) {
            showShortCutView();
        } else {
            hideShortCutView();
        }
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setData(intent);

        boolean isSwitchCompany = intent.getBooleanExtra("isSwitchCompany", false);
        if (isSwitchCompany) {
//            finish();
//            Intent intent1 = new Intent(getApplicationContext(), Main.class);
//            startActivity(intent1);
            /*Member member = Member.getLocalCache();
            if (member != null) {// 本地无缓存，直接登录
                String username = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_USERNAME, null);
                String password = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_PASSWORD, null);
                member.setPassword(password);
                LoginActivity login = new LoginActivity(this);
                //登录请求
                login.login(this, username, password, false);
            }*/
            Intent intent1 = new Intent(this, LauncherActivity.class);
            startActivity(intent1);
            finish();
        }

        //此处专供刷新推送消息使用
        int index = intent.getIntExtra("index", -1);
        if (index == 0) {
            mPagerManager.setSelection(0);
        }
    }


    private void setData(Intent intent1) {
        boolean isExit = intent1.getBooleanExtra("isExit", false);
        if (isExit) {
            finish();
            Intent intent2 = new Intent(this, LoginActivity.class);
            startActivity(intent2);
            return;
        }
        String extra = intent1.getStringExtra("extra");
        String type = "";
        String id = "";
        String msgId = "";
        try {
            JSONObject json = new JSONObject(extra);
            Iterator<String> it = json.keys();

            while (it.hasNext()) {
                String myKey = it.next().toString();
                Log.d("Main", "结果：[" + myKey + "：" + json.optString(myKey) + "]");
                if ("type".equals(myKey)) {
                    type = json.optString(myKey);
                } else if ("id".equals(myKey)) {
                    id = json.optString(myKey);
                } else if ("msgId".equals(myKey)) {
                    msgId = json.optString(myKey);
                }
            }
        } catch (Exception e) {
            Log.e("Main", "Get message extra JSON error!");
            return;
        }
        try {
            Intent intent;
            switch (type) {
                case "1"://系统消息
                    intent = new Intent(this, MessageDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    break;
                case "11"://公告消息
                    intent = new Intent(this, MessageDetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", 11);
                    startActivity(intent);
                    break;
                case "23"://日志消息
                    break;
                case "25"://日程管理
                    intent = new Intent(this, ScheduleInfoActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("sign", 1);
                    intent.putExtra("flag", -1);
                    startActivity(intent);
                    break;
                case "31"://审批管理
                    intent = new Intent(this, ApplyDetailActivity.class);
                    intent.putExtra("approvalID", id);
                    startActivity(intent);
                    break;
                case "32"://任务
                    intent = new Intent(this, MissionDetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    break;
                case "41"://会议通知
                    break;
                case "51"://跟进评论
                    break;
                case "55"://评论回复
                    break;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示快捷入口
     */
    private void showShortCutView() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (mShortCutFragment == null) {
            mShortCutFragment = new ShortCutFragment();
            transaction.add(R.id.mShortCutContainer, mShortCutFragment, mShortCutFragment.mTitle);
            mShortCutFragment.setOnItemSelectListener(new ShortCutFragment.OnItemSelectListener() {
                @Override
                public void OnItemSelected() {
                    hideShortCutView();
                }
            });
        } else {
            transaction.show(mShortCutFragment);
            mShortCutFragment.show();
        }
        transaction.commit();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mShortCut, "rotation", 0, 135));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /**
     * 隐藏快捷入口
     */
    private void hideShortCutView() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mShortCutFragment);
        transaction.commit();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mShortCut, "rotation", 45, 0));
        animatorSet.setInterpolator(new OvershootInterpolator());
        animatorSet.setDuration(400);
        animatorSet.start();
    }

    /**
     * 获取通讯录
     */
    public void findDepartmentContacts() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().getFindDepartmentContacts(ssid);
        callRequest(call, new HttpCallBack(Contacts.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<Contacts> list = mResult.getRows();
                    /*String json = JsonUtil.toJsonString(list);
                    //缓存数据到本地
                    PreferencesUtils.putString(BaseApp.CONTEXT, KEY.CONTACTLIST, json);*/

                    saveToLocal(list);
                } else {
                    //MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
            }
        });
    }

    //通讯录保存至本地
    private void saveToLocal(List<Contacts> list) {
        try {
            BaseApp.DBLoader.dropTable(Contacts.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        mDBUtils.deleteAll();
        Contacts[] array = new Contacts[list.size()];
        list.toArray(array);
        mDBUtils.add(array);
    }

    /**
     * ************* 设置别名 *************************
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    System.out.println("Set alias in handler.");

                    Set<String> tag = new HashSet<String>();
                    tag.add("EnterpriseApp");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj, tag, mAliasCallback);

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
                    System.out.println(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(MainActivity.this, KEY.ISSETALIAS, true);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    System.out.println(logs);

                    // 保存是否设置别名状态
                    PreferencesUtils.putBoolean(MainActivity.this, KEY.ISSETALIAS, false);

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

    /**
     * 自动升级
     */
    public void getNewVersion() {
        Call call = HttpRequest.getIResourceOA().getNewVersion("Android");
        callRequest(call, new HttpCallBack(AppVersion.class, MainActivity.this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    AppVersion entity = (AppVersion) mResult.getData();
                    AppUpdateUtils.init(MainActivity.this, entity, true, false);
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
