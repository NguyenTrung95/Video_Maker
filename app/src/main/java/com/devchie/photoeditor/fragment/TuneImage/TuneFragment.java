package com.devchie.photoeditor.fragment.TuneImage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.devchie.videomaker.listener.TuneImageFragmentListener;
import com.devchie.videomaker.R;

public class TuneFragment extends Fragment {
    TuneFragmentListener listener;
    TabLayout tabLayoutTune;
    ViewPager viewPagerTune;

    public interface TuneFragmentListener {
        void onBrightnessChosse(int i);

        void onConstrastChosse(int i);

        void onHueChosee(int i);

        void onSaturationChosse(int i);
    }

    public void setTuneFragmentListener(TuneFragmentListener tuneFragmentListener) {
        this.listener = tuneFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_tune, viewGroup, false);
        this.tabLayoutTune = inflate.findViewById(R.id.tablayoutTune);
        ViewPager findViewById = inflate.findViewById(R.id.viewpagerTune);
        this.viewPagerTune = findViewById;
        findViewById.setAdapter(new TuneViewPagerAdapter(getChildFragmentManager(), getActivity(), new TuneImageFragmentListener() {
            public void onBrightnessChanged(int i) {
                TuneFragment.this.listener.onBrightnessChosse(i - 50);
            }

            public void onSaturationChanged(int i) {
                TuneFragment.this.listener.onSaturationChosse(i);
            }

            public void onConstrantChanged(int i) {
                TuneFragment.this.listener.onConstrastChosse(i);
            }

            public void onHueChanged(int i) {
                TuneFragment.this.listener.onHueChosee(i);
            }
        }));
        this.viewPagerTune.setOffscreenPageLimit(4);
        this.tabLayoutTune.setupWithViewPager(this.viewPagerTune);
        return inflate;
    }
}
