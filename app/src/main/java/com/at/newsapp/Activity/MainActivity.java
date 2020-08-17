package com.at.newsapp.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.at.newsapp.Adapters.ArticlesAdapter;
import com.at.newsapp.dagger.component.AppComponent;

import com.at.newsapp.dagger.component.DaggerAppComponent;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.at.newsapp.Adapters.DocsAdapter;
import com.at.newsapp.Rest.ApiInterface;
import com.at.newsapp.Models.Articles;
import com.at.newsapp.Models.TotalResponse;
import com.at.newsapp.R;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewArticles;
    private ShimmerFrameLayout shimmerFrameLayout;
    private List<Articles> articlesList;
//    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    @Inject
    public FirebaseRemoteConfig firebaseRemoteConfig;
    @Inject
    public Retrofit retrofit;
    private AdLoader adLoader;
    ArticlesAdapter articlesAdapter;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private List<Object> mRecyclerViewItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        HashMap<String, Object> defaults = new HashMap<>();

        defaults.put("showAds", 1);
        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().
                setDeveloperModeEnabled(true).build());
        firebaseRemoteConfig.setDefaults(defaults);
        final Task<Void> fetch = firebaseRemoteConfig.fetch(0);
        fetch.addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
                if(firebaseRemoteConfig.getBoolean("showAds")) {
                    Toast.makeText(MainActivity.this, "Ads Enabled",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Ads Disabled",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        setSupportActionBar(toolbar);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        recyclerViewArticles = findViewById(R.id.articles_recyclerview);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        TextView countryCode = findViewById(R.id.country_code);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.syncState();
        ApiInterface apiService =
                retrofit.create(ApiInterface.class);
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        Call<TotalResponse> call;
        if(countryCodeValue.equalsIgnoreCase("us")){
            call = apiService.getResponseUs();
            countryCode.setText("US");
        }else{
            call = apiService.getResponseIn();
            countryCode.setText("IND");
        }

        call.enqueue(new Callback<TotalResponse>() {
            @Override
            public void onResponse(Call<TotalResponse> call, Response<TotalResponse> response) {
                int statusCode = response.code();
                Log.d("Response8", String.valueOf(statusCode));
                TotalResponse totalResponse = response.body();
                String status = totalResponse.getStatus();
                String totalResults = totalResponse.getNumberOfResults();
                articlesList = totalResponse.getArticlesList();
                int index = 0;
                for (Articles articles : articlesList){
                    mRecyclerViewItems.add(index, articles);
                    index++;
                }
                fillRecyclerView();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Log.d("Response11", String.valueOf(mRecyclerViewItems.size()));
            }

            @Override
            public void onFailure(Call<TotalResponse> call, Throwable t) {
                // Log error here since request failed
                Log.d("Response13", t.toString());
            }
        });
        if(firebaseRemoteConfig.getBoolean("showAds")) {
            MobileAds.initialize(this, initializationStatus -> {
                adLoader.loadAds(new AdRequest.Builder().build(), 3);
                Log.d("nahi", initializationStatus.getAdapterStatusMap().toString());
            });
            Log.d("whattodo","jsdkj");
            adLoader = new AdLoader.Builder(getApplicationContext(), "ca-app-pub-3940256099942544/2247696110")
                    .forUnifiedNativeAd(unifiedNativeAd -> {
                        // Show the ad.
                        mNativeAds.add(unifiedNativeAd);
                        Log.d("Answersss",unifiedNativeAd.getHeadline() +"hey"+mNativeAds.size()+"Hey"+ unifiedNativeAd.getAdvertiser());
                        if (!adLoader.isLoading()) {
                            // The AdLoader is still loading ads.
                            // Expect more adLoaded or onAdFailedToLoad callbacks.
                            insertAdsInMenuItems();

                        } else {
                            // The AdLoader has finished loading ads.
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Handle the failure by logging, altering the UI, and so on.
                            Log.d("Failedaf", String.valueOf(errorCode));
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
        }
    }
    private void fillRecyclerView(){
        Log.d("Size", String.valueOf(mRecyclerViewItems.size()));
        PreLoadingLinearLayoutManager linearLayoutManager = new PreLoadingLinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewArticles.setHasFixedSize(true);
        recyclerViewArticles.setItemViewCacheSize(50);
        recyclerViewArticles.setDrawingCacheEnabled(true);
        recyclerViewArticles.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerViewArticles.setLayoutManager(linearLayoutManager);
        articlesAdapter = new ArticlesAdapter(mRecyclerViewItems);
        Log.d("Size", String.valueOf(mRecyclerViewItems.size()));
        recyclerViewArticles.setAdapter(articlesAdapter);
        articlesAdapter.notifyDataSetChanged();
    }
    private void insertAdsInMenuItems() {
        Log.d("ADSADS", String.valueOf(mNativeAds.size()));
        if (mNativeAds.size() <= 0) {
            fillRecyclerView();
            return;
        }
        int offset = (articlesList.size() / mNativeAds.size()) + 1;
        int index = 0;
        for (UnifiedNativeAd ad : mNativeAds) {
            mRecyclerViewItems.add(index, ad);
            index = index + offset;
        }
        fillRecyclerView();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
