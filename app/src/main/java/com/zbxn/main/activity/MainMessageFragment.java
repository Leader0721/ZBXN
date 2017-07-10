package com.zbxn.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseApp;
import com.pub.base.BaseFragment;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.JsonUtil;
import com.pub.utils.KEY;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.message.MessageListActivity;
import com.zbxn.main.adapter.MessageCenterAdapter;
import com.zbxn.main.entity.OaAlertEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 主页-消息界面
 * Created by Administrator on 2016/12/26.
 */
public class MainMessageFragment extends BaseFragment {

    @BindView(R.id.listView_MessageCenter)
    ListView listViewMessageCenter;
    private List<OaAlertEntity> mLists;
    private MessageCenterAdapter mMessageCenterAdapter;

    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.activity_messagecenter, container, false);
        ButterKnife.bind(this, root);
        try {
            //获取本地缓存数据    KEY.MESSAGELISTUNREAD
            String messageList = PreferencesUtils.getString(BaseApp.CONTEXT, KEY.MESSAGELISTUNREAD, "[]");
            mLists = JsonUtil.fromJsonList(messageList, OaAlertEntity.class);
        } catch (Exception e) {
            mLists = new ArrayList<>();
        }

        mMessageCenterAdapter = new MessageCenterAdapter(mLists, getContext());
        listViewMessageCenter.setAdapter(mMessageCenterAdapter);
        listViewMessageCenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageListActivity.class);
                intent.putExtra("title",mLists.get(position).getTitle());
                intent.putExtra("label", mLists.get(position).getLabel() + "");
                intent.putExtra("keyword", "");
                startActivity(intent);
            }
        });
        findBaseUserOaAlertUnRead();
        return root;
    }

    @Override
    public void onResume() {
        findBaseUserOaAlertUnRead();
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            findBaseUserOaAlertUnRead();
        } else {
            //相当于Fragment的onPause
        }
    }

    /**
     * 获取未读消息列表
     */
    public void findBaseUserOaAlertUnRead() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().FindBaseUserOaAlertUnRead(ssid);
        callRequest(call, new HttpCallBack(OaAlertEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    //显示加载完成
                    if (!StringUtils.isEmpty(mResult.getRows())) {
                        mLists.clear();
                        mLists.addAll(mResult.getRows());
                        String json = JsonUtil.toJsonString(mLists);
                        //缓存数据到本地
                        PreferencesUtils.putString(BaseApp.CONTEXT, KEY.MESSAGELISTUNREAD, json);

                        mMessageCenterAdapter = new MessageCenterAdapter(mLists, getContext());
                        listViewMessageCenter.setAdapter(mMessageCenterAdapter);
                    }
                } else {
                    //显示加载失败
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                //显示加载失败
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }
}
