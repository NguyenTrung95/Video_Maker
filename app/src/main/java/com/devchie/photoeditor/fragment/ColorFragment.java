package com.devchie.photoeditor.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.adapter.ColorAdapter;
import com.devchie.photoeditor.interfaces.ColorFragmentListener;
import com.devchie.photoeditor.view.RoundFrameLayout;

public class ColorFragment extends Fragment implements ColorAdapter.ColorAdapterListener {
    private ColorAdapter adapterTextColor;

    public RoundFrameLayout btn_color_picker_text;

    public ColorFragmentListener listener;
    private RecyclerView recyclerColorText;
    private SeekBar sbTranparencyText;

    public void setListener(ColorFragmentListener colorFragmentListener) {
        this.listener = colorFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_text_color, viewGroup, false);
        this.sbTranparencyText = (SeekBar) inflate.findViewById(R.id.sbTranparencyText);
        RecyclerView findViewById = inflate.findViewById(R.id.recycler_color_text);
        this.recyclerColorText = findViewById;
        findViewById.setHasFixedSize(true);
        this.recyclerColorText.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ColorAdapter colorAdapter = new ColorAdapter(getActivity(), this);
        this.adapterTextColor = colorAdapter;
        this.recyclerColorText.setAdapter(colorAdapter);
        RoundFrameLayout roundFrameLayout =  inflate.findViewById(R.id.btn_picker_color_text);
        this.btn_color_picker_text = roundFrameLayout;
        roundFrameLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(ColorFragment.this.getActivity()).setTitle("Select color").wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setPositiveButton((CharSequence) "OK", (ColorPickerClickListener) new ColorPickerClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                        if (ColorFragment.this.listener != null) {
                            ColorFragment.this.listener.onColorSelected(i);
                            ColorFragment.this.btn_color_picker_text.getDelegate().setBackgroundColor(i);
                            ColorFragment.this.btn_color_picker_text.getDelegate().setStrokeColor(ColorFragment.this.getResources().getColor(R.color.icChecked));
                        }
                    }
                }).setNegativeButton( "Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).build().show();
            }
        });
        this.sbTranparencyText.setMax(255);
        this.sbTranparencyText.setProgress(255);
        this.sbTranparencyText.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (ColorFragment.this.listener != null) {
                    ColorFragment.this.listener.onColorOpacityChangeListerner(i);
                }
            }
        });
        return inflate;
    }

    public void onColorItemSelected(int i) {
        ColorFragmentListener colorFragmentListener = this.listener;
        if (colorFragmentListener != null) {
            colorFragmentListener.onColorSelected(i);
        }
    }
}
