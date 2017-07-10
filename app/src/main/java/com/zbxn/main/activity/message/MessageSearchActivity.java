package com.zbxn.main.activity.message;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.common.ToolbarParams;
import com.pub.utils.StringUtils;
import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息搜索
 */
public class MessageSearchActivity extends BaseActivity {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.linearlayout_sixLogo)
    LinearLayout linearlayoutSixLogo;
    @BindView(R.id.linearlayout_search)
    LinearLayout linearlayoutSearch;
    private FragmentManager mFragmentManager;
    private MessageListFragment mMessageCenterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmessagecenter);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        initView();

        tvCancel.setVisibility(View.VISIBLE);
    }

    private void initView() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(etSearch)) {

                        linearlayoutSearch.setVisibility(View.VISIBLE);
                        linearlayoutSixLogo.setVisibility(View.GONE);

                        if (null == mMessageCenterFragment) {
                            mMessageCenterFragment = new MessageListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("label", "");
                            bundle.putString("keyword", StringUtils.getEditText(etSearch));
                            mMessageCenterFragment.setArguments(bundle);
                            FragmentTransaction transaction = mFragmentManager.beginTransaction();
                            transaction.replace(R.id.linearlayout_search, mMessageCenterFragment);
                            transaction.commit();
                        } else {
                            mMessageCenterFragment.setRefresh();
                        }
                    }
                }
                return true;
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                imgDelete.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                imgDelete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = (s == null || s.length() == 0);
                imgDelete.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            }
        });


    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setNavigationIcon(null);
        getLayoutInflater().inflate(R.layout.view_search, toolbar);
        return super.onToolbarConfiguration(toolbar, params);
    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @OnClick(R.id.img_delete)
    public void onClick() {
        etSearch.setText("");
    }

}
