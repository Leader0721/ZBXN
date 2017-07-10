package com.zbxn.main.activity.org;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.OrganizeDepartmentOnlyAdapter;
import com.zbxn.main.adapter.OrganizePeopleAdapter;
import com.zbxn.main.adapter.PopupwindowMoreAdapter;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.ContactsDepartment;
import com.zbxn.main.listener.ICustomListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 项目名称：通讯录 按组织架构
 * 创建人：Wuzy
 * 创建时间：2016/11/1 8:52
 */
public class OrganizeActivity extends BaseActivity {
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    @BindView(R.id.mListViewDepartment)
    MyListView mListViewDepartment;
    @BindView(R.id.mListViewPeople)
    MyListView mListViewPeople;
    @BindView(R.id.tv_department_tip)
    TextView tvDepartmentTip;
    @BindView(R.id.tv_people_tip)
    TextView tvPeopleTip;
    @BindView(R.id.layout_tip)
    LinearLayout layoutTip;
    @BindView(R.id.mLine)
    ImageView mLine;
    @BindView(R.id.addDept)
    TextView addDept;
    @BindView(R.id.addEmployee)
    TextView addEmployee;
    @BindView(R.id.inviteEmployee)
    TextView inviteEmployee;

    private LayoutInflater mInflater;

    private int mIsActive = 1;//1--启用  0--停用

    private String mParentId = "0";//当前部门ID
    private String mParentName = "";//当前部门名称
    private String mParentDesc = "";//当前部门描述

    @Override
    public void initRight() {
        setTitle("组织框架");
        setRight1("更多");
        setRight2("搜索");
        setRight1Icon(R.mipmap.my_more);
        setRight2Icon(R.mipmap.mission_search);
        setRight1Show(true);
        setRight2Show(true);
    }

