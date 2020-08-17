package com.at.newsapp.Rest;

import com.at.newsapp.Models.TotalResponse;
import com.at.newsapp.Utils;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("top-headlines?country=in&apiKey=2497d9888a9a4b33b8190700e9d769a8")
    Call<TotalResponse> getResponseIn();
    @GET("top-headlines?country=us&apiKey=2497d9888a9a4b33b8190700e9d769a8")
    Call<TotalResponse> getResponseUs();
}
