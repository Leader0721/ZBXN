package com.zbxn.main.activity.memberCenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.widget.dbutils.DBUtils;
import com.zbxn.R;
import com.zbxn.main.activity.MainActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.SwitchCompanyAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.RepeatNewTaskEntity;
import com.zbxn.main.entity.ZmsCompanyListEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/22.
 */
public class SwitchCompanyActivity extends BaseActivity {

    @BindView(R.id.mListView)
    ListView mListView;

    private SwitchCompanyAdapter mAdapter;
    private List<ZmsCompanyListEntity> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switchcompany);
        ButterKnife.bind(this);

        setTitle("切换公司");

        mList = new ArrayList<>();

        initView();

        findCompany();
    }

    @Override
    public void initRight() {
        setRight1Show(false);
        setRight2Show(false);
        super.initRight();
    }

    private void initView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String zmsCompanyId = PreferencesUtils.getString(SwitchCompanyActivity.this, LoginActivity.FLAG_ZMSCOMPANYID);
                if (zmsCompanyId.equals(mList.get(i).getId())) {
                    Toast.makeText(SwitchCompanyActivity.this, "已是当前公司，不需切换", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateDefaultCompany(zmsCompanyId, mList.get(i).getId());
                DBUtils<Contacts> mDBUtils = new DBUtils<>(Contacts.class);
                mDBUtils.deleteAll();
            }
        });
    }

    /**
     * 查看考勤记录
     */
    public void findCompany() {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        Call call = HttpRequest.getIResourceOA().FindCompany(ssid);
        callRequest(call, new HttpCallBack(ZmsCompanyListEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    List<ZmsCompanyListEntity> list = mResult.getRows();
                    mList.addAll(list);
                    if (mList != null) {
                        mAdapter = new SwitchCompanyAdapter(getApplicationContext(), mList);
                        mListView.setAdapter(mAdapter);
                    }
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
     * 切换公司
     *
     * @param zmsCompanyIdOld
     * @param zmsCompanyIdNow
     */
    public void updateDefaultCompany(String zmsCompanyIdOld, final String zmsCompanyIdNow) {
        //请求网络
        Map<String, String> map = new HashMap<String, String>();
        //将用户输入的账号保存起来，以便下次使用
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("zmsCompanyIdOld", zmsCompanyIdOld);//初始化公司id
        map.put("zmsCompanyIdNow", zmsCompanyIdNow);//切换的公司id
        Call call = HttpRequest.getIResourceOA().SwitchCompany(ssid, zmsCompanyIdOld, zmsCompanyIdNow);
        callRequest(call, new HttpCallBack(RepeatNewTaskEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    PreferencesUtils.putString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, zmsCompanyIdNow);

                    Intent intent = new Intent(BaseApp.getContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("isSwitchCompany", true);
                    BaseApp.getContext().startActivity(intent);
                    finish();
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
