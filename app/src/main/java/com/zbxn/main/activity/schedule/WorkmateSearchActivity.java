package com.zbxn.main.activity.schedule;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.EventCustom;
import com.pub.common.ToolbarParams;
import com.pub.utils.MyToast;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.jobManagement.CreateJobActivity;
import com.zbxn.main.activity.message.MessageListFragment;
import com.zbxn.main.adapter.OrganizePeopleAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.entity.WorkmateEntity;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorkmateSearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.mShortCutContainer)
    FrameLayout mShortCutContainer;
    private WorkmateListFragment workmateListFragment;
    List<WorkmateEntity> mList = new ArrayList<WorkmateEntity>();
    private FragmentManager manager;
    private FragmentTransaction ft;
    private List<WorkmateEntity> mList1 = new ArrayList<>();
    private List<WorkmateEntity> mList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workmate_search);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tvCancel.setVisibility(View.VISIBLE);
        mList2 = (List<WorkmateEntity>) getIntent().getSerializableExtra("mList");
        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mList", (Serializable) mList1);
        workmateListFragment = new WorkmateListFragment();
        workmateListFragment.setArguments(bundle);
        ft.add(R.id.mShortCutContainer, workmateListFragment);
        ft.commit();
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

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(etSearch)) {
                        search();
                        if (mList1.size() > 0) {
                            FragmentTransaction ft1 = manager.beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("mList", (Serializable) mList1);
                            WorkmateListFragment fragment1 = new WorkmateListFragment();
                            fragment1.setArguments(bundle);
                            ft1.replace(R.id.mShortCutContainer, fragment1);
                            ft1.commit();
                        } else {
                            MyToast.showToast("没有搜索到相应记录");
                        }
                    } else {
                        MyToast.showToast("请输入搜索内容");
                        FragmentTransaction ft2 = manager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("mList", (Serializable) mList1);
                        WorkmateListFragment fragment = new WorkmateListFragment();
                        fragment.setArguments(bundle);
                        ft2.replace(R.id.mShortCutContainer, fragment);
                        ft2.commit();
                    }
                }
                return true;
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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


    //  搜索方法
    public void search() {
        mList1.clear();
        String content = StringUtils.getEditText(etSearch);
        if (mList2 != null && mList2.size() > 0) {
            for (int i = 0; i < mList2.size(); i++) {
                if (mList2.get(i).getUserName().indexOf(content) != -1) {
                    mList1.add(mList2.get(i));
                }
            }
        } else {
            MyToast.showToast("暂时没有相应记录");
        }

    }

    /**
     * 更新是否选中
     *
     * @param lists
     */
    private void chechIsSelect(final List<Contacts> lists) {
        List<Contacts> listTemp = new ArrayList<>();
        listTemp.addAll(lists);
        lists.clear();
        for (int i = 0; i < listTemp.size(); i++) {
            if (ContactsChoseActivity.mHashMap.containsKey(listTemp.get(i).getId() + "")) {
                listTemp.get(i).setSelected(true);
            } else {
                listTemp.get(i).setSelected(false);
            }
        }
        lists.addAll(listTemp);
    }
}
