package com.example.myapp.network;

import androidx.annotation.Nullable;

import com.example.model.ResponseToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetDataService {

    @FormUrlEncoded
    @POST("/signup")
    Call <ResponseToken> signUp (@Field("username") String username, @Field("email") String email, @Field("password") String password);
}
