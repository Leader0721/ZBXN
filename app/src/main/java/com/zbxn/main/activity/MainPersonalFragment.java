package com.zbxn.main.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseApp;
import com.pub.base.BaseFragment;
import com.pub.dialog.MessageDialog;
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
import com.pub.widget.dbutils.DBUtils;
import com.pub.widget.eventbus.EventMember;
import com.pub.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.zbxn.R;
import com.zbxn.main.activity.jobManagement.JobManagementActivity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.memberCenter.AboutActivity;
import com.zbxn.main.activity.memberCenter.CollectCenterActivity;
import com.zbxn.main.activity.memberCenter.FeedBackActivity;
import com.zbxn.main.activity.memberCenter.IntegralActivity;
import com.zbxn.main.activity.memberCenter.IntegralDetailsActivity;
import com.zbxn.main.activity.memberCenter.MemberCenterActivity;
import com.zbxn.main.activity.memberCenter.Settings;
import com.zbxn.main.activity.memberCenter.ShareActivity;
import com.zbxn.main.activity.memberCenter.SwitchCompanyActivity;
import com.zbxn.main.activity.mission.uploadfile.BaseAsyncTaskFile;
import com.zbxn.main.activity.org.OrganizeActivity;
import com.zbxn.main.entity.Contacts;
import com.zbxn.main.entity.IntegralEntity;
import com.zbxn.main.entity.Member;
import com.zbxn.main.listener.ICustomListener;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * 主页-我的界面
 * Created by Administrator on 2016/12/26.
 */
public class MainPersonalFragment extends BaseFragment {

    /**
     * 登录回调
     */
    private static final int Flag_Callback_Login = 1000;
    private static final int Flag_Callback_Switch = 1001;

    PullToZoomScrollViewEx mScrollView;
    @BindView(R.id.mId)
    TextView mId;
    @BindView(R.id.mPortrait)
    CircleImageView mPortrait;// 头像
    @BindView(R.id.mRemarkName)
    TextView mRemarkName;// 昵称
    @BindView(R.id.mDepartment)
    TextView mDepartment;// 部门
    @BindView(R.id.mServicePhone)
    LinearLayout mServicePhone;//客服
    @BindView(R.id.mAboutZBX)
    LinearLayout mAboutZBX;//关于淄博星
    @BindView(R.id.mMoreSettings)
    LinearLayout mMoreSettings;//设置
    @BindView(R.id.MyHeadPortrait)
    LinearLayout MyHeadPortrait;//头部
    @BindView(R.id.mMoreRecommend)
    LinearLayout mMoreRecommend;//推荐基友
    @BindView(R.id.MyCollection)
    LinearLayout MyCollection;//收藏
    @BindView(R.id.MyIntegral)
    LinearLayout MyIntegral;//N币
    @BindView(R.id.mForAdvice)
    LinearLayout mForAdvice;//意见反馈
    @BindView(R.id.mSwitchCompany)
    LinearLayout mSwitchCompany;//切换公司

    @BindView(R.id.mIntegral1)
    LinearLayout mIntegral1;//N币
    @BindView(R.id.mRanking)
    LinearLayout mRanking;//N币排名
    @BindView(R.id.myRanking)
    TextView myRanking;//排名数

    @BindView(R.id.mLuckDraw)
    LinearLayout mLuckDraw;
    @BindView(R.id.mOrganizeFrame)
    LinearLayout mOrganizeFrame;
    @BindView(R.id.mOrganizeLine)
    View mOrganizeLine;
    @BindView(R.id.mJobManagementFrameLine)
    View mJobManagementFrameLine;
    @BindView(R.id.mJobManagementFrame)
    LinearLayout mJobManagementFrame;

    private String touxiang;
    private String name;
    private String paiming;
    /**
     * Header头部显示的比例
     */
    private float mHeaderShowRadio = 0f;
    private String leiji = "";

    private static final int REQUEST_UI = 1;
    private boolean isFirst;

