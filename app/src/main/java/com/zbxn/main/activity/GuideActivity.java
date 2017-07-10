package com.zbxn.main.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.widget.smarttablayout.SmartTabLayout;
import com.pub.widget.smarttablayout.utils.ViewPagerItem;
import com.pub.widget.smarttablayout.utils.ViewPagerItemAdapter;
import com.pub.widget.smarttablayout.utils.ViewPagerItems;
import com.zbxn.R;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页面
 * Created by ysj on 2016/8/16.
 */
public class GuideActivity extends Activity {

    @BindView(R.id.guide_viewpager)
    ViewPager guideViewPager;
    @BindView(R.id.guide_button)
    RelativeLayout guideButton;
    @BindView(R.id.guide_jump)
    TextView guideJump;
    @BindView(R.id.guide_text_title)
    TextView guideTextTitle;
    @BindView(R.id.guide_text_content)
    TextView guideTextContent;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;

    private List<ImageView> images;
    private int[] imageId = {R.mipmap.guid_01, R.mipmap.guid_02, R.mipmap.guid_03};
    private GuideAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
        addAdapter();
    }

    private void addAdapter() {
        adapter = new GuideAdapter(images);
        guideViewPager.setAdapter(adapter);
        guideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 != imageId.length - 1) {
                    guideButton.setVisibility(View.GONE);
                } else {
                    guideButton.setVisibility(View.VISIBLE);
                }
                switch (arg0) {
                    case 0:
                        guideTextTitle.setText("记录");
                        guideTextContent.setText(R.string.app_guider_page1);
                        break;
                    case 1:
                        guideTextTitle.setText("沟通");
                        guideTextContent.setText(R.string.app_guider_page2);
                        break;
                    case 2:
                        guideTextTitle.setText("五的N次方");
                        guideTextContent.setText(R.string.app_guider_page4);
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initView() {
        guideTextTitle.setText("记录");
        guideTextContent.setText(R.string.app_guider_page1);

        images = new ArrayList<>();
        for (int i = 0; i < imageId.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(imageId[i]);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            images.add(iv);
        }

        ViewPagerItems pagers = new ViewPagerItems(this);
        for (int i = 0; i < imageId.length; i++) {
            pagers.add(ViewPagerItem.of("", images.size()));
            ViewPagerItemAdapter adapter = new ViewPagerItemAdapter(pagers);
            guideViewPager.setAdapter(adapter);
            viewpagertab.setViewPager(guideViewPager);
        }
    }

    @OnClick({R.id.guide_jump, R.id.guide_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guide_jump:
            case R.id.guide_button:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
