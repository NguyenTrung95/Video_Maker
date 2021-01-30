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

public class SaturationFragment extends Fragment {
    SaturationFragmentListener listener;
    private CustomSeekBar seekBar;

    public TextView tvProgress;

    public interface SaturationFragmentListener {
        void onSaturationChoose(int i);
    }

    public void setSaturationListener(SaturationFragmentListener saturationFragmentListener) {
        this.listener = saturationFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_tune_seekbar, viewGroup, false);
        this.seekBar = inflate.findViewById(R.id.seekbar_tune);
        this.tvProgress = (TextView) inflate.findViewById(R.id.tvTuneProgress);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i - 50;
                SaturationFragment.this.tvProgress.setText(String.valueOf(i2));
                SaturationFragment.this.listener.onSaturationChoose(i2);
            }
        });
        return inflate;
    }
}
