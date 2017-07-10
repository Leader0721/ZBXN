package com.zbxn.crm.activity.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pub.base.BaseApp;
import com.pub.base.BaseFragment;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.ProgressLayout;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.crm.adapter.CostDetailAdapter;
import com.zbxn.crm.adapter.DealDetailAdapter;
import com.zbxn.crm.entity.CostDetailEntity;
import com.zbxn.main.activity.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/16.
 */
public class DealDetailFragment extends BaseFragment{
    @BindView(R.id.mListView)
    PullRefreshListView mListView;
    private DealDetailAdapter mAdapter;
    private List<CostDetailEntity> mList = new ArrayList<>();
    private int mPage = 1;
    private int mPageSize = 10;
    private CostDetailActivity activity;
    @BindView(R.id.layout_progress)
    ProgressLayout layoutProgress;
    private static final int Edit_CallBack = 10000;
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_costdetail, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }


    public void initView() {
        mListView.startFirst();
        mList = new ArrayList<>();
        mAdapter = new DealDetailAdapter(mList, getContext());
        mListView.setAdapter(mAdapter);
        activity = (CostDetailActivity) getActivity();
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoad() {
                getListData();
            }
        });
        refresh();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CostDetailEntity entity = mList.get(position);
                Intent intent = new Intent(getContext(),NewDealCostActvity.class);
                intent.putExtra("First",entity.getFeeProject());
                intent.putExtra("Second",entity.getFee());
                intent.putExtra("Third",entity.getFeeTimeStr());
                intent.putExtra("Forth",entity.getRemark());
                intent.putExtra("CustomID",entity.getID());
                intent.putExtra("ID",CustomActivity.CUSTOMID);
                intent.putExtra("Title","编辑成交费用");
                startActivityForResult(intent,Edit_CallBack);
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



    public void getListData() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getDealList(ssid, CurrentCompanyId,CustomActivity.CUSTOMID,mPage,mPageSize);
        callRequest(call, new HttpCallBack(CostDetailEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    layoutProgress.showContent();
                    List<CostDetailEntity> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    if (!StringUtils.isEmpty(list)) {
                        mPage++;
                    }
                    mList.addAll(list);
                    setMore(list);
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

    public void setRefresh() {
        mPage = 1;
        getListData();
    }


    /**
     * 刷新
     */
    public void refresh() {
        mPage = 1;
        getListData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != activity.RESULT_OK) {
            return;
        }
        if (requestCode == Edit_CallBack) {
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }


}
