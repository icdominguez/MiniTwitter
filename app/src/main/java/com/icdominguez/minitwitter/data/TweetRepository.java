package com.icdominguez.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.icdominguez.minitwitter.common.MyApp;
import com.icdominguez.minitwitter.retrofit.AuthMiniTwitterClient;
import com.icdominguez.minitwitter.retrofit.AuthMiniTwitterService;
import com.icdominguez.minitwitter.retrofit.request.RequestCreateTweet;
import com.icdominguez.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterService authMiniTwitterService;
    private AuthMiniTwitterClient authMiniTwitterClient;
    MutableLiveData<List<Tweet>> allTweets;

    TweetRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        allTweets = getAllTweets();
    }

    public MutableLiveData<List<Tweet>> getAllTweets () {

        if(allTweets == null) {
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = authMiniTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {

                if (response.isSuccessful()) {
                    allTweets.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public void createTweet (String tweet) {
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(tweet);
        Call<Tweet> call = authMiniTwitterService.createTweet(requestCreateTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()) {
                    List<Tweet> clonedList = new ArrayList<>();
                    // We add first the first tweet that comes from the server
                    clonedList.add(response.body());

                    for(int i=0; i < allTweets.getValue().size(); i++) {
                        clonedList.add(new Tweet(allTweets.getValue().get(i)));
                    }

                    allTweets.setValue(clonedList);

                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal, inténtelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Problemas con la conexión, vuelva a intentarlo más tarde",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
