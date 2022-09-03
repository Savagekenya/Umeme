package com.example.umeme.interceptor;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.example.umeme.BuildConfig;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import java.io.IOException;

public class  AccessTokenInterceptor implements Interceptor{
    public AccessTokenInterceptor(){

    }
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        String keys = BuildConfig.CONSUMER_KEY + ":" + BuildConfig.CONSUMER_SECRET;


        Request request = chain.request().newBuilder()
                .addHeader("Authorization","Basic" + Base64.encodeToString(keys.getBytes(), Base64.NO_WRAP))
                .build();
        return chain.proceed(request);

    }
}