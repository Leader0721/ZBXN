package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.utils.JsonUtil;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.adapter.FollowTypeAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟进记录类型
 *
 * @author: ysj
 * @date: 2017-01-16 14:38
 */
public class FollowTypeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mTypeListView)
    ListView mTypeListView;

    private List<StaticTypeEntity> mList;
    private FollowTypeAdapter mAdapter;

    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followtype);
        ButterKnife.bind(this);
        setTitle("跟进记录类型");
        initView();
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mList = new ArrayList<>();
        String jsonStr = PreferencesUtils.getString(this, CustomActivity.RECORDTYPELIST);
        mList = JsonUtil.fromJsonList(jsonStr, StaticTypeEntity.class);
        id = FollowTypeAdapter.typeId;
        if (StringUtils.isEmpty(id)) {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getValue().equals("跟进记录")) {
                    id = mList.get(i).getKey();
                }
            }
        } else {
            id = getIntent().getStringExtra("id");
        }
        mAdapter = new FollowTypeAdapter(this, mList);
        mAdapter.typeId = id;
        mTypeListView.setAdapter(mAdapter);
        mTypeListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.typeId = mList.get(position).getKey();
        mAdapter.notifyDataSetChanged();
        Intent data = new Intent();
        data.putExtra("typeId", mList.get(position).getKey());
        data.putExtra("typeName", mList.get(position).getValue());
        setResult(RESULT_OK, data);
        finish();
    }
}
