package com.zbxn.main.activity.memberCenter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.adapter.InteragrailsAdapter;
import com.zbxn.main.entity.IntergralDatailsEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * @author: ysj
 * @date: 2016-11-02 10:32
 */
public class IntegralParticularsActivity extends BaseActivity {

    @BindView(R.id.all_detail)
    TextView allDetail;
    @BindView(R.id.get_detail)
    TextView getDetail;
    @BindView(R.id.expand_detail)
    TextView expandDetail;
    @BindView(R.id.mListView)
    PullRefreshListView mListView;

    private InteragrailsAdapter mAdapter;
    public List<IntergralDatailsEntity> mList;
    private int pageSize = 10;
    private int m_index = 1;
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integralparticulars);
        setTitle("N币明细");
        initView();

    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new InteragrailsAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                setRefresh();
            }

            @Override
            public void onLoad() {
                getListData();
            }
        });
        allDetail.performClick();
    }

    /**
     * 刷新
     */
    public void setRefresh() {
        m_index = 1;
        getListData();
    }

    public void getListData() {
        getIntegralDetail(this, m_index, type);
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
        if (pageTotal >= pageSize) {
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

    @OnClick({R.id.all_detail, R.id.get_detail, R.id.expand_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.all_detail:
                allDetail.setSelected(true);
                getDetail.setSelected(false);
                expandDetail.setSelected(false);
                type = -1;
                break;
            case R.id.get_detail:
                allDetail.setSelected(false);
                getDetail.setSelected(true);
                expandDetail.setSelected(false);
                type = 1;
                break;
            case R.id.expand_detail:
                allDetail.setSelected(false);
                getDetail.setSelected(false);
                expandDetail.setSelected(true);
                type = 0;
                break;
        }
        mList.clear();
        mAdapter.notifyDataSetChanged();
        //开始加载显示圈圈
        mListView.startFirst();
        setRefresh();
    }

    /**
     * N币明细
     *
     * @param context
     * @param page    分页
     * @param panding 类型（1：收入 0：支出 -1:全部）
     */
    public void getIntegralDetail(Context context, int page, int panding) {
        Map<String, String> map = new HashMap<String, String>();

        String pad = "";
        if (panding != -1) {
            pad = panding + "";
        }

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("page", page + "");
        map.put("panding", pad);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        Call call = HttpRequest.getIResourceOA().GetIntegralDetailNoUserId(ssid,page+"",pad);
        callRequest(call, new HttpCallBack(IntergralDatailsEntity.class,this,false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<IntergralDatailsEntity> list = mResult.getRows();
                    if (m_index == 1) {
                        mList.clear();
                    }
                    m_index++;
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                    setMore(list);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast( message);
                    mListView.onRefreshFinish();
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast( "获取网络数据失败");
                mListView.onRefreshFinish();
            }
        });
    }
}
