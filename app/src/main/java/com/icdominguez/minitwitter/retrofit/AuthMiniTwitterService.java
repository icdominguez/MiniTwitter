package com.icdominguez.minitwitter.retrofit;

import com.icdominguez.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthMiniTwitterService {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();
}
