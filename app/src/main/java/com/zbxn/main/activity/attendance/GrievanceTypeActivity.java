package com.zbxn.main.activity.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.GrievanceAdapter;
import com.zbxn.main.entity.GrievanceTypeEntity;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 考勤申诉类型
 * Created by Administrator on 2016/12/19.
 */
public class GrievanceTypeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.mListView)
    ListView mListView;
    private GrievanceAdapter mAdapter;
    private List<GrievanceTypeEntity> mList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_repeal);
        ButterKnife.bind(this);
        setTitle("申诉类型");
        initView();

    }

    private void initView() {
        mList = new ArrayList<>();
        getAppealType(this);
        mListView.setOnItemClickListener(this);
    }

    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    mList = (List<GrievanceTypeEntity>) obj1;
                    if (mList != null) {
                        mAdapter = new GrievanceAdapter(getApplicationContext(), mList);
                        mListView.setAdapter(mAdapter);
                    }
                    break;
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent data = new Intent();
        view.findViewById(R.id.mImageview).setVisibility(View.VISIBLE);
        String typeName = mList.get(position).getAppealTypeName();
        String appealType = mList.get(position).getAppealtype() + "";
        data.putExtra("typeName", typeName);
        data.putExtra("appealType", appealType);
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * 获取考勤申诉类型
     *
     * @param context
     * @param listener
     */
    public void getAppealType(Context context) {
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String currentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().GetAppealType(ssid, currentCompanyId);
        callRequest(call, new HttpCallBack(GrievanceTypeEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    mList.clear();
                    List<GrievanceTypeEntity> list = mResult.getRows();
                    mList.addAll(list);
                    mAdapter = new GrievanceAdapter(getApplicationContext(), mList);
                    mListView.setAdapter(mAdapter);
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
}
