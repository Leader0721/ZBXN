package com.zbxn.main.activity.mission;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.pub.base.BaseActivity;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.IsEmptyUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.MissionEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 项目名称：更新进度条
 * 创建人：LiangHanXin
 * 创建时间：2016/11/14 13:40
 */
public class ProgressActivity extends BaseActivity {
    /**
     * 输出标识java.util.List<{@link }>
     */
    public static final String Flag_Callback_ContactsPicker1 = "ontacts";
    @BindView(R.id.mProgress)
    EditText mProgresss;
    Intent data = new Intent();
    private MessageDialog mMessageDialog;

    private String progress;
    private int Progress;
    private String id;
    private int nowProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);
        setTitle("完成进度");
        initView();
    }

    /**
     * 接收任務id
     */
    private void initView() {
        id = getIntent().getStringExtra("id");
        nowProgress = getIntent().getIntExtra("progress", -1);
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_establish, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 创建按钮监听
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    /**
     * 创建按钮的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mEstablish:

                progress = StringUtils.getEditText(mProgresss);
                if (progress.length() > 4) {
                    MyToast.showToast("你输入的有误，请重新输入");
                    break;
                }

                if (ValidationUtil.isNumeric(progress)) {

                    if (!StringUtils.isEmpty(progress)) {
                        Progress = Integer.decode(progress);

                        if (IsEmptyUtil.isEmpty(mProgresss, "输入进度不能为空")) {

                            if (nowProgress != -1 && Progress > nowProgress) {
                                if (Progress == 100) {
                                    mMessageDialog = new MessageDialog(this);
                                    mMessageDialog.setTitle("任务提示");
                                    mMessageDialog.setMessage(getResources().getString(R.string.app_mission_tishi));
                                    mMessageDialog.setNegativeButton("取消");
                                    mMessageDialog.setPositiveButton("确认",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                progress(ProgressActivity.this, id + "", 100 + "");
                                                    postTaskState("", 13 + "");
                                                }

                                            });
                                    mMessageDialog.show();
                                } else if (Progress < 100) {//进度小于100时
                                    progress(id, Progress + "");
                                } else if (Progress > 100) {
                                    MyToast.showToast("你输入的有误，请重新输入");
                                    break;
                                }
                            } else {
                                MyToast.showToast("不能小于现有进度");
                            }
                        } else {
                            MyToast.showToast("输入不能为空");
                        }
                    } else {
                        MyToast.showToast("输入不能为空");
                    }

                } else {
                    MyToast.showToast("请输入进度");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 修改任务进度
     *
     * @param taskId
     * @param taskProgress
     */
    public void progress(String taskId, String taskProgress) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postProgress(ssid, currentCompanyId, taskId, taskProgress);
        callRequest(call, new HttpCallBack(MissionEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("修改成功");
                    setResult(RESULT_OK);
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

    /**
     * 修改任务状态
     *
     * @param context
     * @param taskState
     */
    public void postTaskState(String context, String taskState) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postTaskState(ssid, currentCompanyId, id, context, taskState);
        callRequest(call, new HttpCallBack(MissionEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    Intent intent = new Intent(getApplicationContext(), MissionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
