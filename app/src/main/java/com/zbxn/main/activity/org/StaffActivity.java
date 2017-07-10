package com.zbxn.main.activity.org;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.dbutils.DBUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.AddStaffEntity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.DepartmentPositionEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;


/**
 * 新增员工类
 * author:Huang
 * time:2016年12月27日10:22:51
 */
public class StaffActivity extends BaseActivity {

    int DepartmentPosition;
    int Gender = 1;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_branch1)
    TextView tvBranch1;
    @BindView(R.id.tv_job1)
    TextView tvJob1;
    @BindView(R.id.ll_extra1)
    LinearLayout llExtra1;
    @BindView(R.id.delete_child1)
    ImageView deleteChild1;
    @BindView(R.id.tv_branch2)
    TextView tvBranch2;
    @BindView(R.id.tv_job2)
    TextView tvJob2;
    @BindView(R.id.ll_extra2)
    LinearLayout llExtra2;
    @BindView(R.id.delete_child2)
    ImageView deleteChild2;
    @BindView(R.id.tv_branch3)
    TextView tvBranch3;
    @BindView(R.id.tv_job3)
    TextView tvJob3;
    @BindView(R.id.ll_extra3)
    LinearLayout llExtra3;
    @BindView(R.id.et_addjob)
    TextView etAddjob;
    @BindView(R.id.rll_add)
    LinearLayout rllAdd;
    @BindView(R.id.tv_branch1_right)
    TextView tvBranch1Right;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.tv_job_right)
    TextView tvJobRight;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.tv_branch2_right)
    TextView tvBranch2Right;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.tv_job2_right)
    TextView tvJob2Right;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.tv_branch3_right)
    TextView tvBranch3Right;
    @BindView(R.id.imageView8)
    ImageView imageView8;
    @BindView(R.id.tv_job3_right)
    TextView tvJob3Right;
    @BindView(R.id.layout_branch1)
    RelativeLayout layoutBranch1;
    @BindView(R.id.layout_job1)
    RelativeLayout layoutJob1;
    @BindView(R.id.layout_branch2)
    RelativeLayout layoutBranch2;
    @BindView(R.id.layout_job2)
    RelativeLayout layoutJob2;
    @BindView(R.id.layout_branch3)
    RelativeLayout layoutBranch3;
    @BindView(R.id.layout_job3)
    RelativeLayout layoutJob3;
    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.radio_woman)
    RadioButton radioWoman;
    @BindView(R.id.radio_staff)
    RadioGroup radioStaff;
    private String mDepartmentName1 = null;
    private String mDepartmentName2 = null;
    private String mDepartmentName3 = null;
    private int mDepartmentId1 = 0;
    private int mDepartmentId2 = 0;
    private int mDepartmentId3 = 0;
    private String mPositionName1 = null;
    private String mPositionName2 = null;
    private String mPositionName3 = null;
    private String mPositionId1 = null;
    private String mPositionId2 = null;
    private String mPositionId3 = null;
    List mList = new ArrayList<DepartmentPositionEntity>();
    private DBUtils<Contacts> mDBUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        ButterKnife.bind(this);
        String name = getIntent().getStringExtra("name");
        if ("".equals(name)) {
            tvBranch1Right.setText(name);
        }
        radioStaff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_man:
                        Gender = 1;//男
                        break;
                    case R.id.radio_woman:
                        Gender = 0;//女
                        break;
                }
            }
        });
        deleteChild1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llExtra2.setVisibility(View.GONE);
                mPositionId2 = null;
                mDepartmentId2 = 0;
                tvBranch2Right.setText("");
                tvJob2Right.setText("");
            }
        });


        deleteChild2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llExtra3.setVisibility(View.GONE);
                mPositionId2 = null;
                mDepartmentId3 = 0;
                deleteChild1.setVisibility(View.VISIBLE);
                deleteChild2.setVisibility(View.GONE);
                tvBranch3Right.setText("");
                tvJob3Right.setText("");
                rllAdd.setVisibility(View.VISIBLE);
            }
        });
        if (mDBUtils == null) {
            mDBUtils = new DBUtils<>(Contacts.class);
        }
    }


    @Override
    public void initRight() {
        setTitle("新增员工");
        setRight1("保存");
        setRight1Icon(R.mipmap.complete2);
        setRight1Show(true);
    }


    //    重写按钮的监听事件，上传新增员工资料
    @Override
    public void actionRight1(MenuItem menuItem) {
        if (validate1()) {
            addStaff(this, StringUtils.getEditText(etName), StringUtils.getEditText(etPsw), Gender, DepartmentPosition);
        }

    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate1() {

        if (StringUtils.isEmpty(etName)) {
            MyToast.showToast("请输入姓名");
            return false;
        }

        if (StringUtils.isEmpty(etPsw)) {
            MyToast.showToast("请输入密码");
            return false;
        }
        if (mDepartmentId1 == 0) {
            MyToast.showToast("请输入部门信息");
            return false;
        }
        if (StringUtils.isEmpty(mPositionId1)) {
            MyToast.showToast("请输入职位信息");
            return false;
        }

        if (llExtra2.getVisibility() == View.VISIBLE) {
            if (StringUtils.isEmpty(mPositionId2)) {
                MyToast.showToast("请填写职位信息");
                return false;
            }
            if (mDepartmentId2 == 0) {
                MyToast.showToast("请填写部门信息");
                return false;
            }
        }


        if (llExtra3.getVisibility() == View.VISIBLE) {
            if (StringUtils.isEmpty(mPositionId3)) {
                MyToast.showToast("请填写职位信息");
                return false;
            }
            if (mDepartmentId3 == 0) {

                if (llExtra3.getVisibility() == View.VISIBLE) {
                    MyToast.showToast("请填写部门信息");
                    return false;
                }
            }

        }


        return true;
    }

    /**
     * 添加兼职部门1前的验证
     */
    private boolean validate2() {
        if (mDepartmentId1 == 0) {
            MyToast.showToast("请输入部门信息");
            return false;
        }
        if (StringUtils.isEmpty(mPositionName1)) {
            MyToast.showToast("请输入职位信息");
            return false;
        }
        return true;
    }

    /**
     * 添加兼职部门2前的验证
     */
    private boolean validate3() {
        if (mDepartmentId2 == 0) {
            MyToast.showToast("请输入部门信息");
            return false;
        }
        if (StringUtils.isEmpty(mPositionName2)) {
            MyToast.showToast("请输入职位信息");
            return false;
        }
        return true;
    }

    /**
     * 上传员工资料验证1 &&
     */
    private boolean validate4() {
        if (mDepartmentId1 == 0) {
            return false;
        }
        if (StringUtils.isEmpty(mPositionId1)) {
            return false;
        }
        return true;
    }

    /**
     * 上传员工资料验证2 &&
     */
    private boolean validate5() {
        if (mDepartmentId2 == 0) {
            return false;
        }
        if (StringUtils.isEmpty(mPositionId2)) {
            return false;
        }
        return true;
    }

    /**
     * 上传员工资料验证3 &&
     */
    private boolean validate6() {
        if (mDepartmentId3 == 0) {
            return false;
        }
        if (StringUtils.isEmpty(mPositionId3)) {
            return false;
        }
        return true;
    }
    //上传员工资料的方法
    public void addStaff(Context context, final String UserName, final String PassWord, final int Gender, final int DepartmentPosition) {
        //mList 添加entity
        if (validate4()) {
            DepartmentPositionEntity entity = new DepartmentPositionEntity();
            entity.setUDPID("");
            entity.setPositionId(mPositionId1);
            Log.d("==mPositionId1====", mPositionId1 + "========");
            entity.setDepartmentId(mDepartmentId1 + "");
            mList.add(entity);
        }
        if (validate5()) {
            DepartmentPositionEntity entity = new DepartmentPositionEntity();
            entity.setUDPID("");
            entity.setPositionId(mPositionId2);
            entity.setDepartmentId(mDepartmentId2 + "");
            mList.add(entity);
        }
        if (validate6()) {
            DepartmentPositionEntity entity = new DepartmentPositionEntity();
            entity.setUDPID("");
            entity.setPositionId(mPositionId3);
            entity.setDepartmentId(mDepartmentId3 + "");
            mList.add(entity);
        }
        //新版数据上传
        Gson gson = new Gson();
        String tokenid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getAddStaff(tokenid, CurrentCompanyId, UserName, PassWord, Gender, gson.toJson(mList));
        callRequest(call, new HttpCallBack(AddStaffEntity.class, context, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("创建成功");
                    Contacts entity = new Contacts();
                    AddStaffEntity entityTemp = (AddStaffEntity) mResult.getData();
                    entity.setUserName(UserName);
                    int userID = entityTemp.getUserID();
                    entity.setId(userID);
                    entity.setGender(Gender);
                    entity.setDepartmentName(mDepartmentName1);
                    entity.setDepartmentId(mDepartmentId1 + "");
                    entity.setPositionName(mPositionName1);
                    entity.setPositionId(Integer.parseInt(mPositionId1));
                    entity.setIsactive(1);
                    List<Contacts> contactses = mDBUtils.queryAll();
                    Log.d("===qian===", contactses.size() + "======");
                    List<Contacts> list = new ArrayList<Contacts>();
                    list.add(entity);
                    Contacts[] array = new Contacts[list.size()];
                    list.toArray(array);
                    mDBUtils.add(array);
                    contactses = mDBUtils.queryAll();
                    Log.d("===hou===", contactses.size() + "======");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
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
    //onclick点击事件的监听事件
    private void method(Intent intent, int code) {
        intent.setClass(this, ChooseJobActivity.class);
        startActivityForResult(intent, code);
    }
    //    onclick1点击事件的部门部分公共部分提取出来作为方法
    private void method1(Intent intent, int code) {

        intent.setClass(this, OrganizeChooseActivity.class);
        startActivityForResult(intent, code);
    }
    @OnClick({R.id.layout_branch1, R.id.layout_branch2, R.id.layout_branch3, R.id.layout_job1, R.id.layout_job2, R.id.layout_job3, R.id.rll_add})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            //部门的点击事件
            case R.id.layout_branch1:
                if (mDepartmentId1!=0){
                    intent.putExtra("id", mDepartmentId1 + "");
                }else{
                    intent.putExtra("id", getIntent().getIntExtra("id", 0) + "");
                }
                method1(intent, 1001);
                break;
            case R.id.layout_branch2:
                if (mDepartmentId2!=0){
                    intent.putExtra("id", mDepartmentId2 + "");
                }else{
                    intent.putExtra("id", getIntent().getIntExtra("id", 0) + "");
                }
                method1(intent, 1002);
                break;
            case R.id.layout_branch3:
                if (mDepartmentId3!=0){
                    intent.putExtra("id", mDepartmentId3 + "");
                }else{
                    intent.putExtra("id", getIntent().getIntExtra("id", 0) + "");
                }
                method1(intent, 1003);
                break;
            //职位的点击事件
            case R.id.layout_job1:
                method(intent, 2001);
                break;
            case R.id.layout_job2:
                method(intent, 2002);
                break;
            case R.id.layout_job3:
                method(intent, 2003);
                break;
            //点击添加兼职部门
            case R.id.rll_add:
                //&&!StringUtils.isEmpty(tvBranch2Right.getText().toString()) &&!StringUtils.isEmpty(mPositionName2)
                //显示第三个
                if (llExtra2.getVisibility() == view.VISIBLE && llExtra3.getVisibility() == view.GONE) {
                    if (validate3()) {
                        llExtra3.setVisibility(View.VISIBLE);
                        deleteChild1.setVisibility(View.GONE);
                        deleteChild2.setVisibility(View.VISIBLE);
                        rllAdd.setVisibility(View.GONE);
                    }
                }
                //&&!StringUtils.isEmpty(tvBranch1Right.getText().toString()) &&!StringUtils.isEmpty(mPositionName1)
                //显示第二个
                if (llExtra2.getVisibility() == View.GONE) {
                    if (validate2()) {
                        llExtra2.setVisibility(View.VISIBLE);
                        deleteChild1.setVisibility(View.VISIBLE);
                        deleteChild2.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //部门
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            mDepartmentName1 = data.getStringExtra("name").trim();
            mDepartmentId1 = data.getIntExtra("id", 0);
            tvBranch1Right.setText(mDepartmentName1);
        }
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            mDepartmentName2 = data.getStringExtra("name").trim();
            mDepartmentId2 = data.getIntExtra("id", 0);
            tvBranch2Right.setText(mDepartmentName2);
        }
        if (requestCode == 1003 && resultCode == RESULT_OK) {
            mDepartmentName3 = data.getStringExtra("name").trim();
            mDepartmentId3 = data.getIntExtra("id", 0);
            tvBranch3Right.setText(mDepartmentName3);
        }
        //职位
        if (requestCode == 2001 && resultCode == RESULT_OK) {
            mPositionName1 = data.getStringExtra("positionName");
            mPositionId1 = data.getStringExtra("positionId");
            tvJobRight.setText(mPositionName1);
        }
        if (requestCode == 2002 && resultCode == RESULT_OK) {
            mPositionName2 = data.getStringExtra("positionName");
            mPositionId2 = data.getStringExtra("positionId");
            tvJob2Right.setText(mPositionName2);
        }
        if (requestCode == 2003 && resultCode == RESULT_OK) {
            mPositionName3 = data.getStringExtra("positionName");
            mPositionId3 = data.getStringExtra("positionId");
            tvJob3Right.setText(mPositionName3);
        }
    }
}
