package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.utils.JsonUtil;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.crm.activity.custom.CustomActivity;
import com.zbxn.crm.adapter.IndustryAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 时间提醒界面
 * Created by Administrator on 2017/2/10.
 */
public class TimeAlerterActivity extends BaseActivity {
    String[] strings = new String[]{"关闭提醒", "开始时提醒", "10分钟前", "15分钟前", "30分钟前", "1小时前", "2小时前", "1天前"};
    @BindView(R.id.listView_industry)
    ListView listViewIndustry;
    private List<StaticTypeEntity> mList = new ArrayList<>();
    private IndustryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry);
        setTitle("提醒");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        for (int i = 0; i < strings.length; i++) {
            StaticTypeEntity entity = new StaticTypeEntity();
            entity.setValue(strings[i]);
            entity.setKey(i + "");
            mList.add(i, entity);
        }


        mAdapter = new IndustryAdapter(mList, this);
        listViewIndustry.setAdapter(mAdapter);
        listViewIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent industry;
                switch (position) {
                    case 0:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 0 + "");
                        industry.putExtra("AlarmType", "");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 1:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "0");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 2:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "1");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 3:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "2");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 4:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "3");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 5:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "4");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 6:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "5");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                    case 7:
                        industry = new Intent();
                        industry.putExtra("alert", mList.get(position).getValue());
                        industry.putExtra("IsAlarm", 1 + "");
                        industry.putExtra("AlarmType", "6");
                        setResult(RESULT_OK, industry);
                        finish();
                        break;
                }

            }
        });
    }

}
