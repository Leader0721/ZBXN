package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.zbxn.R;
import com.zbxn.crm.entity.CreateContactEntity;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.entity.Contacts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/1/11.
 */
public class NewCustomActivity extends BaseActivity {
    @BindView(R.id.IsShowMore)
    TextView IsShowMore;
    @BindView(R.id.linearLayout_two)
    LinearLayout linearLayoutTwo;
    @BindView(R.id.et_client)
    EditText client;
    @BindView(R.id.et_name)
    EditText clientName;
    @BindView(R.id.et_phonenum)
    EditText clientPhone;
    @BindView(R.id.tt_state)
    TextView clientState;
    @BindView(R.id.et_address)
    EditText clientAddress;
    @BindView(R.id.et_telephonenum)
    EditText clientTelephone;
    @BindView(R.id.et_fax)
    EditText clientFax;
    @BindView(R.id.et_http)
    EditText clientHttp;
    @BindView(R.id.et_email)
    EditText clientEmail;
    @BindView(R.id.et_content)
    EditText mContent;
    @BindView(R.id.container_layout)
    LinearLayout containerLayout;
    @BindView(R.id.tt_source)
    TextView ttSource;
    @BindView(R.id.follow_textView)
    TextView followTextView;
    @BindView(R.id.tt_province)
    TextView ttProvince;
    @BindView(R.id.image_IsShowMore)
    ImageView imageIsShowMore;
    @BindView(R.id.linear_newcontact)
    LinearLayout linearNewcontact;
    @BindView(R.id.tt_pool)
    TextView ttPool;
    @BindView(R.id.line_show)
    View lineShow;
    @BindView(R.id.view_line)
    View viewLine;

