package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.adapter.IndustryAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/13.
 */
public class RepeatActivity extends BaseActivity {
    String[] strings = new String[]{"一次性日程", "每日", "每周", "每月", "每年", "自定义"};
    @BindView(R.id.listView_industry)
    ListView listViewIndustry;
    private static final int Repeat_CallBack = 1001;//自定义重复
    private List<StaticTypeEntity> mList = new ArrayList<>();
    private IndustryAdapter mAdapter;
    private String RepeatType = ""; //重复类型
    private String Frequence = "";   //重复频率
    private String WeekStr = "";   //重复周字符串
    private String FinishType = "";//结束方式
    private String FinishTimes = "";//结束次数
    private String FinishTime = "";//结束时间
    private Intent industry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry);
        setTitle("重复");
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Repeat_CallBack) {
            RepeatType = data.getStringExtra("RepeatType");
            Frequence = data.getStringExtra("Frequency");
            WeekStr = data.getStringExtra("WeekStr");
            FinishType = data.getStringExtra("FinishType");
            FinishTimes = data.getStringExtra("FinishTimes");
            FinishTime = data.getStringExtra("FinishTime");

            industry = new Intent();
            industry.putExtra("repeat", "自定义");
            industry.putExtra("IsRepeat", 1 + "");
            industry.putExtra("RepeatType", RepeatType);
            industry.putExtra("Frequency", "" + Frequence);
            industry.putExtra("WeekStr", "" + WeekStr);
            industry.putExtra("FinishType", "" + FinishType);
            industry.putExtra("FinishTimes", "" + FinishTimes);
            industry.putExtra("FinishTime", "" + FinishTime);
            setResult(RESULT_OK, industry);
            finish();
        }

    }

    private void getWeek(){
        String week = null;

        if (StringUtils.isEmpty(NewScheduActivity.week)){
            Calendar c = Calendar.getInstance();
            week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        }else {
            week = NewScheduActivity.week;
        }
        if ("1".equals(week)) {
            WeekStr = "7";
        }
        if ("2".equals(week)) {
            WeekStr = "1";
        }
        if ("3".equals(week)) {
            WeekStr = "2";
        }
        if ("4".equals(week)) {
            WeekStr = "3";
        }
        if ("5".equals(week)) {
            WeekStr = "4";
        }
        if ("6".equals(week)) {
            WeekStr = "5";
        }
        if ("7".equals(week)) {
            WeekStr = "6";
        }
    }




    private void initView() {
        for (int i = 0; i < strings.length; i++) {
            StaticTypeEntity entity = new StaticTypeEntity();
            entity.setValue(strings[i]);
            mList.add(i, entity);
        }
        mAdapter = new IndustryAdapter(mList, this);
        listViewIndustry.setAdapter(mAdapter);
        listViewIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        RepeatType = "";
                        industry = new Intent();
                        industry.putExtra("repeat", "一次性日程");
                        industry.putExtra("IsRepeat", 0 + "");
                        industry.putExtra("RepeatType", RepeatType);
                        industry.putExtra("Frequency", ""+1);
                        industry.putExtra("WeekStr", "" + WeekStr);
                        industry.putExtra("FinishType", "" + FinishType);
                        industry.putExtra("FinishTimes", "" + FinishTimes);
                        industry.putExtra("FinishTime", "" + FinishTime);
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 1:
                        RepeatType = "" + 0;
                        FinishType = "" + 0;
                        industry = new Intent();
                        industry.putExtra("repeat", "每日");
                        industry.putExtra("IsRepeat", 1 + "");
                        industry.putExtra("RepeatType", RepeatType);
                        industry.putExtra("Frequency", "" + 1);
                        industry.putExtra("WeekStr", "" + WeekStr);
                        industry.putExtra("FinishType", "" + FinishType);
                        industry.putExtra("FinishTimes", "" + FinishTimes);
                        industry.putExtra("FinishTime", "" + FinishTime);
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 2:
                        RepeatType = "" + 1;
                        FinishType = "" + 0;
                        industry = new Intent();
                        getWeek();
                        industry.putExtra("repeat", "每周");
                        industry.putExtra("IsRepeat", 1 + "");
                        industry.putExtra("RepeatType", RepeatType);
                        industry.putExtra("Frequency", "" + 1);
                        industry.putExtra("WeekStr", "" + WeekStr);
                        industry.putExtra("FinishType", "" + FinishType);
                        industry.putExtra("FinishTimes", "" + FinishTimes);
                        industry.putExtra("FinishTime", "" + FinishTime);
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 3:
                        RepeatType = "" + 2;
                        FinishType = "" + 0;
                        industry = new Intent();
                        industry.putExtra("repeat", "每月");
                        industry.putExtra("IsRepeat", 1 + "");
                        industry.putExtra("RepeatType", RepeatType);
                        industry.putExtra("Frequency", "" + 1);
                        industry.putExtra("WeekStr", "" + WeekStr);
                        industry.putExtra("FinishType", "" + FinishType);
                        industry.putExtra("FinishTimes", "" + FinishTimes);
                        industry.putExtra("FinishTime", "" + FinishTime);
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 4:
                        RepeatType = "" + 3;
                        FinishType = "" + 0;
                        industry = new Intent();
                        industry.putExtra("repeat", "每年");
                        industry.putExtra("IsRepeat", 1 + "");
                        industry.putExtra("RepeatType", RepeatType);
                        industry.putExtra("Frequency", "" + 1);
                        industry.putExtra("WeekStr", "" + WeekStr);
                        industry.putExtra("FinishType", "" + FinishType);
                        industry.putExtra("FinishTimes", "" + FinishTimes);
                        industry.putExtra("FinishTime", "" + FinishTime);
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 5:
                        industry = new Intent(RepeatActivity.this, RepeatSettingActivity.class);
                        startActivityForResult(industry, Repeat_CallBack);
                        break;
                }

            }
        });
    }

}
