package com.zbxn.main.activity.org;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.ChooseJobAdapter;
import com.zbxn.main.entity.JobEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 *
 */
public class ChooseJobActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mListView)
    ListView mListView;

    private List<JobEntity> mList;
    private ChooseJobAdapter mAdapter;


    @Override
    public void initRight() {
        setTitle("选择职位");

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_choose_job);
        ButterKnife.bind(this);
        initView();
        getJobData();
        //显示加载成功界面

    }

    private void initView() {
        mList = new ArrayList<>();
        mAdapter = new ChooseJobAdapter(getApplicationContext(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    //列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.findViewById(R.id.mImageview).setSelected(true);
        String positionName = mList.get(position).getPositionName();
        String positionId = mList.get(position).getPositionID() + "";
        Intent data = new Intent();
        data.putExtra("positionName", positionName);
        data.putExtra("positionId", positionId);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 获取当前公司职位列表
     */
    public void getJobData() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);

        Call call = HttpRequest.getIResourceOANetAction().getChooseJob(ssid, currentCompanyId);
        callRequest(call, new HttpCallBack(JobEntity.class, ChooseJobActivity.this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
//                    MyToast.showToast("修改成功");
                    List<JobEntity> list1 = mResult.getRows();
                    mList.clear();
                    mList.addAll(list1);
                    mAdapter.notifyDataSetChanged();
//                    finish();
                } else {
                    MyToast.showToast(mResult.getSuccess());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });

//        Map<String, String> map = new HashMap<>();
//        map.put("tokenid", ssid);
//        map.put("CurrentCompanyId", currentCompanyId);
//        String netServer = ConfigUtils.getConfig(ConfigUtils.KEY.NetServer_ACTION);
//        new BaseAsyncTask(this, false, 0, netServer + "Position/getPositionList", new BaseAsyncTaskInterface() {
//            @Override
//            public void dataSubmit(int funId) {
//
//            }
//
//            @Override
//            public Object dataParse(int funId, String json) throws Exception {
//                return new ResultData<JobEntity>().parse(json, JobEntity.class);
//            }
//
//            @Override
//            public void dataSuccess(int funId, Object result) {
//                ResultData mResult = (ResultData) result;
//                if (mResult.getSuccess().equals("0")) {
//                    List<JobEntity> list1 = mResult.getRows();
//                    mList.clear();
//                    mList.addAll(list1);
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    MyToast.showToast(mResult.getMsg());
//                }
//            }
//
//            @Override
//            public void dataError(int funId) {
//                MyToast.showToast("获取网络数据失败");
//            }
//        }).execute(map);
    }
}
