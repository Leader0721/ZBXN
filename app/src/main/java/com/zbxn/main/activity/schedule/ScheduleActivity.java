package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.entity.WorkmateEntity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：日程
 * 创建人：wuzy
 * 创建时间：2016/9/21 8:24
 */
public class ScheduleActivity extends BaseActivity {
    public static final String SUCCESS = "ScheduleActivitySuccess";
    private ScheduleFragmentManager mPagerManager;
    private FragmentManager mFragmentManager;
    public static List<WorkmateEntity> list;
    private String mUserID;
    private String mName;
    private boolean isWorkmate;
    private static final int New_CallBack = 1001;//新建活动
    public static final String SUCCESS1 = "ScheduleActivitySuccess1";
    public static final String SUCCESS2 = "ScheduleActivitySuccess2";
    public static final String SUCCESS3 = "ScheduleActivitySuccess3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        mPagerManager = new ScheduleFragmentManager(this);
        init();
    }


    @Override
    public void initRight() {
        switch (mPagerManager.getCurrentPageIndex()) {
            case 0:
                setRight1Show(true);
                setRight2Show(false);
                setRight1Icon(R.mipmap.schedule_today1);
                break;
            case 1:
                setRight1Show(true);
                setRight2Show(false);
                setRight1Icon(R.mipmap.nav_search);
                break;
            default:
                setRight1Show(false);
                setRight2Show(false);
                break;
        }
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        Intent intent;
        switch (mPagerManager.getCurrentPageIndex()) {
            case 0:
                EventCustom eventCustom = new EventCustom();
                eventCustom.setTag(ScheduleActivity.SUCCESS1);
                EventBus.getDefault().post(eventCustom);
                break;
            case 1:
                intent = new Intent(this, WorkmateSearchActivity.class);
                List<WorkmateEntity> mList = ScheduleActivity.list;
                intent.putExtra("mList", (Serializable) mList);
                startActivity(intent);
                break;
            default:
                setRight1Show(false);
                break;
        }
        super.actionRight1(menuItem);
    }


    @OnClick(R.id.mShortCut)
    public void onClick() {
        startActivityForResult(new Intent(this, NewScheduActivity.class), New_CallBack);
    }

    private void init() {
        list = new ArrayList<>();
        mUserID = getIntent().getStringExtra("id");
        mName = getIntent().getStringExtra("name");
        isWorkmate = getIntent().getBooleanExtra("isWorkmate", false);
        mFragmentManager = getSupportFragmentManager();
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (eventCustom.getTag().equals(ScheduleActivity.SUCCESS)) {
            String name = (String) eventCustom.getObj();
            if (!StringUtils.isEmpty(name)) {
                setTitle(name);
            }
        }
    }



}
