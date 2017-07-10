package com.zbxn.crm.activity.custom;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pub.base.BaseActivity;
import com.pub.base.BaseApp;
import com.pub.common.EventCustom;
import com.pub.common.KeyEvent;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.http.uploadfile.BaseAsyncTaskInterface;
import com.pub.http.uploadfile.Result;
import com.pub.utils.ConfigUtils;
import com.pub.utils.ConstantBaidu;
import com.pub.utils.FileAccessor;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyGridView;
import com.pub.widget.MyListView;
import com.pub.widget.multi_image_selector.MultiImageSelector;
import com.pub.widget.multi_image_selector.utils.FileUtils;
import com.zbxn.R;
import com.zbxn.crm.adapter.RecordAdapter;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.main.activity.login.LoginActivity;
import com.zbxn.main.activity.mission.PhotoDetailActivity;
import com.zbxn.main.activity.mission.uploadfile.BaseAsyncTaskFile;
import com.zbxn.main.adapter.PhotoListAdapter;
import com.zbxn.main.entity.PhotosEntity;
import com.zbxn.main.listener.ICustomListener;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 新建跟进记录
 *
 * @author: ysj
 * @date: 2017-01-16 11:25
 */
public class CreatFollowUpActivity extends BaseActivity {

    private static final int FLAG_FOLLOWYTPE = 10001;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x3;
    private static final int FLAG_SPEECH_RECOGNITION = 10003;
    private static final int RECORD_AUDIO = 10004;

