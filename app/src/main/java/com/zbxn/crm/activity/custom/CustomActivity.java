package com.zbxn.crm.activity.custom;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.ProgressLayout;
import com.pub.widget.pulltorefreshlv.PullRefreshListView;
import com.zbxn.R;
import com.zbxn.crm.adapter.CustomAdapter;
import com.zbxn.crm.entity.CustomListEntity;
import com.zbxn.crm.entity.IndustryEntity;
import com.zbxn.crm.entity.StaticTypeEntity;
import com.zbxn.main.activity.contacts.ContactsChoseActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.schedule.ScheduleActivity;
import com.zbxn.main.adapter.PopupwindowMoreAdapter;
import com.zbxn.main.adapter.PopupwindowTypeListAdapter;
import com.zbxn.main.entity.ApprovalEntity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.listener.ICustomListener;
import com.zbxn.main.popupwindow.PopupWindowFiltrate;
import com.zbxn.main.popupwindow.PopupWindowType;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 客户主界面
 *
 * @author: ysj
 * @date: 2017-01-10 10:43
 */
public class CustomActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    /**
     * 选择接收人回调
     */
    private static final int Flag_Callback_ContactsPicker = 1001;
    private static final int Flag_Callback_CustomDetails = 1002;
    private static final int Flag_Callback_NewCustom = 1003;
    private static final int Flag_Callback_Province = 1004;
    private static final int Item_Callback = 1005;
    @BindView(R.id.mLeft)
    TextView mLeft;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.mRight)
    TextView mRight;
    @BindView(R.id.layout_right)
    RelativeLayout layoutRight;
    @BindView(R.id.listView_Client)
    PullRefreshListView mListView;
    @BindView(R.id.mLine)
    ImageView mLine;
    @BindView(R.id.layout_progress)
    ProgressLayout layoutProgress;

    private List<ApprovalEntity> listLeft;
    private List<StaticTypeEntity> listRight;
    private PopupWindowFiltrate mPopupWindowFiltrate;
    //选择人
    private ArrayList<Contacts> mListContacts = new ArrayList<>();
    //接收人Id数组
    private String[] mReceiveArray;
    private List<CustomListEntity> mList;
    private CustomAdapter mCustomAdapter;
    private int mPage = 1;
    private int mPageSize = 10;
    //客户类型
    private int type = 1;
    private String mFileName = "";
    private String mFileValue = "";
    private String mCondition = "";
    private String typeName = "";
    public static String CUSTOMID;
    //下拉列表类型
    public static String CUSTPUBLICPOOL = "CustPublicPool";//客户公共池
    public static String CUSTSOURCE = "CustSource";//来源
    public static String CUSTSTATE = "CustState";//客户状态
    public static String RECORDTYPELIST = "RecordTypeList";//跟进记录类型
    public static String INDUSTRY = "IndustryEntity";//行业
    public static String FILTRATETYPE = "FiltrateType";//筛选类型

    private String names = "CustPublicPool,CustSource,CustState,RecordTypeList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        PopupwindowTypeListAdapter.savePosionLeft = -1;
        PopupwindowTypeListAdapter.TYPE_APPLY = 101;
        ButterKnife.bind(this);
        setTitle("客户");
        initLeftTypeData();
        initListData();
        refresh();
        layoutProgress.showLoading();
    }

    /**
     * 初始化列表
     */
    private void initListData() {
        getTypeList(names);
        getIndustryCatalog();
        mList = new ArrayList<>();
        mCustomAdapter = new CustomAdapter(this, mList);
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnPullListener(new PullRefreshListView.OnPullListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoad() {
                getCustomList();
            }
        });
    }

    //初始化左边选项
    private void initLeftTypeData() {
        listLeft = new ArrayList<>();
        listLeft.add(new ApprovalEntity(1, "全部客户"));
        listLeft.add(new ApprovalEntity(2, "我跟进的客户"));
        listLeft.add(new ApprovalEntity(3, "我创建的客户"));
        listLeft.add(new ApprovalEntity(4, "已成交的客户"));
        listLeft.add(new ApprovalEntity(5, "无效的客户"));
        listRight = new ArrayList<>();
        String types = PreferencesUtils.getString(this, FILTRATETYPE);
        listRight = JsonUtil.fromJsonList(types, StaticTypeEntity.class);
    }

    @OnClick({R.id.layout_left, R.id.layout_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                setPopupWindow(mLeft, mLeft.getWidth(), listLeft, "类型", 0, mLeft);
                break;
            case R.id.layout_right:
                setPopupWindowLayout(mRight, mRight.getWidth(), listRight, "状态", 1, mLeft);
                break;
        }
    }

    //类型item的点击事件
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id == 0) {
                ApprovalEntity entity = listLeft.get(position);
                mLeft.setText(entity.getName());
                type = entity.getTypeid();
                refresh();
                mListView.startFirst();
                PopupwindowTypeListAdapter.savePosionLeft = position;

            }
        }
    };

    //筛选点击回调
    private ICustomListener iListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            Intent intent;
            switch (obj0) {
                case 0://添加筛选条件
                    intent = new Intent(CustomActivity.this, AddFiltrateActivity.class);
                    startActivity(intent);
                    break;
                case 1://选择跟进人
                    intent = new Intent(CustomActivity.this, ContactsChoseActivity.class);
                    //intent.putExtra("list", mListContacts);
                    intent.putExtra("type", 2);
                    startActivityForResult(intent, Flag_Callback_ContactsPicker);
                    break;
                case 2://重置
                    setClick(0);
                    break;
                case 3://确定
                    setClick(1);
                    mPopupWindowFiltrate.dismiss();
                    break;
                case 4://省市区
                    intent = new Intent(CustomActivity.this, ProvinceActivity.class);
                    startActivityForResult(intent, Flag_Callback_Province);
                    break;
                case 5://重置并刷新
                    mFileName = "";
                    mFileValue = "";
                    refresh();
                    mPopupWindowFiltrate.dismiss();
                    break;
                default:
                    break;
            }
        }
    };





    /**
     * 筛选: 重置 或 确定 点击事件
     *
     * @param type
     */
    public void setClick(int type) {
        if (type == 0) {//重置
            String typeName = CustomFiltrateSave.itemSelect;
            switch (typeName) {
                case "CustName"://客户名称
                    CustomFiltrateSave.mCustomName = "";
                    break;
                case "CustState"://客户状态
                    CustomFiltrateSave.mStateSelect = "";
                    break;
                case "CreateTime"://创建时间
                    CustomFiltrateSave.creatTime = CustomFiltrateSave.date;
                    break;
                case "UpdateTime"://更新时间
                    CustomFiltrateSave.updateTime = CustomFiltrateSave.date;
                    break;
                case "FollowUser"://跟进人----------------
                    CustomFiltrateSave.peopleName = "请选择跟进人";
                    CustomFiltrateSave.peopleId = "";
                    if (!StringUtils.isEmpty(mListContacts)) {
                        mListContacts.clear();
                    } else {
                        mListContacts = new ArrayList<>();
                    }
                    break;
                case "region"://省市区----------------
                    CustomFiltrateSave.mProvinceName = "";
                    CustomFiltrateSave.mCityName = "";
                    CustomFiltrateSave.mRegionName = "";
                    CustomFiltrateSave.mProvinceId = "";
                    CustomFiltrateSave.mCityId = "";
                    CustomFiltrateSave.mRegionId = "";
                    break;
                case "Address"://地址
                    CustomFiltrateSave.mCustomAddress = "";
                    break;
                case "Telephone"://电话
                    CustomFiltrateSave.mCustomPhone = "";
                    break;
                case "Source"://来源
                    CustomFiltrateSave.mSourceSelect = "";
                    break;
                case "Industry"://行业
                    CustomFiltrateSave.mTradeSelect = "";
                    break;
                case "Remark"://备注
                    CustomFiltrateSave.mNote = "";
                    break;
                default:
                    break;
            }

            mPopupWindowFiltrate.refresh(typeName);
        } else {//确定
            mFileName = CustomFiltrateSave.itemSelect;
            switch (CustomFiltrateSave.itemSelect) {
                case "CustName"://客户名称
                    if (StringUtils.isEmpty(CustomFiltrateSave.mCustomName)) {
                        MyToast.showToast("请填写客户名称");
                        return;
                    }
                    mFileValue = CustomFiltrateSave.mCustomName;
                    mCondition = "包含";
                    break;
                case "CustState"://客户状态
                    mFileValue = CustomFiltrateSave.mStateSelect;
                    mCondition = "等于";
                    break;
                case "CreateTime"://创建时间
                    mFileValue = CustomFiltrateSave.creatTime;
                    mCondition = "等于";
                    break;
                case "UpdateTime"://更新时间
                    mFileValue = CustomFiltrateSave.updateTime;
                    mCondition = "等于";
                    break;
                case "FollowUser"://跟进人----------------
                    mFileValue = CustomFiltrateSave.peopleId;
                    mCondition = "包含";
                    break;
                case "region"://省市区----------------
//                    CustomFiltrateSave.mProvinceId;
//                    CustomFiltrateSave.mCityId;
//                    CustomFiltrateSave.mRegionId;
                    break;
                case "Address"://地址
                    mFileValue = CustomFiltrateSave.mCustomAddress;
                    mCondition = "包含";
                    break;
                case "Telephone"://电话
                    mFileValue = CustomFiltrateSave.mCustomPhone;
                    mCondition = "等于";
                    break;
                case "Source"://来源
                    mFileValue = CustomFiltrateSave.mSourceSelect;
                    mCondition = "等于";
                    break;
                case "Industry"://行业
                    mFileValue = CustomFiltrateSave.mTradeSelect;
                    mCondition = "等于";
                    break;
                case "Remark"://备注
                    mFileValue = CustomFiltrateSave.mNote;
                    mCondition = "包含";
                    break;
                default:
                    break;
            }
            mListView.startFirst();
            refresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Flag_Callback_ContactsPicker) {//选人
            mListContacts = (ArrayList<Contacts>) data.getExtras().getSerializable(ContactsChoseActivity.Flag_Output_Checked);
            String content = data.getStringExtra("name");
            CustomFiltrateSave.peopleName = content;
            CustomFiltrateSave.peopleId = data.getIntExtra("id", 0) + "";
            mPopupWindowFiltrate.refreshPeopleTextView(1);
        }
        if (requestCode == Flag_Callback_CustomDetails) {//详情
            mListView.startFirst();
            refresh();
        }
        if (requestCode == Flag_Callback_NewCustom) {//新建
            mListView.startFirst();
            refresh();
        }
        if (requestCode == Flag_Callback_Province) {//省市区
            CustomFiltrateSave.mProvinceName = data.getStringExtra("province");
            CustomFiltrateSave.mCityName = data.getStringExtra("city");
            CustomFiltrateSave.mRegionName = data.getStringExtra("region");
            CustomFiltrateSave.mProvinceId = data.getStringExtra("provinceId");
            CustomFiltrateSave.mCityId = data.getStringExtra("cityId");
            CustomFiltrateSave.mRegionId = data.getStringExtra("regionId");
            mPopupWindowFiltrate.refreshPeopleTextView(2);
        }
        if (requestCode == Item_Callback) {
            mListView.startFirst();
            refresh();
        }
    }

    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (KeyEvent.CLIENTFILTRATE.equals(eventCustom.getTag())) {//客户界面筛选
            listRight = (List<StaticTypeEntity>) eventCustom.getObj();
            PreferencesUtils.putString(this, FILTRATETYPE, JsonUtil.toJsonString(listRight));
            mPopupWindowFiltrate.refreshListViewUI(listRight);
        }

        if (eventCustom.getTag().equals(ScheduleActivity.SUCCESS2)) {
            refresh();
        }

    }

    //设置PopupWindow(ListView)
    public void setPopupWindow(View view, float width, List<ApprovalEntity> list, String type, long flag, View location) {
        PopupWindowType mpopupWindowMeetingType = new PopupWindowType(this, view, width, listener, list, type, flag, 1);
        ColorDrawable dw1 = new ColorDrawable(0xffffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        mpopupWindowMeetingType.setBackgroundDrawable(dw1);
        mpopupWindowMeetingType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLeft.setTextColor(getResources().getColor(R.color.app_primary_text));
            }
        });
        //设置layout在PopupWindow中显示的位置
        mpopupWindowMeetingType.showAsDropDown(location, 1, 1);
        mLeft.setTextColor(getResources().getColor(R.color.app_listview_slidemenu_1));
    }

    //设置PopupWindow
    public void setPopupWindowLayout(View view, float width, List<StaticTypeEntity> list, String type, long flag, View location) {
        mPopupWindowFiltrate = new PopupWindowFiltrate(this, iListener, list, getSupportFragmentManager());
        ColorDrawable dw1 = new ColorDrawable(0xffffffff);
        mPopupWindowFiltrate.setBackgroundDrawable(dw1);
        mPopupWindowFiltrate.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mRight.setTextColor(getResources().getColor(R.color.app_primary_text));
            }
        });
        mPopupWindowFiltrate.showAsDropDown(location, 1, 1);
        mRight.setTextColor(getResources().getColor(R.color.app_listview_slidemenu_1));
    }

    /**
     * 刷新
     */
    public void refresh() {
        mPage = 1;
        getCustomList();
    }

    /**
     * 获取客户列表
     */
    public void getCustomList() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getCustomList(ssid, currentCompanyId, type, mFileName, mFileValue, mCondition, mPage, mPageSize);
        callRequest(call, new HttpCallBack(CustomListEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    layoutProgress.showContent();
                    List<CustomListEntity> list = mResult.getRows();
                    if (mPage == 1) {
                        mList.clear();
                    }
                    mPage++;
                    mList.addAll(list);
                    setMore(list);
                    mCustomAdapter.notifyDataSetChanged();
                    mListView.onRefreshFinish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                    mListView.onRefreshFinish();
                    layoutProgress.showError(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refresh();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
                mListView.onRefreshFinish();
                layoutProgress.showError(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh();
                    }
                });
            }
        });
    }

    /**
     * 显示加载更多
     *
     * @param mResult
     */
    private void setMore(List mResult) {
        if (mResult == null) {
            mListView.setHasMoreData(true);
            return;
        }
        int pageTotal = mResult.size();
        if (pageTotal >= mPageSize) {
            mListView.setHasMoreData(true);
            mListView.setPullLoadEnabled(true);
        } else {
            mListView.setHasMoreData(false);
            mListView.setPullLoadEnabled(false);
        }
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1Icon(R.mipmap.menu_creat_blog);
        setRight1("添加");
        setRight1Show(true);
        setRight2Icon(R.mipmap.nav_search);
        setRight2("搜索");
        setRight2Show(true);
    }


    //添加
    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
