package com.zbxn.crm.activity.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.ProgressLayout;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.crm.adapter.OperationLogAdapter;
import com.zbxn.crm.entity.OperationLogEntity;
import com.zbxn.main.activity.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 操作记录
 *
 * @author: ysj
 * @date: 2017-01-17 14:24
 */
public class OperationLogActivity extends BaseActivity {

    @BindView(R.id.layout_progress)
    ProgressLayout layoutProgress;
    @BindView(R.id.mListView)
    PullRefreshListView mListView;

    private List<OperationLogEntity> mList;
    private OperationLogAdapter mAdapter;
    private int mPage = 1;
    private int mPageSize = 10;
    private String custId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operationlog);
        ButterKnife.bind(this);
        setTitle("操作记录");
        mListView.setDividerHide();
        custId = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new OperationLogAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoad() {
                getOperationLogList();
            }
        });
        mListView.startFirst();
        refresh();
    }

    /**
     * 获取操作记录列表
     */
    public void getOperationLogList() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getLogList(ssid, currentCompanyId, custId, mPage, mPageSize);
        callRequest(call, new HttpCallBack(OperationLogEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                mListView.onRefreshFinish();
                if (mResult.getSuccess().equals("0")) {
                    List<OperationLogEntity> list = mResult.getRows();
                    layoutProgress.showContent();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    setMore(list);
                    if (StringUtils.isEmpty(list)) {
                        MyToast.showToast("暂无更多数据");
                    } else {
                        mPage++;
                    }
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mResult.getMsg());
                    layoutProgress.showError(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
                mListView.onRefreshFinish();
                layoutProgress.showError(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            }
        });
    }

    /**
     * 刷新
     */
    public void refresh() {
        mPage = 1;
        getOperationLogList();
    }

    /**
     * 显示加载更多
     *
     * @param mResult
     */
    private void setMore(List mResult) {
        if (mResult == null) {
            mListView.setHasMoreData(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= mPageSize) {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

}
