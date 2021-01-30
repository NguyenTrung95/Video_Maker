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

public class ContrastFragment extends Fragment {
    ConstrastFragmentListener listener;
    private CustomSeekBar seekBar;

    public TextView tvProgress;

    public interface ConstrastFragmentListener {
        void onConstrastChoose(int i);
    }

    public void setConstrastListener(ConstrastFragmentListener constrastFragmentListener) {
        this.listener = constrastFragmentListener;
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
                int i2 = i - 50;
                ContrastFragment.this.tvProgress.setText(String.valueOf(i2));
                if (ContrastFragment.this.listener != null) {
                    ContrastFragment.this.listener.onConstrastChoose(i2);
                }
            }
        });
        return inflate;
    }
}
