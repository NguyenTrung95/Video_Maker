package com.devchie.videomaker.fragment.Movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.devchie.videomaker.R;

public class RatioFragment extends Fragment implements View.OnClickListener {
    private FrameLayout border1;
    private FrameLayout border2;
    private FrameLayout border3;
    private FrameLayout border4;
    private FrameLayout border5;
    private RatioFragmentListener listener;

    public interface RatioFragmentListener {
        void onRatio(int i);
    }

    public void RatioFragmentListener(RatioFragmentListener ratioFragmentListener) {
        this.listener = ratioFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_movie_ratio, viewGroup, false);
        inflate.findViewById(R.id.btn_square).setOnClickListener(this);
        inflate.findViewById(R.id.btn34).setOnClickListener(this);
        inflate.findViewById(R.id.btn43).setOnClickListener(this);
        inflate.findViewById(R.id.btn169).setOnClickListener(this);
        inflate.findViewById(R.id.btn916).setOnClickListener(this);
        this.border1 = (FrameLayout) inflate.findViewById(R.id.border_1);
        this.border2 = (FrameLayout) inflate.findViewById(R.id.border_2);
        this.border3 = (FrameLayout) inflate.findViewById(R.id.border_3);
        this.border4 = (FrameLayout) inflate.findViewById(R.id.border_4);
        this.border5 = (FrameLayout) inflate.findViewById(R.id.border_5);
        setBorder(0);
        return inflate;
    }

    public void onClick(View view) {
        if (this.listener != null) {
            int id = view.getId();
            if (id != R.id.btn_square) {
                switch (id) {
                    case R.id.btn169:
                        this.listener.onRatio(169);
                        setBorder(2);
                        return;
                    case R.id.btn34:
                        this.listener.onRatio(34);
                        setBorder(3);
                        return;
                    case R.id.btn43:
                        this.listener.onRatio(43);
                        setBorder(4);
                        return;
                    case R.id.btn916:
                        this.listener.onRatio(916);
                        setBorder(1);
                        return;
                    default:
                        return;
                }
            } else {
                this.listener.onRatio(11);
                setBorder(0);
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.try_again), Toast.LENGTH_SHORT).show();
        }
    }

    private void setBorder(int i) {
        FrameLayout[] frameLayoutArr = {this.border1, this.border2, this.border3, this.border4, this.border5};
        for (int i2 = 0; i2 < 5; i2++) {
            if (i2 == i) {
                frameLayoutArr[i2].setVisibility(View.VISIBLE);
            } else {
                frameLayoutArr[i2].setVisibility(View.GONE);
            }
        }
    }
}
