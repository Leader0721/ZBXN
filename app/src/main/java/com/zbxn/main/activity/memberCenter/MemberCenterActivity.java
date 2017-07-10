package com.zbxn.main.activity.memberCenter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.dialog.InputDialog;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.http.uploadfile.BaseAsyncTaskInterface;
import com.pub.http.uploadfile.Result;
import com.pub.utils.ConfigUtils;
import com.pub.utils.DemoUtils;
import com.pub.utils.FileAccessor;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.utils.ValidationUtil;
import com.pub.widget.NoScrollListview;
import com.pub.widget.eventbus.EventMember;
import com.zbxn.R;
import com.zbxn.main.activity.mission.uploadfile.BaseAsyncTaskFile;
import com.zbxn.main.entity.Member;
import com.zbxn.main.listener.ICustomListener;
import com.zbxn.main.popupwindow.PopupWindowSelectPic;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * Created by Administrator on 2016/12/23.
 */
public class MemberCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    Member member = Member.get();
    @BindView(R.id.mSelectPortrait)
    LinearLayout mSelectPortrait;
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;
    @BindView(R.id.mNoScrollListview)
    NoScrollListview mNoScrollListview;

    private MemberInfoAdapter mAdapter;
    private InputDialog mModifyDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_membercenter);
        setTitle("个人信息");
        ButterKnife.bind(this);
        init();
        refreshUI(null);
    }


    private void init() {
        mAdapter = new MemberInfoAdapter(this, getMemberInfoWithList());
        mNoScrollListview.setAdapter(mAdapter);
        mNoScrollListview.setOnItemClickListener(this);
    }


    @OnClick({R.id.mSelectPortrait})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSelectPortrait:
                //实例化SelectPicPopupWindow
                PopupWindowSelectPic menuWindow = new PopupWindowSelectPic(MemberCenterActivity.this, listenerPic);
                //显示窗口
                menuWindow.showAtLocation(MemberCenterActivity.this.findViewById(R.id.mPortrait), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
        }
    }

    /**
     * 选择图片拍照路径
     */
    private String mFilePath;
    /**
     * request code for tack pic
     */
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x3;
    public static final int REQUEST_CODE_LOAD_IMAGE = 0x4;
    private ICustomListener listenerPic = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    int permission = ContextCompat.checkSelfPermission(MemberCenterActivity.this, Manifest.permission.CAMERA);
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        //调用照相机
                        if (!FileAccessor.isExistExternalStore()) {
                            return;
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = FileAccessor.getTackPicFilePath();
                        if (file != null) {
                            Uri uri = Uri.fromFile(file);
                            if (uri != null) {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            }
                            mFilePath = file.getAbsolutePath();
                        }
                        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
                    } else {//请求权限
                        MyToast.showToast("请设置拍照权限");
                        Intent localIntent = new Intent();
                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(localIntent);
                    }


                    break;
                case 1:
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, REQUEST_CODE_LOAD_IMAGE);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         /*回调内容*/
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PICTURE
                || requestCode == REQUEST_CODE_LOAD_IMAGE) {
            if (requestCode == REQUEST_CODE_LOAD_IMAGE) {
                mFilePath = DemoUtils.resolvePhotoFromIntent(this, data, FileAccessor.IMESSAGE_IMAGE);
            }
            if (TextUtils.isEmpty(mFilePath)) {
                return;
            }
            File file = new File(mFilePath);
            if (file == null || !file.exists()) {
                return;
            }
            File fileSmall = null;
            try {
                String filePathStr = FileAccessor.getSmallPicture(file.getPath()); // 压缩文件
                fileSmall = new File(filePathStr);
            } catch (Exception e) {
                System.out.println("上传图片错误：" + e.toString());
                e.printStackTrace();
                fileSmall = file;
            }
            uploadFile(MemberCenterActivity.this, fileSmall, mICustomListener);
            return;
        }
    }

    /**
     * 回调
     */
    private ICustomListener mICustomListener = new ICustomListener() {
        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0:
                    MyToast.showToast("修改成功");

                    String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
                    ImageLoader.getInstance().displayImage(mBaseUrl + obj1, mPortrait);

//                    mBaseUrl + obj1
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(KeyEvent.UPDATEPHOTO);
                    eventCustom.setObj(mBaseUrl + obj1);
                    EventBus.getDefault().post(eventCustom);
                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // 刷新界面
    @Subscriber
    private void refreshUI(EventMember event) {
        Member member = Member.get();
        if (member == null) {
            mPortrait.setImageResource(R.mipmap.temp100);
        } else {
            /*boolean b = PreferencesUtils.getBoolean(this,
                    SelectPortrait.Flag_Output_Portrait, true);
            Glide.with(this).load(b ? R.mipmap.temp110 : R.mipmap.temp111)
                    .error(R.mipmap.temp100).into(mPortrait);*/

            String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                    //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                    .build();                                   // 创建配置过得DisplayImageOption对象
            ImageLoader.getInstance().displayImage(mBaseUrl + member.getPortrait(), mPortrait, options);
            resetData();

        }
    }


    @Subscriber
    public void onEventMainThread(EventCustom eventCustom) {
        if (KeyEvent.UPDATEPHOTO.equals(eventCustom.getTag())) {
            //设置选中数据
            resetData();
        }
    }

    public KeyValue getItem(int position) {
        return mAdapter.getItem(position);
    }

    public void resetData() {
        mAdapter.setData(getMemberInfoWithList());
    }

    /**
     * ListView的点击事件
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 1:
            case 3:
            case 4:
//            case 6://部门不能修改
//                mPresenter.OpenModifyDialog(position);
                OpenModifyDialog(position);
                break;
            default:
//                message().show("暂时无法修改");
//                MyToast.showToast("暂时无法修改");
                break;
        }

    }


    /**
     * 打开修改对话框，弹窗方法。<Br />
     * 数据库里的值为空的时候没法修改，等待进一步的完善！
     *
     * @param position 要修改的Item在列表中的位置
     */
    public void OpenModifyDialog(final int position) {
        // 根据position获取对应的数据
        final KeyValue keyValue = getItem(position);

        mModifyDialog = new InputDialog(this);
        // 更改选项的按钮点击事件
        mModifyDialog.setNegativeButton("取消", null);
        mModifyDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        if (keyValue.getValue() != null) {
                        // 用户输入的内容
                        String content = mModifyDialog.getEditText().getText().toString();
                        // 若无改动，直接返回

                        if (content.length() > 0) {

                            if (content.equals(keyValue.getValue())) {
                                MyToast.showToast("你输入的已存在");
                                return;
                            }
                        } else {
                            MyToast.showToast("修改内容不能为空");
                        }
                        // 输入是否合法
                        boolean isValidate = false;
                        switch (position) {
                            case 1:// 判断姓名
                                isValidate = true;
                                break;
                            case 2:// 判断手机号
                                isValidate = true;
                                break;
                            case 3:// 判断电话
                                isValidate = true;
                                break;
                            case 4:// 判断邮箱
                                isValidate = ValidationUtil.isMailbox(content);
                                break;
                            case 6:// 判断联系地址
                                isValidate = true;
                                break;

                            default:
                                break;
                        }

                        if (StringUtils.isEmpty(content)) {
                            MyToast.showToast("修改内容不能为空");
                        } else if (isValidate) {// 输入合法
                            submitModify(keyValue.getParamKey(), content);
                        } else {
                            MyToast.showToast("你输入的" + keyValue.getKey() + "格式有误");
                        }

//                        }

                    }
                }

        );
        // 设置标题
        mModifyDialog.setTitle("修改" + keyValue.getKey());

        // 设置全部选中里面的内容
        if (keyValue.getValue() != null) {
            mModifyDialog.getEditText().setSelectAllOnFocus(true);
            // 显示弹出要改的内容
            mModifyDialog.show(keyValue.getValue());
        } else if (keyValue.getValue() == null) {
            // 显示弹出要改的内容
            mModifyDialog.show();
            mModifyDialog.getEditText().setHint("请输入" + keyValue.getKey());
        }
    }


    //下面是一些网络请求方法或者是从本地读取数据

    /**
     * 添加员工的基本信息包括生日的转换
     */
    private List<KeyValue> getMemberInfoWithList() {
        Member member = Member.get();
        List<KeyValue> data = new ArrayList<KeyValue>();
        data.add(new KeyValue("账号", member.getNumber(), null));
//        data.add(new KeyValue("姓名", member.getUserName(), "base.userName"));
//        data.add(new KeyValue("手机号", member.getLoginname(), "staff.loginName"));
//        data.add(new KeyValue("电话", member.getTelephone(), "staff.telephone"));
//        data.add(new KeyValue("邮箱", member.getEmail(), "base.email"));
        data.add(new KeyValue("姓名", member.getUserName(), "userName"));
        data.add(new KeyValue("手机号", member.getLoginname(), "loginName"));
        data.add(new KeyValue("电话", member.getTelephone(), "telephone"));
        data.add(new KeyValue("邮箱", member.getEmail(), "email"));
        // 时间转换 Date转换成String类型已 年-月-日的形式输出
        if (member.getBirthday() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = sdf.format(member.getBirthday());
            data.add(new KeyValue("生日", startTime, null));
        } else {
            data.add(new KeyValue("生日", null, null));
        }
        if (!StringUtils.isEmpty(member.getAddress())) {

//            data.add(new KeyValue("联系地址", member.getAddress().trim(), "staff.addressNow"));
            data.add(new KeyValue("联系地址", member.getAddress().trim(), "addressNow"));
        }

        data.add(new KeyValue("所在部门", member.getDepartmentName(), null));
        return data;
    }


    /**
     * 个人中心中
     * 上传更改后的文件
     *
     * @param context
     * @param file
     * @param listener
     */
    public void uploadFile(Context context, File file, final ICustomListener listener) {
        Map<String, String> map = new HashMap<String, String>();

        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);

        Map<String, File> mapFile = new HashMap<>();
        mapFile.put("image", file);

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);

        new BaseAsyncTaskFile(context, false, 0, server + "/common/doupload.do", new BaseAsyncTaskInterface() {
            @Override
            public void dataSubmit(int funId) {

            }

            @Override
            public Object dataParse(int funId, String json) throws Exception {
                return Result.parse(json);
            }

            @Override
            public void dataSuccess(int funId, Object result) {
                Result mResult = (Result) result;
                if ("0".equals(mResult.getSuccess())) {//0成功
                    listener.onCustomListener(0, mResult.getData(), 0);
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
                MyToast.showToast("获取网络数据失败");
            }
        }, mapFile).execute(map);
    }

    /**
     * 消息中心中进行个人信息的一个更新
     *
     * @param key   上传的数据对应的key
     * @param value 数据对应的value
     */
    public void submitModify(String key, String value) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");

        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        Call call = HttpRequest.getIResourceOA().MemberCenterUpdateUserInfo(ssid, map);
        callRequest(call, new HttpCallBack(Member.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    Member.clear();
                    Member member = (Member) mResult.getData();
                    Member.save(member);
                    MyToast.showToast("修改成功");
                } else {
                    MyToast.showToast("修改失败," + mResult.getData());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

}
