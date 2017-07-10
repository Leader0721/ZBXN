package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.pub.widget.smarttablayout.SmartTabLayout;
import com.zbxn.R;
import com.zbxn.crm.adapter.CostDetailAdapter;
import com.zbxn.crm.entity.CostDetailEntity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.FragmentAdapter;
import com.zbxn.main.entity.OkrRankingEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CostDetailActivity extends BaseActivity {
    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costdetail);
        ButterKnife.bind(this);
        setTitle("费用明细");
        initView();
    }

    private void initView() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        CostDetailFragment costDetailFragment = new CostDetailFragment();
        costDetailFragment.setmTitle("花费明细");
        DealDetailFragment dealDetailFragment = new DealDetailFragment();
        dealDetailFragment.setmTitle("成交明细");
        mAdapter.addFragment(costDetailFragment, dealDetailFragment);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
    }
}
