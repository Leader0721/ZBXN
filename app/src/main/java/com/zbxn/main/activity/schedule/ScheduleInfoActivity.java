package com.zbxn.main.activity.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.EventCustom;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.ScheduleDetailEntity;
import com.zbxn.main.entity.ScheduleRuleEntity;

import org.simple.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/10.
 */
public class ScheduleInfoActivity extends BaseActivity {
    @BindView(R.id.iv_editInfo)
    ImageView ivEditInfo;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.tt_title)
    TextView ttTitle;
    @BindView(R.id.mContent)
    WebView mContent;
    @BindView(R.id.tt_time)
    TextView ttTime;
    @BindView(R.id.tt_Alert)
    TextView ttAlert;
    @BindView(R.id.Alert_linear)
    RelativeLayout AlertLinear;
    @BindView(R.id.tt_CreateMan)
    TextView ttCreateMan;
    @BindView(R.id.rll_CreateMan)
    RelativeLayout rllCreateMan;
    @BindView(R.id.tt_PartMan)
    TextView ttPartMan;
    @BindView(R.id.ttFinishTime)
    TextView ttFinishTime;
    @BindView(R.id.part_linear)
    RelativeLayout partLinear;
    @BindView(R.id.m_tv)
    TextView mTv;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private String mScheduleID;
    private ScheduleDetailEntity entity;
    private boolean isWorkmate;    //判断是同事的还是自己的

    private List<Contacts> listAll = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduleinfo);
        setTitle("日程详情");
        ButterKnife.bind(this);
        initView();
