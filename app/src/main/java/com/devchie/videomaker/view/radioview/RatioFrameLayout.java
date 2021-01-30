package com.devchie.videomaker.view.radioview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RatioFrameLayout extends FrameLayout implements RatioMeasureDelegate {
    private RatioLayoutDelegate mRatioLayoutDelegate;

    public RatioFrameLayout(Context context) {
        super(context);
    }

    public RatioFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attributeSet);
    }

    public RatioFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attributeSet, i);
    }

    public RatioFrameLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mRatioLayoutDelegate = RatioLayoutDelegate.obtain(this, attributeSet, i, i2);
    }


    public void onMeasure(int i, int i2) {
        RatioLayoutDelegate ratioLayoutDelegate = this.mRatioLayoutDelegate;
        if (ratioLayoutDelegate != null) {
            ratioLayoutDelegate.update(i, i2);
            i = this.mRatioLayoutDelegate.getWidthMeasureSpec();
            i2 = this.mRatioLayoutDelegate.getHeightMeasureSpec();
        }
        super.onMeasure(i, i2);
    }

    public void setRatio(RatioDatumMode ratioDatumMode, float f, float f2) {
        RatioLayoutDelegate ratioLayoutDelegate = this.mRatioLayoutDelegate;
        if (ratioLayoutDelegate != null) {
            ratioLayoutDelegate.setRatio(ratioDatumMode, f, f2);
        }
    }

    public void setSquare(boolean z) {
        RatioLayoutDelegate ratioLayoutDelegate = this.mRatioLayoutDelegate;
        if (ratioLayoutDelegate != null) {
            ratioLayoutDelegate.setSquare(z);
        }
    }

    public void setAspectRatio(float f) {
        RatioLayoutDelegate ratioLayoutDelegate = this.mRatioLayoutDelegate;
        if (ratioLayoutDelegate != null) {
            ratioLayoutDelegate.setAspectRatio(f);
        }
    }
}
