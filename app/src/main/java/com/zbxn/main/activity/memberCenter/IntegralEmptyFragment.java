package com.zbxn.main.activity.memberCenter;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pub.base.BaseFragment;
import com.zbxn.R;

import butterknife.ButterKnife;


public class IntegralEmptyFragment extends BaseFragment {

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_integral_empty, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
    }

    public void setFragmentTitle(String title) {
        mTitle = title;
    }

}
