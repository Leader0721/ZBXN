package com.zbxn.main.activity.message;


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
import com.pub.bean.Bulletin;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.JsonUtil;
import com.pub.utils.KEY;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.approvalmodule.ApplyDetailActivity;
import com.zbxn.main.activity.mission.MissionDetailsActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.activity.schedule.ScheduleInfoActivity;
import com.zbxn.main.adapter.BulletinAdapter;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 消息列表Fragment
 */
public class MessageListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.listview_content)
    PullRefreshListView mListView;

    private Activity mActivity;
    private String mLabel = "";
    private String mKeyWord = "";

    private BulletinAdapter mAdapter;
    private List<Bulletin> mList;
    private int mPage = 1;
    private int mPageSize = 10;
    private static final int Bullet_CallBack = 10000;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.main_messagecenter, container, false);
        ButterKnife.bind(this, view);

        mLabel = getArguments().getString("label");
        mKeyWord = getArguments().getString("keyword");

        mActivity = getActivity();
        mList = new ArrayList<>();

        loadLocalCache();

        initView();

        mListView.startFirst();
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (eventCustom.getTag().equals(ScheduleActivity.SUCCESS2)) {
            setRefresh();
            mAdapter.notifyDataSetChanged();
        }

    }
    private void initView() {
        mAdapter = new BulletinAdapter(mList, mActivity);
        mListView.setDividerHide();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

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
        setRefresh();
    }

    public void setRefresh() {
        setRefresh(mLabel, mKeyWord);
    }

    public void setRefresh(String label, String keyword) {
        mLabel = label;
        mKeyWord = keyword;
        mPage = 1;
        getListData();
    }

    /**
     * 刷新
     */
    public void getListData() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().MessageCenterGetCount(ssid, mPage + "", mPageSize + "", mLabel, mKeyWord);
        callRequest(call, new HttpCallBack(Bulletin.class, mActivity, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                mListView.onRefreshFinish();
                if (mResult.getSuccess().equals("0")) {
                    List<Bulletin> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                        String json = JsonUtil.toJsonString(list);
                        //缓存数据到本地
                        PreferencesUtils.putString(BaseApp.getContext(), mLabel + KEY.MESSAGELIST, json);
                    }

                    if (StringUtils.isEmpty(list)) {
                        MyToast.showToast("暂无更多数据");
                    } else {
                        mPage++;
                    }

                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
                mListView.onRefreshFinish();
            }
        });
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        Bulletin item = mList.get(position);
        switch (item.getLabel()) {
            case 1://系统消息
                intent = new Intent(mActivity, MessageDetailActivity.class);
                intent.putExtra(MessageDetailActivity.Flag_Input_Bulletin, item);
                intent.putExtra("type", 1);
                setRead(position, "1");
                startActivity(intent);
                break;
            case 11://公告消息
                intent = new Intent(mActivity, MessageDetailActivity.class);
                intent.putExtra(MessageDetailActivity.Flag_Input_Bulletin, item);
                intent.putExtra("type", 11);
                setRead(position, "1");
                startActivityForResult(intent, Bullet_CallBack);
                break;
            case 25://日程管理
                intent = new Intent(mActivity, ScheduleInfoActivity.class);
                intent.putExtra("id", item.getRelatedid() + "");
                intent.putExtra("sign", 1);
                intent.putExtra("flag", -1);
                setRead(position, "1");
                startActivity(intent);
                break;
            case 31://审批管理
                intent = new Intent(mActivity, ApplyDetailActivity.class);
                intent.putExtra("approvalID", item.getRelatedid() + "");
                setRead(position, "1");
                startActivity(intent);
                break;
            case 32://任务管理
                intent = new Intent(mActivity, MissionDetailsActivity.class);
                intent.putExtra("id", item.getRelatedid() + "");
                setRead(position, "1");
                startActivity(intent);
                break;
            case 41://会议通知
                break;
            case 51://跟进评论
                break;
            case 55://评论回复
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == Bullet_CallBack) {
            setRefresh();
        }

    }

    /**
     * 将通知公告标记为已读或者未读    state 标志位 0：未读   1：已读
     *
     * @param position
     * @param state
     */
    public void setRead(int position, String state) {
        final Bulletin bulletin = mList.get(position);
        if (bulletin.isRead() == 1) {
            return;
        }

        // 提交服务器
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().MessageCenterSetRead(ssid, String.valueOf(bulletin.getId()), state, "");
        callRequest(call, new HttpCallBack(Bulletin.class, mActivity, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    bulletin.setRead(1);
                    mAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    /**
     * 将通知公告标记为已读或者未读    state 标志位 0：未读   1：已读
     *
     * @param state
     * @param label
     */
    public void setRead(String state, String label) {
        // 提交服务器
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().MessageCenterSetRead(ssid, "", state, label);
        callRequest(call, new HttpCallBack(Bulletin.class, mActivity, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setRead(1);
                    }
                    mAdapter.notifyDataSetChanged();
                    MyToast.showToast("标记全部已读成功");
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    /**
     * 加载本地缓存
     *
     * @return
     */
    public void loadLocalCache() {
        try {
            //获取本地缓存数据
            String messageList = PreferencesUtils.getString(mActivity, mLabel + KEY.MESSAGELIST, "[]");
            mList = JsonUtil.fromJsonList(messageList, Bulletin.class);
        } catch (Exception e) {
            mList = new ArrayList<>();
        }
    }
}
