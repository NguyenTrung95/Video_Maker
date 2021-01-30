package com.devchie.photoeditor.fragment.texttools;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.flask.colorpicker.BuildConfig;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.adapter.ColorAdapter;
import com.devchie.photoeditor.interfaces.HightLightFragmentListener;
import com.devchie.photoeditor.view.RoundFrameLayout;

public class HightLightTextFragment extends Fragment implements ColorAdapter.ColorAdapterListener {


    public String aa = "28";
    private ColorAdapter adapter;

    public RoundFrameLayout btn_color_picker_highlight;

    public HightLightFragmentListener listener;
    private RecyclerView recyclerColorHighLight;
    private SeekBar sbRadius;
    private SeekBar sbTranparency;


    public String Changeprogress(int i) {
        switch (i) {
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "05";
            case 6:
                return "06";
            case 7:
                return "07";
            case 8:
                return "08";
            case 9:
                return "09";
            case 10:
                return "0A";
            case BuildConfig.VERSION_CODE /*11*/:
                return "0B";
            case 12:
                return "0C";
            case 13:
                return "0D";
            case 14:
                return "0E";
            case 15:
                return "0F";
            default:
                return null;
        }
    }

    public void setListener(HightLightFragmentListener hightLightFragmentListener) {
        this.listener = hightLightFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_text_highlight, viewGroup, false);
        this.sbTranparency =  inflate.findViewById(R.id.sbTranparencyHighlight);
        this.sbRadius =  inflate.findViewById(R.id.sbRadius);
        RecyclerView findViewById = inflate.findViewById(R.id.recycler_color_highlight);
        this.recyclerColorHighLight = findViewById;
        findViewById.setHasFixedSize(true);
        this.recyclerColorHighLight.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        ColorAdapter colorAdapter = new ColorAdapter(getActivity(), this);
        this.adapter = colorAdapter;
        this.recyclerColorHighLight.setAdapter(colorAdapter);
        this.sbTranparency.setProgress(0);
        this.sbTranparency.setMax(255);
        this.sbTranparency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i < 16) {
                    HightLightTextFragment hightLightTextFragment = HightLightTextFragment.this;
                  hightLightTextFragment.aa = hightLightTextFragment.Changeprogress(i);
                } else {
                     HightLightTextFragment.this.aa = Integer.toHexString(i);
                }
                if (HightLightTextFragment.this.listener != null) {
                    HightLightTextFragment.this.listener.onHightLightColorOpacityChangeListerner(HightLightTextFragment.this.aa);
                }
            }
        });
        this.sbRadius.setMax(100);
        this.sbRadius.setProgress(0);
        this.sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (HightLightTextFragment.this.listener != null) {
                    HightLightTextFragment.this.listener.onHighLightRadius(i);
                }
            }
        });
        RoundFrameLayout roundFrameLayout =  inflate.findViewById(R.id.btn_picker_color_highlight);
        this.btn_color_picker_highlight = roundFrameLayout;
        roundFrameLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(HightLightTextFragment.this.getActivity()).setTitle("Select color").wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setPositiveButton((CharSequence) "OK", (ColorPickerClickListener) new ColorPickerClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                        if (HightLightTextFragment.this.listener != null) {
                            HightLightTextFragment.this.listener.onHightLightColorSelected(i);
                            HightLightTextFragment.this.btn_color_picker_highlight.getDelegate().setBackgroundColor(i);
                            HightLightTextFragment.this.btn_color_picker_highlight.getDelegate().setStrokeColor(HightLightTextFragment.this.getResources().getColor(R.color.icChecked));
                        }
                    }
                }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).build().show();
            }
        });
        return inflate;
    }

    public void onColorItemSelected(int i) {
        HightLightFragmentListener hightLightFragmentListener = this.listener;
        if (hightLightFragmentListener != null) {
            hightLightFragmentListener.onHightLightColorSelected(i);
        }
    }
}
