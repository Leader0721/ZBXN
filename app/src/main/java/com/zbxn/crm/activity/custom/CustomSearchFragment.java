package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.pub.base.BaseFragment;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.ProgressLayout;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.crm.adapter.CustomAdapter;
import com.zbxn.crm.entity.CustomListEntity;
import com.zbxn.main.activity.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/19.
 */
public class CustomSearchFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static String KEYWORD = "";
    private List<CustomListEntity> mList;
    private CustomAdapter mCustomAdapter;
    @BindView(R.id.layout_progress)
    ProgressLayout layoutProgress;
    private int mPage = 1;
    private int mPageSize = 10;
    //客户类型
    private int type = 1;
    private String typeName = "CustName";
    @BindView(R.id.listview_content)
    PullRefreshListView mListView;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_messagecenter, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

    private void initView() {
        mList = new ArrayList<>();
        mListView.startFirst();
        mCustomAdapter = new CustomAdapter(getActivity(), mList);

        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
                getCustomList();
            }
        });
        setRefresh();
    }

    public void setRefresh() {
        mPage = 1;
        getCustomList();
    }


    /**
     * 获取客户列表
     */
    public void getCustomList() {
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getCustomList(ssid, currentCompanyId, type, typeName, KEYWORD, "包含", mPage, mPageSize);
        callRequest(call, new HttpCallBack(CustomListEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                layoutProgress.showContent();
                if (mResult.getSuccess().equals("0")) {
                    List<CustomListEntity> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    mPage++;
                    mList.addAll(list);
                    setMore(list);
                    mCustomAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                    mListView.onRefreshFinish();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CustomDetailsActivity.class);
        intent.putExtra("id", mList.get(position).getID());
        intent.putExtra("name", mList.get(position).getCustName());
        CustomActivity.CUSTOMID = mList.get(position).getID();
        startActivity(intent);
    }


    /**
     * 刷新
     */
    public void refresh() {
        mPage = 1;
        getCustomList();
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


}
