package com.zbxn.crm.activity.custom;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.dialog.MessageDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.main.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/13.
 */
public class CustomerInfoActivity extends BaseActivity {
    @BindView(R.id.tt_customer)
    TextView ttCustomer;
    @BindView(R.id.tt_name)
    TextView ttName;
    @BindView(R.id.tt_phone)
    TextView ttPhone;
    @BindView(R.id.tt_state)
    TextView ttState;
    @BindView(R.id.tt_province)
    TextView ttProvince;
    @BindView(R.id.tt_address)
    TextView ttAddress;
    @BindView(R.id.tt_telephone)
    TextView ttTelephone;
    @BindView(R.id.tt_fax)
    TextView ttFax;
    @BindView(R.id.tt_http)
    TextView ttHttp;
    @BindView(R.id.tt_email)
    TextView ttEmail;
    @BindView(R.id.tt_pool)
    TextView ttPool;
    @BindView(R.id.tt_source)
    TextView ttSource;
    @BindView(R.id.tt_industry)
    TextView ttIndustry;
    @BindView(R.id.tt_follower)
    TextView ttFollower;
    @BindView(R.id.tt_createtime)
    TextView ttCreatetime;
    @BindView(R.id.tt_createman)
    TextView ttCreateman;
    @BindView(R.id.tt_updatetime)
    TextView ttUpdatetime;
    @BindView(R.id.tt_remark)
    TextView ttRemark;
//    @BindView(R.id.linear_phoneNum)
//    LinearLayout linearPhoneNum;
//    @BindView(R.id.linear_telephoneNum)
//    LinearLayout linearTelephoneNum;
//    @BindView(R.id.linear_fax)
//    LinearLayout linearFax;
//    @BindView(R.id.linear_http)
//    LinearLayout linearHttp;
//    @BindView(R.id.linear_email)
//    LinearLayout linearEmail;
    private String province;
    private String city;
    private String region;
    private String ID;
    private int followerId;
    private String sourceId;
    private String poolId;
    private String industryId;
    private String stateId;
    private String provinceId;
    private String cityId;
    private String regionId;
    private static final int Edit_CallBack = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customerinfo);
        ButterKnife.bind(this);
        setTitle("客户信息");
        ID = CustomActivity.CUSTOMID;
        initView();
    }


    private void initView() {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getCustomerDetail(ssid, CurrentCompanyId, ID);
        callRequest(call, new HttpCallBack(CustomEntity.class, CustomerInfoActivity.this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    CustomEntity entity = (CustomEntity) mResult.getData();
                    if (entity != null) {
                        ttCustomer.setText(entity.getCustName());
                        ttName.setText(entity.getName());
                        ttPhone.setText(entity.getMobile());
                        ttState.setText(entity.getCustStateName());
                        String area = "";
                        if (!StringUtils.isEmpty(entity.getProvinceName())) {
                            area = entity.getProvinceName();
                        }
                        if (!StringUtils.isEmpty(entity.getProvinceName()) && !StringUtils.isEmpty(entity.getCityName())) {
                            area = entity.getProvinceName() + "/" + entity.getCityName();
                        }
                        if (!StringUtils.isEmpty(entity.getProvinceName()) && !StringUtils.isEmpty(entity.getCityName()) && !StringUtils.isEmpty(entity.getAreaName())) {
                            area = entity.getProvinceName() + "/" + entity.getCityName() + "/" + entity.getAreaName();
                        }
                        ttProvince.setText(area);
                        ttAddress.setText(entity.getAddress());
                        ttTelephone.setText(entity.getTelephone());
                        ttFax.setText(entity.getFax());
                        ttHttp.setText(entity.getWebSite());
                        ttEmail.setText(entity.getEmail());
                        ttPool.setText(entity.getCustPublicPoolName());
                        ttSource.setText(entity.getSourceName());
                        ttIndustry.setText(entity.getIndustryName());
                        ttFollower.setText(entity.getFollowUserName());
                        followerId = entity.getFollowUser();
                        ttCreatetime.setText(entity.getCreateTimeStr());
                        ttCreateman.setText(entity.getCreateUserName());
                        ttUpdatetime.setText(entity.getUpdateTimeStr());

                        sourceId = entity.getSource();
                        poolId = entity.getCustPublicPool();
                        industryId = entity.getIndustry();
                        stateId = entity.getCustState();
                        provinceId = entity.getProvince();
                        cityId = entity.getCity();
                        regionId = entity.getArea();
                        ttRemark.setText(entity.getRemark());
                        province = entity.getProvinceName();
                        city = entity.getCityName();
                        region = entity.getAreaName();
                    } else {
                        return;
                    }

                } else {
                    MyToast.showToast("获取数据失败，请重试");
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });

    }


    @Override
    public void initRight() {
        setRight2Show(false);
        setRight1Show(true);
        setRight1Icon(R.mipmap.write);
        setRight1("确定");
        super.initRight();
    }

    // 获取部门请求结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Edit_CallBack) {
            setResult(RESULT_OK);
            finish();
        }

    }


    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        Intent intent = new Intent(CustomerInfoActivity.this, EditDetailActivity.class);
        intent.putExtra("ID", ID);
        intent.putExtra("client", ttCustomer.getText().toString());
        intent.putExtra("name", ttName.getText().toString());
        intent.putExtra("phone", ttPhone.getText().toString());
        intent.putExtra("state", ttState.getText().toString());
        intent.putExtra("province", province);
        intent.putExtra("city", city);
        intent.putExtra("region", region);
        intent.putExtra("address", ttAddress.getText().toString());
        intent.putExtra("telephone", ttTelephone.getText().toString());
        intent.putExtra("fax", ttFax.getText().toString());
        intent.putExtra("http", ttHttp.getText().toString());
        intent.putExtra("email", ttEmail.getText().toString());
        intent.putExtra("pool", ttPool.getText().toString());
        intent.putExtra("source", ttSource.getText().toString());
        intent.putExtra("industry", ttIndustry.getText().toString());
        intent.putExtra("follower", ttFollower.getText().toString());
        intent.putExtra("remark", ttRemark.getText().toString());
        intent.putExtra("poolId", poolId);
        intent.putExtra("sourceId", sourceId);
        intent.putExtra("industryId", industryId);
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("cityId", cityId);
        intent.putExtra("regionId", regionId);
        intent.putExtra("stateId", stateId);
        intent.putExtra("followerId", followerId);
        startActivityForResult(intent, Edit_CallBack);
    }
    private void showCallServiceDialog(final String phoneNum) {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setMessage("确定拨打  " + phoneNum + " ?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phoneNum);
                intent.setData(data);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.show();
    }


    @OnClick({R.id.linear_phoneNum, R.id.linear_telephoneNum, R.id.linear_fax, R.id.linear_http, R.id.linear_email})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linear_phoneNum:
                if (!StringUtils.isEmpty(ttPhone)) {
                    showCallServiceDialog(ttPhone.getText().toString());
                }
                break;
            case R.id.linear_telephoneNum:
                if (!StringUtils.isEmpty(ttTelephone)) {
                    showCallServiceDialog(ttTelephone.getText().toString());
                }
                break;
            case R.id.linear_fax:

                break;
            case R.id.linear_http:
                if (!StringUtils.isEmpty(ttHttp)){
                    String url = "";
                    if (ttHttp.getText().toString().startsWith("http")){
                        url = StringUtils.getEditText(ttHttp);
                    }else {
                        url = "http://"+StringUtils.getEditText(ttHttp);
                    }
                    intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                    startActivity(intent);
                }
                break;
            case R.id.linear_email:
                if (!StringUtils.isEmpty(ttEmail.getText().toString())) {
                    try {
                        intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setType("text/plain");
                        intent.setData(Uri.parse("mailto:" + ttEmail.getText().toString()));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "邮件标题");
                        intent.putExtra(Intent.EXTRA_TEXT, "邮件内容");
                        startActivity(intent);
                    } catch (Exception e) {
                        MessageDialog dialog = new MessageDialog(this);
                        dialog.setTitle("提示");
                        dialog.setMessage("手机上未安装任何邮件应用");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                }
                break;
        }
    }
}