//        接口暂时没用，需要用老的
        getScheduleDetail();
    }

    //获取日程详情
    private void getScheduleDetail() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        String userID = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        Call call = HttpRequest.getIResourceOANetAction().getScheduleDetails(ssid, currentCompanyId, mScheduleID, userID);
        //ScheduleDetailEntity需要更改
        callRequest(call, new HttpCallBack(ScheduleDetailEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    entity = (ScheduleDetailEntity) mResult.getData();
                    if (entity != null) {

                        if (!StringUtils.isEmpty(entity.getTitle())) {
                            String title = entity.getTitle();
                            ttTitle.setText("标题 : " + title);
                        } else {
                            MyToast.showToast("该日程不存在");
                            finish();
                        }


                        if (!StringUtils.isEmpty(entity.getButtonList())) {
                            if (entity.getButtonList().contains("Edit")) {
                                ivEditInfo.setVisibility(View.VISIBLE);
                            }

                            if (entity.getButtonList().contains("Delete")) {
                                ivDelete.setVisibility(View.VISIBLE);
                            }
                            if (entity.getButtonList().contains("Share")) {
                                ivShare.setVisibility(View.VISIBLE);
                            }
                        }

                        String weekStr = "";
                        if (!StringUtils.isEmpty(entity.getWeekStr())) {
                            if (entity.getWeekStr().contains("Monday")) {
                                weekStr = "周一  ";
                            }
                            if (entity.getWeekStr().contains("Tuesday")) {
                                weekStr = weekStr + "周二  ";
                            }
                            if (entity.getWeekStr().contains("Wednesday")) {
                                weekStr = weekStr + "周三  ";
                            }
                            if (entity.getWeekStr().contains("Thursday")) {
                                weekStr = weekStr + "周四  ";
                            }
                            if (entity.getWeekStr().contains("Friday")) {
                                weekStr = weekStr + "周五  ";
                            }
                            if (entity.getWeekStr().contains("Saturday")) {
                                weekStr = weekStr + "周六  ";
                            }
                            if (entity.getWeekStr().contains("Sunday")) {
                                weekStr = weekStr + "周天  ";
                            }
                        }

                        String repeat = "";
                        String finish = "";

                        if (entity.isIsRepeat()) {
                            switch (entity.getRepeatType()) {
                                case 0:
                                    repeat = "每 " + entity.getFrequency() + " 天";
                                    break;
                                case 1:
                                    if (StringUtils.isEmpty(weekStr)) {
                                        repeat = "每 " + entity.getFrequency();
                                    } else {
                                        repeat = "每 " + entity.getFrequency() + " 周" + "(   " + weekStr + " )";
                                    }
                                    break;
                                case 2:
                                    repeat = "每 " + entity.getFrequency() + " 月";
                                    break;
                                case 3:
                                    repeat = "每 " + entity.getFrequency() + " 年";
                                    break;
                            }
                        } else {
                            repeat = "一次性日程";
                        }


                        switch (entity.getFinishType()) {
                            case 0:
                                finish = "永不结束";
                                break;
                            case 1:
                                finish = "一共循环  " + entity.getFinishTimes() + "  次";
                                break;
                            case 2:
                                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                Date endDate = null;
                                try {
                                    endDate = sdf1.parse(entity.getFinishTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                finish = " 至: " + sdf1.format(endDate);
                                break;
                        }

                        if (!entity.isIsRepeat()) {
                            ttFinishTime.setText(repeat);
                        } else {
                            ttFinishTime.setText(repeat + " / " + finish);
                        }




                       /* if (!entity.isIsRepeat()) {
                            ttFinishTime.setVisibility(View.GONE);
                        } else {
                            if (StringUtils.isEmpty(weekStr)) {
                                if (entity.getFinishTimes() == 0) {
                                    if (StringUtils.isEmpty(entity.getFinishTime())) {

                                    } else {
                                        ttFinishTime.setVisibility(View.VISIBLE);
                                        ttFinishTime.setText("至: " + entity.getFinishTime());
                                    }
                                } else {
                                    ttFinishTime.setVisibility(View.VISIBLE);
                                    ttFinishTime.setText("一共循环  " + entity.getFinishTimes() + "  次");
                                }
                            } else {
                                if (entity.getFinishTimes() == 0) {
                                    if (StringUtils.isEmpty(entity.getFinishTime())) {
                                        ttFinishTime.setVisibility(View.VISIBLE);
                                        ttFinishTime.setText("每周 ( " + weekStr + " )");
                                    } else {
                                        ttFinishTime.setVisibility(View.VISIBLE);
                                        entity.getFinishTime().replace("T", "   ");
                                        ttFinishTime.setText("每周 ( " + weekStr + " )" + "  /  " + " 至: " + entity.getFinishTime());
                                    }
                                } else {
                                    ttFinishTime.setVisibility(View.VISIBLE);
                                    ttFinishTime.setText("每周 ( " + weekStr + " )" + "  /  " + "一共循环  " + entity.getFinishTimes() + "  次");
                                }
                            }
                        }
*/
                        if (!StringUtils.isEmpty(entity.getStartTime()) && !StringUtils.isEmpty(entity.getEndTime())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                            try {
                                if (entity.isIsAllday()) {
                                    Date startDate = sdf1.parse(entity.getStartTime());
                                    Date endDate = sdf1.parse(entity.getEndTime());
                                    if (startDate.equals(endDate)) {
                                        ttTime.setText("全天 :" + sdf1.format(startDate));
                                    } else {
                                        ttTime.setText("全天 :" + sdf1.format(startDate) + " --- " + sdf1.format(endDate));
                                    }
                                } else {
                                    Date startDate = sdf.parse(entity.getStartTime());
                                    Date endDate = sdf.parse(entity.getEndTime());
                                    ttTime.setText("起始时间：" + sdf.format(startDate) + " --- " + sdf.format(endDate));
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            MyToast.showToast("该日程不存在");
                            finish();

                        }


                        try {
                            listAll = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("isDepartment", "=", null).orderBy("captialChar"));//
                        } catch (DbException e) {
                            e.printStackTrace();
                        }


                        for (int i = 0; i < listAll.size(); i++) {
                            if (listAll.get(i).getId() == entity.getUserID()) {
                                ttCreateMan.setText(listAll.get(i).getUserName());
                            }
                        }

                        if (entity.isIsAlarm()) {
                            switch (entity.getAlarmType()) {
                                case 0:
                                    ttAlert.setText("开始时提醒");
                                    break;
                                case 1:
                                    ttAlert.setText("10分钟前");
                                    break;
                                case 2:
                                    ttAlert.setText("15分钟前");
                                    break;
                                case 3:
                                    ttAlert.setText("30分钟前");
                                    break;
                                case 4:
                                    ttAlert.setText("1小时前");
                                    break;
                                case 5:
                                    ttAlert.setText("2小时前");
                                    break;
                                case 6:
                                    ttAlert.setText("一天前");
                                    break;
                            }
                        } else {
                            ttAlert.setText("关闭提醒");
                        }

                        if (!StringUtils.isEmpty(entity.getSelectUserList())) {
                            List<ScheduleDetailEntity.SelectUserListBean> list = entity.getSelectUserList();
                            if (list.size() > 3) {
                                String sb;
                                sb = list.get(0).getUserName() + " , " + list.get(1).getUserName() + " 等 " + list.size() + " 人 ";
                                ttPartMan.setText(sb.toString().substring(0, sb.length() - 1));
                            } else if (list.size() == 3) {
                                ttPartMan.setText(list.get(0).getUserName() + "," + list.get(1).getUserName() + "," + list.get(2).getUserName());
                            } else if (list.size() == 2) {
                                ttPartMan.setText(list.get(0).getUserName() + "," + list.get(1).getUserName());
                            } else if (list.size() == 1) {
                                ttPartMan.setText(list.get(0).getUserName());
                            } else if (list.size() == 0) {
                                ttPartMan.setText("无");
                            }
                        }

                        //這個暫時用的是根據長度進行的判斷，以后进行相应的修改
//                        if (entity.getScheduleDetail().length() <= 29) {
//                           llContent.setVisibility(View.GONE);
//                        }else {
//                            llContent.setVisibility(View.VISIBLE);
//                        }

                        if (StringUtils.isEmpty(entity.getScheduleDetail())) {
                            llContent.setVisibility(View.GONE);
                        }else {
                            llContent.setVisibility(View.VISIBLE);
                        }




                        String content = "<head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />" +
                                "<style >img{max-width:100%;height:auto;}</style>" + "</head>\n<body><div>" +
                                entity.getScheduleDetail() + "</div></body>";

                        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
                        String mMimType = "text/html";
                        String mEncoding = "utf-8";
                        String mFailUrl = null;

                        mContent.loadDataWithBaseURL(mBaseUrl, content, mMimType, mEncoding, mFailUrl);

                    }
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    private void initView() {
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

        mScheduleID = getIntent().getStringExtra("id");
        isWorkmate = getIntent().getBooleanExtra("isWorkmate", false);
        if (!isWorkmate) {
            ivShare.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.part_linear, R.id.iv_delete, R.id.iv_share, R.id.iv_editInfo,})
    public void onClick(View view) {
        Intent intent = null;
        Bundle bundle = null;
        switch (view.getId()) {
            case R.id.part_linear:

                if (entity.getSelectUserList().size() == 0) {
                    MyToast.showToast("该日程暂无参与人");
                } else {
                    //参与人
                    intent = new Intent();
                    bundle = new Bundle();
//                bundle.putSerializable("entity", entity);
                    intent.putExtra("entity", entity);
                    intent.putExtras(bundle);
                    intent.setClass(ScheduleInfoActivity.this, PartManActivity.class);
                    startActivity(intent);
                }


                break;
            case R.id.iv_delete:
                //删除
                final MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.setMessage("确认删除吗 ？ ");
                messageDialog.setTitle("提示");
                messageDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSchedule();
                        messageDialog.dismiss();
                    }
                });
                messageDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageDialog.dismiss();
                    }
                });
                messageDialog.show();
                break;
            case R.id.iv_share:
                //分享
                ForwardSchedule(mScheduleID);
                break;
            case R.id.iv_editInfo:
                //编辑
                intent = new Intent();
                bundle = new Bundle();
                bundle.putSerializable("entity", entity);
                intent.putExtras(bundle);
                intent.putExtra("isEdit", true);

                intent.setClass(ScheduleInfoActivity.this, NewScheduActivity.class);
                startActivityForResult(intent, 1000);

                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1000) {
            finish();
        }


    }

    /**
     * 分享日程
     */
    public void ForwardSchedule(String id) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        String userID = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
        Call call = HttpRequest.getIResourceOANetAction().ForwardSchedule(ssid, currentCompanyId, id, userID);
        callRequest(call, new HttpCallBack(ScheduleDetailEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    MyToast.showToast("复制日程成功");
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(ScheduleActivity.SUCCESS2);
                    EventBus.getDefault().post(eventCustom);
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
     * 删除日程
     */
    public void deleteSchedule() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postDeleteSchedule(ssid, currentCompanyId, mScheduleID);
        callRequest(call, new HttpCallBack(ScheduleRuleEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("删除成功");
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(ScheduleActivity.SUCCESS2);
                    EventBus.getDefault().post(eventCustom);
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
