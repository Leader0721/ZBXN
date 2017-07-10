package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.zbxn.R;
import com.zbxn.crm.adapter.TransferCustomAdapter;
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
 * 转移他人
 *
 * @author: ysj
 * @date: 2017-01-20 16:43
 */
public class TransferCustomActivity extends BaseActivity {

    private static final int Flag_Callback_Charge = 1001;//负责人

    @BindView(R.id.tv_customName)
    TextView tvCustomName;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.mListView)
    ListView mListView;
    @BindView(R.id.ll_name)
    LinearLayout llName;

    private String customId;
    private String customName;
    private String chargeId;
    private String chargeName;
    private ArrayList<Contacts> mListContactsCharge = new ArrayList<>();
    private List<String> list;
    private TransferCustomAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_custom);
        ButterKnife.bind(this);
        setTitle("转移他人");
        customId = getIntent().getStringExtra("id");
        customName = getIntent().getStringExtra("name");
        tvCustomName.setText(customName);
        initData();
    }

    private void initData() {
        list = new ArrayList<>();
        list.add("联系人");
        list.add("机会");
        list.add("合同");
        list.add("销售费用");
        list.add("跟进记录");
        adapter = new TransferCustomAdapter(this, list);
        mListView.setAdapter(adapter);
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1Icon(0);
        setRight1("确定");
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        if (StringUtils.isEmpty(tvName)) {
            MyToast.showToast("请选择新负责人");
            return;
        }
        postTransferCustomer();
    }

    @OnClick(R.id.ll_name)
    public void onClick() {
        Intent intent = new Intent(this, ContactsChoseActivity.class);
        intent.putExtra("list", mListContactsCharge);
        intent.putExtra("type", 2);//0-查看 1-多选 2-单选
        startActivityForResult(intent, Flag_Callback_Charge);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_Charge) {
            chargeId = data.getIntExtra("id", 0) + "";
            chargeName = data.getStringExtra("name");
            tvName.setText(chargeName);
        }
    }

    /**
     * 转移他人
     */
    public void postTransferCustomer() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postTransferCustomer(ssid, currentCompanyId, customId, chargeId);
        callRequest(call, new HttpCallBack(CustomEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("转移成功");
                    Intent intent = new Intent(TransferCustomActivity.this, CustomActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
