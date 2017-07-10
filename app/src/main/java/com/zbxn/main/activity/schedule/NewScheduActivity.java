package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.pub.widget.slidedatetimepicker.SlideDateTimeListener;
import com.pub.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.ScheduleDetailEntity;
import com.zbxn.main.entity.ScheduleRuleEntity;
import com.zcw.togglebutton.ToggleButton;

import org.simple.eventbus.EventBus;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

import static com.pub.utils.StringUtils.getEditText;

/**
 * Created by Administrator on 2017/2/10.
 */
public class NewScheduActivity extends BaseActivity {
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.mToggleButtonwholeday)
    ToggleButton mToggleButtonwholeday;
    @BindView(R.id.tt_starttime)
    TextView ttStarttime;
    @BindView(R.id.startTime_linear)
    RelativeLayout startTimeLinear;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.tt_endTime)
    TextView ttEndTime;
    @BindView(R.id.endTime_linear)
    RelativeLayout endTimeLinear;
    @BindView(R.id.linearLayout_one)
    LinearLayout linearLayoutOne;
    @BindView(R.id.tt_repeat)
    TextView ttRepeat;
    @BindView(R.id.repeat_linear)
    RelativeLayout repeatLinear;
    @BindView(R.id.tt_alert)
    TextView ttAlert;
    @BindView(R.id.alert_linear)
    RelativeLayout alertLinear;
    @BindView(R.id.et_detail)
    WebView etDetail;
    @BindView(R.id.part_textView)
    TextView partTextView;
    @BindView(R.id.part_linear)
    RelativeLayout partLinear;
    @BindView(R.id.linearLayout_two)
    LinearLayout linearLayoutTwo;
    @BindView(R.id.IsShowMore)
    TextView IsShowMore;
    @BindView(R.id.linear_isShow)
    RelativeLayout linearIsShow;
    //    @BindView(R.id.view_IsShowMore)
