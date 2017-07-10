package com.zbxn.crm.activity.custom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.pub.base.BaseActivity;
import com.zbxn.R;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 省市区三级联动
 * Created by Administrator on 2017/1/12.
 */
public class ProvinceActivity extends BaseActivity {
    @BindView(R.id.container)
    LinearLayout container;


    private ProvinceFragment provinceFragment;
    private FragmentManager mFragmentManager;
    private Fragment currentFragment;
    public static String ParentCode;
    public static int RegionLevel;
    public static String province;
    public static String city;
    public static String region;
    public static String provinceID;
    public static String cityID;
    public static String regionID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        setTitle("请选择");
        ButterKnife.bind(this);
        RegionLevel = 0;
        mFragmentManager = getSupportFragmentManager();
        provinceFragment = new ProvinceFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.container, provinceFragment, "province")
                .addToBackStack("province")
                .commit();
        currentFragment = provinceFragment;
    }


    protected void replace(String tag, Fragment fragment) {
        if (fragment == null) {
            fragment = new ProvinceFragment();
//            mFragmentManager.beginTransaction().hide(currentFragment)
//                    .add(R.id.container, fragment, tag)
//                    .addToBackStack(tag).commit();

            mFragmentManager.beginTransaction().replace(R.id.container,fragment,tag).addToBackStack(tag).commit();
        } else {
            mFragmentManager.beginTransaction()
                    .hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (RegionLevel == 0){
                province = null;
                city = null;
                region = null;
                provinceID = null;
                cityID = null;
                finish();
            }else if (RegionLevel == 1){
                city = null;
                region = null;
                cityID = null;
                regionID = null;
                RegionLevel = 0;
            }else if (RegionLevel == 2){
                region = null;
                regionID = null;
                RegionLevel = 1;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
