package com.zbxn.main.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.NoScrollListview;
import com.pub.widget.slidedatetimepicker.NewSlideDateTimeDialogFragment;
import com.pub.widget.slidedatetimepicker.SlideDateTimeListener;
import com.pub.widget.slidedatetimepicker.SlideDateTimePicker;
import com.zbxn.R;
import com.zbxn.crm.activity.custom.CustomActivity;
import com.zbxn.crm.activity.custom.CustomFiltrateSave;
import com.zbxn.crm.adapter.FiltratePopupAdapter;
import com.zbxn.crm.adapter.SelectItemAdapter;
import com.zbxn.crm.entity.StaticTypeEntity;
import com.zbxn.main.listener.ICustomListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: ysj
 * @date: 2017-01-10 13:25
 */
public class PopupWindowFiltrate extends PopupWindow {

    private TextView mSubmit;
    private TextView mRest;
    private TextView mAdd;
    private NoScrollListview mLeftListView;
    private EditText mEditTextRight;
    private ListView mRightListView;
    private TextView mTimeSelect;
    private ImageView mImgPeople;
    private LinearLayout mLayoutTime;

    private View mView;
    private ICustomListener mListener;
    //左边的ligt
    private List<StaticTypeEntity> mList;
    private FiltratePopupAdapter mAdapter;
    private SelectItemAdapter selectAdapter;
    private List<StaticTypeEntity> mRightList;
    private Context mContext;
    //客户状态
    private List<StaticTypeEntity> mCustomStateList;
    //来源
    private List<StaticTypeEntity> mCustomSourceList;
    //行业
    private List<StaticTypeEntity> mCustomTradeList;
    private FragmentManager mManager;
    private String mDate;//时间
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public PopupWindowFiltrate(final Activity context, final ICustomListener listener, final List<StaticTypeEntity> list
            , final FragmentManager mManager) {
        super(context);
        this.mListener = listener;
        this.mList = list;
        this.mContext = context;
        this.mManager = mManager;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.popupwindow_filtrate, null);
        initView();
        initListData();


