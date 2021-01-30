package com.devchie.photoeditor.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;
import com.devchie.videomaker.R;

public class DiscardDialog extends Dialog {
    Activity context;
    TextView tvDiscard;
    TextView tvKeep;

    public DiscardDialog(Activity activity) {
        super(activity);
        this.context = activity;
        setContentView(R.layout.edit_photo_discard_dialog);
        addControls();
        addEvents();
    }

    private void addEvents() {
        this.tvDiscard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscardDialog.this.context.finish();
            }
        });
        this.tvKeep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DiscardDialog.this.dismiss();
            }
        });
    }

    private void addControls() {
        this.tvDiscard =  findViewById(R.id.tv_discard);
        this.tvKeep =  findViewById(R.id.tv_keep);
        setCanceledOnTouchOutside(true);
    }
}
