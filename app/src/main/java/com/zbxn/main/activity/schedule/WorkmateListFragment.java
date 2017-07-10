package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pub.base.BaseFragment;
import com.pub.common.EventCustom;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.jobManagement.CreateJobActivity;
import com.zbxn.main.adapter.WorkmateAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.WorkmateEntity;

import org.simple.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WorkmateListFragment extends BaseFragment {

    @BindView(R.id.mListView)
    PullRefreshListView mListView;
    List<WorkmateEntity> mList = new ArrayList<WorkmateEntity>();
    private WorkmateAdapter adapter;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_workmate_list, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mList = (List<WorkmateEntity>) getArguments().getSerializable("mList");
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String userName = mList.get(position).getUserName();
                String userId = mList.get(position).getUserId();
                String permission = mList.get(position).getPermission();
                intent.setClass(getContext(), WorkmateScheduleActivity.class);
                intent.putExtra("id", userId);
                intent.putExtra("name", userName);
                intent.putExtra("isWorkmate", true);
                intent.putExtra("permission", permission);
                startActivity(intent);
            }
        });
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnabled(false);
        adapter = new WorkmateAdapter(this.getActivity(), mList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

}
