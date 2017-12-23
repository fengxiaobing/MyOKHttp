package com.example.myokhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myokhttp.callback.MyCallBack;
import com.example.myokhttp.http.OkHttpUtils;
import com.example.myokhttp.intercepter.HttpParamInterceptor;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.btn_postNet)
    Button btnPostNet;
    @BindView(R.id.tv_result)
    TextView tvResult;

    //QQ下载地址
    private String url = "http://p.gdown.baidu.com/0707fcaed6f4e3de8992f8f26b90ef98efc5bab5" +
            "f75e3c632a63b66712b02ffa696a5265f4c03038964e4cbff3673ce76503f72b3b2daedabd9e8a27bdf23033" +
            "1a9864d2150ecf78faf8df327997b5ad994ca93b0cd7b856df8f094e630f6ab5c58f29b5df2eeb115aff3e91dd529997d8" +
            "00fd1e7600cf21706a5c2fea70f826b436959b6402346beb434e26" +
            "e89549a5e1194685da697fae36e9cbe202d0e2c6d7f257b3a2383666553606fcfe4fdce1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_postNet)
    public void onViewClicked() {
        String path = Environment.getExternalStorageDirectory().getPath()+"/111";
        OkHttpUtils.getInstance().download(url, path, new MyCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("TAG","成功");
                tvResult.setText("成功");

            }

            @Override
            public void onFail(String err) {
                super.onFail(err);
            }

            @Override
            public void onDownloading(int progress) {
                super.onDownloading(progress);
                Log.e("onDownloading","下载进度"+progress);
                tvResult.setText(progress+"");
            }
        });


    }
}
