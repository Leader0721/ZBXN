package com.zbxn.main.activity.mission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.pub.utils.MyToast;
import com.pub.widget.PhotoViewPager;
import com.pub.widget.photoview.PhotoView;
import com.zbxn.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PhotoDetailActivity extends Activity {
    private RelativeLayout m_layout;
    private PhotoViewPager mViewPager;
    private ArrayList<String> list_Ads = new ArrayList<String>(); // 需要展示的图片
    private int position = 0;
    private ImageView imageViewResource;

    private TextView m_tip;
    private TextView m_finish;
    private String string = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);
        imageViewResource = new ImageView(this);
        list_Ads = getIntent().getStringArrayListExtra("list");
        position = getIntent().getIntExtra("position", 0);


        m_layout = (RelativeLayout) findViewById(R.id.m_layout);
        mViewPager = (PhotoViewPager) findViewById(R.id.viewpager);
        m_tip = (TextView) findViewById(R.id.m_tip);
        m_finish = (TextView) findViewById(R.id.m_finish);

        m_tip.setText((position + 1) + "/" + String.valueOf(list_Ads.size()));

        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setCurrentItem(position);

        m_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        m_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // 页面更变
            @Override
            public void onPageSelected(int arg0) {
                m_tip.setText(String.valueOf(arg0 + 1) + "/" + String.valueOf(list_Ads.size()));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    public void fileScan(String fName) {
        Uri data = Uri.parse("file:///" + fName);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));

//        方式一，发送一个广播，
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file:///" + getSDPath())));
//        方式二，通过MediaScannerConnection 类
        MediaScannerConnection.scanFile(this, new String[]{getSDPath().toString()}, null, null);
    }


    /**
     * 获取sd卡的缓存路径，
     * 一般在卡中sdCard就是这个目录
     *
     * @return SDPath
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
        } else {
            MyToast.showToast("手机没有SD卡");
        }
        return sdDir.toString() + "/DCIM";
    }


    private void storeInSD(Bitmap bitmap1) {
        File file = new File(getSDPath());
        if (!file.exists()) {
            file.mkdir();
        }
        string = getFileName();
        File imageFile = new File(file, string + ".png");

        try {
            imageFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap1.compress(Bitmap.CompressFormat.PNG, 50, fos);

            MyToast.showToast("保存成功");
            fileScan(string);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            MyToast.showToast("保存失败" + e);
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            MyToast.showToast("保存失败" + e);
        }
    }

    //    FileUtils类：
    public static String getCharacterAndNumber() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        return rel;
    }

    public static String getFileName() {
        String fileNameRandom = getCharacterAndNumber();
        return fileNameRandom;
    }


    @OnClick({ R.id.tv_download, R.id.ll_downloadPic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_download:
            case R.id.ll_downloadPic:
                storeInSD(mViewPager.getChildAt(0).getDrawingCache());
                scanFileAsync(this,getSDPath());
                scanDirAsync(this,getSDPath());
                break;
        }
    }

    public void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }

    //扫描指定目录
    public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    public void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }






    private class MyAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            PhotoView imageView = new PhotoView(PhotoDetailActivity.this);
            imageView.setMaximumScale(3.0f);
            imageView.setMinimumScale(1.0f);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            imageViewResource.setImageDrawable(imageView.getDrawable());
            // 设置图片
            if (list_Ads.get(position).contains("http")) {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                        .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                        .imageScaleType(ImageScaleType.NONE)
                        //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                        .build();                                   // 创建配置过得DisplayImageOption对象
                ImageLoader.getInstance().displayImage(list_Ads.get(position), imageView, options);
//                ImageLoader.getInstance().displayImage(list_Ads.get(position), imageView);
            } else {
                try {
                    //是图片
//                    标红的代码如不设置，则会使用默认的设置，为ImageScaleType.IN_SAMPLE_POWER_OF_2
//                    imageScaleType(ImageScaleType imageScaleType)
//                    imageScaleType:
//                    EXACTLY :图像将完全按比例缩小的目标大小
//                    EXACTLY_STRETCHED:图片会缩放到目标大小完全
//                    IN_SAMPLE_INT:图像将被二次采样的整数倍
//                    IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
//                    NONE:图片不会调整

                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                            .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                            .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                            .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                            .imageScaleType(ImageScaleType.NONE)
                            //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                            .build();                                   // 创建配置过得DisplayImageOption对象

                    ImageLoader.getInstance().displayImage("file:///" + list_Ads.get(position), imageView, options);
                } catch (Exception e) {

                }
            }
            container.addView(imageView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public int getCount() {
            return list_Ads.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((PhotoViewPager) container).removeView((View) object);
        }
    }
}
