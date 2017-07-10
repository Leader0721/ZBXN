package com.zbxn.main.activity.jobManagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.common.ToolbarParams;
import com.pub.dialog.MessageDialog;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.adapter.FrameSearchAdapter;
import com.zbxn.main.adapter.ManagerJobAdapter;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class JobSearchActivity extends BaseActivity  {

    @BindView(R.id.image_line)
    ImageView imageLine;
    @BindView(R.id.listView_SearchContact)
    ListView listViewSearchContact;
    @BindView(R.id.textTip)
    TextView textTip;
    @BindView(R.id.listView_FrameSearch)
    ListView listViewFrameSearch;
    @BindView(R.id.activity_job_search)
    LinearLayout activityJobSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    private List<String> mList;
    private FrameSearchAdapter mAdapterFrame;
    private String mSearch = "mSearch";
    private List<String> fromJsonList;
    private String keyword;
    private List<JobEntity> mAllList;
    private List<JobEntity> mNeededList;
    private ManagerJobAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);
        ButterKnife.bind(this);

        mAllList = (List<JobEntity>) getIntent().getExtras().getSerializable("list");
        mNeededList = new ArrayList();
        keyword = getIntent().getStringExtra("keyWord");
        if (StringUtils.isEmpty(keyword)) {
            keyword = "";
        }
        etSearch.setText(keyword);
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mList = new ArrayList<>();
        mAdapterFrame = new FrameSearchAdapter(this, mList, mListener);
        listViewFrameSearch.setAdapter(mAdapterFrame);
        init();
        initHistory();

    }

    //设置toolbar
    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setNavigationIcon(null);
        getLayoutInflater().inflate(R.layout.view_search, toolbar);
        return super.onToolbarConfiguration(toolbar, params);
    }

    private ICustomListener mListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            String result = obj1.toString();
            switch (obj0) {
                case 0://删除处理方式
                    updateListHistory(result, false);
                    break;
                default:
                    break;
            }
        }
    };

    protected void updateListHistory(String keyword, boolean add) {
        for (String str : mList) {
            if (str.equals(keyword)) {
                mList.remove(str);
                break;
            }
        }
        if (add) {
            mList.add(0, keyword);
        } else {
            mAdapterFrame.notifyDataSetChanged();
            if (mList.size() <= 0) {
                setVis(false);
            } else {
                setVis(true);
            }
        }
        //保存LISTHISTORY
        PreferencesUtils.putString(this, mSearch, list2Json(mList));
    }

    private List<String> json2List(String json) {
        fromJsonList = JsonUtil.fromJsonList(json, String.class);
        return fromJsonList;
    }

    private String list2Json(List<String> list) {
        return JsonUtil.toJsonString(list);
    }

    /**
     * 设置是否显示
     *
     * @param isVis
     */
    private void setVis(boolean isVis) {
        if (isVis) {
            listViewFrameSearch.setVisibility(View.VISIBLE);
            textTip.setVisibility(View.VISIBLE);
            imageLine.setVisibility(View.VISIBLE);
        } else {
            listViewFrameSearch.setVisibility(View.GONE);
            textTip.setVisibility(View.GONE);
            imageLine.setVisibility(View.GONE);
        }
    }


    private void init() {


        listViewSearchContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前点击的用户是否为登录用户
//                Contacts contacts = (Contacts) listViewSearchContact.getAdapter().getItem(position);
                /*int itemId = contacts.getId();
                int memberId = Member.get().getId();
                if (memberId == itemId)
                    return;*/
                Intent intent = new Intent(JobSearchActivity.this, OfficerActivity.class);
                //列表list的
                intent.putExtra("id", mNeededList.get(position).getPositionID());
                intent.putExtra("name", mNeededList.get(position).getPositionName());
                intent.putExtra("Desc", mNeededList.get(position).getDesc());
                startActivity(intent);
                return;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean isEmpty = (s == null || s.length() == 0);
                imgDelete.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etSearch.setText("");
                    }
                });
                //历史记录
                String listHistoryStr = PreferencesUtils.getString(JobSearchActivity.this, mSearch, "[]");
                mList = json2List(listHistoryStr);
                if (mList.size() <= 0) {
                    setVis(false);
                } else {
                    setVis(true);
                }
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String content = etSearch.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        MyToast.showToast("请输入搜索内容");
                    } else {
                        search();
                    }
                }
                //return false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(etSearch)) {
                        updateListHistory(StringUtils.getEditText(etSearch), true);
                    }
                }
                return false;

            }
        });
    }

    //  搜索方法
    public void search() {
        String content = StringUtils.getEditText(etSearch);
        mNeededList.clear();
        if (mAllList!=null){
            for (int i = 0; i < mAllList.size(); i++) {
                if (mAllList.size() > 0) {
                    if (mAllList.get(i).getPositionName().indexOf(content) != -1) {
                        mNeededList.add(mAllList.get(i));
                    }
                }

            }
        }


        if (StringUtils.isEmpty(mNeededList)||mNeededList.size()==0) {
            mNeededList = new ArrayList<>();
            mAdapter = new ManagerJobAdapter(this, mNeededList, mListener);
            listViewSearchContact.setAdapter(mAdapter);
            MyToast.showToast("没找到对应的记录");
        } else {
            mAdapter = new ManagerJobAdapter(this, mNeededList, mListener);
            listViewSearchContact.setAdapter(mAdapter);
        }

    }


    //历史记录
    private void initHistory() {
        //历史记录
        String listHistoryStr = PreferencesUtils.getString(this, mSearch, "[]");
        mList = json2List(listHistoryStr);
        if (mList.size() <= 0) {
            setVis(false);
            //     setHintTextSize(editTextNewOkrSearch, "搜索", 20);
        } else {
            setVis(true);
            //setHintTextSize(editTextNewOkrSearch, mList.get(0), 20);
        }
        mAdapterFrame = new FrameSearchAdapter(this, mList, mListener);
        listViewFrameSearch.addFooterView(createView());
        listViewFrameSearch.setAdapter(mAdapterFrame);
        listViewFrameSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSearch.setText(mList.get(position));
                updateListHistory(mList.get(position), true);
                setVis(false);

                //显示键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etSearch, InputMethodManager.SHOW_FORCED);
            }
        });
    }
    /**
     * 设置listview 尾部
     */
    private View createView() {
        View mContainer = LayoutInflater.from(this).inflate(
                R.layout.list_item_search_history_footer, null);
        RelativeLayout m_layout = (RelativeLayout) mContainer
                .findViewById(R.id.m_layout);
        m_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog(JobSearchActivity.this);
                messageDialog.setTitle("提示");
                messageDialog.setMessage("是否确认删除?");
                messageDialog.setNegativeButton("取消");
                messageDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.clear();
                        PreferencesUtils.putString(JobSearchActivity.this, mSearch, list2Json(mList));
                        mAdapterFrame.notifyDataSetChanged();
                        if (mList.size() <= 0) {
                            setVis(false);
                        }
                    }
                });
                messageDialog.show();
            }
        });
        return mContainer;
    }

}
