package com.icdominguez.minitwitter.data;

import android.provider.MediaStore;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.icdominguez.minitwitter.common.Constants;
import com.icdominguez.minitwitter.common.MyApp;
import com.icdominguez.minitwitter.common.SharedPreferencesManager;
import com.icdominguez.minitwitter.retrofit.AuthMiniTwitterClient;
import com.icdominguez.minitwitter.retrofit.AuthMiniTwitterService;
import com.icdominguez.minitwitter.retrofit.request.RequestCreateTweet;
import com.icdominguez.minitwitter.retrofit.request.RequestUserProfile;
import com.icdominguez.minitwitter.retrofit.response.Like;
import com.icdominguez.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.icdominguez.minitwitter.retrofit.response.ResponseUserProfile;
import com.icdominguez.minitwitter.retrofit.response.Tweet;
import com.icdominguez.minitwitter.retrofit.response.TweetDeleted;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private AuthMiniTwitterService authMiniTwitterService;
    private AuthMiniTwitterClient authMiniTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;
    MutableLiveData<String> photoProfile;

    ProfileRepository() {
        authMiniTwitterClient = AuthMiniTwitterClient.getInstance();
        authMiniTwitterService = authMiniTwitterClient.getAuthMiniTwitterService();
        userProfile = getProfile();

        if(photoProfile == null) {
            photoProfile = new MutableLiveData<>();
        }
    }

    public MutableLiveData<String> getPhotoProfile() {
        return photoProfile;
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {

        if(userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authMiniTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal, inténtelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Problemas con la conexión, vuelva a intentarlo más tarde",Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        Call<ResponseUserProfile> call = authMiniTwitterService.updateProfile(requestUserProfile);
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal, inténtelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Problemas con la conexión, vuelva a intentarlo más tarde",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadPhoto(String photoPath) {
        File file = new File(photoPath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = authMiniTwitterService.uploadProfilePhoto(requestBody);

        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if(response.isSuccessful()){
                    SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal, inténtelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Problemas con la conexión, vuelva a intentarlo más tarde",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
