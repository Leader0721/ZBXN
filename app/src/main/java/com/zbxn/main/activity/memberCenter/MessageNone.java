package com.zbxn.main.activity.memberCenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pub.base.BaseActivity;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.zbxn.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/12/21.
 */
public class MessageNone extends BaseActivity {
    @BindView(R.id.imageView_Open)
    ImageView imageViewOpen;
    @BindView(R.id.imageView_OpenNight)
    ImageView imageViewOpenNight;
    @BindView(R.id.imageView_Close)
    ImageView imageViewClose;
    private  int MESSAGENONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagenone);
        ButterKnife.bind(this);
        setTitle("消息免打扰");
        MESSAGENONE = PreferencesUtils.getInt(this,"MESSAGENONE",3);
        setView();
    }

    //根据SharedPreference 中的数据进行相应的视图设置
    private void setView(){
        switch (MESSAGENONE){
            case 1:
                JPushInterface.stopPush(getApplicationContext());
                imageViewOpen.setVisibility(View.VISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                break;
            case 2:
                JPushInterface.setSilenceTime(getApplicationContext(),22,0,8,0);
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.VISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                break;
            case 3:
                JPushInterface.resumePush(getApplicationContext());
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.VISIBLE);
                break;
            default:
                JPushInterface.resumePush(getApplicationContext());
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.mMessageNoneOpen, R.id.mMessageNoneOpenNight, R.id.mMessageNoneClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mMessageNoneOpen:
                JPushInterface.stopPush(getApplicationContext());
                imageViewOpen.setVisibility(View.VISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                PreferencesUtils.putInt(this,"MESSAGENONE",1);
                break;
            case R.id.mMessageNoneOpenNight:
                JPushInterface.setSilenceTime(getApplicationContext(),22,0,8,0);
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.VISIBLE);
                imageViewClose.setVisibility(View.INVISIBLE);
                PreferencesUtils.putInt(this,"MESSAGENONE",2);
                break;
            case R.id.mMessageNoneClose:
                JPushInterface.resumePush(getApplicationContext());
                imageViewOpen.setVisibility(View.INVISIBLE);
                imageViewOpenNight.setVisibility(View.INVISIBLE);
                imageViewClose.setVisibility(View.VISIBLE);
                PreferencesUtils.putInt(this,"MESSAGENONE",3);
                break;
        }
    }
}
