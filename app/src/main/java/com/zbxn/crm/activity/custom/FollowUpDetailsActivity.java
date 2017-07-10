package com.zbxn.crm.activity.custom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pub.base.BaseActivity;
import com.pub.http.HttpCallBack;
import com.pub.http.HttpRequest;
import com.pub.http.ResultData;
import com.pub.utils.ConfigUtils;
import com.pub.utils.ConstantBaidu;
import com.pub.utils.DateUtils;
import com.pub.utils.JsonUtil;
import com.pub.utils.MyToast;
import com.pub.utils.PreferencesUtils;
import com.pub.utils.StringUtils;
import com.pub.widget.MyListView;
import com.zbxn.R;
import com.zbxn.crm.adapter.FollowCommentAdapter;
import com.zbxn.crm.entity.CustomEntity;
import com.zbxn.crm.entity.FollowCommentEntity;
import com.zbxn.crm.entity.FollowUpEntity;
import com.zbxn.crm.entity.StaticTypeEntity;
import com.zbxn.main.activity.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;

/**
 * 跟进记录详情界面
 *
 * @author: ysj
 * @date: 2017-01-16 11:14
 */
public class FollowUpDetailsActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final int FLAG_SPEECH_RECOGNITION = 10003;

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_creatTime)
    TextView tvCreatTime;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_relevance)
    TextView tvRelevance;
    @BindView(R.id.mComment)
    EditText mComment;
    @BindView(R.id.mPublish)
    TextView mPublish;
    @BindView(R.id.ll_custom)
    LinearLayout llCustom;
    @BindView(R.id.mCommentListView)
    MyListView mCommentListView;
    @BindView(R.id.img_speech)
    ImageView imgSpeech;
    @BindView(R.id.tv_num)
    TextView tvNum;

    private String customId;//客户id
    private String customName;//客户名称
    private FollowUpEntity entity;
    private List<StaticTypeEntity> typeList;
    private String mFollowRecordID;
    private String mCommentContent;
    private String mReplyToId;
    private String mToUser;

    private List<FollowCommentEntity> mList;
    private FollowCommentAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_details);
        ButterKnife.bind(this);
        setTitle("跟进记录");
        initData();
        initView();
        getCommentList();
    }

    private void initData() {
        String typeStr = PreferencesUtils.getString(this, CustomActivity.RECORDTYPELIST);
        typeList = JsonUtil.fromJsonList(typeStr, StaticTypeEntity.class);
        customId = getIntent().getStringExtra("id");
        customName = getIntent().getStringExtra("name");
        entity = getIntent().getBundleExtra("bundle").getParcelable("follow");
        mFollowRecordID = entity.getID();
        mReplyToId = entity.getID();
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.userhead_img)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.userhead_img)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.userhead_img)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象
        String server = ConfigUtils.getConfig(ConfigUtils.KEY.webServer);
        ImageLoader.getInstance().displayImage(server + entity.getCreateUserIcon(), imgHead, options);
    }

    private void initView() {
        tvName.setText(entity.getCreateUserName());
        tvCreatTime.setText(DateUtils.fromTodaySimple(StringUtils.convertToDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), entity.getCreateTimeStr())));
        tvRelevance.setText(customName);
        for (int i = 0; i < typeList.size(); i++) {
            if (entity.getRecordType() == Integer.decode(typeList.get(i).getKey())) {
                tvType.setText(typeList.get(i).getValue());
            }
        }
        tvContent.setText(entity.getRecordContent());
        mList = new ArrayList<>();
        mAdapter = new FollowCommentAdapter(this, mList);
        mCommentListView.setAdapter(mAdapter);
        mCommentListView.setOnItemClickListener(this);
        mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s.toString())) {
                    return;
                }
                if (StringUtils.isEmpty(mToUser)) {
                    return;
                }
                if (mToUser.contains(s.toString())) {
                    if (s.toString().length() < mToUser.length()) {
                        mComment.setText("");
                        mReplyToId = mFollowRecordID;
                        mToUser = "";
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvNum.setText("评论(" + mList.size() + ")");
    }

    @OnClick({R.id.ll_custom, R.id.img_speech, R.id.mPublish})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_custom:
                intent = new Intent(this, CustomDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", customId);
                intent.putExtra("name", customName);
                startActivity(intent);
                break;
            case R.id.img_speech://语音识别
                intent = new Intent();
                bindParams(intent);
                intent.setAction("com.baidu.action.RECOGNIZE_SPEECH");
                startActivityForResult(intent, FLAG_SPEECH_RECOGNITION);
                break;
            case R.id.mPublish://提交评论
                if (StringUtils.isEmpty(mComment)) {
                    MyToast.showToast("回复内容不能为空");
                    return;
                }
                mCommentContent = StringUtils.getEditText(mComment);
                if (!StringUtils.isEmpty(mToUser)) {
                    if (mCommentContent.contains(mToUser)) {
                        if (StringUtils.isEmpty(mCommentContent.substring(mToUser.length()).trim())) {
                            MyToast.showToast("回复内容不能为空");
                            return;
                        }
                    }
                }
                if (!StringUtils.isEmpty(mToUser)) {
                    mCommentContent = mCommentContent.substring(mToUser.length());
                }
                postComment();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == FLAG_SPEECH_RECOGNITION) {
            onResults(data.getExtras());
        }
    }

    /**
     * 获取跟进记录评论列表
     */
    public void getCommentList() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().getFollowUpCommentList(ssid, currentCompanyId, mFollowRecordID);
        callRequest(call, new HttpCallBack(FollowCommentEntity.class, this, false) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    mList.clear();
                    List<FollowCommentEntity> list = mResult.getRows();
                    mList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                    tvNum.setText("评论(" + mList.size() + ")");
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

    /**
     * 提交评论(回复)
     */
    public void postComment() {
        String ssid = PreferencesUtils.getString(this, LoginActivity.FLAG_SSID);
        String currentCompanyId = PreferencesUtils.getString(this, LoginActivity.FLAG_ZMSCOMPANYID);
        Call call = HttpRequest.getIResourceOANetAction().postFollowUpComment(ssid, currentCompanyId, mFollowRecordID, mCommentContent, mReplyToId);
        callRequest(call, new HttpCallBack(CustomEntity.class, this, true) {
            @Override
            public void onSuccess(ResultData mResult) {
                if (mResult.getSuccess().equals("0")) {
                    MyToast.showToast("评论成功");
                    mComment.setText("");
                    mToUser = "";
                    mReplyToId = mFollowRecordID;
                    getCommentList();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mReplyToId = mList.get(position).getID();
        mToUser = "@" + mList.get(position).getCreateUserName() + ":";
        mComment.setText(mToUser);
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
        String content = StringUtils.getEditText(mComment);
        mComment.setText(content + nbs);
    }
}
