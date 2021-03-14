package com.devchie.photoeditor.fragment.TuneImage;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.devchie.videomaker.R;
import com.google.android.material.tabs.TabLayout;

public class TuneFragment extends Fragment {
    TuneFragmentListener listener;
    TabLayout tabLayoutTune;
    ViewPager viewPagerTune;
    SeekBar sbBrightness;
    SeekBar sbContrast;
    SeekBar sbHue;
    SeekBar sbSaturation;

    TextView tvBrightness;
    TextView tvContrast;
    TextView tvHue;
    TextView tvSaturation;

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
     /*   this.tabLayoutTune = inflate.findViewById(R.id.tablayoutTune);
        ViewPager findViewById = inflate.findViewById(R.id.viewpagerTune);*/
        this.sbBrightness = inflate.findViewById(R.id.sb_brightness);
        this.sbContrast = inflate.findViewById(R.id.sb_contrast);
        this.sbHue = inflate.findViewById(R.id.sb_hue);
        this.sbSaturation = inflate.findViewById(R.id.sb_saturation);

        this.tvBrightness = inflate.findViewById(R.id.tv_brightness);
        this.tvContrast = inflate.findViewById(R.id.tv_contrast);
        this.tvHue = inflate.findViewById(R.id.tv_hue);
        this.tvSaturation = inflate.findViewById(R.id.tv_saturation);

        sbBrightness.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);

        sbBrightness.setProgressTintList(ColorStateList.valueOf(Color.RED));
      //  this.viewPagerTune = findViewById;
      /*  findViewById.setAdapter(new TuneViewPagerAdapter(getChildFragmentManager(), getActivity(), new TuneImageFragmentListener() {
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
        }));*/
      //  this.viewPagerTune.setOffscreenPageLimit(4);
    //    this.tabLayoutTune.setupWithViewPager(this.viewPagerTune);

        //
        this.sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                tvBrightness.setText(String.valueOf(i - 50));
                if (listener != null) {
                    listener.onBrightnessChosse(i-50);
                }

            }
        });


        this.sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                tvContrast.setText(String.valueOf(i - 50));
                if (listener != null) {
                    listener.onBrightnessChosse(i);
                }

            }
        });

        this.sbHue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                tvHue.setText(String.valueOf(i - 50));
                if (listener != null) {
                    listener.onBrightnessChosse(i);
                }

            }
        });

        this.sbSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                tvSaturation.setText(String.valueOf(i - 50));
                if (listener != null) {
                    listener.onBrightnessChosse(i);
                }

            }
        });
        return inflate;
    }


}


