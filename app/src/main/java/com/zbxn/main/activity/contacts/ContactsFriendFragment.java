package com.zbxn.main.activity.contacts;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pub.base.BaseFragment;
import com.zbxn.R;

import butterknife.ButterKnife;

/**
 * 通讯录按拼音排序
 *
 * @author GISirFive
 * @time 2016/8/8
 */
public class ContactsFriendFragment extends BaseFragment {

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_contacts_friend, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

}
