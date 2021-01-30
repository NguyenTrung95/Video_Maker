package com.devchie.videomaker.fragment.Movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.devchie.videomaker.R;
    public class DurationFragment extends Fragment {
    DurationFragmentListener listener;
    RadioGroup radioGroupDuration;

    public interface DurationFragmentListener {
        void onDurationSelect(int i);
    }

    public void setDurationFragmentListener(DurationFragmentListener durationFragmentListener) {
        this.listener = durationFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_movie_duration, viewGroup, false);
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.rdgDuration);
        this.radioGroupDuration = radioGroup;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (DurationFragment.this.listener != null) {
                    switch (i) {
                        case R.id.rd10:
                            DurationFragment.this.listener.onDurationSelect(1000);
                            return;
                        case R.id.rd15:
                            DurationFragment.this.listener.onDurationSelect(2000);
                            return;
                        case R.id.rd20:
                            DurationFragment.this.listener.onDurationSelect(PathInterpolatorCompat.MAX_NUM_POINTS);
                            return;
                        case R.id.rd25:
                            DurationFragment.this.listener.onDurationSelect(5000);
                            return;
                        case R.id.rd30:
                            DurationFragment.this.listener.onDurationSelect(10000);
                            return;
                        default:
                            return;
                    }
                } else {
                    Toast.makeText(DurationFragment.this.getActivity(), DurationFragment.this.getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return inflate;
    }
}
