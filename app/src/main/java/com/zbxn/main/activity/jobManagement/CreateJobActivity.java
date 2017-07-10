package com.zbxn.main.activity.jobManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.PermissionListAdapter;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.entity.PermissionEntity;
import com.zbxn.main.entity.PermissionIDEntity;
import com.zbxn.main.entity.TreeElement;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class CreateJobActivity extends BaseActivity implements AdapterView.OnItemClickListener, PermissionSelectedFragment.CallBackValue {

    @BindView(R.id.mlv_permission)
    ListView mlvPermission;
    EditText etJobName;
    EditText etDescribe;
    @BindView(R.id.m_drawerlayout)
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private PermissionSelectedFragment mPermissionSelectedFragment;
    public static final String  SUCCESS="Success";
    private int mPositionID;
    private String mDesc = "";
    private String mPerList = "";
    private String mPositionName;

    private PermissionListAdapter mAdapter;
    public static List<PermissionEntity> mList;
    private List<PermissionEntity> mListParent;
    private List<PermissionIDEntity> mListChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_job);
        ButterKnife.bind(this);
        mPositionID = getIntent().getIntExtra("PostionID", 0);
        mPositionName = getIntent().getStringExtra("PositionName");
        mDesc = getIntent().getStringExtra("Desc");

        initView();

        getPermissionIdList();

    }

    private void initView() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mPermissionSelectedFragment = (PermissionSelectedFragment) getSupportFragmentManager().findFragmentById(R.id.SelectBy_Fragment);

        mList = new ArrayList<>();
        mListParent = new ArrayList<>();
        View headView = LayoutInflater.from(this).inflate(R.layout.createjob_head, null);
        etJobName = (EditText) headView.findViewById(R.id.et_jobName);
        etDescribe = (EditText) headView.findViewById(R.id.et_describe);
        etJobName.setText(mPositionName);
        etDescribe.setText(mDesc);
        mlvPermission.addHeaderView(headView);
        mAdapter = new PermissionListAdapter(this, mListParent);
        mlvPermission.setAdapter(mAdapter);
        mlvPermission.setOnItemClickListener(this);

    }

    @Override
    public void initRight() {
        if (mPositionID == 0) {
            setTitle("新增职位");
        } else {
            setTitle("编辑职位");
        }
        setRight1("保存");
        setRight1Icon(R.mipmap.complete2);
        setRight1Show(true);
        super.initRight();
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        mPerList = "";
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isCheck()) {
                mPerList += mList.get(i).getID() + ",";
            }
        }
        if (mPerList.length() > 0) {
            mPerList = mPerList.substring(0, mPerList.length() - 1);
        }

        if (StringUtils.isEmpty(etJobName)) {
            MyToast.showToast("请填写职位名称");
        } else {
            if (mPositionID == 0) {
                //新增职位
                if (StringUtils.isEmpty(mPerList)){
                    //TODO 当前这个库的57，58这两个权限ParentId为 55，56，但后两者不存在，所以mPerlist里默认有"57,58",待解决
                    MyToast.showToast("请至少选择一个权限");
                }else {
                    newAddJob();
                    setResult(RESULT_OK);
                }

            } else {
                //编辑职位
                if (StringUtils.isEmpty(mPerList)){
                    MyToast.showToast("权限不能为空");
                }else {
                    compileJob();
                }

            }

        }


    }


    //新增职位
    private void newAddJob() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        mPositionName = StringUtils.getEditText(etJobName);
        mDesc = StringUtils.getEditText(etDescribe);
        if (StringUtils.isEmpty(mDesc)) {
            mDesc = "该部门没有描述，您可以稍后完善哦";
        }
        Call call = HttpRequest.getIResourceOANetAction().getaddPosition(ssid, currentCompanyId, mPositionName, mDesc, mPerList);
        callRequest(call, new HttpCallBack(JobEntity.class, CreateJobActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("添加成功");
                    finish();
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

    //编辑职位
    private void compileJob() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        mPositionName=StringUtils.getEditText(etJobName);
        mDesc = StringUtils.getEditText(etDescribe);
        if (StringUtils.isEmpty(mDesc)) {
            mDesc = "该部门没有描述，您可以稍后完善哦";
        }
        Call call = HttpRequest.getIResourceOANetAction().getUpdatePosition(ssid, currentCompanyId, mPositionID, mPositionName, mDesc, mPerList);
        callRequest(call, new HttpCallBack(JobEntity.class, CreateJobActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("编辑成功");
                    JobEntity entity = new JobEntity();
                    entity.setPositionName(mPositionName);
                    entity.setPositionDesc(mDesc);
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(CreateJobActivity.SUCCESS);
                    eventCustom.setObj(entity);
                    EventBus.getDefault().post(eventCustom);



                    finish();

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

    //获取权限列表
    private void getPermissionList() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getPositionPermissionList(ssid, currentCompanyId, 0);
        callRequest(call, new HttpCallBack(PermissionEntity.class, CreateJobActivity.this, true) {

            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    mList.clear();
                    List<PermissionEntity> listTemp = mResult.getRows();
                    for (int i = 0; i < listTemp.size(); i++) {
                        for (int j = 0; j < mListChecked.size(); j++) {
                            if (mListChecked.get(j).getID() == listTemp.get(i).getID()) {
                                listTemp.get(i).setCheck(true);
                            }
                        }
                        if (listTemp.get(i).getParentID() == 0) {
                            mListParent.add(listTemp.get(i));
                        }
                    }
                    mList.addAll(listTemp);

                    mAdapter.notifyDataSetChanged();
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

    //获取职位下权限ID列表
    private void getPermissionIdList() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getPermissionIDList(ssid, currentCompanyId, mPositionID);
        callRequest(call, new HttpCallBack(PermissionIDEntity.class, CreateJobActivity.this, true) {

            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    mListChecked = mResult.getRows();

                    getPermissionList();
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

    //    权限列表的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mDrawerLayout.isDrawerOpen(mPermissionSelectedFragment.getView())) {
            mDrawerLayout.closeDrawer(mPermissionSelectedFragment.getView());
        } else {
            List<PermissionEntity> listTemp = CreateJobActivity.mList;
            int count = 0;
            for (int i = 0; i < listTemp.size(); i++) {
                if (listTemp.get(i).getParentID() == mListParent.get(position - 1).getID()) {
                    count++;
                }
            }
            if (count > 0) {
                mDrawerLayout.openDrawer(mPermissionSelectedFragment.getView());
                mPermissionSelectedFragment.initData(mListParent.get(position - 1).getID(), CreateJobActivity.this);
            }

        }
    }

    @Override
    public void SendMessageValue(String str) {
        if ("CANCEL".equals(str)) {
            mDrawerLayout.closeDrawer(mPermissionSelectedFragment.getView());
        } else {
            mDrawerLayout.closeDrawer(mPermissionSelectedFragment.getView());
            mListParent.clear();
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).getParentID() == 0) {
                    int countCheck = 0;
                    for (int j = 0; j < mList.size(); j++) {
                        if (mList.get(i).getID() == mList.get(j).getParentID()) {
                            if (mList.get(j).isCheck()) {
                                countCheck++;
                            }
                        }
                    }
                    if (countCheck > 0) {
                        mList.get(i).setCheck(true);
                    } else {
                        for (int j = 0; j < mList.size(); j++) {
                            if (mList.get(i).getID() == mList.get(j).getParentID()) {
                                mList.get(i).setCheck(false);
                            }
                        }

                    }

                    mListParent.add(mList.get(i));
                }
            }

            mAdapter.notifyDataSetChanged();
        }

    }

    //点击返回键关闭
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(mPermissionSelectedFragment.getView())) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mDrawerLayout.closeDrawer(mPermissionSelectedFragment.getView());
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