//        List<String> list = new ArrayList<>();
//        list.add("名片扫描");
//        list.add("手动输入");
//        ShowPopupWindow(list, new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        MyToast.showToast("该功能将在稍后版本提供");
//                        break;
//                    case 1:
//                        startActivity(new Intent(CustomActivity.this, NewCustomActivity.class));
//                        break;
//                }
//            }
//        });
        startActivityForResult(new Intent(CustomActivity.this, NewCustomActivity.class), Flag_Callback_NewCustom);
    }

    //搜索
    @Override
    public void actionRight2(MenuItem menuItem) {
        super.actionRight2(menuItem);
        startActivity(new Intent(CustomActivity.this, CustomSearchActivity.class));
    }

    /**
     * 列表点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CustomDetailsActivity.class);
        intent.putExtra("id", mList.get(position).getID());
        intent.putExtra("name", mList.get(position).getCustName());
        CUSTOMID = mList.get(position).getID();
        startActivityForResult(intent, Item_Callback);
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

    /**
     * 根据name获取筛选下拉框列表
     *
     * @param typeName
     */
    public void getTypeList(final String typeName) {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, typeName);
        final Call call = HttpRequest.getIResourceOANetAction().getStaticList(ssid, currentCompanyId, typeName);
        callRequest(call, new HttpCallBack(StaticTypeEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    List<StaticTypeEntity> list = mResult.getRows();
                    List<StaticTypeEntity> poolList = new ArrayList<StaticTypeEntity>();
                    List<StaticTypeEntity> sourceList = new ArrayList<StaticTypeEntity>();
                    List<StaticTypeEntity> stateList = new ArrayList<StaticTypeEntity>();
                    List<StaticTypeEntity> typeList = new ArrayList<StaticTypeEntity>();
                    for (int i = 0; i < list.size(); i++) {
                        switch (list.get(i).getName()) {
                            case "CustPublicPool":
                                poolList.add(list.get(i));
                                break;
                            case "CustSource":
                                sourceList.add(list.get(i));
                                break;
                            case "CustState":
                                stateList.add(list.get(i));
                                break;
                            case "RecordTypeList":
                                typeList.add(list.get(i));
                                break;
                            default:
                                break;
                        }
                    }
                    String poolStr = JsonUtil.toJsonString(poolList);
                    PreferencesUtils.putString(CustomActivity.this, CUSTPUBLICPOOL, poolStr);
                    String sourceStr = JsonUtil.toJsonString(sourceList);
                    PreferencesUtils.putString(CustomActivity.this, CUSTSOURCE, sourceStr);
                    String stateStr = JsonUtil.toJsonString(stateList);
                    PreferencesUtils.putString(CustomActivity.this, CUSTSTATE, stateStr);
                    String typeListStr = JsonUtil.toJsonString(typeList);
                    PreferencesUtils.putString(CustomActivity.this, RECORDTYPELIST, typeListStr);
                } else {
                    Log.d("下拉列表getStatic:", mResult.getSuccess() + "----" + mResult.getMsg() + "----" + "Name:" + typeName);
                }
            }

            @Override
            public void onFailure(String string) {
                Log.d("下拉列表getStatic:", "网络异常");
            }
        });
    }

    /**
     * 获取行业列表
     */
    public void getIndustryCatalog() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID, "");
        Call call = HttpRequest.getIResourceOANetAction().getIndustryCatalog(ssid, currentCompanyId);
        callRequest(call, new HttpCallBack(IndustryEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    List<IndustryEntity> list = mResult.getRows();
                    List<StaticTypeEntity> industryList = new ArrayList<StaticTypeEntity>();
                    for (int i = 0; i < list.size(); i++) {
                        StaticTypeEntity entity = new StaticTypeEntity();
                        entity.setKey(list.get(i).getParentCatalogCode());
                        entity.setValue(list.get(i).getParentCatalogName());
                        industryList.add(entity);
                    }

                    String industryStr = JsonUtil.toJsonString(industryList);
                    PreferencesUtils.putString(CustomActivity.this, INDUSTRY, industryStr);
                } else {
                    Log.d("下拉列表getStatic:", mResult.getSuccess() + "----" + mResult.getMsg() + "行业");
                }
            }

            @Override
            public void onFailure(String string) {
                Log.d("下拉列表getStatic:", "网络异常");
            }
        });
    }

}
