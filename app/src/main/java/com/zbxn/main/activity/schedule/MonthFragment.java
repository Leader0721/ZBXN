package com.zbxn.main.activity.schedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseFragment;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.ScheduleAdapter;
import com.zbxn.main.entity.ScheduleListEntity;
import com.zbxn.main.entity.ScheduleRuleEntity;
import com.zbxn.main.widget.calendar.ScrollLayout;
import com.zbxn.main.widget.calendar.bizs.calendars.Lauar;
import com.zbxn.main.widget.calendar.bizs.calendars.ZSSChineseCalendar;
import com.zbxn.main.widget.calendar.views.MonthView;
import com.zbxn.main.widget.calendar.views.WeekView;

import org.simple.eventbus.Subscriber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/2/9.
 */
public class MonthFragment extends BaseFragment implements
        MonthView.OnMonthChangeEventListener, MonthView.OnSelectDayEventListener,
        View.OnClickListener, ScrollLayout.OnWeekMonthStyleChangeEventListener, AdapterView.OnItemClickListener {

    private static final int Flag_Callback_Add = 1001;
    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.fragmentschedule_titleDateLinearLayout)
    LinearLayout fragmentscheduleTitleDateLinearLayout;
    @BindView(R.id.month_calendar)
    MonthView monthCalendar;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.main_layout)
    LinearLayout mainLayout;
    @BindView(R.id.week_calendar)
    WeekView weekCalendar;
    @BindView(R.id.main_scrolllayout)
    ScrollLayout mainScrolllayout;
    @BindView(R.id.cotent_listview)
    MyListView contentList;
    @BindView(R.id.image_line)
    View imageLine;
    @BindView(R.id.mCalendar)
    TextView mCalendar;//农历
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.ll_nodata)
    LinearLayout ll_nodata;

    private Calendar now;
    private List<ScheduleListEntity> listDay;
    private List<ScheduleListEntity> listDayIsAllDay;//全天的
    private List<ScheduleListEntity> listDayNotIsAllDay;//非全天的
    private List<ScheduleListEntity> listCrossDay;//跨天的

    private ScheduleAdapter mAdapter;


    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private BaseActivity activity;

    private List<ScheduleListEntity> listMonth;

    private String date = "";
    private String date1 = "";
    private String selectData;
    private String UserID;
    private Bundle bundle;
    private boolean isWorkmate = false;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_month, null);
        ButterKnife.bind(this, view);
        listMonth = new ArrayList<>();
        listDayIsAllDay = new ArrayList<>();
        listDayNotIsAllDay = new ArrayList<>();
        listCrossDay = new ArrayList<>();
        bundle = getArguments();
        activity = (BaseActivity) getActivity();
        if (bundle != null) {
            UserID = bundle.getString("id");
            isWorkmate = true;
        }

        initView();
        initData();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");//设置日期格式
        date = sf.format(new Date());

        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        selectData = sf1.format(new Date());

        ll_nodata.setVisibility(View.GONE);
        contentList.setEmptyView(ll_nodata);
        return view;
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (eventCustom.getTag().equals(ScheduleActivity.SUCCESS1)) {
            monthCalendar.gotoToday();
            monthCalendar.setTodayDisplay(true);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            date1 = sf.format(new Date());
            onSelectDayEvent(date1);
        }
        if (eventCustom.getTag().equals(ScheduleActivity.SUCCESS2)) {
            getScheduleList();
            mAdapter.notifyDataSetChanged();
        }
    }


    private void initView() {
        listDay = new ArrayList<>();
        if (listDay != null) {
            mAdapter = new ScheduleAdapter(getActivity(), listDay, isWorkmate);
            contentList.setAdapter(mAdapter);
        }
        contentList.setOnItemClickListener(this);
        monthCalendar.setTodayDisplay(true);
        monthCalendar.setOnMonthChangeEventListener(this);
        monthCalendar.setOnDatePickedListener(this);

        weekCalendar.setTodayDisplay(true);
        weekCalendar.setOnSelectDayEventListener(this);
        mainScrolllayout.setOnWeekMonthStyleChangeEventListener(this);
        fragmentscheduleTitleDateLinearLayout.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initData() {
        now = Calendar.getInstance();
        if (isWorkmate) {
            String name = bundle.getString("name", "日程");
            activity.setTitle(name);
        } else {
//            activity.setTitle(PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_USERNAME));
            activity.setTitle("日程");
        }

        mTitle.setText("日程   " + now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月");
        monthCalendar.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        weekCalendar.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);

    }

    @OnClick({R.id.fragmentschedule_titleDateLinearLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragmentschedule_titleDateLinearLayout://标题
                break;
        }
    }

    @Override
    public void onMonthChangeEvent(int year, int month) {
        Calendar c = Calendar.getInstance();

        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH) + 1;
        /*if ((nowYear == year) && (nowMonth == month)) {
            schedule_topbar_comenow_button.setVisibility(View.INVISIBLE);
        } else {
            schedule_topbar_comenow_button.setVisibility(View.VISIBLE);
        }*/
        mTitle.setText(year + "年" + month + "月");
        if (isWorkmate) {
            String name = bundle.getString("name", "日程");
            activity.setTitle(name);
        } else {
//            activity.setTitle(PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_USERNAME));
            activity.setTitle("日程");
        }
        listMonth.clear();
        listDay.clear();
        mAdapter.notifyDataSetChanged();
        date = year + "-" + (month < 10 ? "0" + month : month);

        //需要置换的请求接口
//        findSchedule(getActivity(), date);
        getScheduleList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != activity.RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_Add) {
            activity.finish();
        }
    }

    @Override
    public void onSelectDayEvent(String date) {
        listDayIsAllDay.clear();
        listDayNotIsAllDay.clear();
        listCrossDay.clear();
        date = StringUtils.convertDate(date.replace(".", "-"));
        try {
            if (TextUtils.isEmpty(date)) {
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);

            //通过当前天的选择，进而在不同的天创建日程
            NewScheduActivity.dateStart = calendar.getTime();
            NewScheduActivity.week = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

            mCalendar.setText("农历  " + new Lauar(calendar).toString());
            listDay.clear();
            for (int i = 0; i < listMonth.size(); i++) {
                if (listMonth.get(i).getStartTime().startsWith(date)) {
                    listDay.add(listMonth.get(i));
                }
            }
            Collections.sort(listDay, new Comparator<ScheduleListEntity>() {
                @Override
                public int compare(ScheduleListEntity a, ScheduleListEntity b) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date dateA = sdf.parse(a.getStartTime());
                        Date dateB = sdf.parse(b.getStartTime());
                        if (dateA.before(dateB)) {
                            return -1;
                        } else if (!dateA.before(dateB)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });


            //对是否全天进行一个判断
            if (listDay.size() != 0) {
                for (int i = 0; i < listDay.size(); i++) {
                    if (listDay.get(i).getIsCrossday() == 1){
                        listCrossDay.add(listDay.get(i));
                    }else {
                        if (listDay.get(i).isIsAllday()) {
                            listDayIsAllDay.add(listDay.get(i));
                        } else {
                            listDayNotIsAllDay.add(listDay.get(i));
                        }
                    }
                }
            }
            listDay.clear();
            listDay.addAll(listDayIsAllDay);
            listDay.addAll(listCrossDay);
            listDay.addAll(listDayNotIsAllDay);

            mAdapter.notifyDataSetChanged();
            selectData = date;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWeekMonthStyleChangeEvent(int style) {
        /*if (style == 1) {
//            Toast.makeText(this, "切换成周啦", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(this, "切换成月啦", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ScheduleInfoActivity.class);
//        intent.putExtra("sign", 1);
        if (listDay != null) {
            intent.putExtra("isWorkmate", isWorkmate);
            intent.putExtra("id", listDay.get(position).getScheduleID() + "");
            intent.putExtra("mScheduleRuleType", listDay.get(position).getPermission());
//        intent.putExtra("scheduleId",listDay.get(position).)
        }
        startActivityForResult(intent, Flag_Callback_Add);
    }

    //获取日程列表
    private void getScheduleList() {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        String ID = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_ID);
        //查看UserID是否有值，如果有，是查看同事日程，否则是自己
        if (StringUtils.isEmpty(UserID)) {
            UserID = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_INPUT_ID);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Call call = HttpRequest.getIResourceOANetAction().getScheduleList(ssid, currentCompanyId, UserID, ID, date);
        callRequest(call, new HttpCallBack(ScheduleListEntity.class, getContext(), true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    List<ScheduleListEntity> list = mResult.getRows();
                    listMonth.clear();
                    listMonth.addAll(list);
                    List<String> listTemp = new ArrayList<>();
                    int year = 0;
                    int month = 0;
                    for (int i = 0; i < list.size(); i++) {
                        try {
                            Date date = sf.parse(list.get(i).getStartTime());
                            int day = date.getDate();
                            year = date.getYear();
                            month = date.getMonth();
                            listTemp.add(day + "");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    ZSSChineseCalendar.SCHEDULE.clear();
                    ZSSChineseCalendar.SCHEDULE.put(month + "", listTemp);

                    monthCalendar.setInvalidate();
                    weekCalendar.setInvalidate();
                    onSelectDayEvent(selectData);

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


    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }


    public void findSchedule(Context context, String starttime) {
        String ssid = PreferencesUtils.getString(getActivity(), LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getMonthSchedule(ssid, starttime);
        callRequest(call, new HttpCallBack(ScheduleRuleEntity.class, getActivity(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ScheduleListEntity> list = mResult.getRows();
                    listMonth.clear();
                    listMonth.addAll(list);
                    List<String> listTemp = new ArrayList<>();
                    int year = 0;
                    int month = 0;
                    MyToast.showToast(list.get(2).getStartTime());
                    for (int i = 0; i < list.size(); i++) {
                        try {
                            Date date = sf.parse(list.get(i).getStartTime());
                            int day = date.getDate();
                            year = date.getYear();
                            month = date.getMonth();
                            listTemp.add(day + "");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    ZSSChineseCalendar.SCHEDULE.clear();
                    ZSSChineseCalendar.SCHEDULE.put(month + "", listTemp);

                    monthCalendar.setInvalidate();
                    weekCalendar.setInvalidate();

                    onSelectDayEvent(selectData);
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
