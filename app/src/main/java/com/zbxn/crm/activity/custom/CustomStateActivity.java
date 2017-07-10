package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.crm.adapter.IndustryAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/11.
 */
public class CustomStateActivity extends BaseActivity{
    @BindView(R.id.listView_industry)
    ListView listViewIndustry;
    private List<StaticTypeEntity>mList = new ArrayList<>();
    private IndustryAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry);
        setTitle("客户状态");
        ButterKnife.bind(this);
        String stateStr = PreferencesUtils.getString(CustomStateActivity.this, CustomActivity.CUSTSTATE);

        mList = JsonUtil.fromJsonList(stateStr,StaticTypeEntity.class);


        initView();
    }

    private void initView() {
        mAdapter = new IndustryAdapter(mList,this);
        listViewIndustry.setAdapter(mAdapter);
        listViewIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent industry = new Intent();
                industry.putExtra("state", mList.get(position).getValue());
                industry.putExtra("id",mList.get(position).getKey());
                setResult(RESULT_OK, industry);
                finish();
            }
        });
    }

}
