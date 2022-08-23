package com.example.umeme.Services;

import com.example.umeme.Models.AccessToken;
import com.example.umeme.Models.STKPush;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface STKPushService {
    Call<STKPush> sendPush(@Body STKPush stkPush);

    @GET("oauth/v1/generate?grant_type=client_credentials")
    Call<AccessToken> getAccessToken();
}
