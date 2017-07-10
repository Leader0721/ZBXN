package com.zbxn.main.activity.memberCenter;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zbxn.R;
import com.zbxn.main.entity.Member;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ShareActivity extends BaseActivity {

    @BindView(R.id.mQRCode)
    ImageView mQRCode;//二维码
    @BindView(R.id.mShareAll)
    TextView mShareAll;//分享QQ
    @BindView(R.id.mSharePic)
    TextView mSharePic;//分享微信

    private SHARE_MEDIA share_media = SHARE_MEDIA.ALIPAY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        setTitle("我的二维码");

        //友盟分享 适配android6.0
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    @Override
    public void initRight() {
        setRight1Show(false);
        setRight2Show(false);
        super.initRight();
    }

    @OnClick({R.id.mShareAll, R.id.mSharePic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mShareAll:
                new ShareAction(ShareActivity.this)
                        .withTitle("五的N次方")
                        .withText("享受工作的每一分")
                        .withMedia(new UMImage(ShareActivity.this, R.mipmap.ic_launcher))
                        .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).open();

//                message().show("功能建设中...");
//                new ShareAction(Share.this).setPlatform(SHARE_MEDIA.QQ)
//                        .withTitle("五的N次方")
//                        .withText("享受工作的每一分")
//                        .withMedia(new UMImage(Share.this, R.mipmap.ic_launcher))
//                        .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
//                        .setCallback(umShareListener)
//                        .share();
                break;
            case R.id.mSharePic://设置微信朋友圈分享内容
                //http://www.zbzbx.com/appdownload/AndroidIOSDownload.html
                UMImage image = new UMImage(ShareActivity.this, R.mipmap.zmes);//资源文件
                new ShareAction(ShareActivity.this)
                        .withTitle("五的N次方")
                        .withText("享受工作的每一分")
                        .withMedia(image)
                        .setDisplayList(SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).open();

//                message().show("功能建设中...");
//                new ShareAction(Share.this).setPlatform(SHARE_MEDIA.WEIXIN)
//                        .withTitle("五的N次方")
//                        .withText("享受工作的每一分")
//                        .withMedia(new UMImage(Share.this, R.mipmap.ic_launcher))
//                        .withTargetUrl("http://n.zbzbx.com/Login/Login/Register?SharerNO=" + Member.get().getNumber())
//                        .setCallback(umShareListener)
//                        .share();
                break;
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(ShareActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(Share.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            Log.e("cancael", "platform" + platform);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }


}