    @BindView(R.id.et_followUp)
    EditText etFollowUp;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.img_face)
    ImageView imgFace;
    @BindView(R.id.img_picture)
    ImageView imgPicture;
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.img_speech_recognition)
    ImageView imgSpeechRecognition;
    @BindView(R.id.img_record)
    ImageView imgRecord;
    @BindView(R.id.mGridView)
    MyGridView mGridView;
    @BindView(R.id.mListView)
    MyListView mListView;

    //获取图片
    private static final int REQUEST_IMAGE = 2002;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;

    //图片路径的集合
    private ArrayList<String> mSelectPath;
    private String ssid;
    private String mGuid = "";

    private ArrayList<PhotosEntity> lists;
    /**
     * 选择图片拍照路径
     */
    private String mFilePath;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;
    private static final int REQUEST_CAMERA = 100;
    private File mTmpFile;

    //录音路径
    private List<String> mRecordPath;
    private RecordAdapter mAdapter;
    private ArrayList<PhotosEntity> audioList;

    private String mContent = "";
    private String custId;
    private String mType = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_creat);
        ButterKnife.bind(this);
        setTitle("跟进记录");
        mGuid = StringUtils.getGuid();
        custId = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        lists = new ArrayList<PhotosEntity>();
        mRecordPath = new ArrayList<>();
        audioList = new ArrayList<>();
        mAdapter = new RecordAdapter(this, mRecordPath, listener);
        mListView.setAdapter(mAdapter);
        //录音列表点击事件 播放
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    String type = "audio/*";
                    intent.setDataAndType(Uri.fromFile(new File(mRecordPath.get(position))), type);
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        lists.clear();
        lists = updatePhotos(lists, null);
        PhotoListAdapter adapterPhotos = new PhotoListAdapter(this, lists, R.layout.list_item_select_photos, listener, true);
        mGridView.setAdapter(adapterPhotos);

    }

    @Override
    public void initRight() {
        super.initRight();
        setRight1Icon(R.mipmap.complete2);
        setRight1("确定");
        setRight1Show(true);
    }

    @Override
    public void actionRight1(MenuItem menuItem) {
        super.actionRight1(menuItem);
        mContent = StringUtils.getEditText(etFollowUp);
        if (StringUtils.isEmpty(mContent)) {
            MyToast.showToast("请填写跟进记录");
            return;
        }
        if (audioList.size() > 0) {
            uploadFileAudio(CreatFollowUpActivity.this, audioList);
        }
        if (lists.size() >= 1) {
            uploadFile(CreatFollowUpActivity.this, lists);
        } else {
            postFollowUp();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // delete tmp file
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
            return;
        }
        if (requestCode == FLAG_SPEECH_RECOGNITION) {
            onResults(data.getExtras());
        }
        if (requestCode == RECORD_AUDIO) {
            try {
                Uri uri = data.getData();
                String filePath = "";
                if (uri.getPath().contains(".amr")) {
                    filePath = uri.getPath();
                } else {
                    filePath = getAudioFilePathFromUri(uri);
                }
                System.out.println("filePath:" + filePath);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                String filePaths = filePath.substring(0, filePath.length() - fileName.length() - 1);
                if (!StringUtils.isEmpty(filePath)) {
                    mRecordPath.add(fileName);
                    mAdapter.notifyDataSetChanged();
                }
                PhotosEntity entity = new PhotosEntity();
                entity.setImgurl(filePath);
                audioList.add(entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (requestCode == FLAG_FOLLOWYTPE) {
            if (data != null) {
                mType = data.getStringExtra("typeId");
                String name = data.getStringExtra("typeName");
                tvType.setText(name);
            }
        }
        if (requestCode == REQUEST_IMAGE || requestCode == REQUEST_CAMERA) {//图片
            if (requestCode == REQUEST_IMAGE) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
            }
            if (mTmpFile != null) {
                if (StringUtils.isEmpty(mSelectPath)) {
                    mSelectPath = new ArrayList<>();
                }
                mFilePath = mTmpFile.getAbsolutePath();
                mSelectPath.add(mFilePath);
            }
            StringBuilder sb = new StringBuilder();
            PhotosEntity entity = null;
            lists.clear();
            for (String p : mSelectPath) {
                sb.append(p);
                sb.append("\n");

                entity = new PhotosEntity();
                entity.setAppname("");
                entity.setId("");
                entity.setImgurl(p);
                entity.setImgurlNet(p);
                lists = updatePhotos(lists, entity);
            }
            PhotoListAdapter photoListAdapter = new PhotoListAdapter(this, lists, R.layout.list_item_select_photos, listener, true);
            mGridView.setAdapter(photoListAdapter);
        }
    }

    @OnClick({R.id.tv_type, R.id.img_face, R.id.img_picture, R.id.img_photo, R.id.img_speech_recognition, R.id.img_record})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_type://跟进记录类型
                intent = new Intent(this, FollowTypeActivity.class);
                intent.putExtra("id", mType);
                startActivityForResult(intent, FLAG_FOLLOWYTPE);
                break;
            case R.id.img_face://表情
                break;
            case R.id.img_picture://从图片中选择
                pickImage();
                break;
            case R.id.img_photo://拍照
                //调用照相机
                showCameraAction();
                break;
            case R.id.img_speech_recognition://语音识别
                intent = new Intent();
                bindParams(intent);
                intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
                startActivityForResult(intent, FLAG_SPEECH_RECOGNITION);
                break;
            case R.id.img_record://录音
                if (mRecordPath.size() >= 5) {
                    MyToast.showToast("录音文件不能超过5个");
                    return;
                }
//                Intent intentRecord = new Intent(this, RecordAudioActivity.class);
//                startActivity(intentRecord);
                /*Intent intentRecord = new Intent(Intent.ACTION_MAIN);
                intentRecord.setClassName("com.android.soundrecorder", "com.android.soundrecorder.SoundRecorder");
                final String EXTRA_MAX_BYTES = android.provider.MediaStore.Audio.Media.EXTRA_MAX_BYTES;
                long bytes = (long) (5900 * 4L);
                //设置录音文件的大小，10K左右大小在默认比特率下可以录音6秒左右
                //注意：该值为long型的，否则该设置不起作用
                intentRecord.putExtra(EXTRA_MAX_BYTES, bytes);
                if (isAvailable(getApplicationContext(), intentRecord)) {
                    startActivityForResult(intentRecord, RECORD_AUDIO);
                } else {
                    MyToast.showToast("该机型暂不支持语音功能，极力完善中");
                }*/
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/amr");
                intent.setClassName("com.android.soundrecorder",
                        "com.android.soundrecorder.SoundRecorder");
                if (isAvailable(getApplicationContext(), intent)) {
                    startActivityForResult(intent, RECORD_AUDIO);
                } else {
                    MyToast.showToast("暂不支持该机型录音功能，极力完善中");
                }
                break;
        }
    }

    /**
     * 判断录音是否可用
     *
     * @param ctx
     * @param intent
     * @return
     */
    public static boolean isAvailable(Context ctx, Intent intent) {
        final PackageManager mgr = ctx.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 打开相机
     */
    private void showCameraAction() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(com.pub.R.string.mis_permission_rationale_write_storage),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                try {
                    mTmpFile = FileUtils.createTmpFile(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(this, R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 更新照片list
     *
     * @param photoList
     * @param entity
     * @return
     */
    private ArrayList<PhotosEntity> updatePhotos(ArrayList<PhotosEntity> photoList, PhotosEntity entity) {
        if (StringUtils.isEmpty(photoList)) {
            photoList = new ArrayList<PhotosEntity>();
        }
        List<PhotosEntity> listPhotosTemp = new ArrayList<PhotosEntity>();
        listPhotosTemp.addAll(photoList);
        photoList.clear();
        if (null != entity) {
            listPhotosTemp.add(entity);
        }

        photoList.addAll(listPhotosTemp);
        return photoList;
    }

    private ICustomListener listener = new ICustomListener() {

        @Override
        public void onCustomListener(int obj0, Object obj1, int position) {
            switch (obj0) {
                case 0://刪除
                    lists.remove(position);
                    mSelectPath.remove(position);
                    lists = updatePhotos(lists, null);
                    PhotoListAdapter photoListAdapter = new PhotoListAdapter(CreatFollowUpActivity.this, lists, R.layout.list_item_select_photos, listener, true);
                    mGridView.setAdapter(photoListAdapter);
                    break;
                case 1://删除录音
                    mRecordPath.remove(position);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 3://显示大图
                    ArrayList<String> list_Ads = new ArrayList<>();
                    for (int j = 0; j < lists.size(); j++) {
                        list_Ads.add(lists.get(j).getImgurl());
                    }
                    Intent intent = new Intent(CreatFollowUpActivity.this, PhotoDetailActivity.class);
                    intent.putExtra("list", list_Ads);
                    intent.putExtra("position", position);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 选择图片
     */
    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            boolean showCamera = true;//是否显示相机
            int maxNum = 9;//最大选择9张
            boolean isMulti = true;//是否多选

            MultiImageSelector selector = MultiImageSelector.create(CreatFollowUpActivity.this);
            selector.showCamera(showCamera);
            selector.count(maxNum);
            if (isMulti) {
                selector.multi();
            } else {
                selector.single();
            }
            selector.origin(mSelectPath);
            selector.start(this, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CreatFollowUpActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 上传图片
     */
    public void uploadFile(Context context, ArrayList<PhotosEntity> list) {
        Map<String, String> map = new HashMap<String, String>();
        //附件集合
        Map<String, File> mapFile = new HashMap<>();

        ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("guId", mGuid);
        for (int i = 0; i < list.size() - 1; i++) {
            File file = new File(list.get(i).getImgurl());
            if (file == null || !file.exists()) {
                break;
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
            mapFile.put("image" + i, fileSmall);
        }

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        new BaseAsyncTaskFile(context, true, 0, server + "/common/doUpLoads.do", new BaseAsyncTaskInterface() {
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
                    postFollowUp();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
            }
        }, mapFile).execute(map);
    }

    /**
     * 上传录音文件
     */
    public void uploadFileAudio(Context context, ArrayList<PhotosEntity> list) {
        Map<String, String> map = new HashMap<String, String>();
        //附件集合
        Map<String, File> mapFile = new HashMap<>();

        ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
        map.put("tokenid", ssid);
        map.put("guId", mGuid);
        for (int i = 0; i < list.size() - 1; i++) {
            File file = new File(list.get(i).getImgurl());
            if (file == null || !file.exists()) {
                break;
            }
            mapFile.put("audio" + i, file);
        }

        String server = ConfigUtils.getConfig(ConfigUtils.KEY.server);
        new BaseAsyncTaskFile(context, true, 0, server + "/common/doUpLoads.do", new BaseAsyncTaskInterface() {
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
//                    postFollowUp();
                } else {
                    String message = mResult.getMsg();
                    MyToast.showToast(message);
                }
            }

            @Override
            public void dataError(int funId) {
            }
        }, mapFile).execute(map);
    }

    /**
     * 设置语音识别参数
     *
     * @param intent
     */
    public void bindParams(Intent intent) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("tips_sound", true)) {
            intent.putExtra(ConstantBaidu.EXTRA_SOUND_START, R.raw.bdspeech_recognition_start);
            intent.putExtra(ConstantBaidu.EXTRA_SOUND_END, R.raw.bdspeech_speech_end);
            intent.putExtra(ConstantBaidu.EXTRA_SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
            intent.putExtra(ConstantBaidu.EXTRA_SOUND_ERROR, R.raw.bdspeech_recognition_error);
            intent.putExtra(ConstantBaidu.EXTRA_SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);
        }
        if (sp.contains(ConstantBaidu.EXTRA_INFILE)) {
            String tmp = sp.getString(ConstantBaidu.EXTRA_INFILE, "").replaceAll(",.*", "").trim();
            intent.putExtra(ConstantBaidu.EXTRA_INFILE, tmp);
        }
        if (sp.getBoolean(ConstantBaidu.EXTRA_OUTFILE, false)) {
            intent.putExtra(ConstantBaidu.EXTRA_OUTFILE, "sdcard/outfile.pcm");
        }
        if (sp.getBoolean(ConstantBaidu.EXTRA_GRAMMAR, false)) {
            intent.putExtra(ConstantBaidu.EXTRA_GRAMMAR, "assets:///baidu_speech_grammar.bsg");
        }
        if (sp.contains(ConstantBaidu.EXTRA_SAMPLE)) {
            String tmp = sp.getString(ConstantBaidu.EXTRA_SAMPLE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(ConstantBaidu.EXTRA_SAMPLE, Integer.parseInt(tmp));
            }
        }
        if (sp.contains(ConstantBaidu.EXTRA_LANGUAGE)) {
            String tmp = sp.getString(ConstantBaidu.EXTRA_LANGUAGE, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(ConstantBaidu.EXTRA_LANGUAGE, tmp);
            }
        }
        if (sp.contains(ConstantBaidu.EXTRA_NLU)) {
            String tmp = sp.getString(ConstantBaidu.EXTRA_NLU, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(ConstantBaidu.EXTRA_NLU, tmp);
            }
        }

        if (sp.contains(ConstantBaidu.EXTRA_VAD)) {
            String tmp = sp.getString(ConstantBaidu.EXTRA_VAD, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(ConstantBaidu.EXTRA_VAD, tmp);
            }
        }
        String prop = null;
        if (sp.contains(ConstantBaidu.EXTRA_PROP)) {
            String tmp = sp.getString(ConstantBaidu.EXTRA_PROP, "").replaceAll(",.*", "").trim();
            if (null != tmp && !"".equals(tmp)) {
                intent.putExtra(ConstantBaidu.EXTRA_PROP, Integer.parseInt(tmp));
                prop = tmp;
            }
        }

        // offline asr
        {
            intent.putExtra(ConstantBaidu.EXTRA_OFFLINE_ASR_BASE_FILE_PATH, "/sdcard/easr/s_1");
            if (null != prop) {
                int propInt = Integer.parseInt(prop);
                if (propInt == 10060) {
                    intent.putExtra(ConstantBaidu.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_Navi");
                } else if (propInt == 20000) {
                    intent.putExtra(ConstantBaidu.EXTRA_OFFLINE_LM_RES_FILE_PATH, "/sdcard/easr/s_2_InputMethod");
                }
            }
            //intent.putExtra(ConstantBaidu.EXTRA_OFFLINE_SLOT_DATA, buildTestSlotData());
        }
    }

    /**
     * 处理语音识别结果
     *
     * @param results
     */
    public void onResults(Bundle results) {
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String nbs = Arrays.toString(nbest.toArray(new String[nbest.size()]));//识别完后的结果可能带了[]
        if (nbs.contains("[")) {
            nbs = nbs.replace("[", "");
        }
        if (nbs.contains("]")) {
            nbs = nbs.replace("]", "");
        }
        String content = StringUtils.getEditText(etFollowUp);
        etFollowUp.setText(content + nbs);
        /*long end2finish = System.currentTimeMillis() - speechEndTime;
        status = STATUS_None;
        ArrayList<String> nbest = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        print("识别成功：" + Arrays.toString(nbest.toArray(new String[nbest.size()])));
        String json_res = results.getString("origin_result");
        try {
            print("origin_result=\n" + new JSONObject(json_res).toString(4));
        } catch (Exception e) {
            print("origin_result=[warning: bad json]\n" + json_res);
        }
        btn.setText("开始");
        String strEnd2Finish = "";
        if (end2finish < 60 * 1000) {
            strEnd2Finish = "(waited " + end2finish + "ms)";
        }
        txtResult.setText(nbest.get(0) + strEnd2Finish);
        time = 0;*/
    }

    /**
     * 提交跟进记录
     */
    public void postFollowUp() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postFollowUp(ssid, currentCompanyId, custId, "", mContent, mType, mGuid);
        callRequest(call, new HttpCallBack(CustomEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("新增跟进记录成功");
                    EventCustom eventCustom = new EventCustom();
                    eventCustom.setTag(KeyEvent.CREATFOLLOWUP);
                    EventBus.getDefault().post(eventCustom);
                    finish();
                } else {
                    MyToast.showToast(mResult.getMsg());
                }
            }

            @Override
            public void onFailure(String string) {
                MyToast.showToast(R.string.NETWORKERROR);
            }
        });
    }

    private String getAudioFilePathFromUri(Uri uri) {
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        return cursor.getString(index);
    }

}
