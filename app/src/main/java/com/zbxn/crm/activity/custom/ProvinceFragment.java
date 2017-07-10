package com.zbxn.crm.activity.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseFragment;
import com.zbxn.R;
import com.zbxn.crm.adapter.ProvinceAdapter;
import com.zbxn.crm.entity.ProvinceEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/1/12.
 */
public class ProvinceFragment extends BaseFragment {
    @BindView(R.id.listView_industry)
    ListView listViewIndustry;
    private ProvinceAdapter mAdapter;
    private FragmentManager mFragmentManager;
    private Fragment currentFragment;
    private ProvinceFragment provinceFragment, cityFragment, regionFragment;


    private ProvinceActivity mActivity;
    private List<ProvinceEntity> mList = new ArrayList<>();
    private ProvinceDBHelper provinceDbHelper;

    @Override
    public void setmTitle(String mTitle) {
        super.setmTitle(mTitle);
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.activity_industry, container, false);
        ButterKnife.bind(this, root);
        mFragmentManager = getChildFragmentManager();
        mActivity = (ProvinceActivity) getActivity();
        provinceDbHelper = new ProvinceDBHelper(getContext());
        initView();
        return root;
    }
    private void initView() {
        mList.clear();
        switch (ProvinceActivity.RegionLevel) {
            case 0:
                ProvinceActivity.RegionLevel = 0;
                mList = provinceDbHelper.getArea("0","");
                break;
            case 1:
                ProvinceActivity.RegionLevel = 1;
                mList = provinceDbHelper.getArea("1",ProvinceActivity.ParentCode);
                break;
            case 2:
                ProvinceActivity.RegionLevel = 2;
                mList = provinceDbHelper.getArea("2",ProvinceActivity.ParentCode);
                break;
        }
        mAdapter = new ProvinceAdapter(mList, getContext());
        listViewIndustry.setAdapter(mAdapter);

        listViewIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (ProvinceActivity.RegionLevel) {
                    case 0:
                        ProvinceEntity entity0 = (ProvinceEntity) mAdapter.getItem(position);
                        if (entity0 != null) {
                            ProvinceActivity.province = entity0.getRegionName();
                            ProvinceActivity.provinceID = entity0.getRegionCode()+"";
                            ProvinceActivity.ParentCode = entity0.getRegionCode();
                            ProvinceActivity.RegionLevel = 1;
                            mActivity.replace("city", cityFragment);
                        }
                        break;
                    case 1:
                        ProvinceEntity entity1 = (ProvinceEntity) mAdapter.getItem(position);
                        if (entity1 != null) {
                            ProvinceActivity.city = entity1.getRegionName();
                            ProvinceActivity.ParentCode = entity1.getRegionCode();
                            ProvinceActivity.cityID = entity1.getRegionCode()+"";
                            ProvinceActivity.RegionLevel = 2;
                            mActivity.replace("region", regionFragment);
                        }
                        break;
                    case 2:
                        ProvinceEntity entity2 = (ProvinceEntity) mAdapter.getItem(position);
                        if (entity2 != null) {
                            ProvinceActivity.region = entity2.getRegionName();
                            ProvinceActivity.ParentCode = entity2.getRegionCode();
                            ProvinceActivity.regionID = entity2.getRegionCode()+"";
                            Intent data = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("province", ProvinceActivity.province);
                            bundle.putString("city", ProvinceActivity.city);
                            bundle.putString("region", ProvinceActivity.region);
                            bundle.putString("provinceId",ProvinceActivity.provinceID);
                            bundle.putString("cityId",ProvinceActivity.cityID);
                            bundle.putString("regionId",ProvinceActivity.regionID);
                            data.putExtras(bundle);
                            mActivity.setResult(Activity.RESULT_OK, data);
                            mActivity.finish();
                        }
                        break;
                }
            }
        });
    }
    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

}
