package com.zbxn.main.activity.memberCenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.bean.Bulletin;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.message.MessageDetailActivity;
import com.zbxn.main.activity.mission.MissionActivity;
import com.zbxn.main.adapter.BulletinAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/22.
 */
public class CollectCenterActivity extends BaseActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.listview_content)
    PullRefreshListView mListView;
    private ProgressDialog progressDialog;
    private BulletinAdapter mAdapter;
    private List<Bulletin> mList;
    private int pageSize = 10;
    private int m_index;
    private int rows;
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_collect);
        ButterKnife.bind(this);
        setTitle("我的收藏");

        progressDialog = new ProgressDialog(this);
        initView();

    }

    private void initView() {
        mList = new ArrayList<>();
        String json = PreferencesUtils.getString(BaseApp.CONTEXT, "COLLECT", "[]");
        List<Bulletin> list = JsonUtil.fromJsonList(json, Bulletin.class);
        mList.addAll(list);
        mAdapter = new BulletinAdapter(mList, this);
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

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        setRefresh();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MessageDetailActivity.class);
        intent.putExtra(MessageDetailActivity.Flag_Input_Bulletin, mList.get(position));
        intent.putExtra("position", position);
        intent.putExtra("type", mList.get(position).getLabel());
        startActivityForResult(intent, 1001);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        MessageDialog messageDialog = new MessageDialog(CollectCenterActivity.this);
        messageDialog.setTitle("提示");
        messageDialog.setMessage("是否确认删除?");
        messageDialog.setNegativeButton("取消");
        messageDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelCollect(position);
            }
        });
        messageDialog.show();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                if (data != null) {
                    setRefresh();
                }
                return;
            default:
                break;
        }
    }

    /**
     * 获取收藏列表
     *
     * @param context
     * @param rows
     */
    public void getIntegralDetail(Context context, int page, int rows) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().GetCollectionList(ssid, page + "", pageSize + "");
        callRequest(call, new HttpCallBack(Bulletin.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<Bulletin> list = mResult.getRows();
                    if (m_index == 1) {
                        String json = JsonUtil.toJsonString(list);
                        //缓存数据到本地
                        PreferencesUtils.putString(BaseApp.CONTEXT, "COLLECT", json);
                        mList.clear();
                    }
                    m_index++;
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                    setMore(list);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                    mListView.onRefreshFinish();
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
                mListView.onRefreshFinish();
            }
        });
    }


    /**
     * 取消收藏
     *
     * @param position
     */
    public void cancelCollect(final int position) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Bulletin bulletin = mAdapter.getItem(position);
        Call call = HttpRequest.getIResourceOA().CancelCollect(ssid, bulletin.getId() + "", "false");
        callRequest(call, new HttpCallBack(MissionActivity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mList.remove(position);
                    mAdapter.notifyDataSetChanged();
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
}
