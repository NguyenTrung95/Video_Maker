package com.devchie.photoeditor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.interfaces.OverlaysFragmentListener;

public class OverlaysFragment extends Fragment implements View.OnClickListener {
    LinearLayout icChristmas;
    LinearLayout icFood;
    LinearLayout icLove;
    LinearLayout icMotivation;
    LinearLayout icPhrases;
    LinearLayout icSayings;
    LinearLayout icSummer;
    LinearLayout icTravel;
    LinearLayout icWinter;
    OverlaysFragmentListener listener;

    public void setListener(OverlaysFragmentListener overlaysFragmentListener) {
        this.listener = overlaysFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_overlays, viewGroup, false);
        this.icPhrases =  inflate.findViewById(R.id.ic_phrases);
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
        this.icMotivation.setOnClickListener(this);
        return inflate;
    }

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
}
