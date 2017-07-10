package com.zbxn.crm.activity.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.utils.JsonUtil;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.NoScrollListview;
import com.zbxn.R;
import com.zbxn.crm.adapter.AddFiltrateAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;
import com.zbxn.main.listener.ICustomListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zbxn.crm.activity.custom.CustomActivity.FILTRATETYPE;

/**
 * 自定义筛选类型
 *
 * @author: ysj
 * @date: 2017-01-11 09:43
 */
public class AddFiltrateActivity extends BaseActivity {

    @BindView(R.id.listView_added)
    NoScrollListview listViewAdded;
    @BindView(R.id.layout_added)
    LinearLayout layoutAdded;
    @BindView(R.id.listView_unAdded)
    NoScrollListview listViewUnAdded;
    @BindView(R.id.layout_unAdded)
    LinearLayout layoutUnAdded;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    public static String FILTRATETYPES = "FiltrateTypes";//未选择的筛选类型
    //已添加的list
    private List<StaticTypeEntity> addedList;
    private AddFiltrateAdapter addedAdapter;
    //未添加的list
    private List<StaticTypeEntity> unAddedList;
    private AddFiltrateAdapter unAddedAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_filtrate);
        ButterKnife.bind(this);
        setTitle("添加筛选项");
        mScrollView.smoothScrollTo(0, 0);
        initData();
        initView();
    }


    @Override
    public void initRight() {
        super.initRight();
        setRight1Icon(0);
        setRight1("完成");
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        EventCustom eventCustom = new EventCustom();
        eventCustom.setTag(KeyEvent.CLIENTFILTRATE);
        eventCustom.setObj(addedList);
        EventBus.getDefault().post(eventCustom);
        PreferencesUtils.putString(this, FILTRATETYPES, JsonUtil.toJsonString(unAddedList));
        finish();
    }

    //初始化list数据
    private void initData() {
        addedList = new ArrayList<>();
        unAddedList = new ArrayList<>();
        unAddedList.add(new StaticTypeEntity(0, "CustName", "客户名称", null));
//        unAddedList.add(new StaticTypeEntity(1, "", "客户级别", null));
        unAddedList.add(new StaticTypeEntity(2, "CustState", "客户状态", null));
        unAddedList.add(new StaticTypeEntity(3, "CreateTime", "创建时间", null));
        unAddedList.add(new StaticTypeEntity(4, "UpdateTime", "更新时间", null));
        unAddedList.add(new StaticTypeEntity(5, "FollowUser", "跟进人", null));
//        unAddedList.add(new StaticTypeEntity(6, "region", "省市区", null));
        unAddedList.add(new StaticTypeEntity(7, "Address", "地址", null));
        unAddedList.add(new StaticTypeEntity(8, "Telephone", "电话", null));
//        unAddedList.add(new StaticTypeEntity(9, "", "所属公海", null));
        unAddedList.add(new StaticTypeEntity(10, "Source", "来源", null));
        unAddedList.add(new StaticTypeEntity(11, "Industry", "行业", null));
        unAddedList.add(new StaticTypeEntity(12, "Remark", "备注", null));
        initCacheData();
    }

    /**
     * 缓存的筛选项
     */
    private void initCacheData() {
        String type = PreferencesUtils.getString(this, FILTRATETYPE);
        addedList = JsonUtil.fromJsonList(type, StaticTypeEntity.class);
        String types = PreferencesUtils.getString(this, FILTRATETYPES);
        if (!StringUtils.isEmpty(types)) {
            unAddedList = JsonUtil.fromJsonList(types, StaticTypeEntity.class);
        }
    }

    private void initView() {
        isLayoutShow();
        addedAdapter = new AddFiltrateAdapter(this, addedList, 1, listener);
        unAddedAdapter = new AddFiltrateAdapter(this, unAddedList, 0, listener);
        listViewAdded.setAdapter(addedAdapter);
        listViewUnAdded.setAdapter(unAddedAdapter);

    }

    private ICustomListener listener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    addedList.add(unAddedList.get(position));
                    unAddedList.remove(position);
                    Collections.sort(addedList);
                    Collections.sort(unAddedList);
                    addedAdapter.notifyDataSetChanged();
                    unAddedAdapter.notifyDataSetChanged();
                    isLayoutShow();
                    break;
                case 1:
                    unAddedList.add(addedList.get(position));
                    addedList.remove(position);
                    Collections.sort(addedList);
                    Collections.sort(unAddedList);
                    addedAdapter.notifyDataSetChanged();
                    unAddedAdapter.notifyDataSetChanged();
                    isLayoutShow();
                    break;
                default:
                    break;
            }
        }
    };

    public void isLayoutShow() {
        if (StringUtils.isEmpty(addedList)) {
            layoutAdded.setVisibility(View.GONE);
        } else {
            layoutAdded.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isEmpty(unAddedList)) {
            layoutUnAdded.setVisibility(View.GONE);
        } else {
            layoutUnAdded.setVisibility(View.VISIBLE);
        }
    }

}
