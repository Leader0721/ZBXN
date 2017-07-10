package com.zbxn.main.activity.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseActivity;
import com.pub.common.ToolbarParams;
import com.pub.dialog.MessageDialog;
import com.pub.utils.ConfigUtils;
import com.pub.utils.ScreenUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.zbxn.R;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 联系人详情
 *
 * @author GISirFive
 * @time 2016/8/10
 */
public class ContactsDetailActivity extends BaseActivity {

    public static final String Flag_Input_Contactor = "contactor";

    @BindView(R.id.mScrollView)
    PullToZoomScrollViewEx mScrollView;
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;
    @BindView(R.id.mRemarkName)
    TextView mRemarkName;
    @BindView(R.id.mCompanyName)
    TextView mCompanyName;
    @BindView(R.id.mDepartmentName)
    TextView mDepartmentName;
    @BindView(R.id.mPosition)
    TextView mPosition;
    @BindView(R.id.mMobileNumber)
    TextView mMobileNumber;
    @BindView(R.id.mPhoneNumber)
    TextView mPhoneNumber;
    @BindView(R.id.mEmail)
    TextView mEmail;
    @BindView(R.id.mUserNumber)
    TextView mUserNumber;

    private Contacts mContacts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ButterKnife.bind(this);
        mContacts = (Contacts) getIntent().getSerializableExtra(Flag_Input_Contactor);

        if (mContacts == null) {
            finish();
        }

        init();
        refreshUI();
    }

    @Override
    public boolean onToolbarConfiguration(Toolbar toolbar, ToolbarParams params) {
        toolbar.setTitle("同事");
        params.overlay = true;
        params.shadowEnable = false;
        return super.onToolbarConfiguration(toolbar, params);
    }

    @OnClick({R.id.mLayoutEmail, R.id.mLayoutMobile, R.id.mLayoutPhone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLayoutEmail:
                if (!TextUtils.isEmpty(mContacts.getEmail())) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setType("text/plain");
                        intent.setData(Uri.parse("mailto:" + mContacts.getEmail()));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "邮件标题");
                        intent.putExtra(Intent.EXTRA_TEXT, "邮件内容");
                        startActivity(intent);
                    } catch (Exception e) {
                        MessageDialog dialog = new MessageDialog(this);
                        dialog.setTitle("提示");
                        dialog.setMessage("手机上未安装任何邮件应用");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                }
                break;
            case R.id.mLayoutMobile:
                if (!TextUtils.isEmpty(mContacts.getLoginname())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mContacts.getLoginname());
                    intent.setData(data);
                    startActivity(intent);
//                    showCallDialog(mContacts.getTelephone());
                }
                break;
            case R.id.mLayoutPhone:
                if (!TextUtils.isEmpty(mContacts.getTelephone())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + mContacts.getTelephone());
                    intent.setData(data);
                    startActivity(intent);
//                    showCallDialog(mContacts.getOfficeTelOut());
                }
                break;
        }
    }

    private void init() {
        // 调整ZoomView的高度
        int screenWidth = ScreenUtils.getScreenWidth(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                screenWidth, (int) (screenWidth * 9 / 16));
        mScrollView.setHeaderLayoutParams(params);

    }

    private void refreshUI() {
        mUserNumber.setText(mContacts.getNumber());//用户号
        mRemarkName.setText(mContacts.getUserName());//姓名   userName
        mCompanyName.setText(Member.get().getZmsCompanyName());//公司名称
        mDepartmentName.setText(mContacts.getDepartmentName());//部门
        mPosition.setText(mContacts.getPositionName());//职位

        if (!StringUtils.isEmpty(mContacts.getLoginname())) {
            mMobileNumber.setText(mContacts.getLoginname());//手机号
        } else {
            mMobileNumber.setText(" ");//手机号

        }
        String extTel = "";
        if (!StringUtils.isEmpty(mContacts.getExtTel())) {
            extTel = "-" + mContacts.getExtTel();
        }
        if (!StringUtils.isEmpty(mContacts.getLoginname())) {
            mPhoneNumber.setText(mContacts.getTelephone() + extTel);//电话
        } else {
            mPhoneNumber.setText("");//电话
        }


        mEmail.setText(mContacts.getEmail());//邮箱
        String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        ImageLoader.getInstance().displayImage(mBaseUrl + mContacts.getPortrait(), mPortrait, options);
    }


    /**
     * 显示拨打电话提示对话框
     *
     * @param number
     */
    private void showCallDialog(final String number) {
        MessageDialog dialog = new MessageDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定拨打 " + number + "?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + number);
                intent.setData(data);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消");
        dialog.show();
    }
}
