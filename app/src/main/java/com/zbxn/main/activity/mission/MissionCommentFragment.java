package com.zbxn.main.activity.mission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pub.base.BaseFragment;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.CommendListViewAdapter;
import com.zbxn.main.entity.CommendListEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;




public class MissionCommentFragment extends BaseFragment {

    @BindView(R.id.mListView)
    MyListView mListView;
    private List<CommendListEntity> commendList;
    private CommendListViewAdapter commendListViewAdapter;
    private String ID;



    static MissionCommentFragment newInstance(){
        MissionCommentFragment myFragment = new MissionCommentFragment();
        return myFragment;
    }

    //动态设置listview高度
    public void setListViewHeightBasedOnChildren(ListView listView){

        if (commendListViewAdapter==null){
            return;
        }
        int totalHeight=0;
        for (int i = 0; i < commendListViewAdapter.getCount(); i++) {
            View listItem = commendListViewAdapter.getView(i, null, listView);
            listItem.measure(0,0);
            totalHeight+=listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height=totalHeight+(listView.getDividerHeight()*(commendListViewAdapter.getCount()-1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10,10,10,10);
        listView.setLayoutParams(params);

    }
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mission_comment, container, false);
        ButterKnife.bind(this, view);
        ID = getArguments().getString("ID");
        //评论列表
        commendList = new ArrayList<>();
        //先设置适配器
        commendListViewAdapter = new CommendListViewAdapter(getContext(), commendList);
        mListView.setAdapter(commendListViewAdapter);
        setListViewHeightBasedOnChildren(mListView);
        String ssid = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_SSID);
        String CurrentCompanyId = PreferencesUtils.getString(getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getMissionCommentList(ssid, CurrentCompanyId, ID, 1 + "", 100 + "");
        callRequest(call, new HttpCallBack(CommendListEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<CommendListEntity> tempList = mResult.getRows();
                    commendList.clear();
                    commendList.addAll(tempList);
                    commendListViewAdapter.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
        return view;
    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {

    }
    public void setFragmentTitle(String title) {
        mTitle = title;
    }



}
