package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.utils.MyToast;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.listener.ICustomListener;

import org.simple.eventbus.EventBus;

public class WorkmateScheduleActivity extends BaseActivity {

    private String id;
    private String name;
    private String permission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workmate_schedule);
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        permission = getIntent().getStringExtra("permission");
        setTitle(name);
        /*MyToast.showToast(name);
        if (!StringUtils.isEmpty(name)){
            setTitle(name);
        }*/
        initView();
    }
    private void initView() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        MonthFragment fragment = new MonthFragment();
        Bundle bundle = new Bundle();
        if (!StringUtils.isEmpty(id)) {
            bundle.putString("id", id);
        }
        bundle.putString("name", name);
        bundle.putBoolean("isWorkmate", true);
        fragment.setArguments(bundle);
        ft.add(R.id.mShortCutContainer, fragment);
        ft.commit();
    }

    ICustomListener iCustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {

        }
    };

    @Override
    public void initRight() {
        setRight1Icon(R.mipmap.menu_creat_blog);
        if ("100".equals(permission)){
            setRight1Show(false);
        }else {
            setRight1Show(true);
        }

        setRight2Show(true);
        setRight2Icon(R.mipmap.schedule_today1);



        super.initRight();
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        Intent intent = new Intent();
        intent.setClass(this, NewScheduActivity.class);
        intent.putExtra("forUserID", id);
        intent.putExtra("isCreateWorkmate",true);
        startActivity(intent);
    }

    @Override
    public void actionRight2(MenuItem menuItem) {
        super.actionRight2(menuItem);
        EventCustom eventCustom = new EventCustom();
        eventCustom.setTag(ScheduleActivity.SUCCESS1);
        EventBus.getDefault().post(eventCustom);
    }
}
