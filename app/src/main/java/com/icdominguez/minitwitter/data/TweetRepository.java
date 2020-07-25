package com.icdominguez.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.icdominguez.minitwitter.MyTweetRecyclerViewAdapter;
import com.icdominguez.minitwitter.common.MyApp;
import com.icdominguez.minitwitter.retrofit.AuthMiniTwitterClient;
import com.icdominguez.minitwitter.retrofit.AuthMiniTwitterService;
import com.icdominguez.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private AuthMiniTwitterService authMiniTwitterService;
    private AuthMiniTwitterClient authMiniTwitterClient;
    LiveData<List<Tweet>> allTweets;

    TweetRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        allTweets = getAllTweets();
    }

    public LiveData<List<Tweet>> getAllTweets () {
        final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        Call<List<Tweet>> call = authMiniTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {

                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return data;
    }
}
