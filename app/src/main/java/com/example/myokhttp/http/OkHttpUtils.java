package com.example.myokhttp.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.myokhttp.callback.MyCallBack;
import com.example.myokhttp.intercepter.HttpParamInterceptor;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/23.
 */

public class OkHttpUtils {

    private static OkHttpUtils okHttpUtils = null;
    private OkHttpClient httpClient;
    private Gson mGson;
    private Handler mDelivery;

    private OkHttpUtils() {
        //创建okHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpParamInterceptor());
        httpClient = builder.build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils getInstance() {
        if (okHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }

    //异步get请求
    public void get(String url, final MyCallBack myCallBack) {
        //创建请求
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = "请求失败";
                sendFailResult(myCallBack, err);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessResult(myCallBack, response.body().string());
            }
        });
    }

    //异步文件下载
    public void download(final String url, final String destFileDir, final MyCallBack myDownloadCallBack) {
        //创建请求
        Request request = new Request.Builder()
                .url(url)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String err = "请求失败";
                sendFailResult(myDownloadCallBack, err);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.e("TAG", "=========" + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }
                //判断文件夹是否存在，如果不存在就创建文件夹
                File dirFolder = new File(destFileDir);
                if (!dirFolder.exists()) { //如果该文件夹不存在，则进行创建
                    dirFolder.mkdirs();//创建文件夹
                }
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                is = response.body().byteStream();
                long total = response.body().contentLength();
                File file = new File(destFileDir, "QQ.apk");
                fos = new FileOutputStream(file);
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);

                    sum += len;
                    final int progress = (int) (sum * 1.0f / total * 100);
                    // 下载中
//                            myDownloadCallBack.onDownloading(progress);
                    sendDownloadProgress(myDownloadCallBack, progress);
                }
                fos.flush();
                //如果下载文件成功，第一个参数为文件的绝对路径

                sendSuccessResult(myDownloadCallBack, file.getAbsolutePath());
            }
        });
    }

    private void sendDownloadProgress(final MyCallBack myDownloadCallBack, final int progress) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                myDownloadCallBack.onDownloading(progress);

            }
        });
    }

    /**
     * 根据请求地址判断下载文件的名称
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /**
     * 在主线程返回成功结果
     *
     * @param myCallBack
     * @param response
     */
    private void sendSuccessResult(final MyCallBack myCallBack, final String response) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (myCallBack.mType == String.class) {
                    myCallBack.onSuccess(response);

                } else {
                    mGson = new Gson();
                    Object o = mGson.fromJson(response, myCallBack.mType);
                    myCallBack.onSuccess(o);
                }
            }
        });


    }

    /**
     * 在主线程返回失败结果
     */
    private void sendFailResult(final MyCallBack myCallBack, final String err) {

        mDelivery.post(new Runnable() {
            @Override
            public void run() {

                myCallBack.onFail(err);
            }
        });
    }

}
