package com.zbxn.main.activity.okr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.common.ToolbarParams;
import com.pub.dialog.MessageDialog;
import com.pub.utils.JsonUtil;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.adapter.NewOkrSearchAdapter;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OkrNewSearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.listView_NewOkrSearch)
    ListView mListView;
    @BindView(R.id.text_Tip)
    TextView textTip;
    @BindView(R.id.image_line)
    ImageView imageLine;

    private NewOkrSearchAdapter mAdapter;
    private List<String> mList;

    private String mOkrSearch = "mOkrSearch";

    private String keyword = "";
    private List<String> fromJsonList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okr_new_search);
        ButterKnife.bind(this);

        keyword = getIntent().getStringExtra("keyword");
        if (StringUtils.isEmpty(keyword)) {
            keyword = "";
        }else {
            imgDelete.setVisibility(View.VISIBLE);
        }
        etSearch.setText(keyword);
        tvCancel.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();
        mAdapter = new NewOkrSearchAdapter(this, mList, mListener);
        mListView.setAdapter(mAdapter);
        initView();
        initHistory();

    }

    @Override
    public void initRight() {
        setRight1Show(false);
        setRight2Show(false);
        super.initRight();
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

    @OnClick({R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
        }
    }


    private void initView() {
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

                //历史记录
                String listHistoryStr = PreferencesUtils.getString(OkrNewSearchActivity.this, mOkrSearch, "[]");
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
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!StringUtils.isEmpty(etSearch)) {
                        updateListHistory(StringUtils.getEditText(etSearch), true);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("keyword", StringUtils.getEditText(etSearch));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                return true;
            }
        });
    }

    /**
     * 设置是否显示
     *
     * @param isVis
     */
    private void setVis(boolean isVis) {
        if (isVis) {
            mListView.setVisibility(View.VISIBLE);
            textTip.setVisibility(View.VISIBLE);
            imageLine.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.GONE);
            textTip.setVisibility(View.GONE);
            imageLine.setVisibility(View.GONE);
        }
    }

    //历史记录
    private void initHistory() {
        //历史记录
        String listHistoryStr = PreferencesUtils.getString(this, mOkrSearch, "[]");
        mList = json2List(listHistoryStr);
        if (mList.size() <= 0) {
            setVis(false);
            //     setHintTextSize(etSearch, "搜索", 20);
        } else {
            setVis(true);
            //setHintTextSize(etSearch, mList.get(0), 20);
        }
        mAdapter = new NewOkrSearchAdapter(this, mList, mListener);
        mListView.addFooterView(createView());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            mAdapter.notifyDataSetChanged();
            if (mList.size() <= 0) {
                setVis(false);
            } else {
                setVis(true);
            }
        }
        //保存LISTHISTORY
        PreferencesUtils.putString(this, mOkrSearch, list2Json(mList));
    }

    private List<String> json2List(String json) {
        fromJsonList = JsonUtil.fromJsonList(json, String.class);
        return fromJsonList;
    }

    private String list2Json(List<String> list) {
        return JsonUtil.toJsonString(list);
    }

    /**
     * 设置listview 尾部
     */
    private View createView() {
        View mContainer = (View) LayoutInflater.from(this).inflate(
                R.layout.list_item_search_history_footer, null);
        RelativeLayout m_layout = (RelativeLayout) mContainer
                .findViewById(R.id.m_layout);
        m_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog messageDialog = new MessageDialog(OkrNewSearchActivity.this);
                messageDialog.setTitle("提示");
                messageDialog.setMessage("是否确认删除?");
                messageDialog.setNegativeButton("取消");
                messageDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.clear();
                        PreferencesUtils.putString(OkrNewSearchActivity.this, mOkrSearch, list2Json(mList));
                        mAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.img_delete)
    public void onClick() {
        etSearch.setText("");
    }
}
