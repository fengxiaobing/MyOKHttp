package com.example.myokhttp.intercepter;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by RF
 * on 2017/11/28.
 * 请求时数据处理拦截器
 */

public class HttpParamInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl.Builder httpUrl = request.url().newBuilder()
        //添加同一参数 如手机唯一标识符，token等
        .addQueryParameter("city","CHSH000000");

        Request newrequest = new Request.Builder()
        //添加公共的头部
        .addHeader("User-Agent", "pinxiango")
                .method(request.method(),request.body())
                .url(httpUrl.build())
                .build();
//        printReponseMessage(chain.proceed(newrequest),httpUrl.build().toString());
        return chain.proceed(newrequest);
    }

    /**
     * 打印网络请求日志
     * @param response
     */
    private void printReponseMessage(Response response, String url)  throws IOException{
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.defaultCharset();
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String bodyString = buffer.clone().readString(charset);
        Log.e("OK冰",url+ bodyString);
    }

}

