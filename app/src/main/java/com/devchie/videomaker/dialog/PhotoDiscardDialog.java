package com.devchie.videomaker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;
import com.devchie.videomaker.R;
import com.devchie.videomaker.activities.MovieActivity;

public class PhotoDiscardDialog extends Dialog {
    Activity context;
    TextView tvDiscard;
    TextView tvKeep;

    public PhotoDiscardDialog(Activity activity) {
        super(activity);
        this.context = activity;
        setContentView(R.layout.dialog_discard);
        addControls();
        addEvents();
    }

    private void addEvents() {
        this.tvDiscard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotoDiscardDialog.this.context.finish();
                if (PhotoDiscardDialog.this.context instanceof MovieActivity) {
                    ((MovieActivity) PhotoDiscardDialog.this.context).destroyMovie();
                }
            }
        });
        this.tvKeep.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PhotoDiscardDialog.this.dismiss();
            }
        });
    }

    private void addControls() {
        this.tvDiscard = (TextView) findViewById(R.id.tv_discard);
        this.tvKeep = (TextView) findViewById(R.id.tv_keep);
        setCanceledOnTouchOutside(true);
    }
}
