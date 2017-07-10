package com.zbxn.main.activity.org;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.dialog.InputDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.CompileStaffEntity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.JobEntity;
import com.zbxn.main.entity.UserDeptPositionEntity;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class CompileStaffActivity extends BaseActivity {

    private static final int Flag_ChooseJob_CallBack1 = 10001;
    private static final int Flag_ChooseJob_CallBack2 = 10002;
    private static final int Flag_ChooseJob_CallBack3 = 10003;
    private static final int Flag_ChooseDepartment_CallBack1 = 20001;
    private static final int Flag_ChooseDepartment_CallBack2 = 20002;
    private static final int Flag_ChooseDepartment_CallBack3 = 20003;

    @BindView(R.id.et_name)
    TextView etName;
    @BindView(R.id.tv_mbranch1)
    TextView tvMbranch1;
    @BindView(R.id.tv_mjob1)
    TextView tvMjob1;
    @BindView(R.id.tv_mbranch2)
    TextView tvMbranch2;
    @BindView(R.id.tv_mjob2)
    TextView tvMjob2;
    @BindView(R.id.tv_mbranch3)
    TextView tvMbranch3;
    @BindView(R.id.tv_mjob3)
    TextView tvMjob3;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.mToggleButton)
    ToggleButton mToggleButton;
    @BindView(R.id.layout_second)
    LinearLayout layoutSecond;
    @BindView(R.id.layout_third)
    LinearLayout layoutThird;
    @BindView(R.id.addDepartment)
    LinearLayout addDepartment;
    @BindView(R.id.delete_child2)
    ImageView deleteChild2;
    @BindView(R.id.delete_child3)
    ImageView deleteChild3;
    @BindView(R.id.radio_staff)
    RadioGroup radioStaff;
    @BindView(R.id.ll_delete2)
    LinearLayout llDelete2;
    @BindView(R.id.ll_delete3)
    LinearLayout llDelete3;
    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.radio_woman)
    RadioButton radioWoman;

    private String positionName1;//职位名称
    private String positionName2;//第二职位名称
    private String positionName3;//第三职位名称
    private String positionId1;//职位id
    private String positionId2;//第二职位id
    private String positionId3;//第三职位id
    private String udpId1 = "";
    private String udpId2 = "";
    private String udpId3 = "";
    private String departmentName1;//部门名称
    private String departmentName2;
    private String departmentName3;
    private String departmentId1 = "";//部门
    private String departmentId2 = "";
    private String departmentId3 = "";

    private String userId;//当前所选人员id
    private boolean isOn;//记录开关状态
    private String mName;//名字
    private int mActive;//是否停用 1:启用 0:停用
    private int mGender = 1;//性别： 1男, 0女
    private List<UserDeptPositionEntity> mList;
    private String mId;
    private String mUserDeptPosition;//json字符串
    private String mDepartmentName;
    private String mPositionname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_compile_staff);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("id");
        mName = getIntent().getStringExtra("name");
        mGender = getIntent().getIntExtra("gender", 1);
        mActive = getIntent().getIntExtra("isActive", 1);
        mDepartmentName = getIntent().getStringExtra("departmentName");
        mPositionname = getIntent().getStringExtra("positionName");
        etName.setText(mName);
        tvMbranch1.setText(mDepartmentName);
        tvMjob1.setText(mPositionname);
        if (mGender == 1) {//男
            radioMan.setChecked(true);
        } else {
            radioWoman.setChecked(true);
        }

        if (mActive == 1) {//启用
            mToggleButton.setToggleOff();
        } else {
            mToggleButton.setToggleOn();
        }

        getDepartmentJob();
        mList = new ArrayList<>();
        mToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    postStopEmployee(0);//停用
                } else {
                    postStopEmployee(1);//启用
                }
                isOn = on;
            }
        });
        radioStaff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_man:
                        mGender = 1;//男
                        break;
                    case R.id.radio_woman:
                        mGender = 0;//女
                        break;
                }
            }
        });
    }

    @Override
    public void initRight() {
        setTitle("编辑资料");
        setRight1("保存");
        setRight1Icon(R.mipmap.complete2);
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        if (StringUtils.isEmpty(etName)) {
            MyToast.showToast("请输入姓名");
        }
        mList.clear();
        UserDeptPositionEntity entity = new UserDeptPositionEntity();
        if (StringUtils.isEmpty(tvMbranch1)) {
            MyToast.showToast("请选择部门");
        }
        if (StringUtils.isEmpty(tvMjob1)) {
            MyToast.showToast("请选择职位");
        }
        entity.setPositionId(positionId1);
        entity.setDepartmentId(departmentId1);
        entity.setUDPID(udpId1);
        mList.add(entity);
        if (layoutSecond.getVisibility() == View.VISIBLE) {
            if (StringUtils.isEmpty(tvMbranch2)) {
                MyToast.showToast("请选择部门");
            }
            if (StringUtils.isEmpty(tvMjob2)) {
                MyToast.showToast("请选择职位");
            }
            UserDeptPositionEntity entity2 = new UserDeptPositionEntity();
            entity2.setPositionId(positionId2);
            entity2.setDepartmentId(departmentId2);
            entity2.setUDPID(udpId2);
            mList.add(entity2);
        }
        if (layoutThird.getVisibility() == View.VISIBLE) {
            if (StringUtils.isEmpty(tvMbranch3)) {
                MyToast.showToast("请选择部门");
            }
            if (StringUtils.isEmpty(tvMjob3)) {
                MyToast.showToast("请选择职位");
            }
            UserDeptPositionEntity entity3 = new UserDeptPositionEntity();
            entity3.setPositionId(positionId3);
            entity3.setDepartmentId(departmentId3);
            entity3.setUDPID(udpId3);
            mList.add(entity3);
        }
        Gson gson = new Gson();
        mUserDeptPosition = gson.toJson(mList);
        postSave();
    }


    @OnClick({R.id.ll_name, R.id.ll_branch1, R.id.ll_branch2, R.id.ll_branch3, R.id.ll_job, R.id.ll_job2, R.id.ll_job3, R.id.et_addjob, R.id.delete_child2, R.id.delete_child3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_name://姓名
                final InputDialog inputDialog = new InputDialog(this);
                inputDialog.setTitle("请输入用户名");
                inputDialog.setNegativeButton("取消", null);
                inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = inputDialog.getEditText().getText().toString();
                        etName.setText(content);
                    }
                });
                inputDialog.getEditText().setSelectAllOnFocus(true);
                inputDialog.show(StringUtils.getEditText(etName));
                break;
            case R.id.ll_branch1://获取第一部门
                Intent intent1 = new Intent(this, OrganizeChooseActivity.class);
                intent1.putExtra("id", mId);
                startActivityForResult(intent1, Flag_ChooseDepartment_CallBack1);
                break;
            case R.id.ll_branch2:
                Intent intent2 = new Intent(this, OrganizeChooseActivity.class);
                intent2.putExtra("id", mId);
                startActivityForResult(intent2, Flag_ChooseDepartment_CallBack2);
                break;
            case R.id.ll_branch3:
                Intent intent3 = new Intent(this, OrganizeChooseActivity.class);
                intent3.putExtra("id", mId);
                startActivityForResult(intent3, Flag_ChooseDepartment_CallBack3);
                break;
            case R.id.ll_job://获取第一职位
                Intent intentJob1 = new Intent(this, ChooseJobActivity.class);
                startActivityForResult(intentJob1, Flag_ChooseJob_CallBack1);
                break;
            case R.id.ll_job2://获取第一职位
                Intent intentJob2 = new Intent(this, ChooseJobActivity.class);
                startActivityForResult(intentJob2, Flag_ChooseJob_CallBack2);
                break;
            case R.id.ll_job3://获取第一职位
                Intent intentJob3 = new Intent(this, ChooseJobActivity.class);
                startActivityForResult(intentJob3, Flag_ChooseJob_CallBack3);
                break;
            case R.id.et_addjob://添加职位
                if (layoutSecond.getVisibility() == View.GONE) {
                    if (!StringUtils.isEmpty(tvMbranch1) && !StringUtils.isEmpty(tvMjob1)) {
                        layoutSecond.setVisibility(View.VISIBLE);
                    } else {
                        MyToast.showToast("请完善以上信息");
                    }
                } else {
                    if (layoutThird.getVisibility() == View.GONE) {
                        if (!StringUtils.isEmpty(tvMbranch2) && !StringUtils.isEmpty(tvMjob2)) {
                            layoutThird.setVisibility(View.VISIBLE);
                            llDelete2.setVisibility(View.GONE);
                            addDepartment.setVisibility(View.GONE);
                        } else {
                            MyToast.showToast("请完善以上信息");
                        }
                    }
                }
                break;
            case R.id.delete_child2://删除第二组
                if (layoutThird.getVisibility() == View.VISIBLE) {//如果第三组已显示，则赋值到第二组
                    addDepartment.setVisibility(View.VISIBLE);
                    layoutThird.setVisibility(View.GONE);
                    tvMbranch2.setText(StringUtils.getEditText(tvMbranch3));
                    tvMjob2.setText(StringUtils.getEditText(tvMjob3));
                    tvMbranch3.setText("");
                    tvMjob3.setText("");
                    departmentId3 = "";
                } else {
                    layoutSecond.setVisibility(View.GONE);
                    if (layoutSecond.getVisibility() == View.GONE || layoutThird.getVisibility() == View.GONE) {
                        addDepartment.setVisibility(View.VISIBLE);
                    }
                    tvMbranch2.setText("");
                    tvMjob2.setText("");
                    departmentId2 = "";
                }
                break;
            case R.id.delete_child3://删除第三组
                layoutThird.setVisibility(View.GONE);
                if (layoutSecond.getVisibility() == View.GONE || layoutThird.getVisibility() == View.GONE) {
                    addDepartment.setVisibility(View.VISIBLE);
                    llDelete2.setVisibility(View.VISIBLE);
                }
                tvMbranch3.setText("");
                tvMjob3.setText("");
                departmentId3 = "";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Flag_ChooseJob_CallBack1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    positionId1 = data.getStringExtra("positionId");
                    positionName1 = data.getStringExtra("positionName");

                    tvMjob1.setText(positionName1);
                }
            }
        }
        if (requestCode == Flag_ChooseJob_CallBack2) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    positionId2 = data.getStringExtra("positionId");
                    positionName2 = data.getStringExtra("positionName");

                    tvMjob2.setText(positionName2);
                }
            }
        }
        if (requestCode == Flag_ChooseJob_CallBack3) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    positionId3 = data.getStringExtra("positionId");
                    positionName3 = data.getStringExtra("positionName");

                    tvMjob3.setText(positionName3);
                }
            }
        }
        if (requestCode == Flag_ChooseDepartment_CallBack1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    departmentName1 = data.getStringExtra("name");
                    int id = data.getIntExtra("id", 0);
                    if (id != 0) {
                        departmentId1 = id + "";
                    } else {
                        departmentId1 = "";
                    }

                    tvMbranch1.setText(departmentName1);
                }
            }
        }
        if (requestCode == Flag_ChooseDepartment_CallBack2) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    departmentName2 = data.getStringExtra("name");
                    int id = data.getIntExtra("id", 0);
                    if (id != 0) {
                        departmentId2 = id + "";
                    } else {
                        departmentId2 = "";
                    }

                    tvMbranch2.setText(departmentName2);
                }
            }
        }
        if (requestCode == Flag_ChooseDepartment_CallBack3) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    departmentName3 = data.getStringExtra("name");
                    int id = data.getIntExtra("id", 0);
                    if (id != 0) {
                        departmentId3 = id + "";
                    } else {
                        departmentId3 = "";
                    }

                    tvMbranch3.setText(departmentName3);
                }
            }
        }
    }


    /**
     * 保存修改信息
     */
    public void postSave() {
        final String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        final String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getCompileStaff(ssid, currentCompanyId, userId, mGender + "", StringUtils.getEditText(etName), mUserDeptPosition);
        callRequest(call, new HttpCallBack(JobEntity.class, CompileStaffActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("修改成功");
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

    /**
     * 停用员工请求
     *
     * @param isActive 1:启用 0:停用
     */
    public void postStopEmployee( int isActive) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);


        Call call = HttpRequest.getIResourceOANetAction().getStopStaff(ssid, currentCompanyId, userId, isActive + "");
        callRequest(call, new HttpCallBack(Contacts.class, CompileStaffActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("修改成功");
                    Contacts entity = (Contacts) mResult.getData();
                    int isactive = entity.getIsactive();
                    Log.d("====isActive====", isactive + "");
                    try {
                        List<Contacts> listTemp;
                        BaseApp.DBLoader.update(entity, WhereBuilder.b("id", "=",userId),"isactive");
                        listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("userName", "=",mName));
                        int isactive1 = listTemp.get(0).getIsactive();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }


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

    /**
     * 获取user的职位部门
     */
    public void getDepartmentJob() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        Call call = HttpRequest.getIResourceOA().getUserPosition(ssid, userId);
        callRequest(call, new HttpCallBack(CompileStaffEntity.class, CompileStaffActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    List<CompileStaffEntity> list = (List<CompileStaffEntity>) mResult.getRows();
                    if (list != null) {
                        switch (list.size()) {
                            case 1:
                                tvMbranch1.setText(list.get(0).getDepartmentName());
                                tvMjob1.setText(list.get(0).getPositionName());
                                positionId1 = list.get(0).getPositionid() + "";
                                departmentId1 = list.get(0).getDepartmentid() + "";
                                udpId1 = list.get(0).getId() + "";
                                break;
                            case 2:
                                tvMbranch1.setText(list.get(0).getDepartmentName());
                                tvMjob1.setText(list.get(0).getPositionName());
                                layoutSecond.setVisibility(View.VISIBLE);
                                tvMbranch2.setText(list.get(1).getDepartmentName());
                                tvMjob2.setText(list.get(1).getPositionName());
                                positionId1 = list.get(0).getPositionid() + "";
                                positionId2 = list.get(1).getPositionid() + "";
                                udpId1 = list.get(0).getId() + "";
                                udpId2 = list.get(1).getId() + "";
                                departmentId1 = list.get(0).getDepartmentid() + "";
                                departmentId2 = list.get(1).getDepartmentid() + "";
                                break;
                            case 3:
                                llDelete2.setVisibility(View.GONE);
                                tvMbranch1.setText(list.get(0).getDepartmentName());
                                tvMjob1.setText(list.get(0).getPositionName());
                                layoutSecond.setVisibility(View.VISIBLE);
                                tvMbranch2.setText(list.get(1).getDepartmentName());
                                tvMjob2.setText(list.get(1).getPositionName());
                                layoutThird.setVisibility(View.VISIBLE);
                                tvMbranch3.setText(list.get(2).getDepartmentName());
                                tvMjob3.setText(list.get(2).getPositionName());
                                addDepartment.setVisibility(View.GONE);
                                positionId1 = list.get(0).getPositionid() + "";
                                positionId2 = list.get(1).getPositionid() + "";
                                positionId3 = list.get(2).getPositionid() + "";
                                udpId1 = list.get(0).getId() + "";
                                udpId2 = list.get(1).getId() + "";
                                udpId3 = list.get(2).getId() + "";
                                departmentId1 = list.get(0).getDepartmentid() + "";
                                departmentId2 = list.get(1).getDepartmentid() + "";
                                departmentId3 = list.get(2).getDepartmentid() + "";
                                break;
                            default:
                                break;
                        }
                    }

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


