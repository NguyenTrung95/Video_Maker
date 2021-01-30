package com.devchie.videomaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;

import com.devchie.videomaker.R;
import com.devchie.videomaker.ads.AdmobAds;

public class SplashActivity extends BaseSplitActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3500;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        MobileAds.initialize(this);
        AudienceNetworkAds.initialize(this);
        AdmobAds.initFullAdStart(this);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashActivity.this.startToMainActivity();
            }
        }, 3500);

    }


    public void startToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public void onDestroy() {
        super.onDestroy();
    }


}
