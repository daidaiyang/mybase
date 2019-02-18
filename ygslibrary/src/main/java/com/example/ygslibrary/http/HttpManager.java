package com.example.ygslibrary.http;

import android.util.Log;

import com.example.ygslibrary.RxRetrofitApp;
import com.example.ygslibrary.api.BaseApi;
import com.example.ygslibrary.exception.RetryWhenNetworkException;
import com.example.ygslibrary.http.cookie.CookieInterceptor;
import com.example.ygslibrary.listener.HttpOnNextListener;
import com.example.ygslibrary.subscribers.ProgressSubscriber;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HttpManager {


    private volatile static HttpManager INSTANCE;

    //构造方法私有
    private HttpManager() {
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 处理http请求
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseApi basePar){
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
        builder.addInterceptor(new CookieInterceptor(basePar.isCache(),basePar.getUrl()));
        //如果是全局debug状态，则加入拦截器
        if (RxRetrofitApp.isDebug()){
            builder.addInterceptor(getHttpLoggingInterceptor());
        }

        //创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();

        //rxjava处理请求
        ProgressSubscriber subscriber = new ProgressSubscriber<>(basePar);
        Observable observable = basePar.getObservable(retrofit)
                /*请求失败后重新请求的次数延迟等*/
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),basePar.getRetryDelay(),basePar.getRetryIncreaseDelay()))
                /*生命周期绑定*/
                .compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                //回调线程
                .observeOn(AndroidSchedulers.mainThread())
                .map(basePar);

        /*链接式对象返回*/
        SoftReference<HttpOnNextListener> httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null && httpOnNextListener.get() != null) {
            httpOnNextListener.get().onNext(observable);
        }

        /*数据回调*/
        observable.subscribe(subscriber);
    }








    /**
     * 日志输出
     * @return
     */
    private HttpLoggingInterceptor getHttpLoggingInterceptor(){
        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RxRetrofit","Retrofit====Message:"+message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }
}
