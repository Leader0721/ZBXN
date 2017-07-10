package com.zbxn.main.activity.memberCenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseActivity;
import com.pub.utils.ConfigUtils;
import com.pub.utils.FragmentAdapter;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.widget.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/23.
 */
public class IntegralActivity extends BaseActivity {
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;
    @BindView(R.id.mName)
    TextView mName;
    @BindView(R.id.mTotalIntegral)
    TextView mTotalIntegral;//累计N币
    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.ll_need)
    LinearLayout ll_need;
    @BindView(R.id.tv_good)
    TextView tvGood;
    @BindView(R.id.tv_need)
    TextView tvNeed;
    private ProgressDialog progressDialog;
    public String m;
    //根据 type 判断当前页面类型
    String touxiang;
    String myname;
    //    String paiming;
    String leiji;
    private FragmentAdapter mAdapter;
    protected static int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral);
        setTitle("N币排名");
        ButterKnife.bind(this);
        //获取传值,设置数据
        Intent intent = getIntent();
        //没数据隐藏
        String good = intent.getStringExtra("good");
        String need = intent.getStringExtra("need");
        if (StringUtils.isEmpty(need)) {
            ll_need.setVisibility(View.INVISIBLE);
        } else {
            ll_need.setVisibility(View.VISIBLE);
            tvNeed.setText("差"+need+"N币");
            tvGood.setText(good);
        }
        touxiang = intent.getStringExtra("touxiang");
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(mBaseUrl + touxiang, mPortrait);//头像
        myname = intent.getStringExtra("name");
        mName.setText(myname);
        leiji = intent.getStringExtra("leiji");
        mTotalIntegral.setText(leiji);
        progressDialog = new ProgressDialog(this);
        initViewPager();
        //隐藏阴影
        getToolbarHelper().setShadowEnable(false);
    }

    /**
     * 显示排名
     */
    private void initViewPager() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        IntegralFragment fragment1 = new IntegralFragment();
        fragment1.setFragmentTitle("公司");
        bundle = new Bundle();
        bundle.putInt("types", 101);
        fragment1.setArguments(bundle);
        IntegralEmptyFragment fragment2 = new IntegralEmptyFragment();
        fragment2.setFragmentTitle("好友");
        bundle = new Bundle();
        bundle.putInt("types", 102);
        fragment2.setArguments(bundle);
        IntegralFragment fragment3 = new IntegralFragment();
        fragment3.setFragmentTitle("全部");
        bundle = new Bundle();
        bundle.putInt("types", 103);
        fragment3.setArguments(bundle);
        mAdapter.addFragment(fragment1, fragment2, fragment3);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mAdapter);
        mSmartTabLayout.setViewPager(mViewPager);
        mSmartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                index = position;

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
