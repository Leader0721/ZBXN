package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pub.base.BaseFragment;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.ProgressLayout;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.crm.adapter.FollowUpAdapter;
import com.zbxn.crm.entity.FollowUpEntity;
import com.zbxn.main.activity.login.LoginActivity;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 客户跟进记录
 *
 * @author: ysj
 * @date: 2017-01-13 10:36
 */
public class FollowUpFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.mListView)
    PullRefreshListView mListView;
    @BindView(R.id.layout_progress)
    ProgressLayout layoutProgress;

    private List<FollowUpEntity> mList;
    private FollowUpAdapter mAdapter;
    private int mPage = 1;
    private int mPageSize = 10;
    private String customId;
    private String customName;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_follow_up, null);
        ButterKnife.bind(this, view);
        customId = getArguments().getString("id");
        customName = getArguments().getString("name");
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        initView();
        mListView.startFirst();
        refresh();
    }

    /**
     * 初始化
     */
    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new FollowUpAdapter(getContext(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoad() {
                getFollowList();
            }
        });
    }

    /**
     * 刷新
     */
    public void refresh() {
        mPage = 1;
        getFollowList();
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        refresh();
    }

    /**
     * 获取跟进记录列表
     */
    public void getFollowList() {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getFollowList(ssid, currentCompanyId, customId, mPage, mPageSize);
        callRequest(call, new HttpCallBack(FollowUpEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    layoutProgress.showContent();
                    List<FollowUpEntity> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    setMore(list);
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                    mListView.onRefreshFinish();
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
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), FollowUpDetailsActivity.class);
        FollowUpEntity entity = mList.get(position);
        intent.putExtra("id", customId);
        intent.putExtra("name", customName);
        Bundle bundle = new Bundle();
        bundle.putParcelable("follow", entity);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

}
