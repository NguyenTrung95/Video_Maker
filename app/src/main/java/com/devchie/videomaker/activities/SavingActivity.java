package com.devchie.videomaker.activities;

import android.os.Bundle;
import android.view.View;
import com.devchie.videomaker.R;
import com.devchie.videomaker.ads.AdmobAds;

public class SavingActivity extends BaseSplitActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.layout_saving);
        AdmobAds.loadNativeAds(this, (View) null);

    }
}
