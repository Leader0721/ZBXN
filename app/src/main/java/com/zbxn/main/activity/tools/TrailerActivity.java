package com.zbxn.main.activity.tools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.utils.JsonUtil;
import com.pub.utils.ScreenUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyGridView;
import com.pub.widget.MyGridViewLine;
import com.zbxn.R;
import com.zbxn.main.adapter.ToolsAdapter;
import com.zbxn.main.entity.RecTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: ysj
 * @date: 2016-12-15 18:32
 */
public class TrailerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.mGridView)
    MyGridViewLine mGridView;
    private List<RecTool> listTemp;
    private ToolsAdapter mAdapter;

    private String toolsList = "[{\"id\":5,\"title\":\"会议\",\"\":114,\"img\":\"temp114\",\"isvisible\":true}" +
            ",{\"id\":8,\"title\":\"文件\",\"menuid\":118,\"img\":\"temp118\",\"isvisible\":true}" +
            ",{\"id\":11,\"title\":\"客户\",\"menuid\":121,\"img\":\"temp121\",\"isvisible\":true}" +
            ",{\"id\":12,\"title\":\"车辆\",\"menuid\":122,\"img\":\"temp122\",\"isvisible\":true}" +
            ",{\"id\":13,\"title\":\"物资\",\"menuid\":123,\"img\":\"temp123\",\"isvisible\":true}" +
            ",{\"id\":14,\"title\":\"公司制度\",\"menuid\":124,\"img\":\"temp124\",\"isvisible\":true}]";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);
        setTitle("预告");
        initView();
    }

    private void initView() {
        listTemp = new ArrayList<>();
        try {
            listTemp = JsonUtil.fromJsonList(toolsList, RecTool.class);
            if (!StringUtils.isEmpty(listTemp)) {
                setData(listTemp);
            }
        } catch (Exception e) {

        }
        mAdapter = new ToolsAdapter(this, listTemp);
        mGridView.setAdapter(mAdapter);
        mGridView.setFocusable(false);
        mGridView.setOnItemClickListener(this);
    }

    public void setData(List<RecTool> list) {
        int size = list.size() ;
        int line = size / 3;//一共有几行
        if (size % 3 > 0) {
            line += 1;
        }
        int height = ScreenUtils.dipToPx(this, line * 96);
        ViewGroup.LayoutParams params = mGridView.getLayoutParams();
        params.height = height;
        mGridView.setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "敬请期待...", Toast.LENGTH_SHORT).show();
    }
}
