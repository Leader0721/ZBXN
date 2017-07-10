package com.zbxn.main.activity.message;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.pub.base.BaseActivity;
import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/26.
 */
public class MessageListActivity extends BaseActivity {

    @BindView(R.id.layout_content)
    LinearLayout layoutContent;
    private FragmentManager mFragmentManager;
    private MessageListFragment mFragment;

    private String mTitle = "";
    private String mLabel = "";
    private String mKeyWord = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagecenterdetail);
        ButterKnife.bind(this);

        mTitle = getIntent().getStringExtra("title");
        mLabel = getIntent().getStringExtra("label");
        mKeyWord = getIntent().getStringExtra("keyword");

        setTitle(mTitle);
        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mFragment = new MessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", mLabel);
        bundle.putString("keyword", mKeyWord);
        mFragment.setArguments(bundle);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.layout_content, mFragment);
        transaction.commit();
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1("全部已读");
        setRight1Show(true);
        setRight1Icon(0);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        mFragment.setRead("1", mLabel);
    }
}
