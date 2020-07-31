package com.icdominguez.minitwitter.retrofit;

import com.icdominguez.minitwitter.retrofit.request.RequestCreateTweet;
import com.icdominguez.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthMiniTwitterService {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);

}
