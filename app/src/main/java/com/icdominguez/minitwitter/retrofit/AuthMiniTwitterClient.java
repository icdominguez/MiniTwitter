package com.icdominguez.minitwitter.retrofit;

import com.icdominguez.minitwitter.common.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthMiniTwitterClient {

    private static AuthMiniTwitterClient instance = null;
    private AuthMiniTwitterService authMiniTwitterService;
    private Retrofit retrofit;


    public AuthMiniTwitterClient() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());

        OkHttpClient client = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        authMiniTwitterService = retrofit.create(AuthMiniTwitterService.class);
    }

    // Singleton pattern
    public static AuthMiniTwitterClient getInstance() {

        if(instance == null){
            instance = new AuthMiniTwitterClient();
        }

        return instance;
    }

    public AuthMiniTwitterService getAuthMiniTwitterService(){
        return authMiniTwitterService;
    }
}
