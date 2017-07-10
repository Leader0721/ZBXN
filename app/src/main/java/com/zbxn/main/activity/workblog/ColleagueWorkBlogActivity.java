package com.zbxn.main.activity.workblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.activity.schedule.WorkmateScheduleActivity;
import com.zbxn.main.adapter.WorkmateAdapter;
import com.zbxn.main.entity.ScheduleDetailEntity;
import com.zbxn.main.entity.WorkmateEntity;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class ColleagueWorkBlogActivity extends BaseActivity {

    @BindView(R.id.mListView)
    PullRefreshListView mListView;
    List<WorkmateEntity> mList = new ArrayList<WorkmateEntity>();
    private WorkmateAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleague_work_blog);
        ButterKnife.bind(this);
        setTitle("同事列表");
        initView();
        mListView.startFirst();
        getOtherUserList();
    }
    private void initView() {
        adapter = new WorkmateAdapter(this, mList);
        mListView.setAdapter(adapter);
        mListView.setPullLoadEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkmateEntity entity = mList.get(position);
                Intent intent = new Intent();
                intent.putExtra("entity",entity);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                getOtherUserList();
            }

            @Override
            public void onLoad() {

            }
        });
    }

    //获取日志同事列表
    public void getOtherUserList() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getOtherUserList(ssid);
        callRequest(call, new HttpCallBack(WorkmateEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                mListView.onRefreshFinish();
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mList.clear();
                    List<WorkmateEntity> list = mResult.getRows();
                    mList.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

}
