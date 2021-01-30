package com.devchie.photoeditor.fragment.texttools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.fragment.app.Fragment;
import com.devchie.videomaker.R;
import com.devchie.photoeditor.interfaces.FormatTextFragmentListener;
import com.devchie.photoeditor.view.ToggleImageButton;

public class FormatTextFragment extends Fragment implements View.OnClickListener {
    ImageView btnAlignCenter;
    ImageView btnAlignLeft;
    ImageView btnAlignRight;
    ToggleImageButton btnAllCaps;
    ToggleImageButton btnBold;
    ToggleImageButton btnItalic;
    FormatTextFragmentListener listener;
    SeekBar seekBarPadding;
    SeekBar seekBarTextSize;

    public void setListener(FormatTextFragmentListener formatTextFragmentListener) {
        this.listener = formatTextFragmentListener;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_text_format, viewGroup, false);
        this.btnAlignLeft =  inflate.findViewById(R.id.btn_align_left);
        this.btnAlignCenter =  inflate.findViewById(R.id.btn_align_center);
        this.btnAlignRight =  inflate.findViewById(R.id.btn_align_right);
        this.btnAlignLeft.setOnClickListener(this);
        this.btnAlignRight.setOnClickListener(this);
        this.btnAlignCenter.setOnClickListener(this);
        this.btnBold =  inflate.findViewById(R.id.btn_bold);
        this.btnItalic =  inflate.findViewById(R.id.btn_italic);
        this.btnAllCaps =  inflate.findViewById(R.id.btn_all_caps);
        SeekBar seekBar =  inflate.findViewById(R.id.seebar_text_size);
        this.seekBarTextSize = seekBar;
        seekBar.setMax(60);
        this.seekBarTextSize.setProgress(15);
        this.seekBarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (FormatTextFragment.this.listener != null) {
                    FormatTextFragment.this.listener.onTextSize(i);
                }
            }
        });
        SeekBar seekBar2 =  inflate.findViewById(R.id.sbPadding);
        this.seekBarPadding = seekBar2;
        seekBar2.setMax(100);
        this.seekBarPadding.setProgress(5);
        this.seekBarPadding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (FormatTextFragment.this.listener != null) {
                    FormatTextFragment.this.listener.onTextPadding(i);
                }
            }
        });
        this.btnBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (FormatTextFragment.this.listener == null) {
                    return;
                }
                if (z) {
                    if (FormatTextFragment.this.btnItalic.isChecked()) {
                        FormatTextFragment.this.listener.onTextStyle(1);
                    } else {
                        FormatTextFragment.this.listener.onTextStyle(2);
                    }
                } else if (FormatTextFragment.this.btnItalic.isChecked()) {
                    FormatTextFragment.this.listener.onTextStyle(3);
                } else {
                    FormatTextFragment.this.listener.onTextStyle(4);
                }
            }
        });
        this.btnItalic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (FormatTextFragment.this.listener == null) {
                    return;
                }
                if (z) {
                    if (FormatTextFragment.this.btnBold.isChecked()) {
                        FormatTextFragment.this.listener.onTextStyle(1);
                    } else if (!FormatTextFragment.this.btnBold.isChecked()) {
                        FormatTextFragment.this.listener.onTextStyle(3);
                    }
                } else if (FormatTextFragment.this.btnBold.isChecked()) {
                    FormatTextFragment.this.listener.onTextStyle(2);
                } else if (!FormatTextFragment.this.btnBold.isChecked()) {
                    FormatTextFragment.this.listener.onTextStyle(4);
                }
            }
        });
        this.btnAllCaps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (FormatTextFragment.this.listener == null) {
                    return;
                }
                if (z) {
                    FormatTextFragment.this.listener.onTextStyle(5);
                } else {
                    FormatTextFragment.this.listener.onTextStyle(6);
                }
            }
        });
        return inflate;
    }

    public void onClick(View view) {
        if (this.listener != null) {
            switch (view.getId()) {
                case R.id.btn_align_center:
                    this.listener.onTextAlign(2);
                    return;
                case R.id.btn_align_left:
                    this.listener.onTextAlign(1);
                    return;
                case R.id.btn_align_right:
                    this.listener.onTextAlign(3);
                    return;
                default:
                    return;
            }
        }
    }
}
