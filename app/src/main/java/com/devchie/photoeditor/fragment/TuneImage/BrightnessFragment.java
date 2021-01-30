package com.devchie.photoeditor.fragment.TuneImage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.devchie.videomaker.R;
import com.devchie.videomaker.view.CustomSeekBar;

public class BrightnessFragment extends Fragment {
    BrightnessFragmentListener listener;
    private CustomSeekBar seekBar;

    public TextView tvProgress;

    public interface BrightnessFragmentListener {
        void onBrightnessChoose(int i);
    }

    public void setBrightnessListener(BrightnessFragmentListener brightnessFragmentListener) {
        this.listener = brightnessFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tune_seekbar, viewGroup, false);
        this.seekBar = inflate.findViewById(R.id.seekbar_tune);
        this.tvProgress =  inflate.findViewById(R.id.tvTuneProgress);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                BrightnessFragment.this.tvProgress.setText(String.valueOf(i - 50));
                if (BrightnessFragment.this.listener != null) {
                    BrightnessFragment.this.listener.onBrightnessChoose(i);
                }
            }
        });
        return inflate;
    }
}