    @Override
    public void actionRight2(MenuItem menuItem) {
        Intent intent;
        intent = new Intent(this, FrameSearchActivity.class);
        //intent.putExtra("keyword", mKeyword);
        startActivity(intent);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        Intent intent;
        List<String> list = new ArrayList<>();
        list.add("启用");
        list.add("停用");
        ShowPopupWindow(list, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mIsActive = 1;
                        break;
                    case 1:
                        mIsActive = 0;
                        break;
                }
                getDepartment("0", "公司");
                //解决每次产生的刷新产生null
                mListV = mListV.subList(0, 1);
                refresh();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //InviteNewEmployeeActivity.COUNT = 0;

        setContentView(R.layout.activity_organize);
        ButterKnife.bind(this);
        mInflater = LayoutInflater.from(this);
        initView();
        String zmsCompanyName = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_ZMSCOMPANYNAME);
        addView(zmsCompanyName, "0");
        getDepartment("0", "公司");
    }

    private void initView() {
    }

    private List<View> mListV = new ArrayList<>();

    private void addView(String name, String parentId) {
        View view = mInflater.inflate(R.layout.view_organize_text, mLayout, false);
        view.setTag(parentId);
        TextView textView = (TextView) view.findViewById(R.id.mText);
        textView.setText(name);
        textView.setTag(parentId);
//        Drawable drawable = getResources().getDrawable(R.drawable.drawable);
//        /// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if ("0".equals(parentId)) {
            textView.setCompoundDrawables(null, null, null, null);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDepartment(v.getTag().toString() + "", StringUtils.getEditText((TextView) v));
                int index = -1;
                for (int i = 0; i < mListV.size(); i++) {
                    if (v.getTag().toString().equals(mListV.get(i).getTag().toString())) {
                        index = i;
                    }
                }
                //解决每次产生的刷新产生null
                mListV = mListV.subList(0, index + 1);
                //mListV = mListV.subList(0, index );
                refresh();
            }
        });
        mListV.add(view);
        refresh();
    }

    /**
     * 刷新顶部视图
     */
    private void refresh() {
        mLayout.removeAllViews();
        Drawable drawable = getResources().getDrawable(R.mipmap.my_org_arrows);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        for (int i = 0; i < mListV.size(); i++) {
            if (mListV.size() - 1 == i) {
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setCompoundDrawables(null, null, null, null);
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setTextColor(getResources().getColor(R.color.tvc9));
            } else {
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setCompoundDrawables(null, null, drawable, null);
                ((TextView) mListV.get(i).findViewById(R.id.mText)).setTextColor(getResources().getColor(R.color.cpb_blue));
            }
            mLayout.addView(mListV.get(i));
        }
    }

    private List<ContactsDepartment> listDepartment = new ArrayList<>();
    private List<Contacts> listContacts = new ArrayList<>();

    private void getDepartment(String parentId, String parentName) {
        mParentId = parentId;
        mParentName = parentName;
        listDepartment.clear();
        listContacts.clear();
        //例：分组聚合查询出  Parent表中 非重复的name和它的对应数量
        try {
            //, "count(departmentName) as countPeople"
            List<DbModel> dbModelsDepartment = BaseApp.DBLoader.findDbModelAll(Selector.from(Contacts.class)
                    .select("departmentId", "departmentName")
                    .where("isDepartment", "=", "1").and("parentId", "=", parentId));
            ContactsDepartment contactsDepartment = null;
            for (int i = 0; i < dbModelsDepartment.size(); i++) {
                contactsDepartment = new ContactsDepartment();
//                contactsDepartment.setCaptialChar(StringUtils.getPinYinHeadChar(dbModelsDepartment.get(i).getString("departmentName")));
                contactsDepartment.setDepartmentId(dbModelsDepartment.get(i).getString("departmentId"));
                contactsDepartment.setDepartmentName(dbModelsDepartment.get(i).getString("departmentName"));
                listDepartment.add(contactsDepartment);
            }
            OrganizeDepartmentOnlyAdapter adapter = new OrganizeDepartmentOnlyAdapter(this, listDepartment, listener);
            mListViewDepartment.setAdapter(adapter);
            mListViewDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    addView(listDepartment.get(position).getDepartmentName(), listDepartment.get(position).getDepartmentId());
                    getDepartment(listDepartment.get(position).getDepartmentId(), listDepartment.get(position).getDepartmentName());
                }
            });
            if (StringUtils.isEmpty(listDepartment)) {
                layoutTip.setVisibility(View.GONE);
                tvDepartmentTip.setVisibility(View.GONE);
            } else {
                layoutTip.setVisibility(View.GONE);
                tvDepartmentTip.setVisibility(View.VISIBLE);
            }

            listContacts = BaseApp.DBLoader.findAll(Selector.from(Contacts.class)
                    .where("isDepartment", "=", null).and("departmentId", "=", parentId).and("isactive", "=", mIsActive));
            OrganizePeopleAdapter contactsPeopleAdapter = new OrganizePeopleAdapter(this, listContacts, listener, false);
            mListViewPeople.setAdapter(contactsPeopleAdapter);
            mListViewPeople.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(OrganizeActivity.this, EmployeeDetailActivity.class);
                    intent.putExtra("contactor", listContacts.get(position));
                    startActivity(intent);
                }
            });
            if (StringUtils.isEmpty(listContacts)) {
                tvPeopleTip.setVisibility(View.GONE);
            } else {
                tvPeopleTip.setVisibility(View.VISIBLE);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        if ("0".equals(parentId)) {
            inviteEmployee.setText("邀请员工");
        } else {
            inviteEmployee.setText("编辑部门");
        }
    }

    private ICustomListener listener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            Contacts result = (Contacts) obj1;
            switch (obj0) {
                case 0:
                    break;

                default:
                    break;
            }
        }
    };


    @OnClick({R.id.addDept, R.id.addEmployee, R.id.inviteEmployee})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.addDept:
                intent = new Intent(this, AddDepartmentActivity.class);
                intent.putExtra("name", mParentName);
                intent.putExtra("id", mParentId);
                startActivityForResult(intent, 2000);
                break;
            case R.id.addEmployee:
                intent = new Intent(this, StaffActivity.class);
                intent.putExtra("name", mParentName);
                intent.putExtra("id", Integer.parseInt(mParentId));
                startActivityForResult(intent, 2000);
                break;
            case R.id.inviteEmployee:
                if ("邀请员工".equals(StringUtils.getEditText(inviteEmployee))) {//邀请员工
                    intent = new Intent(this, InviteEmployeeActivity.class);
                    startActivity(intent);
                } else {//编辑部门
                    String parentNameGrand = "";
                    String parentIdGrand = "";
                    Contacts department = null;
                    try {
                        List<Contacts> listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("departmentId", "=", mParentId));
                        if (!StringUtils.isEmpty(listTemp)) {
                            department = listTemp.get(0);
                            String parentId = department.getParentId();
                            listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("departmentId", "=", department.getDepartmentId()));
                            if (!StringUtils.isEmpty(listTemp)) {

                                mParentDesc = department.getDepartmentDesc();
                            }

                            listTemp = BaseApp.DBLoader.findAll(Selector.from(Contacts.class).where("departmentId", "=", parentId));
                            if (!StringUtils.isEmpty(listTemp)) {
                                Contacts departmentParent = listTemp.get(0);
                                parentNameGrand = departmentParent.getDepartmentName();
                                parentIdGrand = parentId;
                            }
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    intent = new Intent(this, AddDepartmentActivity.class);
                    intent.putExtra("name", parentNameGrand);//父部门名称
                    intent.putExtra("id", parentIdGrand.trim());//父部门ID
                    intent.putExtra("DepartmentID", mParentId);//部门ID
                    intent.putExtra("DepartmentName", mParentName);//部门名称
                    intent.putExtra("departmentName", mParentDesc);//部门描述
                    startActivityForResult(intent, 2000);
                }
                break;
        }
    }

    public void ShowPopupWindow(List<String> list, final AdapterView
            .OnItemClickListener listener) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.base_popupwindow_more, null);

        ListView listView = (ListView) layout.findViewById(R.id.list_popup);

        layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);//强制绘制layout

        PopupwindowMoreAdapter adapter = new PopupwindowMoreAdapter(this, list);
        listView.setAdapter(adapter);

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int xoff = manager.getDefaultDisplay().getWidth() / 2;
        final PopupWindow m_popupWindow = new PopupWindow(layout, xoff, ActionBar.LayoutParams.WRAP_CONTENT);
        m_popupWindow.setFocusable(true);
        //m_popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        ColorDrawable dw = new ColorDrawable(0x33000000);
        m_popupWindow.setBackgroundDrawable(dw);
        //设置SelectPicPopupWindow弹出窗体的宽
        m_popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        m_popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        m_popupWindow.showAsDropDown(mLine, xoff, 0);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_popupWindow.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_popupWindow.dismiss();
                listener.onItemClick(parent, view, position, id);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 2000:
                getDepartment(mParentId, mParentName);
                int index = -1;
                for (int i = 0; i < mListV.size(); i++) {
                    if (mParentId.equals(mListV.get(i).getTag().toString())) {
                        index = i;
                    }
                }
                //解决每次产生的刷新产生null
                mListV = mListV.subList(0, index + 1);
                //mListV = mListV.subList(0, index );
                refresh();
                break;
            default:
                break;
        }
    }

}