    private int IsShow = 1;
    private String industry = "";
    private String industryId = "0";
    private String stateId = "0";
    private String sourceId = "0";
    private String state = "";
    private String source = "";
    private String follow = "";
    private String pool = "";
    private String poolId = "0";
    private int followId;
    //    private String followId;
    private String province = "";
    private String city = "";
    private String region = "";
    private String provinceId;
    private String cityId;
    private String regionId;
    @BindView(R.id.industry_textView)
    TextView industryTextView;
    private static final int Industry_CallBack = 10000;
    private static final int State_CallBack = 10001;
    private static final int Source_CallBack = 10002;
    private static final int Follow_CallBack = 10003;
    private static final int Province_CallBack = 10004;
    private static final int Pool_CallBack = 10005;
    private boolean IsOne = false;
    private boolean isEmpty = false;
    private String message = "请完善上述联系人消息";
    private String contact;
    //选择跟进人
    private ArrayList<Contacts> mListContactsChecker = new ArrayList<>();
    List<CreateContactEntity> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewclient);
        ButterKnife.bind(this);
        setTitle("新建客户");
        followId = Integer.parseInt(PreferencesUtils.getString(this, LoginActivity.FLAG_INPUT_ID));
        viewLine.setVisibility(View.INVISIBLE);
        lineShow.setVisibility(View.INVISIBLE);
        linearLayoutTwo.setVisibility(View.GONE);
    }
    @Override
    public void initRight() {
        setRight1("确定");
        setRight1Icon(0);
        setRight1Show(true);
        setRight2Show(false);
        super.initRight();
    }

    @Override
    public void actionRight1(MenuItem menuItem) {

        if (!validate()) {
            return;
        }

        if (!StringUtils.isEmpty(mList)) {
            contact = JsonUtil.toJsonString(mList);
        } else {
            contact = null;
        }

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        String CurrentCompanyId = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().createCustomer(ssid, CurrentCompanyId,
                "", client.getText().toString(),
                clientName.getText().toString(), clientPhone.getText().toString(),
                stateId, provinceId,
                cityId, regionId,
                clientAddress.getText().toString(), clientTelephone.getText().toString(),
                clientFax.getText().toString(), clientHttp.getText().toString(),
                clientEmail.getText().toString(), poolId,
                sourceId, industryId,
                followId + "", mContent.getText().toString(),
                contact);
        callRequest(call, new HttpCallBack(CustomEntity.class, NewCustomActivity.this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {
                    setResult(RESULT_OK);
                    finish();
                    MyToast.showToast("新增客户成功");
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
        super.actionRight1(menuItem);
    }

    /**
     * 提交前验证
     *
     * @return
     */
    private boolean validate() {
        boolean isRight = true;
        mList.clear();
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            CreateContactEntity entity = new CreateContactEntity();
            LinearLayout layout = (LinearLayout) containerLayout.getChildAt(i);// 获得子item的layout
            EditText et_Name = (EditText) layout.findViewById(R.id.et_name);// 从layout中获得控件,根据其id
            EditText et_phoneNum = (EditText) layout.findViewById(R.id.et_phoneNum);//
            entity.setContactName(StringUtils.getEditText(et_Name));
            entity.setContactMobile(StringUtils.getEditText(et_phoneNum));

            mList.add(entity);

            if (StringUtils.isEmpty(et_Name)&&StringUtils.isEmpty(et_phoneNum)) {
                MyToast.showToast("请完善联系人信息");
                isRight = false;
            }
            if (!ValidationUtil.isMobile(StringUtils.getEditText(et_phoneNum))&&!StringUtils.isEmpty(et_phoneNum)) {
                MyToast.showToast("请输入正确的联系人手机号");
                isRight = false;
            }
        }
        if (StringUtils.isEmpty(client) && StringUtils.isEmpty(clientName) && StringUtils.isEmpty(clientPhone)) {
            MyToast.showToast("客户名称，姓名，手机号三者必须输入一项");
            isRight = false;
        }

        if (!StringUtils.isEmpty(clientPhone)) {
            if (!ValidationUtil.isMobile(StringUtils.getEditText(clientPhone))) {
                MyToast.showToast("请输入正确的客户手机号");
                isRight = false;
            }
        }

        if (!StringUtils.isEmpty(clientEmail)) {
            if (!ValidationUtil.isMailbox(StringUtils.getEditText(clientEmail))) {
                MyToast.showToast("请输入正确的客户邮箱");
                isRight = false;
            }else {
                isRight = true;
            }
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
            clientState.setText(state);
        }
        if (requestCode == Source_CallBack) {
            source = data.getStringExtra("source");
            sourceId = data.getStringExtra("id");
            ttSource.setText(source);
        }
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

    private void addContact(){
        if (containerLayout.getChildCount() == 1) {
            IsOne = true;
        }
        isEmpty = true;
        final View view1 = LayoutInflater.from(this).inflate(R.layout.linearlayout_item, null, false);
        ImageView deleteItem1 = (ImageView) view1.findViewById(R.id.delete_item3);
        deleteItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                containerLayout.removeView((View) v.getParent().getParent().getParent());
                if (containerLayout.getChildCount() == 1 || containerLayout.getChildCount() == 0) {
                    IsOne = false;
                }
            }
        });
        message = "请完善上述联系人信息";
        for (int i = 0; i < containerLayout.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout) containerLayout.getChildAt(i);// 获得子item的layout
            EditText et_Name = (EditText) layout.findViewById(R.id.et_name);// 从layout中获得控件,根据其id
            EditText et_phoneNum = (EditText) layout.findViewById(R.id.et_phoneNum);//
            System.out.println("the text of " + i + "'s EditText：----------->" + et_Name.getText() + "'eeee EditText：----------->" + et_phoneNum.getText());
            if (StringUtils.isEmpty(et_Name.getText().toString()) && StringUtils.isEmpty(et_phoneNum.getText().toString())) {
                isEmpty = true;
            } else if (!StringUtils.isEmpty(et_phoneNum.getText().toString())){
                if (ValidationUtil.isMobile(et_phoneNum.getText().toString())) {
                    isEmpty = false;
                } else {
                    isEmpty = true;
                    message = "请输入正确的手机号";
                }
            }else if (!StringUtils.isEmpty(et_Name.getText().toString())){
                isEmpty = false;
            }
        }
        if (IsOne) {
            if (isEmpty) {
                MyToast.showToast(message);
            } else {
                containerLayout.addView(view1);
            }
        } else {
            containerLayout.addView(view1);
            IsOne = true;
        }
    }


    @OnClick({R.id.industry_linear, R.id.IsShowMore, R.id.source_linear,
            R.id.follow_linear, R.id.province_linear,
            R.id.container_layout, R.id.createNewEmployee, R.id.clientstate_linear, R.id.linear_isShow,
            R.id.linear_newcontact, R.id.pool_linear})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.industry_linear:
                intent = new Intent(this, IndustryActivity.class);
                startActivityForResult(intent, Industry_CallBack);
                break;
            case R.id.IsShowMore:
                if (IsShow == 1) {
                    IsShow = 2;
                    viewLine.setVisibility(View.VISIBLE);
                    lineShow.setVisibility(View.VISIBLE);
                    linearLayoutTwo.setVisibility(View.VISIBLE);
                    IsShowMore.setText("收起更多消息");
                    imageIsShowMore.setImageResource(R.mipmap.open);
                } else {
                    IsShow = 1;
                    viewLine.setVisibility(View.INVISIBLE);
                    linearLayoutTwo.setVisibility(View.GONE);
                    IsShowMore.setText("展开更多消息");
                    lineShow.setVisibility(View.INVISIBLE);
                    imageIsShowMore.setImageResource(R.mipmap.close);
                }
                break;
            case R.id.linear_isShow:
                if (IsShow == 1) {
                    IsShow = 2;
                    viewLine.setVisibility(View.VISIBLE);
                    lineShow.setVisibility(View.VISIBLE);
                    linearLayoutTwo.setVisibility(View.VISIBLE);
                    IsShowMore.setText("收起更多消息");
                    imageIsShowMore.setImageResource(R.mipmap.open);
                } else {
                    IsShow = 1;
                    viewLine.setVisibility(View.INVISIBLE);
                    linearLayoutTwo.setVisibility(View.GONE);
                    IsShowMore.setText("展开更多消息");
                    lineShow.setVisibility(View.INVISIBLE);
                    imageIsShowMore.setImageResource(R.mipmap.close);
                }
                break;
            case R.id.container_layout:
                break;
            case R.id.createNewEmployee://添加联系人
                addContact();
                break;
            case R.id.linear_newcontact://添加联系人
                addContact();
                break;
            case R.id.clientstate_linear:
                intent = new Intent(this, CustomStateActivity.class);
                startActivityForResult(intent, State_CallBack);
                break;
            case R.id.source_linear:
                intent = new Intent(this, CustomSourceActivity.class);
                startActivityForResult(intent, Source_CallBack);
                break;
            case R.id.follow_linear:
                intent = new Intent(this, ContactsChoseActivity.class);
                intent.putExtra("list", mListContactsChecker);
                intent.putExtra("type", 2);//0-查看 1-多选 2-单选
                startActivityForResult(intent, Follow_CallBack);
                break;
            case R.id.province_linear:
                intent = new Intent(this, ProvinceActivity.class);
                startActivityForResult(intent, Province_CallBack);
                break;
            case R.id.pool_linear:
                intent = new Intent(this, CustomPoolActivity.class);
                startActivityForResult(intent, Pool_CallBack);
                break;
        }
    }
}
