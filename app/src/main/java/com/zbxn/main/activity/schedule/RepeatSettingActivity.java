package com.zbxn.main.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.utils.MyToast;
import com.pub.utils.StringUtils;
import com.zbxn.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/13.
 */
public class RepeatSettingActivity extends BaseActivity {
    @BindView(R.id.tt_timeType)
    TextView ttTimeType;
    @BindView(R.id.number_picker)
    TimePickerView numberPicker;
    @BindView(R.id.timeType_picker)
    TimePickerView timeTypePicker;
    List<String> numberList = new ArrayList<>();
    List<String> timeTypeList = new ArrayList<>();
    @BindView(R.id.tt_endTime)
    TextView ttEndTime;
    @BindView(R.id.linear_end)
    RelativeLayout linearEnd;
    @BindView(R.id.container)
    LinearLayout container;
    private String timeType = "周";
    private static final int End_CallBack = 1001;//结束类型
    String[] list = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private String RepeatType = "1"; //重复类型
    private String Frequence = "2";   //重复频率
    private String WeekStr = "";   //重复周字符串
    private String FinishType = "0";//结束方式
    private String FinishTimes = "";//结束次数
    private String FinishTime = "";//结束时间
    private Intent industry;
    private boolean IsWeekEmpty = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeatsetting);
        setTitle("重复");
        ButterKnife.bind(this);
        ttEndTime.setText("永不");
        initView();
    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1("确定");
        setRight1Show(true);
        setRight2Show(false);
        setRight1Icon(0);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {


        super.actionRight1(menuItem);
        if (RepeatType.equals("1")) {
            for (int i = 0; i < container.getChildCount(); i++) {
                View view = container.getChildAt(i);
                ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
                if (imageView.getVisibility() == View.VISIBLE) {
                    IsWeekEmpty = true;
                    if (container.getChildCount() == 1) {
                        WeekStr = WeekStr + (i + 1) + "";

                    } else {
                        if (i == container.getChildCount() - 1) {
                            WeekStr = WeekStr + (i + 1) + "";
                        } else {
                            WeekStr = WeekStr + (i + 1) + ",";
                        }
                    }
                }
            }
        } else {
            WeekStr = "";
        }

        industry = new Intent();

        if (RepeatType.equals("1")) {

        }else {
            WeekStr = "";
        }
        industry.putExtra("RepeatType", RepeatType);
        industry.putExtra("Frequency", "" + Frequence);
        industry.putExtra("WeekStr", "" + WeekStr);



        if (RepeatType.equals("1")){
            if (IsWeekEmpty){
                //下面三个从下个Activtiy中获取
                industry.putExtra("FinishType", "" + FinishType);
                industry.putExtra("FinishTimes", "" + FinishTimes);
                industry.putExtra("FinishTime", "" + FinishTime);
                setResult(RESULT_OK, industry);
                finish();
            }else {
                MyToast.showToast("一周至少选择一天");
            }
        }else {
            //下面三个从下个Activtiy中获取
            industry.putExtra("FinishType", "" + FinishType);
            industry.putExtra("FinishTimes", "" + FinishTimes);
            industry.putExtra("FinishTime", "" + FinishTime);
            setResult(RESULT_OK, industry);
            finish();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == End_CallBack) {
            ttEndTime.setText(data.getStringExtra("endType"));
            FinishType = data.getStringExtra("FinishType");
            FinishTimes = data.getStringExtra("FinishTimes");
            FinishTime = data.getStringExtra("FinishTime");
        }
    }

    @OnClick(R.id.linear_end)
    public void onClick() {
        Intent intent = new Intent(RepeatSettingActivity.this, EndTypeActivity.class);
        startActivityForResult(intent, End_CallBack);
    }


    private void initView() {
        Calendar c = Calendar.getInstance();
        String week = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        for (int i = 0; i < list.length; i++) {
            final View view = LayoutInflater.from(this).inflate(R.layout.linear_item_switch, null);
            final TextView name = (TextView) view.findViewById(R.id.myRemind);
            final ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView.getVisibility() == View.VISIBLE) {
                        imageView.setVisibility(View.INVISIBLE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            });
            name.setText(list[i]);
            container.addView(view);
        }

        if ("1".equals(week)) {
            View view = container.getChildAt(6);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setVisibility(View.VISIBLE);
        } else if ("2".equals(week)) {
            View view = container.getChildAt(0);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setVisibility(View.VISIBLE);
        } else if ("3".equals(week)) {
            View view = container.getChildAt(1);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setVisibility(View.VISIBLE);
        } else if ("4".equals(week)) {
            View view = container.getChildAt(2);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setVisibility(View.VISIBLE);
        } else if ("5".equals(week)) {
            View view = container.getChildAt(3);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setVisibility(View.VISIBLE);
        } else if ("6".equals(week)) {
            View view = container.getChildAt(4);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setVisibility(View.VISIBLE);
        } else if ("7".equals(week)) {
            View view = container.getChildAt(5);
            ImageView imageView = (ImageView) view.findViewById(R.id.mImageview);
            imageView.setImageResource(View.VISIBLE);
        }


        for (int i = 52; i < 100; i++) {
            numberList.add(i - 52, i + "");
        }
        for (int i = 1; i < 52; i++) {
            numberList.add(i + 47, i + "");
        }
        timeTypeList.add("日");
        timeTypeList.add("月");
        timeTypeList.add("周");
        timeTypeList.add("年");
        numberPicker.setData(numberList);
        timeTypePicker.setData(timeTypeList);
        ttTimeType.setText("每 " + 2 + " 周");
        numberPicker.setOnSelectListener(new TimePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Frequence = text;
                ttTimeType.setText("每 " + Frequence + " " + timeType);
            }
        });
        timeTypePicker.setOnSelectListener(new TimePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                timeType = text;
                ttTimeType.setText("每 " + Frequence + " " + timeType);
                if (timeType.contains("周")) {
                    container.setVisibility(View.VISIBLE);
                } else {
                    container.setVisibility(View.GONE);
                }
                if (timeType.contains("日")) {
                    RepeatType = 0 + "";
                } else if (timeType.contains("周")) {
                    RepeatType = 1 + "";
                } else if (timeType.contains("月")) {
                    RepeatType = 2 + "";
                } else if (timeType.contains("年")) {
                    RepeatType = 3 + "";
                }
            }
        });
    }

}
