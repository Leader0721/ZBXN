package com.zbxn.main.activity.approvalmodule;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.pub.base.BaseActivity;
import com.pub.widget.smarttablayout.SmartTabLayout;
import com.zbxn.R;
import com.zbxn.main.adapter.FragmentAdapter;
import com.zbxn.main.adapter.PopupwindowTypeListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：审批主页面
 * 创建人：LiangHanXin
 * 创建时间：2016/10/10 9:04
 */
public class ApplyActivity extends BaseActivity {

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private FragmentAdapter mAdapter;
    private MenuItem mCollect;

    protected static int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        ButterKnife.bind(this);
        setTitle("审批");
        PopupwindowTypeListAdapter.savePosionLeft = 1;
        PopupwindowTypeListAdapter.savePosionRight = -1;
        PopupwindowTypeListAdapter.savePosionLeft2 = 1;
        PopupwindowTypeListAdapter.savePosionRight2 = -1;
        PopupwindowTypeListAdapter.savePosionLeft3 = 1;
        PopupwindowTypeListAdapter.savePosionRight3 = -1;
        initView();
    }

    /**
     * 创建按钮显示
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 获取menu实例
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mCollect = menu.findItem(R.id.schedule_creat);
        mCollect.setTitle("创建审批表单");
        return true;
    }

    /**
     * 事件监听
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.schedule_creat: //创建审批申请
                Intent intent = new Intent(this, CreatFormActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        Bundle bundle = null;
        ApplyFragment fragment1 = new ApplyFragment();
        fragment1.setFragmentTitle("我的申请");
        bundle = new Bundle();
        bundle.putInt("types", 101);
        fragment1.setArguments(bundle);
        ApplyFragment fragment2 = new ApplyFragment();
        fragment2.setFragmentTitle("我的审批");
        bundle = new Bundle();
        bundle.putInt("types", 102);
        fragment2.setArguments(bundle);
        ApplyFragment fragment3 = new ApplyFragment();
        fragment3.setFragmentTitle("审批查询");
        bundle = new Bundle();
        bundle.putInt("types", 103);
        fragment3.setArguments(bundle);
        mAdapter.addFragment(fragment1, fragment2, fragment3);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
        mSmartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                index = position;
                if (position == 0) {
                    PopupwindowTypeListAdapter.TYPE_APPLY = 101;
                } else if (position == 1) {
                    PopupwindowTypeListAdapter.TYPE_APPLY = 102;
                } else if (position == 2) {
                    PopupwindowTypeListAdapter.TYPE_APPLY = 103;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

}
