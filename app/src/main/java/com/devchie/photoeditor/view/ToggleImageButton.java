package com.devchie.photoeditor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ToggleButton;
import com.devchie.videomaker.R;

public class ToggleImageButton extends ToggleButton {
    private Drawable drawableOff;
    private Drawable drawableOn;

    public ToggleImageButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        obtainStyledAttributes(context, attributeSet);
        removeText();
    }

    public ToggleImageButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        obtainStyledAttributes(context, attributeSet);
        removeText();
    }

    public ToggleImageButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        obtainStyledAttributes(context, attributeSet);
        removeText();
    }

    private void removeText() {
        setTextOn("");
        setTextOff("");
        super.setChecked(!isChecked());
        super.setChecked(!isChecked());
    }

    private void obtainStyledAttributes(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ToggleImageButton, 0, 0);
        this.drawableOn = obtainStyledAttributes.getDrawable(1);
        this.drawableOff = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        syncDrawableState();
    }

    private void syncDrawableState() {
        Drawable drawable;
        Drawable drawable2;
        boolean isChecked = isChecked();
        if (isChecked && (drawable2 = this.drawableOn) != null) {
            setBackgroundDrawable(drawable2);
        } else if (!isChecked && (drawable = this.drawableOff) != null) {
            setBackgroundDrawable(drawable);
        }
    }

    public void setChecked(boolean z) {
        super.setChecked(z);
        syncDrawableState();
    }

    public void setDrawableOn(Drawable drawable) {
        this.drawableOn = drawable;
        syncDrawableState();
    }

    public void setDrawableOff(Drawable drawable) {
        this.drawableOff = drawable;
        syncDrawableState();
    }
}
