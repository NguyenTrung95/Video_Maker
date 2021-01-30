package com.devchie.videomaker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatSeekBar;

public class CustomSeekBar extends AppCompatSeekBar {
    private Paint paint;
    private Rect rect;
    private int seekbar_height;

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.rect = new Rect();
        this.paint = new Paint();
        this.seekbar_height = 6;
    }

    public CustomSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    public synchronized void onDraw(Canvas canvas) {
        this.rect.set(getThumbOffset() + 0, (getHeight() / 2) - (this.seekbar_height / 2), getWidth() - getThumbOffset(), (getHeight() / 2) + (this.seekbar_height / 2));
        this.paint.setColor(-7829368);
        canvas.drawRect(this.rect, this.paint);
        if (getProgress() > 50) {
            this.rect.set(getWidth() / 2, (getHeight() / 2) - (this.seekbar_height / 2), (getWidth() / 2) + ((getWidth() / 100) * (getProgress() - 50)), (getHeight() / 2) + (this.seekbar_height / 2));
            this.paint.setColor(-16711681);
            canvas.drawRect(this.rect, this.paint);
        }
        if (getProgress() < 50) {
            this.rect.set((getWidth() / 2) - ((getWidth() / 100) * (50 - getProgress())), (getHeight() / 2) - (this.seekbar_height / 2), getWidth() / 2, (getHeight() / 2) + (this.seekbar_height / 2));
            this.paint.setColor(-16711681);
            canvas.drawRect(this.rect, this.paint);
        }
        super.onDraw(canvas);
    }
}
