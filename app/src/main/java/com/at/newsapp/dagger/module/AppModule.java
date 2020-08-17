package com.at.newsapp.dagger.module;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.at.newsapp.Activity.MainActivity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    public static final String BASE_URL = "https://newsapi.org/v2/";
    @Provides
    FirebaseRemoteConfig provideFirebaseRemoteConfig(){
        return FirebaseRemoteConfig.getInstance();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
