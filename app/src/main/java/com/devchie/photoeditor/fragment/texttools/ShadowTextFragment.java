package com.devchie.photoeditor.fragment.texttools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.devchie.photoeditor.interfaces.ShadowFragmentListener;
import com.devchie.videomaker.R;

public class ShadowTextFragment extends Fragment {

    public ShadowFragmentListener listener;
    private SeekBar sb_dRadius_shadow;
    private SeekBar sb_Dy_shadow;

    public float getConvertedValue(int i) {
        return (((float) i) * 0.5f) / 10.0f;
    }

    public void setListener(ShadowFragmentListener spacingFragmentListener) {
        this.listener = spacingFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_text_shadow, viewGroup, false);
        this.sb_dRadius_shadow = (SeekBar) inflate.findViewById(R.id.sb_dRadius_shadow);
        this.sb_Dy_shadow = (SeekBar) inflate.findViewById(R.id.sb_Dy_shadow);
        this.sb_Dy_shadow.setMax(90);
        this.sb_Dy_shadow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (ShadowTextFragment.this.listener != null) {
                    ShadowTextFragment.this.listener.onDyShadowChangeListener(i);
                }
            }
        });
        this.sb_dRadius_shadow.setMax(250);
        this.sb_dRadius_shadow.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (ShadowTextFragment.this.listener != null) {
                    ShadowTextFragment.this.listener.onRadiusChangeListener(i);
                }
            }
        });
        return inflate;
    }
}
