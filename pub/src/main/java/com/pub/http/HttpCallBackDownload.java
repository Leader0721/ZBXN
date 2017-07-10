package com.pub.http;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import com.pub.R;
import com.pub.base.BaseApp;
import com.pub.utils.DeviceUtils;
import com.pub.widget.MyProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zbx on 16/3/10.
 */
public abstract class HttpCallBackDownload<T> implements Callback {

    public abstract void onSuccess(ResultData mResult);

    public abstract void onFailure(String string);

    private Class<T> mClazz;
    private Context mContext;
    private String mDir = "";//要保存的目录
    private String mFileName = "";//要保存的文件名称

    private Dialog progressDialog;// 自定义加载弹框

    private String TAG = "";

    /**
     * @param clazz
     * @param context
     */
    public HttpCallBackDownload(Class<T> clazz, Context context) {
        init(clazz, context, null, null);
    }

    /**
     * @param clazz
     * @param context
     * @param isShowProgress 是否显示进度条
     */
    public HttpCallBackDownload(Class<T> clazz, Context context, String dir, String fileName, boolean isShowProgress) {
        init(clazz, context, dir, fileName);
        if (isShowProgress) {
            showDialog();
        }
    }

    private void init(Class<T> clazz, Context context, String dir, String fileName) {
        mClazz = clazz;
        mContext = context;
        mDir = dir;
        mFileName = fileName;

        if (!DeviceUtils.getInstance(context).hasInternet()) {//未联网
            Toast.makeText(context, "无网络连接，请检查网络设置！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Call call, Response response) {
        cancelDialog();
        if (response.isSuccessful()) {
            boolean isSuccess = writeResponseBodyToDisk((ResponseBody) response.body());
            if (isSuccess) {
                ResultData resultData = new ResultData();
                resultData.setSuccess("0");
                onSuccess(resultData);
            } else {
                onFailure("下载成功，保存到本地失败");
            }
        } else {
            onFailure("下载失败");
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        cancelDialog();
        if (t != null && t.getMessage() != null && (t.getMessage().equals("Canceled") || t.getMessage().equals("Socket closed"))) {
            return;
        }
        Toast.makeText(BaseApp.getContext(), R.string.NETWORKERROR, Toast.LENGTH_SHORT).show();

        onFailure(BaseApp.getContext().getString(R.string.NETWORKERROR));
    }

    /**
     * 显示弹窗
     */
    private void showDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = new MyProgressDialog(mContext, true);
            }
            progressDialog.show();
        } catch (Exception e) {
        }
    }

    /**
     * 关闭弹窗
     */
    private void cancelDialog() {
        try {
            if (null != progressDialog) {
                progressDialog.cancel();
            }
        } catch (Exception e) {
        }
    }

    /**
     * 写入到本地
     *
     * @param body
     * @return
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(mDir + mFileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();//文件总大小
                long fileSizeDownloaded = 0;//文件已经下载大小
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
