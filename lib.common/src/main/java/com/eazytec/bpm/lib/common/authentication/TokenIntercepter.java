package com.eazytec.bpm.lib.common.authentication;

import com.eazytec.bpm.lib.utils.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Token默认的拦截器，
 * <p>
 * <p>
 * 遵循BPM规范，用于在网络请求当中Header中加入Token
 * <p>
 * 有些网络请求如果有独立的token的话需要自己实现
 *
 * @author ConDey
 * @version Id: TokenIntercepter, v 0.1 2017/6/2 上午10:34 ConDey Exp $$
 */
public class TokenIntercepter implements Interceptor {

    private static final String TOKEN_KEY = "token";

    @Override public Response intercept(Chain chain) throws IOException {

        Request originalRequest = chain.request();

        CurrentUser currentUser = CurrentUser.getCurrentUser();
        if (currentUser.isLogin() && currentUser.getToken() != null) {
            // 如果有TokenHeader的话就不加Token的Header
            if (StringUtils.isSpace(originalRequest.header(TOKEN_KEY))) {
                // 如果已经登录
                Request authorised = originalRequest.newBuilder()
                        .header(TOKEN_KEY, currentUser.getToken().toString())
                        .build();
                return chain.proceed(authorised);
            }
        }
        return chain.proceed(originalRequest);
    }
}