        //右边的listview
        mRightList = new ArrayList<>();
        selectAdapter = new SelectItemAdapter(context, mRightList);
        mRightListView.setAdapter(selectAdapter);
        mRightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置选中时的img
                switch (CustomFiltrateSave.itemSelect) {
                    case "CustState"://客户状态
                        CustomFiltrateSave.mStateSelect = mRightList.get(position).getKey();
                        break;
                    case "Source"://来源
                        CustomFiltrateSave.mSourceSelect = mRightList.get(position).getKey();
                        break;
                    case "Industry"://行业
                        CustomFiltrateSave.mTradeSelect = mRightList.get(position).getKey();
                        break;
                    default:
                        break;
                }
                selectAdapter.notifyDataSetChanged();
            }
        });

        //左边的listview
        mAdapter = new FiltratePopupAdapter(mList, context);
        mLeftListView.setAdapter(mAdapter);
        //左边的listview点击事件
        mLeftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //设置选中时的颜色
                CustomFiltrateSave.itemSelect = mList.get(position).getKey();
                mAdapter.notifyDataSetChanged();
                //类别
                String typeId = mList.get(position).getKey();
                setRightListItem(typeId);
            }
        });

        //添加筛选项
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCustomListener(0, null, 0);
            }
        });

        //重置
        mRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLeftListView.getCount() == 0) {
                    setViewGone();
                    return;
                }
                listener.onCustomListener(2, null, 0);
            }
        });

        //确定
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果没有筛选条件
                if (mLeftListView.getCount() == 0) {
                    listener.onCustomListener(5, null, 0);
                    return;
                }
                if (mEditTextRight.getVisibility() == View.VISIBLE) {
                    if (StringUtils.isEmpty(mEditTextRight)) {
                        MyToast.showToast("请填写筛选内容");
                        return;
                    }
                }
                listener.onCustomListener(3, null, 0);
            }
        });

        //选择时间以及跟进人
        mLayoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CustomFiltrateSave.itemSelect.equals("CreateTime")) {//创建时间
                    new SlideDateTimePicker.Builder(mManager).setListener(new SlideDateTimeListener() {
                        @Override
                        public void onDateTimeSet(Date date) {
                            CustomFiltrateSave.creatTime = format.format(date);
                            mTimeSelect.setText(format.format(date));
                        }
                    }).setInitialDate(StringUtils.convertToDate(format, StringUtils.getEditText(mTimeSelect)))
                            .setIs24HourTime(true)
                            .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                            .build()
                            .show();
                } else if (CustomFiltrateSave.itemSelect.equals("UpdateTime")) {//更新时间
                    new SlideDateTimePicker.Builder(mManager).setListener(new SlideDateTimeListener() {
                        @Override
                        public void onDateTimeSet(Date date) {
                            CustomFiltrateSave.updateTime = format.format(date);
                            mTimeSelect.setText(format.format(date));
                        }
                    }).setInitialDate(StringUtils.convertToDate(format, StringUtils.getEditText(mTimeSelect)))
                            .setIs24HourTime(true)
                            .setIsHaveTime(NewSlideDateTimeDialogFragment.Have_Date)
                            .build()
                            .show();
                } else if (CustomFiltrateSave.itemSelect.equals("FollowUser")) {//跟进人
                    listener.onCustomListener(1, null, 0);
                } else if (CustomFiltrateSave.itemSelect.equals("region")) {//省市区
                    listener.onCustomListener(4, null, 0);
                }
            }
        });

        mEditTextRight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (CustomFiltrateSave.itemSelect) {
                    case "CustName"://客户名称
                        CustomFiltrateSave.mCustomName = s.toString();
                        break;
                    case "Address"://地址
                        CustomFiltrateSave.mCustomAddress = s.toString();
                        break;
                    case "Telephone"://电话
                        CustomFiltrateSave.mCustomPhone = s.toString();
                        break;
                    case "Remark"://备注
                        CustomFiltrateSave.mNote = s.toString();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //如果mList不为空 加载保存的或默认布局
        if (!StringUtils.isEmpty(mList)) {
            if (!StringUtils.isEmpty(CustomFiltrateSave.itemSelect)) {
                //设置右边layout布局样式
                setRightListItem(CustomFiltrateSave.itemSelect);
            } else {
                setRightListItem(mList.get(0).getKey());
            }
        } else {
            setViewGone();
        }

        setData();
    }


    /**
     * 设置右边layout布局样式
     *
     * @param type
     */
    private void setRightListItem(String type) {
        setViewGone();
        switch (type) {
            case "CustName"://客户名称
                mEditTextRight.setVisibility(View.VISIBLE);
                mEditTextRight.setText(CustomFiltrateSave.mCustomName);
                mEditTextRight.setHint("请输入客户名称");
                break;
            case "CustState"://客户状态
                mRightListView.setVisibility(View.VISIBLE);
                mRightList.clear();
                mRightList.addAll(mCustomStateList);
                selectAdapter.notifyDataSetChanged();
                break;
            case "CreateTime"://创建时间
                mLayoutTime.setVisibility(View.VISIBLE);
                mTimeSelect.setVisibility(View.VISIBLE);
                mTimeSelect.setText(CustomFiltrateSave.creatTime);
                break;
            case "UpdateTime"://更新时间
                mLayoutTime.setVisibility(View.VISIBLE);
                mTimeSelect.setVisibility(View.VISIBLE);
                mTimeSelect.setText(CustomFiltrateSave.updateTime);
                break;
            case "FollowUser"://跟进人----------------
                mLayoutTime.setVisibility(View.VISIBLE);
                mTimeSelect.setVisibility(View.VISIBLE);
                mImgPeople.setVisibility(View.VISIBLE);
                mTimeSelect.setText(CustomFiltrateSave.peopleName);
                break;
            case "region"://省市区----------------
                mLayoutTime.setVisibility(View.VISIBLE);
                mTimeSelect.setVisibility(View.VISIBLE);
                mImgPeople.setVisibility(View.VISIBLE);
                mTimeSelect.setText("省:" + CustomFiltrateSave.mProvinceName + "\n市:" + CustomFiltrateSave.mCityName
                        + "\n区/县:" + CustomFiltrateSave.mRegionName);
                break;
            case "Address"://地址
                mEditTextRight.setVisibility(View.VISIBLE);
                mEditTextRight.setText(CustomFiltrateSave.mCustomAddress);
                mEditTextRight.setHint("请输入地址");
                break;
            case "Telephone"://电话
                mEditTextRight.setVisibility(View.VISIBLE);
                mEditTextRight.setText(CustomFiltrateSave.mCustomPhone);
                mEditTextRight.setHint("请输入电话");
                break;
            case "Source"://来源
                mRightListView.setVisibility(View.VISIBLE);
                mRightList.clear();
                mRightList.addAll(mCustomSourceList);
                selectAdapter.notifyDataSetChanged();
                break;
            case "Industry"://行业
                mRightListView.setVisibility(View.VISIBLE);
                mRightList.clear();
                mRightList.addAll(mCustomTradeList);
                selectAdapter.notifyDataSetChanged();
                break;
            case "Remark"://备注
                mEditTextRight.setVisibility(View.VISIBLE);
                mEditTextRight.setText(CustomFiltrateSave.mNote);
                mEditTextRight.setHint("请输入备注");
                break;
            default:
                setViewGone();
                break;
        }
    }

    //初始化各个list的数据
    private void initListData() {
        //客户状态
        mCustomStateList = new ArrayList<>();
        String stateStr = PreferencesUtils.getString(mContext, CustomActivity.CUSTSTATE);
        mCustomStateList = JsonUtil.fromJsonList(stateStr, StaticTypeEntity.class);
        //客户来源
        mCustomSourceList = new ArrayList<>();
        String sourceStr = PreferencesUtils.getString(mContext, CustomActivity.CUSTSOURCE);
        mCustomSourceList = JsonUtil.fromJsonList(sourceStr, StaticTypeEntity.class);
        //行业
        mCustomTradeList = new ArrayList<>();
        String industryStr = PreferencesUtils.getString(mContext, CustomActivity.INDUSTRY);
        mCustomTradeList = JsonUtil.fromJsonList(industryStr, StaticTypeEntity.class);
    }

    //重置刷新
    public void refresh(String type) {
        setRightListItem(type);
    }

    //刷新PopupWindow的UI
    public void refreshListViewUI(List<StaticTypeEntity> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (!StringUtils.isEmpty(mList)) {
            //记录左边listview第一个item的typeId
            CustomFiltrateSave.itemSelect = mList.get(0).getKey();
            if (StringUtils.isEmpty(CustomFiltrateSave.itemSelect)) {
                //设置右边layout布局样式
                setRightListItem(CustomFiltrateSave.itemSelect);
            } else {
                setRightListItem(mList.get(0).getKey());
            }
        } else {
            setViewGone();
        }
    }

    public void refreshPeopleTextView(int num) {
        if (num == 1) {
            mTimeSelect.setText(CustomFiltrateSave.peopleName);
        } else {
            mTimeSelect.setText("省:" + CustomFiltrateSave.mProvinceName + "\n市:" + CustomFiltrateSave.mCityName
                    + "\n区/县:" + CustomFiltrateSave.mRegionName);
        }
    }

    //初始化view
    private void initView() {
        mRest = ((TextView) mView.findViewById(R.id.tv_reset));
        mSubmit = ((TextView) mView.findViewById(R.id.tv_submit));
        mLeftListView = ((NoScrollListview) mView.findViewById(R.id.left_listView));
        mAdd = ((TextView) mView.findViewById(R.id.tv_add));
        mEditTextRight = (EditText) mView.findViewById(R.id.et_right);
        mRightListView = ((ListView) mView.findViewById(R.id.lv_right));
        mTimeSelect = ((TextView) mView.findViewById(R.id.tv_time));
        mImgPeople = ((ImageView) mView.findViewById(R.id.img_select_people));
        mLayoutTime = ((LinearLayout) mView.findViewById(R.id.ll_time));
    }

    /**
     * 隐藏view
     */
    public void setViewGone() {
        mRightListView.setVisibility(View.GONE);
        mEditTextRight.setVisibility(View.GONE);
        mLayoutTime.setVisibility(View.GONE);
        mTimeSelect.setVisibility(View.GONE);
        mImgPeople.setVisibility(View.GONE);
    }

    //初始化popupwindow
    private void setData() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为透明
//        ColorDrawable dw = new ColorDrawable(0x00000000);
        ColorDrawable dw = new ColorDrawable(0xfff5f5f5);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });
    }

}
