package com.example.ygslibrary.listener;

import java.util.Observable;

public abstract class HttpOnNextListener<T> {

    //请求成功后的回调
    public abstract void onNext(T t);

    //缓存回调
    public void onCacheNext(String string){}

    //成功后的扩展式回调，使用Observable
    public void onNext(Observable observable){}

    //失败或者错误方法-主动调用
    public void onError(Throwable e){}


    // 取消回调
    public void onCancel(){}



}
