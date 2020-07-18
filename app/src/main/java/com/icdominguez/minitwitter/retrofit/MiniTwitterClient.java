package com.icdominguez.minitwitter.retrofit;

import com.icdominguez.minitwitter.common.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    private static MiniTwitterClient instance = null;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;


    public MiniTwitterClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    // Singleton pattern
    public static MiniTwitterClient getInstance() {

        if(instance == null){
            instance = new MiniTwitterClient();
        }

        return instance;
    }

    public MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }
}
