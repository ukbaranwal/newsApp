package com.at.newsapp.dagger.component;

import android.telephony.TelephonyManager;

import com.at.newsapp.Activity.MainActivity;
import com.at.newsapp.dagger.module.AppModule;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Provides;
import retrofit2.Retrofit;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity mainActivity);



}
