package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.pub.base.BaseActivity;
import com.pub.utils.MyToast;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.adapter.IndustryAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/14.
 */
public class EndTypeActivity extends BaseActivity {
    @BindView(R.id.listView_industry)
    ListView listViewIndustry;
    @BindView(R.id.year_picker)
    TimePickerView yearPicker;
    @BindView(R.id.month_picker)
    TimePickerView monthPicker;
    @BindView(R.id.day_picker)
    TimePickerView dayPicker;
    @BindView(R.id.rela_timePicker)
    RelativeLayout relaTimePicker;
    @BindView(R.id.count_picker)
    TimePickerView countPicker;
    @BindView(R.id.rela_cishuPicker)
    RelativeLayout relaCishuPicker;
    private int year;
    private int month;
    private int day;
    private List<StaticTypeEntity> mList = new ArrayList<>();
    private IndustryAdapter mAdapter;
    private List<String> yearList = new ArrayList<>();
    private List<String> monthList = new ArrayList<>();
    private List<String> dayList = new ArrayList<>();
    private List<String> countList = new ArrayList<>();
    private String yearString = "";
    private String monthString = "7";
    private String dayString = "15";
    private String EndString = "";
    private String FinishType = "0";//结束方式
    private String FinishTimes = "2";//结束次数
    private String FinishTime = "";//结束时间
    private Intent industry;
    private boolean yearChange = false;
    private boolean monthChange = false;
    private boolean dayChange = false;
    private int YearInt;
    private Date date = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endtype);
        setTitle("结束");
        ButterKnife.bind(this);
        Calendar ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH);
        day = ca.get(Calendar.DAY_OF_MONTH);
        yearString = year + "";
        if (month == 12) {
            month = 0;
        }
        monthString = (month + 1) + "";
        dayString = day + "";
        StaticTypeEntity entity1 = new StaticTypeEntity();
        entity1.setValue("永不");
        StaticTypeEntity entity2 = new StaticTypeEntity();
        entity2.setValue("次数");
        StaticTypeEntity entity3 = new StaticTypeEntity();
        entity3.setValue("日期");
        mList.add(entity1);
        mList.add(entity2);
        mList.add(entity3);
        initData();
        initView();
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1("确定");
        setRight1Show(true);
        setRight2Show(false);
        setRight1Icon(0);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        Calendar ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH) + 1;
        day = ca.get(Calendar.DAY_OF_MONTH);
        if (!yearChange) {
            yearString = year + "";
        }
        if (!monthChange) {
            monthString = 7 + "";
        }
        if (!dayChange) {
            dayString = 16 + "";
        }
        switch (FinishType) {
            case 0 + "":
                EndString = "永不";
                FinishTime = "";
                FinishTimes = "";
                break;
            case 1 + "":
                FinishTime = "";
                EndString = FinishTimes + "次";
                break;
            case 2 + "":
                FinishTimes = "";
                EndString = yearString + "年" + monthString + "月" + dayString + "日";
                FinishTime = yearString + "-" + monthString + "-" + dayString + " 23:59:00";

//                try {
//                    date = sdf.parse(FinishTime);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                break;
        }


        if (FinishType.equals("2")) {
            if (isEarly()) {
                industry = new Intent();
                industry.putExtra("endType", EndString);
                industry.putExtra("FinishType", "" + FinishType);
                industry.putExtra("FinishTimes", "" + FinishTimes);
                industry.putExtra("FinishTime", "" + FinishTime);
                setResult(RESULT_OK, industry);
                finish();
            } else {
                MyToast.showToast("结束时间不能早于现在");
            }
        } else {
            industry = new Intent();
            industry.putExtra("endType", EndString);
            industry.putExtra("FinishType", "" + FinishType);
            industry.putExtra("FinishTimes", "" + FinishTimes);
            industry.putExtra("FinishTime", "" + FinishTime);
            setResult(RESULT_OK, industry);
            finish();
        }
    }

    private boolean isEarly() {
        Calendar ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH) + 1;
        day = ca.get(Calendar.DAY_OF_MONTH);
        boolean isEarly = false;

        if (year < Integer.parseInt(yearString)) {
            isEarly = true;
        } else if (year == Integer.parseInt(yearString) && month < Integer.parseInt(monthString)) {
            isEarly = true;
        } else if (year == Integer.parseInt(yearString) && month == Integer.parseInt(monthString) && day < Integer.parseInt(dayString)) {
            isEarly = true;
        } else {
            isEarly = false;
        }
        return isEarly;
    }


    private void initData() {
        Calendar ca = Calendar.getInstance();
        year = ca.get(Calendar.YEAR);
        YearInt = year;

        month = ca.get(Calendar.MONTH);
        if (month == 12) {
            month = 0;
        }
        month = month + 1;
        for (int i = 0; i < 30; i++) {
            yearList.add(i, year - (30 - i) + "");
        }
        yearList.add(30, year + "");
        for (int i = 0; i < 30; i++) {
            yearList.add(i + 31, year + i + 1 + "");
        }

        for (int i = 0; i < 12; i++) {
            monthList.add(i, i + 1 + "");
        }
        month = 7;
        getDayCount(month, year);


        for (int i = 52; i < 100; i++) {
            countList.add(i - 52, i + "");
        }
        for (int i = 1; i < 52; i++) {
            countList.add(i + 47, i + "");
        }

        yearPicker.setData(yearList);
        monthPicker.setData(monthList);
        dayPicker.setData(dayList);
        countPicker.setData(countList);
    }

    private void getDayCount(int month, int year) {
        dayList.clear();
        boolean isRunYear = false;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            isRunYear = true;
        } else {
            isRunYear = false;
        }
        switch (month) {
            case 1:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 2:
                if (isRunYear) {
                    for (int i = 0; i < 29; i++) {
                        dayList.add(i, i + 1 + "");
                    }
                } else {
                    for (int i = 0; i < 28; i++) {
                        dayList.add(i, i + 1 + "");
                    }
                }
                break;
            case 3:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 4:
                for (int i = 0; i < 30; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 5:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 6:
                for (int i = 0; i < 30; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 7:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 8:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 9:
                for (int i = 0; i < 30; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 10:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 11:
                for (int i = 0; i < 30; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
            case 12:
                for (int i = 0; i < 31; i++) {
                    dayList.add(i, i + 1 + "");
                }
                break;
        }
    }

    private void initView() {
        mAdapter = new IndustryAdapter(mList, this);
        listViewIndustry.setAdapter(mAdapter);
        listViewIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        FinishType = 0 + "";
                        EndString = "永不";
                        FinishTime = "";
                        FinishTimes = "";
                        Intent industry = new Intent();
                        industry.putExtra("endType", EndString);
                        industry.putExtra("FinishType", "" + FinishType);
                        industry.putExtra("FinishTimes", "" + FinishTimes);
                        industry.putExtra("FinishTime", "" + FinishTime);
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 2:
                        FinishType = 2 + "";
                        relaCishuPicker.setVisibility(View.GONE);
                        relaTimePicker.setVisibility(View.VISIBLE);
                        yearPicker.setOnSelectListener(new TimePickerView.onSelectListener() {
                            @Override
                            public void onSelect(String text) {
                                yearChange = true;
                                for (int i = YearInt - 30; i < YearInt + 31; i++) {
                                    if (text.contains(i + "")) {
                                        year = i;
                                    }
                                }
                                getDayCount(month, year);

                                yearString = text;
                            }
                        });

                        monthPicker.setOnSelectListener(new TimePickerView.onSelectListener() {
                            @Override
                            public void onSelect(String text) {
                                monthChange = true;
                                if (text.contains("1") && !text.contains("11") && !text.contains("12") && !text.contains("10")) {
                                    month = 1;
                                } else if (text.contains("2") && !text.contains("12")) {
                                    month = 2;
                                } else if (text.contains("3")) {
                                    month = 3;
                                } else if (text.contains("4")) {
                                    month = 4;
                                } else if (text.contains("5")) {
                                    month = 5;
                                } else if (text.contains("6")) {
                                    month = 6;
                                } else if (text.contains("7")) {
                                    month = 7;
                                } else if (text.contains("8")) {
                                    month = 8;
                                } else if (text.contains("9")) {
                                    month = 9;
                                } else if (text.contains("10")) {
                                    month = 10;
                                } else if (text.contains("11")) {
                                    month = 11;
                                } else {
                                    month = 12;
                                }
                                getDayCount(month, year);

                                monthString = (Integer.parseInt(text)) + "";
                            }
                        });

                        dayPicker.setOnSelectListener(new TimePickerView.onSelectListener() {
                            @Override
                            public void onSelect(String text) {
                                dayString = text;
                                dayChange = true;
                            }
                        });
                        break;
                    case 1:
                        FinishType = 1 + "";
                        relaCishuPicker.setVisibility(View.VISIBLE);
                        relaTimePicker.setVisibility(View.GONE);
                        countPicker.setOnSelectListener(new TimePickerView.onSelectListener() {
                            @Override
                            public void onSelect(String text) {
                                FinishTimes = text;
                            }
                        });
                        break;
                }
            }
        });
    }
}
