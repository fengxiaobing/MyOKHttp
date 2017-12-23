package com.example.myokhttp.callback;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/12/23.
 */

public abstract class MyCallBack<T> {
    public Type mType;

    public MyCallBack() {
        this.mType =  getSuperclassTypeParameter(getClass());
    }
    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public  void onSuccess(T t){}
    public  void onFail(String err){}
    public  void onDownloading(int progress){}

}
