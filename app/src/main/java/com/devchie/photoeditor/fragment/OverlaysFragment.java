package com.devchie.photoeditor.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.devchie.photoeditor.fragment.Overplay.OverlaysViewPagerAdapter;
import com.devchie.videomaker.R;
import com.google.android.material.tabs.TabLayout;

public class OverlaysFragment extends Fragment {
    /*LinearLayout icChristmas;
    LinearLayout icFood;
    LinearLayout icLove;
    LinearLayout icMotivation;
    LinearLayout icPhrases;
    LinearLayout icSayings;
    LinearLayout icSummer;
    LinearLayout icTravel;
    LinearLayout icWinter;*/
    NewOverlaysFragmentListener listener;
    TabLayout tabLayoutOverlays;
    ViewPager viewPagerOverlays;

    public interface NewOverlaysFragmentListener {
        void onOverlaysFragmentClick(Bitmap bitmap);
    }

    public void setListener(NewOverlaysFragmentListener overlaysFragmentListener) {
        this.listener = overlaysFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_overlays, viewGroup, false);

        /*this.icPhrases =  inflate.findViewById(R.id.ic_phrases);
        this.icFood =  inflate.findViewById(R.id.ic_food);
        this.icLove =  inflate.findViewById(R.id.ic_love);
        this.icChristmas =  inflate.findViewById(R.id.ic_christmas);
        this.icSayings =  inflate.findViewById(R.id.ic_sayings);
        this.icSummer =  inflate.findViewById(R.id.ic_summer);
        this.icWinter =  inflate.findViewById(R.id.ic_winter);
        this.icTravel =  inflate.findViewById(R.id.ic_travel);
        this.icMotivation =  inflate.findViewById(R.id.ic_motivation);
        this.icPhrases.setOnClickListener(this);
        this.icFood.setOnClickListener(this);
        this.icLove.setOnClickListener(this);
        this.icChristmas.setOnClickListener(this);
        this.icSayings.setOnClickListener(this);
        this.icSummer.setOnClickListener(this);
        this.icWinter.setOnClickListener(this);
        this.icTravel.setOnClickListener(this);
        this.icMotivation.setOnClickListener(this);*/

        this.tabLayoutOverlays = inflate.findViewById(R.id.tablayoutOverlays);
        ViewPager findViewById = inflate.findViewById(R.id.viewpagerOverlays);
        this.viewPagerOverlays = findViewById;
        tabLayoutOverlays.setSelectedTabIndicatorColor(Color.parseColor("#2051FF"));
        tabLayoutOverlays.setSelectedTabIndicatorHeight((int) (1 * getResources().getDisplayMetrics().density));
        tabLayoutOverlays.setTabTextColors(Color.parseColor("#ff666666"), Color.parseColor("#2051FF"));

        findViewById.setAdapter(new OverlaysViewPagerAdapter(getChildFragmentManager(), getActivity(), new OverlaysViewPagerAdapter.OverlaysViewPagerAdapterListener() {

            @Override
            public void onOverlays(int i) {
                if (listener != null) {
                    listener.onOverlaysFragmentClick(BitmapFactory.decodeResource(getResources(), i));
                }
            }
        }));
        this.tabLayoutOverlays.setupWithViewPager(this.viewPagerOverlays);
        viewPagerOverlays.setOffscreenPageLimit(9);
        viewPagerOverlays.setCurrentItem(0);

       changeTabsOverlays();
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void changeTabsOverlays() {
        ViewGroup viewGroup = (ViewGroup) this.tabLayoutOverlays.getChildAt(0);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(i);
            int childCount2 = viewGroup2.getChildCount();
            for (int i2 = 0; i2 < childCount2; i2++) {
                View childAt = viewGroup2.getChildAt(i2);
                if (childAt instanceof TextView) {
                    ((TextView) childAt).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), new StringBuilder("font/slabo.ttf").toString()));
                }
            }
        }
    }
    /*   public void onPhrases() {
        if (!this.mPhrasesFragment.isAdded()) {
            this.mPhrasesFragment.show(getSupportFragmentManager(), this.mPhrasesFragment.getTag());
        }
    }

    public void onFood() {
        if (!this.mFoodFragment.isAdded()) {
            this.mFoodFragment.show(getSupportFragmentManager(), this.mFoodFragment.getTag());
        }
    }

    public void onLove() {
        if (!this.mLoveFragment.isAdded()) {
            this.mLoveFragment.show(getSupportFragmentManager(), this.mLoveFragment.getTag());
        }
    }

    public void onChristmas() {
        if (!this.mChristmasFragment.isAdded()) {
            this.mChristmasFragment.show(getSupportFragmentManager(), this.mChristmasFragment.getTag());
        }
    }

    public void onSayings() {
        if (!this.mSayingFragment.isAdded()) {
            this.mSayingFragment.show(getSupportFragmentManager(), this.mSayingFragment.getTag());
        }
    }

    public void onSummer() {
        if (!this.mSummerFragment.isAdded()) {
            this.mSummerFragment.show(getSupportFragmentManager(), this.mSummerFragment.getTag());
        }
    }

    public void onWinter() {
        if (!this.mWinterFragment.isAdded()) {
            this.mWinterFragment.show(getSupportFragmentManager(), this.mWinterFragment.getTag());
        }
    }

    public void onTravel() {
        if (!this.mTravelFragment.isAdded()) {
            this.mTravelFragment.show(getSupportFragmentManager(), this.mTravelFragment.getTag());
        }
    }

    public void onMotivation() {
        if (!this.mMotivationFragment.isAdded()) {
            this.mMotivationFragment.show(getSupportFragmentManager(), this.mMotivationFragment.getTag());
        }
    }
*/

/*
    public void onClick(View view) {
        if (this.listener != null) {
            switch (view.getId()) {
                case R.id.ic_christmas:
                    this.listener.onChristmas();
                    return;
                case R.id.ic_food:
                    this.listener.onFood();
                    return;
                case R.id.ic_love:
                    this.listener.onLove();
                    return;
                case R.id.ic_motivation:
                    this.listener.onMotivation();
                    return;
                case R.id.ic_phrases:
                    this.listener.onPhrases();
                    return;
                case R.id.ic_sayings:
                    this.listener.onSayings();
                    return;
                case R.id.ic_summer:
                    this.listener.onSummer();
                    return;
                case R.id.ic_travel:
                    this.listener.onTravel();
                    return;
                case R.id.ic_winter:
                    this.listener.onWinter();
                    return;
                default:
                    return;
            }
        }
    }
*/
}
