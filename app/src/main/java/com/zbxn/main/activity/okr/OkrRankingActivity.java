package com.zbxn.main.activity.okr;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.OkrRankingAdapter;
import com.zbxn.main.entity.OkrDepartEntity;
import com.zbxn.main.entity.OkrRankingEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/13.
 */
public class OkrRankingActivity extends BaseActivity implements OkrSelectByFragment.CallBackValue {
    public static final int ORK_NEWSEARCH_CALLBACK = 1000;

    @BindView(R.id.general_ranking)
    TextView generalRanking;
    @BindView(R.id.business_ranking)
    TextView businessRanking;
    @BindView(R.id.general_integral)
    LinearLayout generalIntegral;
    @BindView(R.id.business_integral)
    LinearLayout businessIntegral;
    @BindView(R.id.okr_listView)
    PullRefreshListView mListView;
    @BindView(R.id.general_tv)
    TextView generalTv;
    @BindView(R.id.general_img)
    ImageView generalImg;
    @BindView(R.id.business_tv)
    TextView businessTv;
    @BindView(R.id.business_img)
    ImageView businessImg;
    @BindView(R.id.okr_tv)
    TextView okrTv;
    @BindView(R.id.okr_img)
    ImageView okrImg;
    @BindView(R.id.okr_integral)
    LinearLayout okrIntegral;
    @BindView(R.id.mDrawerLayout_newOkr)
    DrawerLayout mDrawerLayout;

    private OkrRankingAdapter mAdapter;
    private List<OkrRankingEntity> mList;

    private ActionBarDrawerToggle mDrawerToggle;
    private OkrSelectByFragment mOkrSelectByFragment;

    private int mIndex = 1;
    private int pageSize = 10;

    private int general_sign = 1;//通用积分标记 0:未选择，1:升序，2:降序
    private int business_sign = 1;//业务 0:未选择,1:升序，2:降序
    private int okr_sign = 1;//okr排名 0:未选择, 1:升序,2:降序

