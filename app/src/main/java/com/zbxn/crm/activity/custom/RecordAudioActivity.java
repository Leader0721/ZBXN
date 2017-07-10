package com.zbxn.crm.activity.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pub.base.BaseActivity;
import com.pub.record.RecordAudio;
import com.pub.utils.MyToast;
import com.zbxn.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 录音
 *
 * @author: ysj
 * @date: 2017-01-18 14:02
 */
public class RecordAudioActivity extends BaseActivity {

    @BindView(R.id.tv_record_time)
    TextView tvRecordTime;
    @BindView(R.id.img_again)
    ImageView imgAgain;
    @BindView(R.id.tv_again)
    TextView tvAgain;
    @BindView(R.id.img_start)
    ImageView imgStart;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.img_complete)
    ImageView imgComplete;
    @BindView(R.id.tv_complete)
    TextView tvComplete;

    private RecordAudio mRecordAudio;
    //是否正在录音
    private boolean isRecord;
    //是否暂停过
    private boolean sigle = false;

    private List<File> files;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ButterKnife.bind(this);
        setTitle("录音");
        files = new ArrayList<>();
        mRecordAudio = new RecordAudio(this);
    }

    @OnClick({R.id.img_again, R.id.img_start, R.id.img_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_again://重新录音
                MyToast.showToast("重新录音");
                if (mRecordAudio.getFile() != null) {
                    File file = mRecordAudio.getFile();
                    mRecordAudio.openFile(file);
                }
                break;
            case R.id.img_start://开始/暂停录音
//                if (isRecord) {//暂停
//                    sigle = true;
//                    MyToast.showToast("暂停");
//                } else {//开始
                mRecordAudio.start();
                isRecord = true;
                MyToast.showToast("开始录音");
//                }
                break;
            case R.id.img_complete://停止录音
                mRecordAudio.recordStop();
                MyToast.showToast("停止录音");
                break;
        }
    }


}
