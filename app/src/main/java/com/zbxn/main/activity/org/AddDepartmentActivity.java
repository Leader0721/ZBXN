package com.zbxn.main.activity.org;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.dbutils.DBUtils;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.AddDepartmentEntity;
import com.zbxn.main.entity.Contacts;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class AddDepartmentActivity extends BaseActivity {


    @BindView(R.id.et_branchname)
    EditText etBranchname;
    @BindView(R.id.et_describe)
    EditText etDescribe;
    @BindView(R.id.tv_branch)
    TextView tvBranch;
    @BindView(R.id.rll_delete)
    RelativeLayout rllDelete;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.ll_branch)
    LinearLayout llBranch;
    private String mHeigherBranchName;
    private String mParentID = "";
    private DBUtils<Contacts> mDBUtils;

    private String DepartmentName = "";//部门名称
    private String DepartmentDesc = "";//部门描述
    private String DepartmentID = "";//部门ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organize_addbranch);
        ButterKnife.bind(this);
        mParentID = getIntent().getStringExtra("id");
        mHeigherBranchName = getIntent().getStringExtra("name");
        DepartmentName = getIntent().getStringExtra("DepartmentName");
        DepartmentID = getIntent().getStringExtra("DepartmentID");
        DepartmentDesc = getIntent().getStringExtra("departmentName");


//        Contacts department = null;
//        try {
//            List<Contacts> listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("departmentId", "=", DepartmentID));
//            if (listTemp.size() != 0) {
//                department = listTemp.get(0);
//                DepartmentDesc = department.getDepartmentDesc();
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }


        if (StringUtils.isEmpty(mHeigherBranchName)) {
            tvBranch.setText("公司");
        } else {
            tvBranch.setText(mHeigherBranchName);
        }

        if (!StringUtils.isEmpty(DepartmentName) && !StringUtils.isEmpty(DepartmentID)) {
            rllDelete.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(DepartmentName)) {
                etBranchname.setText(DepartmentName);
            }
            if (!StringUtils.isEmpty(DepartmentDesc)) {
                etDescribe.setText(DepartmentDesc);
            }
            rllDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageDialog dialog = new MessageDialog(AddDepartmentActivity.this);
                    dialog.setMessage("确认删除此部门 ？");
                    dialog.setTitle("提示");
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String tokenid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
                            Call call = HttpRequest.getIResourceOANetAction().getDeleteDepartment(tokenid, CurrentCompanyId, Integer.parseInt(DepartmentID.trim()));
                            callRequest(call, new HttpCallBack(AddDepartmentEntity.class, AddDepartmentActivity.this, true) {
                                @Override
                                public void onSuccess(ResultData mResult) {
                                    if ("0".equals(mResult.getSuccess())) {
                                        MyToast.showToast("删除成功");
//                                mDBUtils.deleteById(DepartmentID);
                                        try {
                                            BaseApp.DBLoader.delete(Contacts.class, WhereBuilder.b("departmentId", "=", DepartmentID).and("isDepartment", "=", "1"));
                                        } catch (DbException e) {
                                            e.printStackTrace();
                                        }
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
                    });

                    dialog.show();

                }

            });
        }
        if (mDBUtils == null) {
            mDBUtils = new DBUtils<>(Contacts.class);
        }
        //选择部门点击事件
        llBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.setClass(AddDepartmentActivity.this, OrganizeChooseActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
    }


    @Override
    public void initRight() {
        if (!StringUtils.isEmpty(DepartmentName) && !StringUtils.isEmpty(DepartmentID)) {
            setTitle("编辑部门");
        } else {
            setTitle("新增部门");
        }
        setRight1Icon(R.mipmap.complete2);
        setRight1("保存");
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        DepartmentDesc = StringUtils.getEditText(etDescribe).trim();
        DepartmentName = StringUtils.getEditText(etBranchname).trim();
        if ("".equals(mParentID)) {
            mParentID = "0";
        }
        if (!StringUtils.isEmpty(DepartmentName) && !StringUtils.isEmpty(DepartmentID)) {
            //编辑部门

            String tokenid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
            Call call = HttpRequest.getIResourceOANetAction().getUpdateDepartment(tokenid, CurrentCompanyId, Integer.parseInt(mParentID.trim()), Integer.parseInt(DepartmentID.trim()), DepartmentName, DepartmentDesc);
            callRequest(call, new HttpCallBack(AddDepartmentEntity.class, AddDepartmentActivity.this, true) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {
                        MyToast.showToast("保存成功");
                        DepartmentDesc = StringUtils.getEditText(etDescribe);
                        DepartmentName = StringUtils.getEditText(etBranchname);

                        AddDepartmentEntity entityTemp = (AddDepartmentEntity) mResult.getData();
                        Contacts entity = new Contacts();
                        entity.setIsDepartment("1");
                        entity.setParentId(mParentID);
                        entity.setDepartmentId(entityTemp.getID() + "");
                        entity.setDepartmentName(DepartmentName);
                        entity.setDepartmentDesc(DepartmentDesc);
                        try {
                            BaseApp.DBLoader.update(entity, WhereBuilder.b("departmentId", "=", entityTemp.getID() + "").
                                    and("isDepartment", "=", "1"), "departmentName", "departmentDesc");
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
//                        mDBUtils.update(entity);

                        // department = BaseApp.DBLoader.findFirst(Selector.from(Contacts.class).where("departmentId", "=", mParentId));
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
        } else {
            //新增部门
            if (!validate()) {
                return;
            }
            addBranch(this, mParentID, DepartmentName, DepartmentDesc);
        }

    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {

        if (StringUtils.isEmpty(mHeigherBranchName)) {
            MyToast.showToast("请选择上级部门");
            return false;
        }

        if (StringUtils.isEmpty(etBranchname)) {
            MyToast.showToast("请填写部门名称");
            return false;
        }

        return true;
    }

    /**
     * 上传数据
     */
    public void addBranch(Context context, String mParentID, String DepartmentName, final String DepartmentDesc) {


        //新版数据请求  添加部门
        String tokenid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getAddDepartment(tokenid, CurrentCompanyId, mParentID, DepartmentName, DepartmentDesc);
        callRequest(call, new HttpCallBack(AddDepartmentEntity.class, context, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    MyToast.showToast("提交成功");
                    AddDepartmentEntity entityTemp = (AddDepartmentEntity) mResult.getData();
                    Contacts entity = new Contacts();
                    entity.setIsDepartment("1");
                    entity.setParentId(entityTemp.getParentID() + "");
                    entity.setDepartmentId(entityTemp.getDepartmentId() + "");
                    entity.setDepartmentName(entityTemp.getDepartmentName());
                    entity.setDepartmentDesc(DepartmentDesc);
                    mDBUtils.add(entity);
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


    // 获取部门请求结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                tvBranch.setText(data.getStringExtra("name"));
                mParentID = String.valueOf(data.getIntExtra("id", 0));
            }
        }

    }

}
