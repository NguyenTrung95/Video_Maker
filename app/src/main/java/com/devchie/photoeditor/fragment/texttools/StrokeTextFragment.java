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
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.adapter.ColorAdapter;
import com.devchie.photoeditor.interfaces.StrokeFragmentListener;
import com.devchie.photoeditor.view.RoundFrameLayout;

public class StrokeTextFragment extends Fragment implements ColorAdapter.ColorAdapterListener {
    private ColorAdapter adapter;

    public RoundFrameLayout btn_color_picker_stroke;

    public StrokeFragmentListener listener;
    private RecyclerView recyclerStrokeColor;
    private SeekBar sbStrokeWidth;

    public void setListener(StrokeFragmentListener strokeFragmentListener) {
        this.listener = strokeFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_text_stroke, viewGroup, false);
        this.recyclerStrokeColor = inflate.findViewById(R.id.recycler_color_stroke);
        this.sbStrokeWidth = (SeekBar) inflate.findViewById(R.id.sb_stroke_width);
        this.btn_color_picker_stroke = (RoundFrameLayout) inflate.findViewById(R.id.btn_picker_color_stroke);
        this.recyclerStrokeColor.setHasFixedSize(true);
        this.recyclerStrokeColor.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        ColorAdapter colorAdapter = new ColorAdapter(getActivity(), this);
        this.adapter = colorAdapter;
        this.recyclerStrokeColor.setAdapter(colorAdapter);
        this.btn_color_picker_stroke.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(StrokeTextFragment.this.getActivity()).setTitle("Select color").wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setPositiveButton((CharSequence) "OK", (ColorPickerClickListener) new ColorPickerClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                        if (StrokeTextFragment.this.listener != null) {
                            StrokeTextFragment.this.listener.onStrokeColorSelected(i);
                            StrokeTextFragment.this.btn_color_picker_stroke.getDelegate().setBackgroundColor(i);
                            StrokeTextFragment.this.btn_color_picker_stroke.getDelegate().setStrokeColor(StrokeTextFragment.this.getResources().getColor(R.color.icChecked));
                        }
                    }
                }).setNegativeButton( "Cancel",  new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).build().show();
            }
        });
        this.sbStrokeWidth.setMax(200);
        this.sbStrokeWidth.setProgress(0);
        this.sbStrokeWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StrokeTextFragment.this.listener != null) {
                    StrokeTextFragment.this.listener.onStrokeWidthChangeListener(i / 10);
                }
            }
        });
        return inflate;
    }

    public void onColorItemSelected(int i) {
        StrokeFragmentListener strokeFragmentListener = this.listener;
        if (strokeFragmentListener != null) {
            strokeFragmentListener.onStrokeColorSelected(i);
        }
    }
}
