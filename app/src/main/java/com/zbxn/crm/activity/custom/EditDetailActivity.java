package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.EventCustom;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.entity.Contacts;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/13.
 */
public class EditDetailActivity extends BaseActivity {
    @BindView(R.id.et_client)
    EditText etClient;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.tt_state)
    TextView ttState;
    @BindView(R.id.clientstate_linear)
    RelativeLayout clientstateLinear;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_telephonenum)
    EditText etTelephonenum;
    @BindView(R.id.et_fax)
    EditText etFax;
    @BindView(R.id.et_http)
    EditText etHttp;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tt_source)
    TextView ttSource;
    @BindView(R.id.source_linear)
    RelativeLayout sourceLinear;
    @BindView(R.id.industry_textView)
    TextView industryTextView;
    @BindView(R.id.industry_linear)
    RelativeLayout industryLinear;
    @BindView(R.id.follow_textView)
    TextView followTextView;
    @BindView(R.id.follow_linear)
    RelativeLayout followLinear;
    private static final int Industry_CallBack = 10000;
    private static final int State_CallBack = 10001;
    private static final int Source_CallBack = 10002;
    private static final int Follow_CallBack = 10003;
    private static final int Province_CallBack = 10004;
    private static final int Pool_CallBack = 10005;
    @BindView(R.id.tt_province)
    TextView ttProvince;
    @BindView(R.id.tt_pool)
    TextView ttPool;
    @BindView(R.id.et_content)
    EditText etContent;
    private String industry = "";
    private String industryId = "0";
    private String state = "";
    private String stateId = "0";
    private String source = "";
    private String sourceId = "0";
    private String follow = "";
    private int followId;
    private String province = "";
    private String city = "";
    private String region = "";
    private String provinceId;
    private String cityId;
    private String regionId;
    public static final String SUCCESS2 = "ScheduleActivitySuccess2";
    private String ID;
    private String pool = "";
    private String poolId = "0";
    //选择跟进人
    private ArrayList<Contacts> mListContactsChecker = new ArrayList<>();
    private CustomerInfoActivity customerInfoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdetail);
        ButterKnife.bind(this);
        setTitle("编辑详情");
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        ID = CustomActivity.CUSTOMID;
        etClient.setText(intent.getStringExtra("client"));
        etName.setText(intent.getStringExtra("name"));
        etPhonenum.setText(intent.getStringExtra("phone"));
        ttState.setText(intent.getStringExtra("state"));
        etContent.setText(intent.getStringExtra("remark"));
        if (StringUtils.isEmpty(intent.getStringExtra("province"))) {
            ttProvince.setText("");
        } else {
            ttProvince.setText(intent.getStringExtra("province") + "/" + intent.getStringExtra("city") + "/" + intent.getStringExtra("region"));
        }
        etAddress.setText(intent.getStringExtra("address"));
        etTelephonenum.setText(intent.getStringExtra("telephone"));
        etFax.setText(intent.getStringExtra("fax"));
        etHttp.setText(intent.getStringExtra("http"));
        etEmail.setText(intent.getStringExtra("email"));
        ttPool.setText(intent.getStringExtra("pool"));
        ttSource.setText(intent.getStringExtra("source"));
        industryTextView.setText(intent.getStringExtra("industry"));
        followTextView.setText(intent.getStringExtra("follower"));
        followId = intent.getIntExtra("followerId", 0);

        sourceId = intent.getStringExtra("sourceId");
        stateId = intent.getStringExtra("stateId");
        poolId = intent.getStringExtra("poolId");
        industryId = intent.getStringExtra("industryId");
        provinceId = intent.getStringExtra("provinceId");
        cityId = intent.getStringExtra("cityId");
        regionId = intent.getStringExtra("regionId");
    }

    @Override
    public void initRight() {
        setRight1("保存");
        setRight1Icon(0);
        setRight1Show(true);
        setRight2Show(false);
        super.initRight();
    }

    //编辑的时候必须传进去id
    @Override
    public void actionRight1(MenuItem menuItem) {
        if (isRight()) {
            String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
            String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
            Call call = HttpRequest.getIResourceOANetAction().createCustomer(ssid, CurrentCompanyId,
                    ID, etClient.getText().toString(),
                    etName.getText().toString(), etPhonenum.getText().toString(),
                    stateId, provinceId,
                    cityId, regionId,
                    etAddress.getText().toString(), etTelephonenum.getText().toString(),
                    etFax.getText().toString(), etHttp.getText().toString(),
                    etEmail.getText().toString(), poolId,
                    sourceId, industryId,
                    followId + "", etContent.getText().toString()
                    , "");
            callRequest(call, new HttpCallBack(CustomEntity.class, EditDetailActivity.this, false) {
                @Override
                public void onSuccess(ResultData mResult) {
                    if ("0".equals(mResult.getSuccess())) {
                        setResult(RESULT_OK);
                        String ID = (String) mResult.getData();
                        CustomActivity.CUSTOMID = ID;
                        EventCustom eventCustom = new EventCustom();
                        eventCustom.setTag(EditDetailActivity.SUCCESS2);
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
        super.actionRight1(menuItem);
    }
    private boolean isRight() {
        boolean isRight = true;
        if (StringUtils.isEmpty(etClient) && StringUtils.isEmpty(etName) && StringUtils.isEmpty(etPhonenum)) {
            MyToast.showToast("客户名称，姓名，手机号三者必须填写一项");
            isRight = false;
        } else if (!StringUtils.isEmpty(etPhonenum)&&!ValidationUtil.isMobile(etPhonenum.getText().toString())){
            MyToast.showToast("请输入正确的客户手机号");
        } else if (!StringUtils.isEmpty(etEmail.getText().toString())) {
            if (ValidationUtil.isMailbox(etEmail.getText().toString())) {
                isRight = true;
            } else {
                isRight = false;
                MyToast.showToast("请输入正确的客户邮箱");
            }
        } else {
            isRight = true;
        }
        return isRight;
    }


    // 获取部门请求结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Industry_CallBack) {
            industry = data.getStringExtra("industry");
            industryId = data.getStringExtra("id");
            industryTextView.setText(industry);
        }

        if (requestCode == State_CallBack) {
            state = data.getStringExtra("state");
            stateId = data.getStringExtra("id");
            ttState.setText(state);
        }
        if (requestCode == Source_CallBack) {
            source = data.getStringExtra("source");
            sourceId = data.getStringExtra("id");
            ttSource.setText(source);
        }
//        if (requestCode == Follow_CallBack) {
//            follow = data.getStringExtra("follow");
//            followTextView.setText(follow);
//        }

        if (requestCode == Follow_CallBack) {
            follow = data.getStringExtra("name");
            followId = data.getIntExtra("id", 0);
            followTextView.setText(follow);
        }

        if (requestCode == Province_CallBack) {
            province = data.getStringExtra("province");
            city = data.getStringExtra("city");
            region = data.getStringExtra("region");
            provinceId = data.getStringExtra("provinceId");
            cityId = data.getStringExtra("cityId");
            regionId = data.getStringExtra("regionId");
            ttProvince.setText(province + "/" + city + "/" + region);
        }
        if (requestCode == Pool_CallBack) {
            pool = data.getStringExtra("pool");
            poolId = data.getStringExtra("id");
            ttPool.setText(pool);
        }
    }


    @OnClick({R.id.linearLayout_one, R.id.clientstate_linear,
            R.id.province_linear, R.id.source_linear,
            R.id.industry_linear, R.id.follow_linear, R.id.pool_linear})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.linearLayout_one:
                break;
            case R.id.clientstate_linear:
                intent = new Intent(this, CustomStateActivity.class);
                startActivityForResult(intent, State_CallBack);
                break;
            case R.id.province_linear:
                intent = new Intent(this, ProvinceActivity.class);
                startActivityForResult(intent, Province_CallBack);
                break;
            case R.id.source_linear:
                intent = new Intent(this, CustomSourceActivity.class);
                startActivityForResult(intent, Source_CallBack);
                break;
            case R.id.industry_linear:
                intent = new Intent(this, IndustryActivity.class);
                startActivityForResult(intent, Industry_CallBack);
                break;
            case R.id.follow_linear:
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list", mListContactsChecker);
                intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Follow_CallBack);
                break;
            case R.id.pool_linear:
                intent = new Intent(this, CustomPoolActivity.class);
                startActivityForResult(intent, Pool_CallBack);
                break;
        }
    }

}