//    View viewIsShowMore;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat selectFormat = new SimpleDateFormat("HH:mm");
    public boolean myswitch = true;//开关 选择全天的  myswitch false 全天   true 非全天
    private static final int Part_CallBack = 1001;//参与人
    private static final int Repeat_CallBack = 1002;//重复
    private static final int Alert_CallBack = 1003;//提醒
    private static final int Share_CallBack = 1004;//共享人
    private ArrayList<Contacts> partList = new ArrayList<>();
    private ArrayList<Contacts> shareList = new ArrayList<>();

    private String mContentStr = "请输入日程详情";
    private ScheduleRuleEntity entityDetail;

    //0--没开始加载  1--加载中  2--加载完成
    private int mWebviewState = 0;

    private String myStartTime;//拼接完的开始时间
    private String myEndTime;//结束时间

    private String selectData;
    private String selectDataEnd;
    private String selectDay;
    private String selectDayEnd;

    public static Date dateStart = null;
    public static String week = null;

    private Date dateEnd = null;
    private String strStart = "";
    private String strEnd = "";
    private ScheduleDetailEntity entity;
    private List<Contacts> mList;
    private boolean isEdit;
    private String mId = "";
    private String mUserID = "";
    private String mForUserID = "";
    private String mTittle = "";
    private String mIsAllday = "0";
    private String mIsRepeat = "0";
    private String mRepeatType = "";
    private String mFrequency = "1";
    private String mWeekStr = "";
    private String mFinishType = "0";
    private String mFinishTimes = "0";
    private String mFinishTime = "";
    private String mIsAlarm = "1";
    private String mAlarmType = "1";
    private String mScheduleDetail = "";
    private String mLocation = "";
    private String mSelectMemberIDlist = "";
    private List<ScheduleDetailEntity.SelectUserListBean> selectUserList;
    private String scheduleDetail;
    private boolean startChange = false;
    private boolean endChange = false;
    private boolean isCreateWorkmate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newschedule);
        ButterKnife.bind(this);
        entity = (ScheduleDetailEntity) getIntent().getSerializableExtra("entity");
        isEdit = getIntent().getBooleanExtra("isEdit", false);//是否是编辑
        isCreateWorkmate = getIntent().getBooleanExtra("isCreateWorkmate", false);//是否是为同事创建日程
        if (isEdit) {
            setTitle("编辑日程");
            IsShowMore.setVisibility(View.GONE);
            initEdit();
        } else {
            setTitle("新建日程");
            mForUserID = getIntent().getStringExtra("forUserID");
            linearLayoutTwo.setVisibility(View.GONE);
//            viewIsShowMore.setVisibility(View.GONE);
        }
        //获取当前时间方法
        Calendar calendar = Calendar.getInstance();
        if (dateStart.getMonth() == calendar.getTime().getMonth() && dateStart.getDay() == calendar.getTime().getDay()) {
            dateStart = calendar.getTime();
            strStart = format.format(dateStart);
            myStartTime = strStart + ":00";
            calendar.add(Calendar.MINUTE, 30);
            dateEnd = calendar.getTime();
            strEnd = format.format(dateEnd);
            myEndTime = strEnd + ":00";
        } else {
            strStart = format.format(dateStart);
            myStartTime = strStart + ":00";
            dateEnd = dateStart;
            strEnd = format.format(dateEnd);
            myEndTime = strEnd + ":00";
        }
        //获取当前时间方法
        selectDay = selectFormat.format(dateStart);
        selectDayEnd = selectFormat.format(dateEnd);
        initView();

        initData();

        if (isEdit) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //0--没开始加载  1--加载中  2--加载完成
                    while (mWebviewState == 1) {
                        try {
                            Thread.sleep(300);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mWebviewState = 1;
                    handler.sendEmptyMessage(3);
                }
            }).start();
        }
    }
    private void initData() {
        etDetail.setWebViewClient(new WebViewClient());
        WebSettings webSettings = etDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        mWebviewState = 1;
        etDetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                mWebviewState = 2;
                if (url.startsWith("gethtml:")) {
                    try {
                        mContentStr = URLDecoder.decode(url.substring(8).toString(), "UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebviewState = 2;
                super.onPageFinished(view, url);
            }
        });
        etDetail.loadUrl("file:///android_asset/Demo.html");
    }

    //编辑日程模式，设置数据
    private void initEdit() {

        linearIsShow.setVisibility(View.GONE);
        mId = entity.getID() + "";
        mUserID = entity.getUserID() + "";
        mForUserID = entity.getForUserID() + "";
        mTittle = entity.getTitle();
        etTitle.setText(mTittle);

        if (entity.isIsAllday()) {
            mToggleButtonwholeday.setToggleOn();
            mIsAllday = "" + 1;
        } else {
            mToggleButtonwholeday.setToggleOff();
            mIsAllday = "" + 0;
        }
        myStartTime = entity.getStartTime();
        myEndTime = entity.getEndTime();

        try {
            Date startDate = format.parse(entity.getStartTime());
            Date endDate = format.parse(entity.getEndTime());
            ttStarttime.setText(format.format(startDate));
            ttEndTime.setText(format.format(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (entity.isIsRepeat()) {
            mIsRepeat = 1 + "";
        } else {
            mIsRepeat = 0 + "";
        }


        if (entity.isIsRepeat()) {
            switch (entity.getRepeatType()) {
                case 0:
                    ttRepeat.setText("每天");
                    break;
                case 1:
                    ttRepeat.setText("每周");
                    break;
                case 2:
                    ttRepeat.setText("每月");
                    break;
                case 3:
                    ttRepeat.setText("每年");
                    break;
            }
        } else {
            ttRepeat.setText("一次性日程");
        }


        if (!StringUtils.isEmpty(entity.getWeekStr()) || !StringUtils.isEmpty(entity.getFinishTime()) || StringUtils.isEmpty(entity.getFinishTimes() + "")) {
            ttRepeat.setText("自定义");
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

        mRepeatType = entity.getRepeatType() + "";
        mFrequency = entity.getFrequency() + "";
        if (entity.getWeekStr().contains("Monday")) {
            if (entity.getWeekStr().contains("Tuesday")
                    || entity.getWeekStr().contains("Wednesday")
                    || entity.getWeekStr().contains("Thursday")
                    || entity.getWeekStr().contains("Friday")
                    || entity.getWeekStr().contains("Saturday")
                    || entity.getWeekStr().contains("Sunday")
                    ) {
                mWeekStr = "1,";
            } else {
                mWeekStr = "1";
            }

        }
        if (entity.getWeekStr().contains("Tuesday")) {
            if (entity.getWeekStr().contains("Wednesday")
                    || entity.getWeekStr().contains("Thursday")
                    || entity.getWeekStr().contains("Friday")
                    || entity.getWeekStr().contains("Saturday")
                    || entity.getWeekStr().contains("Sunday")
                    ) {
                mWeekStr = mWeekStr + "2";
            } else {
                mWeekStr = mWeekStr + "2,";
            }
        }
        if (entity.getWeekStr().contains("Wednesday")) {
            if (entity.getWeekStr().contains("Thursday")
                    || entity.getWeekStr().contains("Friday")
                    || entity.getWeekStr().contains("Saturday")
                    || entity.getWeekStr().contains("Sunday")
                    ) {
                mWeekStr = mWeekStr + "3";
            } else {
                mWeekStr = mWeekStr + "3,";
            }
        }
        if (entity.getWeekStr().contains("Thursday")) {
            if (entity.getWeekStr().contains("Friday")
                    || entity.getWeekStr().contains("Saturday")
                    || entity.getWeekStr().contains("Sunday")
                    ) {
                mWeekStr = mWeekStr + "4";
            } else {
                mWeekStr = mWeekStr + "4,";
            }
        }
        if (entity.getWeekStr().contains("Friday")) {
            if (entity.getWeekStr().contains("Saturday")
                    || entity.getWeekStr().contains("Sunday")
                    ) {
                mWeekStr = mWeekStr + "5";
            } else {
                mWeekStr = mWeekStr + "5,";
            }
        }
        if (entity.getWeekStr().contains("Saturday")) {
            if (entity.getWeekStr().contains("Sunday")
                    ) {
                mWeekStr = mWeekStr + "6";
            } else {
                mWeekStr = mWeekStr + "6,";
            }
        }
        if (entity.getWeekStr().contains("Sunday")) {
            mWeekStr = mWeekStr + "7";
        }

        mFinishType = entity.getFinishType() + "";
        mFinishTimes = entity.getFinishTimes() + "";
        mFinishTime = entity.getFinishTime();
        mIsAlarm = entity.isIsAlarm() + "";

        if (entity.isIsAlarm()) {
            mIsAlarm = 1 + "";
        } else {
            mIsAlarm = 0 + "";
        }


        mAlarmType = entity.getAlarmType() + "";
        mContentStr = entity.getScheduleDetail();
        mLocation = entity.getLocation() + "";
        selectUserList = entity.getSelectUserList();
        if (selectUserList.size() != 0) {
            for (int i = 0; i < selectUserList.size(); i++) {
                if (i == selectUserList.size() - 1) {
                    mSelectMemberIDlist = mSelectMemberIDlist + selectUserList.get(i).getUserID();
                } else {
                    mSelectMemberIDlist = mSelectMemberIDlist + selectUserList.get(i).getUserID() + ",";
                }
            }
        }

        StringBuffer sb = new StringBuffer();
        if (selectUserList.size() > 1) {
            sb.append(selectUserList.get(0).getUserName()).append(" , ").append(selectUserList.get(1).getUserName());

            if (!StringUtils.isEmpty(sb.toString())) {
                sb.substring(0, sb.length() - 2);
                partTextView.setText(sb.toString() + " 等 " + selectUserList.size() + " 人 ");
            }
        } else {
            partTextView.setText(selectUserList.get(0).getUserName());
        }
        scheduleDetail = entity.getScheduleDetail();
    }

    private void initView() {
        /**
         * 开关的监听
         *
         * @param savedInstanceState
         */
        mToggleButtonwholeday.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {//全天
                    myswitch = false;
                } else {//非全天加时间
                    myswitch = true;
                }
            }
        });


        mToggleButtonwholeday.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {//全天
                    myswitch = false;
                    selectData = format1.format(dateStart);
                    ttStarttime.setText(selectData);
                    ttEndTime.setText(selectData);
                    myStartTime = selectData + " 00:00:00";//拼接时间格式
                    myEndTime = selectDataEnd + " 23:59:00";//
                } else {//非全天加时间
                    myswitch = true;
                    selectData = format1.format(dateStart);
                    ttStarttime.setText(selectData + " " + selectDay);
                    ttEndTime.setText(selectData + " " + selectDayEnd);
                    myStartTime = selectData + " " + selectDay + ":00";
                    myEndTime = selectData + " " + selectDayEnd + ":00";
                }
            }
        });
        if (!StringUtils.isEmpty(ttStarttime) && !StringUtils.isEmpty(ttEndTime)) {

        } else {
            ttStarttime.setText(strStart);
            ttEndTime.setText(strEnd);
        }

    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1Show(true);
        setRight1Icon(R.mipmap.complete2);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);

        if (!validate()) {
            return;
        }

        if (isEdit) {
            //修改日程
            mId = entity.getID() + "";
            mUserID = entity.getUserID() + "";
            mForUserID = entity.getForUserID() + "";
            mTittle = getEditText(etTitle);

            if (myswitch) {
//                    mIsAllday = 0 + "";
            } else {
//                    mIsAllday = 1 + "";
                mId = entity.getID() + "";
                mUserID = entity.getUserID() + "";
                mForUserID = entity.getForUserID() + "";
                mTittle = getEditText(etTitle);
                if (myswitch) {
                    mIsAllday = 0 + "";
                } else {
                    mIsAllday = 1 + "";
                }
            }
        } else {
            //创建程
            mUserID = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
            if (!isCreateWorkmate) {
                mForUserID = PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID);
            }
            mTittle = etTitle.getText().toString();
            if (myswitch) {
                mIsAllday = 0 + "";
            } else {
                mIsAllday = 1 + "";
            }
        }

        mWebviewState = 1;
        //获取内容
        etDetail.loadUrl("javascript:getHTML()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //0--没开始加载  1--加载中  2--加载完成
                while (mWebviewState == 1) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mWebviewState = 1;
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Alert_CallBack) {
            ttAlert.setText(data.getStringExtra("alert"));
            mIsAlarm = data.getStringExtra("IsAlarm");
            mAlarmType = data.getStringExtra("AlarmType");
        }
        if (requestCode == Part_CallBack) {
            mSelectMemberIDlist = "";

            partList = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsChoseActivity.Flag_Output_Checked);

            StringBuilder partName = new StringBuilder();
            if (partList.size() != 0) {
                for (int i = 0; i < partList.size(); i++) {

                    if (i == partList.size() - 1) {
                        mSelectMemberIDlist = mSelectMemberIDlist + partList.get(i).getId();
                        partName.append(partList.get(i).getUserName());
                    } else {
                        mSelectMemberIDlist = mSelectMemberIDlist + partList.get(i).getId() + ",";
                        partName.append(partList.get(i).getUserName()).append(",  ");
                    }
                }
            }
            partTextView.setText(partName);
        }

        if (requestCode == Repeat_CallBack) {
            ttRepeat.setText(data.getStringExtra("repeat"));
            mIsRepeat = data.getStringExtra("IsRepeat");
            mRepeatType = data.getStringExtra("RepeatType");
            mFrequency = data.getStringExtra("Frequency");
            mWeekStr = data.getStringExtra("WeekStr");
            mFinishType = data.getStringExtra("FinishType");
            mFinishTimes = data.getStringExtra("FinishTimes");
            mFinishTime = data.getStringExtra("FinishTime");
        }
    }

    @OnClick({R.id.startTime_linear,
            R.id.endTime_linear, R.id.repeat_linear,
            R.id.alert_linear, R.id.part_linear,
            R.id.IsShowMore, R.id.linear_isShow,})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.startTime_linear:
                selectStart();
                break;
            case R.id.endTime_linear:
                selectEnd();
                break;
            case R.id.repeat_linear:
                intent = new Intent(NewScheduActivity.this, RepeatActivity.class);
                startActivityForResult(intent, Repeat_CallBack);
                break;
            case R.id.alert_linear:
                intent = new Intent(NewScheduActivity.this, TimeAlerterActivity.class);
                startActivityForResult(intent, Alert_CallBack);
                break;
            case R.id.part_linear:
                intent = new Intent(NewScheduActivity.this, ContactsChoseActivity.class);
                intent.putExtra("type", 1);//0-查看 1-多选 2-单选
                if (isEdit) {
                    ArrayList<Contacts> list = new ArrayList();
                    for (int i = 0; i < selectUserList.size(); i++) {
                        Contacts entity = new Contacts();
                        entity.setId(selectUserList.get(i).getUserID());
                        entity.setUserName(selectUserList.get(i).getUserName());
                        list.add(entity);
                    }
                    intent.putExtra("list", list);
                } else {
                    intent.putExtra("list", partList);
                }

                startActivityForResult(intent, Part_CallBack);
                break;
            case R.id.IsShowMore:
                isShowMore();
                break;
            case R.id.linear_isShow:
                isShowMore();
                break;
        }
    }

    private boolean validate() {
        if (StringUtils.isEmpty(etTitle)) {
            MyToast.showToast("请输入日程标题");
            return false;
        }
        if (StringUtils.isEmpty(ttStarttime)) {
            MyToast.showToast("请输入开始时间");
            return false;
        }
        if (StringUtils.isEmpty(ttEndTime)) {
            MyToast.showToast("请输入结束时间");
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date d1 = null;
        Date d2 = null;
        boolean flag;
        if (myswitch) {// myswitch false 全天   true 非全天
            myStartTime = getEditText(ttStarttime) + ":00";
            myEndTime = getEditText(ttEndTime) + ":00";
        } else {//全天加时间
            myStartTime = getEditText(ttStarttime) + " 00:00:00";//拼接时间格式
            myEndTime = getEditText(ttEndTime) + " 23:59:00";//
        }
        try {
            d1 = sdf.parse(myStartTime);
            d2 = sdf.parse(myEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        flag = d2.before(d1);
        if (flag) {
            MyToast.showToast("结束时间不能早于开始时间");
            return false;
        }
        if (StringUtils.isEmpty(ttRepeat)) {
            MyToast.showToast("请选择重复类型");
            return false;
        }
        if (StringUtils.isEmpty(ttAlert)) {
            MyToast.showToast("请选择提醒类型");
            return false;
        }
        if (isFastClick()) {
            return false;
        }

        return true;
    }


    //判断是否重复点击
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    private void selectStart() {
        if (myswitch == true) {
            /**
             * 日期+时间 选择器
             */
            String time = getEditText(ttStarttime);
            new SlideDateTimePicker.Builder(getSupportFragmentManager())
                    .setListener(new SlideDateTimeListener() {
                        @Override
                        public void onDateTimeSet(Date date) {
                            dateStart = date;
                            try {
                                ttStarttime.setText(format.format(date));
                                myStartTime = format.format(date);
                                //开始时间大于结束时间,将结束时间一起赋值
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                                Date endDate = sdf.parse(getEditText(ttEndTime));
                                Date startDate = sdf.parse(getEditText(ttStarttime));
                                if (endDate.before(startDate)) {
                                    //将结束时间赋值为开始时间
                                    ttEndTime.setText(format.format(date));
                                    myEndTime = format.format(date);
                                }
                                //获取当前时间方法
                                selectDay = selectFormat.format(startDate);
                                selectDayEnd = selectFormat.format(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setInitialDate(StringUtils.convertToDate(format, time))
                    .setIs24HourTime(true)
                    .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date_Time)
                    .build()
                    .show();
        } else {
            /**
             * 日期 选择器
             */
            String time = getEditText(ttStarttime);
            new SlideDateTimePicker.Builder(getSupportFragmentManager())
                    .setListener(new SlideDateTimeListener() {
                        @Override
                        public void onDateTimeSet(Date date) {
                            ttStarttime.setText(format1.format(date));
                            myStartTime = format1.format(date);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

                            try {
                                Date endDate = sdf.parse(getEditText(ttEndTime));
                                Date startDate = sdf.parse(getEditText(ttStarttime));
                                if (endDate.before(startDate)) {
                                    //将结束时间赋值为开始时间
                                    ttEndTime.setText(sdf.format(date));
                                    myEndTime = sdf.format(date);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setInitialDate(StringUtils.convertToDate(format1, time))
                    .setIs24HourTime(true)
                    .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                    .build()
                    .show();
                    /*DialogSelectDate dialogSelectDate = new DialogSelectDate(NewTaskActivity.this,
                            new DialogAlertListener() {
                                @Override
                                public void onDialogCreate(Dialog dlg, Object param) {

                                }

                                @Override
                                public void onDialogOk(Dialog dlg, Object param) {
                                    mStartTime.setText(param.toString());
                                    dlg.dismiss();
                                }

                                @Override
                                public void onDialogCancel(Dialog dlg, Object param) {

                                }

                                @Override
                                public void onDialogControl(Dialog dlg, Object param) {

                                }
                            }, time);
                    dialogSelectDate.show();*/

        }
    }


    private void selectEnd() {
        if (myswitch == true) {
            /**
             * 时间选择器
             */
            String time = getEditText(ttEndTime);
            new SlideDateTimePicker.Builder(getSupportFragmentManager())
                    .setListener(new SlideDateTimeListener() {
                        @Override
                        public void onDateTimeSet(Date date) {
                            try {

                                ttEndTime.setText(format.format(date));
                                myEndTime = format.format(date);
                                //开始时间大于结束时间,将结束时间一起赋值
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                                Date endDate = sdf.parse(getEditText(ttEndTime));
                                Date startDate = sdf.parse(getEditText(ttStarttime));
                                if (endDate.before(startDate)) {
                                    ttStarttime.setText(format.format(date));
                                    myStartTime = format.format(date);
                                }
                                //获取当前时间方法
                                selectDay = selectFormat.format(startDate);
                                selectDayEnd = selectFormat.format(endDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setInitialDate(StringUtils.convertToDate(format, time))
                    .setIs24HourTime(true)
                    .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date_Time)
                    .build()
                    .show();
        } else {
            /**
             * 日期选择器
             */
            String time = getEditText(ttEndTime);
            new SlideDateTimePicker.Builder(getSupportFragmentManager())
                    .setListener(new SlideDateTimeListener() {
                        @Override
                        public void onDateTimeSet(Date date) {
                            ttEndTime.setText(format1.format(date));
                            myEndTime = format1.format(date);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

                            try {
                                Date endDate = sdf.parse(getEditText(ttEndTime));
                                Date startDate = sdf.parse(getEditText(ttStarttime));
                                if (endDate.before(startDate)) {
                                    ttStarttime.setText(sdf.format(date));
                                    myStartTime = sdf.format(date);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setInitialDate(StringUtils.convertToDate(format1, time))
                    .setIs24HourTime(true)
                    .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                    .build()
                    .show();
        }
    }


    private void isShowMore() {
        linearLayoutTwo.setVisibility(View.VISIBLE);
        linearIsShow.setVisibility(View.GONE);
    }

    /**
     * 新建日程
     * 修改日程
     */
    public void postUpdateSchedule() {
        if (myswitch) {
            mIsAllday = 0 + "";
        } else {
            mIsAllday = 1 + "";
        }

        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postUpdateSchedule(ssid, currentCompanyId
                , mId
                , mUserID
                , mForUserID
                , mTittle
                , mIsAllday
                , myStartTime
                , myEndTime
                , mIsRepeat
                , mRepeatType
                , mFrequency
                , mWeekStr
                , mFinishType
                , mFinishTimes
                , mFinishTime
                , mIsAlarm
                , mAlarmType
                , mContentStr
                , mLocation
                , mSelectMemberIDlist);
        callRequest(call, new HttpCallBack(ScheduleRuleEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    if (isEdit) {
                        MyToast.showToast("日程编辑成功");

                    } else {
                        MyToast.showToast("日程创建成功");
                    }
                    setResult(RESULT_OK);


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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://
                    postUpdateSchedule();
                    break;
                case 3://
                    //设置内容
                    etDetail.loadUrl("javascript:setHTML('" + scheduleDetail + "')");
                    break;
            }
        }
    };

    @OnClick(R.id.tt_isShowMore)
    public void onClick() {
        isShowMore();
    }
}
