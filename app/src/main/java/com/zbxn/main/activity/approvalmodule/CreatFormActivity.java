package com.zbxn.main.activity.approvalmodule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.pub.base.BaseActivity;
import com.pub.utils.JsonUtil;
import com.pub.utils.StringUtils;
import com.pub.widget.NoScrollListview;
import com.zbxn.R;
import com.zbxn.main.adapter.CreatFormAdpter;
import com.zbxn.main.entity.ApprovalEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 审批表单列表
 * <p>
 * Created by ysj on 2016/11/5.
 */
public class CreatFormActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.mListView)
    NoScrollListview mListView;

    private CreatFormAdpter mToolsAdapter;
    private List<ApprovalEntity> listTemp;
    private String toolsList ="[{\"typeid\":1,\"img\":\"approval_ico_qingjia\",\"name\":\"请假申请单\"},{\"typeid\":2,\"img\":\"approval_ico_baoxiao\",\"name\":\"报销申请单\"},{\"typeid\":3,\"img\":\"approval_ico_wupin\",\"name\":\"物品申领单\"},{\"typeid\":4,\"img\":\"approval_ico_gongzuoqingshi\",\"name\":\"工作请示单\"},{\"typeid\":5,\"img\":\"approval_ico_chuchai\",\"name\":\"出差申请单\"},{\"typeid\":6,\"img\":\"approval_ico_waichu\",\"name\":\"外出申请单\"},{\"typeid\":7,\"img\":\"approval_ico_caigou\",\"name\":\"采购申请单\"},{\"typeid\":8,\"img\":\"approval_ico_fukuan\",\"name\":\"付款申请单\"},{\"typeid\":9,\"img\":\"approval_ico_yongyin\",\"name\":\"用印申请单\"},{\"typeid\":10,\"img\":\"approval_ico_zhuanzheng\",\"name\":\"转正申请单\"},{\"typeid\":11,\"img\":\"approval_ico_lizhi\",\"name\":\"离职申请单\"},{\"typeid\":12,\"img\":\"approval_ico_jiaban\",\"name\":\"加班申请单\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatform);
        ButterKnife.bind(this);
        setTitle("审批表单");
        initView();
    }

    private void initView() {
        listTemp = new ArrayList<>();
        mListView.setOnItemClickListener(this);
        try {
            listTemp = JsonUtil.fromJsonList(toolsList, ApprovalEntity.class);
            if (!StringUtils.isEmpty(listTemp)) {
                mToolsAdapter = new CreatFormAdpter(this, listTemp);
                mListView.setAdapter(mToolsAdapter);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int typeId = listTemp.get(position).getTypeid();
            Intent intent = new Intent(this, ApplyCreatActivity.class);
            intent.putExtra("typeId", typeId + "");
            startActivity(intent);
        } catch (Exception e) {

        }
    }
}
