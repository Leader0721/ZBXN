package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.db.table.Id;
import com.pub.base.BaseActivity;
import com.zbxn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/13.
 */
public class SellingTypeActivity extends BaseActivity {
    private String ID;
    private static final int Cost_CallBack = 10000;
    private static final int Deal_CallBack = 10001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellingtype);
        ButterKnife.bind(this);
        ID = getIntent().getStringExtra("ID");
        setTitle("销售费用类型");
    }


    @OnClick({R.id.cost_rela, R.id.deal_rela})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.cost_rela:
                intent = new Intent(this,NewCostActivity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent,Cost_CallBack);
                break;
            case R.id.deal_rela:
                intent = new Intent(this,NewDealCostActvity.class);
                intent.putExtra("ID", ID);
                startActivityForResult(intent,Deal_CallBack);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == Cost_CallBack) {
            setResult(RESULT_OK);
            finish();
        }
        if (requestCode == Deal_CallBack) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
