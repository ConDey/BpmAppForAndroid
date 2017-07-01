package com.eazytec.bpm.lib.common.webservice;

import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.lib.common.authentication.TokenIntercepter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * bpm的retrofit服务类
 *
 * @author ConDey
 * @version Id: BPMRetrofit, v 0.1 2017/6/2 上午10:18 ConDey Exp $$
 */
public abstract class BPMRetrofit {

    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    
    private static long SERVICE_CONNECT_TIMEOUT = 10;
    private static long SERVICE_READ_TIMEOUT = 20;

    /**
     * 获得BPMRetrofit对象
     * <p>
     * <p>
     * 所有的BPM请求都应该基于此Retrofit对象来实现
     *
     * @return
     */
    public static synchronized Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.WEB_SERVICE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient())
                    .build();
        }
        return retrofit;
    }

    /**
     * 获得默认的OKHttpClient
     * <p>
     * 基于BPM的所有Retrofit都需要实现这个方法
     *
     * @return
     */
    public static synchronized OkHttpClient okHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(SERVICE_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(SERVICE_READ_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new TokenIntercepter())
                    .build();
        }
        return okHttpClient;
    }
}