    private int orderByType = 6;//积分排序状态 1~6
    private int mOrderCustom = 0;//0—无,1—本部门，2—本职位
    private int mDepartmentId = 0;//部门id
    private int mYear = 2016;//年
    private int mMonth;//月
    private String mKeyword = "";//关键词 100字符以内


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okr_ranking);
        //绑定控件
        ButterKnife.bind(this);
        //设置标题
        setTitle("OKR排名");
        init();
        getDepartmentList();
        initView();
    }

    private void init() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mOkrSelectByFragment = (OkrSelectByFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mOkr_SelectBy_Fragment);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        PreferencesUtils.putBoolean(this, "okrdrawlayout", false);


    }

    @Override
    public void initRight() {
        setRight1Show(true);
        setRight2("");
        setRight2Icon(R.mipmap.nav_search);
        setRight2Show(true);
        setRight1("");
        setRight1Icon(R.mipmap.nav_screening);
        super.initRight();
    }

    @Override
    public void actionRight2(MenuItem menuItem) {
        Intent intent = new Intent(this, OkrNewSearchActivity.class);
        intent.putExtra("keyword", mKeyword);
        startActivityForResult(intent, ORK_NEWSEARCH_CALLBACK);
        super.actionRight2(menuItem);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        if (mDrawerLayout.isDrawerOpen(mOkrSelectByFragment.getView())) {
            mDrawerLayout.closeDrawer(mOkrSelectByFragment.getView());
        } else {
            mDrawerLayout.openDrawer(mOkrSelectByFragment.getView());
        }
        super.actionRight1(menuItem);
    }


    public void initView() {
        mListView.startFirst();
        mList = new ArrayList<>();
        mAdapter = new OkrRankingAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        setRefresh();
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mDrawerLayout.isShown()) {

        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 刷新
     */
    public void setRefresh() {
        mIndex = 1;
        getListData();
    }

    public void getListData() {
        getRanking(mIndex);
    }


    @OnClick({R.id.general_integral, R.id.business_integral, R.id.okr_integral})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.general_integral:
                setImageNull();
                business_sign = 1;
                okr_sign = 1;
                if (general_sign == 1) {
                    orderByType = 1;
                    generalImg.setImageResource(R.mipmap.rank_up);
                    general_sign = 2;
                } else if (general_sign == 2) {
                    orderByType = 2;
                    generalImg.setImageResource(R.mipmap.rank_down);
                    general_sign = 1;
                }
                //mListView.startFirst();
                setRefresh();
                break;
            case R.id.business_integral:
                setImageNull();
                general_sign = 1;
                okr_sign = 1;
                if (business_sign == 1) {
                    orderByType = 3;
                    businessImg.setImageResource(R.mipmap.rank_up);
                    business_sign = 2;
                } else if (business_sign == 2) {
                    orderByType = 4;
                    businessImg.setImageResource(R.mipmap.rank_down);
                    business_sign = 1;
                }
                //mListView.startFirst();
                setRefresh();
                break;
            case R.id.okr_integral:
                setImageNull();
                general_sign = 1;
                business_sign = 1;
                if (okr_sign == 1) {
                    orderByType = 5;
                    okrImg.setImageResource(R.mipmap.rank_up);
                    okr_sign = 2;
                } else if (okr_sign == 2) {
                    orderByType = 6;
                    okrImg.setImageResource(R.mipmap.rank_down);
                    okr_sign = 1;
                }
                //mListView.startFirst();
                setRefresh();
                break;
        }
    }


    public void setImageNull() {
        generalImg.setImageResource(R.mipmap.rank_null);
        businessImg.setImageResource(R.mipmap.rank_null);
        okrImg.setImageResource(R.mipmap.rank_null);
    }


    public void getDepartmentList() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().GetDepartmentList(ssid, CurrentCompanyId);
        callRequest(call, new HttpCallBack(OkrDepartEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    List<OkrDepartEntity> list = mResult.getRows();
                    if (!StringUtils.isEmpty(list)) {
                        mOkrSelectByFragment.setDepartList(list);
                    }
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    public void getRanking(int page) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().GetRanking(ssid, CurrentCompanyId, orderByType + "",
                mOrderCustom + "", mDepartmentId + "", mYear + "", mMonth + "", mKeyword, page + "", pageSize + "");
        callRequest(call, new HttpCallBack(OkrRankingEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData result) {
                if ("0".equals(result.getSuccess())) {
                    mListView.onRefreshFinish();
                    List<OkrRankingEntity> list = result.getRows();
                    if (mIndex == 1) {
                        mList.clear();
                    }
                    if (!StringUtils.isEmpty(list)) {
                        mIndex++;
                    }
                    setMore(list);
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mListView.onRefreshFinish();
                    String message = result.getMsg();
                    MyToast.showToast(message);
                }

            }

            @Override
            public void onFailure(String string) {
                mListView.onRefreshFinish();
                MyToast.showToast("获取网络数据失败");
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
        if (pageTotal >= pageSize) {
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //搜索
        if (requestCode == ORK_NEWSEARCH_CALLBACK) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (mDrawerLayout.isDrawerOpen(mOkrSelectByFragment.getView())) {
                        mDrawerLayout.closeDrawer(mOkrSelectByFragment.getView());
                    }
                    mKeyword = data.getStringExtra("keyword");
                    mListView.startFirst();
                    setRefresh();
                }
            }
        }
    }

    @Override
    public void SendMessageValue(String OrderCustom, String Order, String DepartmentId, String Year, String Month) {
        mKeyword = "";
        mDepartmentId = Integer.decode(DepartmentId);
        mOrderCustom = Integer.decode(OrderCustom);
        int order = Integer.decode(Order);
        if (order != 0) {
            orderByType = order;
        }
        if (orderByType == 7 || orderByType == 8) {
            setImageNull();
        } else {
            setImageNull();
            okrImg.setImageResource(R.mipmap.rank_down);
            okr_sign = 1;
        }
        mYear = Integer.decode(Year);
        mMonth = Integer.decode(Month);
        if (mDrawerLayout.isDrawerOpen(mOkrSelectByFragment.getView())) {
            mDrawerLayout.closeDrawer(mOkrSelectByFragment.getView());
        }
        mListView.startFirst();
        setRefresh();
    }
}
