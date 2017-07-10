package com.zbxn.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pub.base.BaseFragment;
import com.pub.widget.smarttablayout.SmartTabLayout;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsDepartmentFragment;
import com.zbxn.main.activity.contacts.ContactsFriendFragment;
import com.zbxn.main.activity.contacts.ContactsPeopleFragment;
import com.zbxn.main.adapter.FragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页-通讯录界面
 * Created by Administrator on 2016/12/26.
 */
public class MainContactsFragment extends BaseFragment {

    @BindView(R.id.mSmartTabLayout)
    SmartTabLayout mSmartTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    private FragmentAdapter mAdapter;

    //0-查看 1-多选 2-单选
    private int mType;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_addressbook, container, false);
        ButterKnife.bind(this, root);
        mType = getArguments().getInt("type", 0);
        return root;
    }


    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            View shader = mRootView.findViewById(R.id.mTopShader);
            Drawable drawable = ScrimUtil.makeCubicGradientScrimDrawable(
                    Color.parseColor("#28000000"), //颜色
                    8, Gravity.TOP);//渐变层数,起始方向
            shader.setBackground(drawable);
        }*/


        mAdapter = new FragmentAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        ContactsPeopleFragment contactsPeopleFragment = new ContactsPeopleFragment();
        contactsPeopleFragment.setFragmentTitle("按同事");
        Bundle bundle = new Bundle();
        bundle.putInt("type", mType);
        contactsPeopleFragment.setArguments(bundle);
        ContactsDepartmentFragment contactsDepartmentFragment = new ContactsDepartmentFragment();
        contactsDepartmentFragment.setFragmentTitle("按部门");
        bundle = new Bundle();
        bundle.putInt("type", mType);
        contactsDepartmentFragment.setArguments(bundle);
        ContactsFriendFragment contactsFriendFragment = new ContactsFriendFragment();
        contactsFriendFragment.setFragmentTitle("按好友");
        bundle = new Bundle();
        bundle.putInt("type", mType);
        contactsFriendFragment.setArguments(bundle);
        mAdapter.addFragment(contactsPeopleFragment, contactsDepartmentFragment, contactsFriendFragment);
        mSmartTabLayout.setViewPager(mViewPager);
    }

}