    public MainPersonalFragment() {
    }


    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.main_membercenter, container, false);
        ButterKnife.bind(this, root);
        System.getProperty("java.classpath");
        mOrganizeLine.setVisibility(View.GONE);
        mOrganizeFrame.setVisibility(View.GONE);
        mJobManagementFrameLine.setVisibility(View.GONE);
        mJobManagementFrame.setVisibility(View.GONE);
        String permissionIds = PreferencesUtils.getString(BaseApp.getContext(), LoginActivity.FLAG_PERMISSIONIDS, "");
        String[] arr = permissionIds.split(",");
        for (int i = 0; i < arr.length; i++) {
            try {
                if ("4".equals(arr[i])) {
                    mOrganizeLine.setVisibility(View.VISIBLE);
                    mOrganizeFrame.setVisibility(View.VISIBLE);
                }
                if ("5".equals(arr[i])) {
                    mJobManagementFrameLine.setVisibility(View.VISIBLE);
                    mJobManagementFrame.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {

            }
        }

        isFirst = PreferencesUtils.getBoolean(getContext(), "myFirst", true);
        //如果是第一次打开此界面
        if (isFirst) {
            PreferencesUtils.putBoolean(getContext(), "myFirst", false);
            mRemarkName.post(new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    mRemarkName.getLocationOnScreen(location);
                    int x = location[0];
                    int y = location[1];
                    showGuide(x, y);
                }
            });
        }


        EventBus.getDefault().register(this);
        Passwor(getContext());
        return root;
    }

    /**
     * 显示蒙版引导
     */
    private void showGuide(float x, float y) {
        final AlertDialog ad = new AlertDialog.Builder(getContext(), R.style.Dialog_Fullscreen).create();// 创建
        ad.setCanceledOnTouchOutside(false);//点击屏幕
        ad.setCancelable(true);//按返回键
        // 显示对话框
        ad.show();
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = ad.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        ad.getWindow().setAttributes(lp);
        Window window = ad.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setContentView(R.layout.dialog_guide_my);

        ImageView img = (ImageView) window.findViewById(R.id.img_bt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });

    }

    @Override
    protected void initialize(View root, @Nullable Bundle savedInstanceState) {
        refreshUI(null);
    }

    @OnClick({R.id.mPortrait, R.id.mServicePhone, R.id.mAboutZBX, R.id.mForAdvice, R.id.mOrganizeFrame,
            R.id.mMoreSettings, R.id.tv_exit, R.id.MyHeadPortrait, R.id.mMoreRecommend, R.id.mJobManagementFrame,
            R.id.MyCollection, R.id.MyIntegral, R.id.mSwitchCompany, R.id.mIntegral1, R.id.mRanking, R.id.mLuckDraw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MyHeadPortrait:
                startActivity(new Intent(getActivity(), MemberCenterActivity.class));
                break;
            case R.id.mPortrait:
//                //实例化SelectPicPopupWindow
//                PopupWindowSelectPic menuWindow = new PopupWindowSelectPic(getActivity(), listenerPic);
//                //显示窗口
//                menuWindow.showAtLocation(getActivity().findViewById(R.id.mPortrait), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                break;
            case R.id.mServicePhone:
                showCallServiceDialog();
                break;
            case R.id.mForAdvice:
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            case R.id.mSwitchCompany:
                startActivityForResult(new Intent(getActivity(), SwitchCompanyActivity.class), Flag_Callback_Switch);
                break;
            case R.id.MyIntegral:
                //startActivity(new Intent(getActivity(), IntegralDetailsActivity.class));
                Intent inten = new Intent(getActivity(), IntegralDetailsActivity.class);
                inten.putExtra("touxiang", touxiang);
                inten.putExtra("name", name);
                inten.putExtra("jifen", leiji);   //用户积分
                startActivity(inten);
                break;
            case R.id.MyCollection:
                startActivity(new Intent(getActivity(), CollectCenterActivity.class));
                break;
            case R.id.mAboutZBX:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.mMoreSettings:
                startActivity(new Intent(getActivity(), Settings.class));
                break;
            case R.id.tv_exit:
                MessageDialog dialog = new MessageDialog(getActivity());
                dialog.setTitle("提示");
                dialog.setMessage("确定要退出登录吗?");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // 调用 Handler 来异步设置别名
                        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));

                        logout();
                    }
                });
                dialog.setNegativeButton("取消");
                dialog.show();
                break;
            case R.id.mMoreRecommend://推荐好友
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.mIntegral1://我的N币MyIntegral
                /*Intent intent = new Intent(getActivity(), IntegralDetailsActivity.class);
                intent.putExtra("touxiang", touxiang);
                intent.putExtra("name", name);
                intent.putExtra("leiji", leiji);
                startActivity(intent);*/
                break;
            case R.id.mRanking://N币排名
                Intent intentRanking = new Intent(getActivity(), IntegralActivity.class);
                intentRanking.putExtra("touxiang", touxiang);
                intentRanking.putExtra("name", name);
                intentRanking.putExtra("leiji", leiji);
                //intentRanking.putExtra("paiming", paiming);
                startActivity(intentRanking);
                break;
            case R.id.mLuckDraw://抽奖
                //    startActivity(new Intent(getActivity(), ShopLuckActivity.class));
                break;
            case R.id.mOrganizeFrame://组织架构
                startActivity(new Intent(getActivity(), OrganizeActivity.class));
                break;
            case R.id.mJobManagementFrame://职位管理
                startActivity(new Intent(getActivity(), JobManagementActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Passwor(getContext());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 清理缓存
     */

    public void logout() {
        Member.clear();
        Jump();
    }

    // 刷新界面
    @Subscriber
    private void refreshUI(EventMember event) {
        Member member = Member.get();
        if (member == null) {
            //登录用户头像
            mPortrait.setImageResource(R.mipmap.userhead_img);
            mRemarkName.setText("");
            mDepartment.setText("");
            mId.setText("");
        } else {
            //登录用户头像
            String headUrl = PreferencesUtils.getString(getActivity(), LoginActivity.FLAG_INPUT_HEADURL, "");
            String mBaseUrl = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                    //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                    .build();                                   // 创建配置过得DisplayImageOption对象
            ImageLoader.getInstance().displayImage(mBaseUrl + headUrl, mPortrait, options);
            mRemarkName.setText(member.getUserName());
            mDepartment.setText(member.getDepartmentName());
            if (!StringUtils.isEmpty(member.getNumber())) {
                mId.setText("ID:" + member.getNumber());
            } else {
                mId.setText("ID:");
            }
        }
    }

    //打电话
    private void showCallServiceDialog() {
        MessageDialog dialog = new MessageDialog(getActivity());
        dialog.setMessage("确定拨打客服热线?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "05336078222");
                intent.setData(data);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog.show();
    }

    /**
     * 获取头部显示的比例
     */
    public float getHeaderShowRadio() {
        return mHeaderShowRadio;
    }


    public void Jump() {
        DBUtils<Contacts> mDBUtils = new DBUtils<>(Contacts.class);
        mDBUtils.deleteAll();
        Intent intent = new Intent(BaseApp.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isExit", true);
        BaseApp.getContext().startActivity(intent);
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
        switch (requestCode) {
            case Flag_Callback_Login:
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
//                    getActivity().finish();
                } else {// 没有登录，关闭应用
                    getActivity().finish();
                }
                break;
            case Flag_Callback_Switch:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().finish();
                }
                break;

            default:
                break;
        }
         /*回调内容*/
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PICTURE
                || requestCode == REQUEST_CODE_LOAD_IMAGE) {
            if (requestCode == REQUEST_CODE_LOAD_IMAGE) {
                mFilePath = DemoUtils.resolvePhotoFromIntent(getActivity(), data, FileAccessor.IMESSAGE_IMAGE);
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
            uploadFile(getActivity(), fileSmall, mICustomListener);
            return;
        }
    }

    /**
     * 个人中心进行图片的修改
     *
     * @param context
     * @param context
     * @param context
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

                    break;
                case 1:
                    MyToast.showToast("修改失败");
                    break;
            }
        }
    };


    /**
     * ************* 设置别名 *************************
     */
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    System.out.println("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                   /* JPushInterface.setAliasAndTags(getActivity(),
                            (String) msg.obj, null, mAliasCallback);*/
                    break;
                default:
                    System.out.println("Unhandled msg - " + msg.what);
            }
        }
    };

    /**
     * 自己的排名
     *
     * @param context
     */
    public void Passwor(Context context) {
        String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        Call call = HttpRequest.getIResourceOA().MemberCenterPaiMing(ssid);
        callRequest(call, new HttpCallBack(IntegralEntity.class, getContext(), false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if ("0".equals(mResult.getSuccess())) {//0成功
                    IntegralEntity entity = (IntegralEntity) mResult.getData();
                    touxiang = entity.getTouxiang();
                    name = entity.getUsername();
                    mRemarkName.setText(name);
                    paiming = entity.getPaiming() + "";
                    myRanking.setText(paiming + "名");//名次
                    leiji = entity.getLeiji();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast("获取网络数据失败");
            }
        });
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        //音量值0~30
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        //开始录音
        @Override
        public void onBeginOfSpeech() {

        }

        //结束录音
        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            System.out.println("结果：" + recognizerResult.getResultString());
        }

        //会话发生错误回调接口
        @Override
        public void onError(SpeechError speechError) {

        }

        //扩展用接口
        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
}
