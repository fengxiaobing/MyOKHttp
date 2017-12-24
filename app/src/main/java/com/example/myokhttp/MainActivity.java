package com.example.myokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myokhttp.bean.WeatherEntity;
import com.example.myokhttp.callback.MyCallBack;
import com.example.myokhttp.http.OkHttpUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   private TextView tv_result;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_getNet = findViewById(R.id.btn_getNet);
        btn_getNet.setOnClickListener(this);
        tv_result = findViewById(R.id.tv_result);
        url = "http://tj.nineton.cn/Heart/index/all?city=CHSH000000";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getNet:
                //获取响应值
                OkHttpUtils.getInstance().get(url, new MyCallBack<WeatherEntity>() {
                    @Override
                    public void onSuccess(WeatherEntity weatherEntity) {
                        WeatherEntity mWeatherEntity = weatherEntity;
                        tv_result.setText(mWeatherEntity.getStatus());
                    }

                    @Override
                    public void onFail(String err) {
                        super.onFail(err);
                    }
                });
//                OkHttpUtils.getInstance().get("http://tj.nineton.cn/Heart/index/all", new MyCallBack<String>() {
//                    @Override
//                    public void onSuccess(String weatherEntity) {
//                        String mWeatherEntity = weatherEntity;
//                        tv_result.setText(mWeatherEntity);
//                    }
//
//                    @Override
//                    public void onFail(String err) {
//
//                    }
//                });

                break;
        }
    }
}
