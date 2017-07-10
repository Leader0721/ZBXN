package com.pub.record;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 录音类
 *
 * @author: ysj
 * @date: 2017-01-18 14:49
 */
public class RecordAudio {

    private Context mContext;

    private String strTempFile = "YYT_";
    private File myRecAudioFile;
    /**
     * 录音保存路径
     **/
    private File myRecAudioDir;
    private File myPlayFile;
    private MediaRecorder mMediaRecorder01;
    private int mMinute;
    private boolean xx = true;
    /**
     * 存放音频文件列表
     **/
    private ArrayList<String> recordFiles;
    private ArrayAdapter<String> adapter;
    /**
     * 文件存在
     **/
    private boolean sdcardExit;
    /**
     * 是否停止录音
     **/
    private boolean isStopRecord;

    private final String SUFFIX = ".amr";

    /**
     * 记录需要合成的几段amr语音文件
     **/
    private ArrayList<String> list;

    int second = 0;

    int minute = 0;

    /**
     * 是否暂停标志位
     **/
    private boolean isPause = false;

    /**
     * 在暂停状态中
     **/
    private boolean inThePause = false;

    //最终的文件
    private File file1;
    private String length1 = null;

    public RecordAudio(Context context) {
        this.mContext = context;

        //初始化list
        list = new ArrayList<String>();
        myPlayFile = null;

        initSdcard();
    }

    /**
     * 初始化sd卡
     */
    public void initSdcard() {
        // 判断sd Card是否插入
        sdcardExit = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        // 取得sd card路径作为录音文件的位置
        if (sdcardExit) {
            String pathStr = Environment.getExternalStorageDirectory().getPath() + "/YYT";
            myRecAudioDir = new File(pathStr);
            if (!myRecAudioDir.exists()) {
                myRecAudioDir.mkdirs();
                Log.v("录音", "创建录音文件！" + myRecAudioDir.exists());
            }
        }
        // 取得sd card 目录里的.arm文件
        getRecordFiles();
    }

    /**
     * 暂停录音
     */
    public void recordPause() {
        isPause = true;

        //已经暂停过了，再次点击按钮 开始录音，录音状态为正在录音
        if (inThePause) {
            start();
            inThePause = false;
        }
        //正在录音，点击暂停,现在录音状态为暂停
        else {
            //当前正在录音的文件名，全程
            list.add(myRecAudioFile.getPath());
            inThePause = true;
            recodeStop();
        }
    }

    /**
     * 停止录音（调用）
     */
    public void recordStop() {
        isStopRecord = true;
        recodeStop();
    }

    /**
     * 停止录音
     */
    private void recodeStop() {
        if(isPause){
            //在暂停状态按下结束键,处理list就可以了
            if(inThePause){
                getInputCollection(list, false);
            }
            //在正在录音时，处理list里面的和正在录音的语音
            else{
                list.add(myRecAudioFile.getPath());
                recodeStop();
                getInputCollection(list, true);
            }

            //还原标志位
            isPause=false;
            inThePause=false;
        }
        //若录音没有经过任何暂停
        else{
            if (myRecAudioFile != null) {
                // 停止录音
                mMediaRecorder01.stop();
                mMediaRecorder01.release();
                mMediaRecorder01 = null;
                DecimalFormat df = new DecimalFormat("#.000");
                if (myRecAudioFile.length() <= 1024*1024) {
                    length1=df.format(myRecAudioFile.length() / 1024.0)+"K";
                } else {
                    length1=df.format(myRecAudioFile.length() / 1024.0 / 1024)+"M";
                }
                System.out.println(length1);
            }

        }

    }

    /**
     * 返回最终的file
     *
     * @return
     */
    public File getFile() {
        if (file1 != null) {
            return file1;
        }
        return null;
    }

    /**
     * 获取目录下的所有音频文件
     */
    public void getRecordFiles() {
        // TODO Auto-generated method stub
        recordFiles = new ArrayList<String>();
        if (sdcardExit) {
            File files[] = myRecAudioDir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().indexOf(".") >= 0) { // 只取.amr 文件
                        String fileS = files[i].getName().substring(
                                files[i].getName().indexOf("."));
                        if (fileS.toLowerCase().equals(".mp3")
                                || fileS.toLowerCase().equals(".amr")
                                || fileS.toLowerCase().equals(".mp4"))
                            recordFiles.add(files[i].getName());
                    }
                }
            }
        }
    }

    // 打开录音播放程序
    public void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        mContext.startActivity(intent);
    }

    private String getMIMEType(File f) {

        String end = f.getName().substring(f.getName().lastIndexOf(".") + 1,
                f.getName().length()).toLowerCase();
        String type = "";
        if (end.equals("mp3") || end.equals("aac") || end.equals("amr")
                || end.equals("mpeg") || end.equals("mp4")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg")) {
            type = "image";
        } else {
            type = "*";
        }
        type += "/";
        return type;
    }

    /**
     * 开始录音
     */
    public void start() {
        try {
            if (!sdcardExit) {
                Toast.makeText(mContext, "请插入SD card",
                        Toast.LENGTH_LONG).show();
                return;
            }
            String mMinute1 = getTime();
            Toast.makeText(mContext, "当前时间是:" + mMinute1, Toast.LENGTH_LONG).show();

            myRecAudioFile = new File(myRecAudioDir, mMinute1 + SUFFIX);
            mMediaRecorder01 = new MediaRecorder();
            // 设置录音为麦克风
            mMediaRecorder01
                    .setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder01
                    .setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            mMediaRecorder01
                    .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //录音文件保存这里
            mMediaRecorder01.setOutputFile(myRecAudioFile
                    .getAbsolutePath());
            mMediaRecorder01.prepare();
            mMediaRecorder01.start();

            mMediaRecorder01.setOnInfoListener(new MediaRecorder.OnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    // TODO Auto-generated method stub
                    int a = mr.getMaxAmplitude();
                    Toast.makeText(mContext, a, Toast.LENGTH_SHORT).show();
                }
            });

            isStopRecord = false;
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    /**
     * 获取当前时间
     *
     * @return
     */
    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH：mm：ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        System.out.println("当前时间");
        return time;
    }

    /**
     * @param isAddLastRecord 是否需要添加list之外的最新录音，一起合并
     * @return 将合并的流用字符保存
     */
    public void getInputCollection(List list, boolean isAddLastRecord) {

        String mMinute1 = getTime();
        Toast.makeText(mContext, "当前时间是:" + mMinute1, Toast.LENGTH_LONG).show();

        // 创建音频文件,合并的文件放这里
        file1 = new File(myRecAudioDir, mMinute1 + SUFFIX);
        FileOutputStream fileOutputStream = null;

        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            fileOutputStream = new FileOutputStream(file1);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
        for (int i = 0; i < list.size(); i++) {
            File file = new File((String) list.get(i));
            Log.d("list的长度", list.size() + "");
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] myByte = new byte[fileInputStream.available()];
                //文件长度
                int length = myByte.length;

                //头文件
                if (i == 0) {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 0, length);
                    }
                }
                //之后的文件，去掉头文件就可以了
                else {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 6, length - 6);
                    }
                }

                fileOutputStream.flush();
                fileInputStream.close();
                System.out.println("合成文件长度：" + file1.length());

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        //结束后关闭流
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //合成一个文件后，删除之前暂停录音所保存的零碎合成文件
        deleteListRecord(isAddLastRecord);
    }

    private void deleteListRecord(boolean isAddLastRecord) {
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
        //正在暂停后，继续录音的这一段音频文件
        if (isAddLastRecord) {
            myRecAudioFile.delete();
        }
    }

}
