package com.devchie.photoeditor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.devchie.videomaker.view.RoundViewDelegate;

public class RoundFrameLayout extends FrameLayout {
    private RoundViewDelegate delegate;

    public RoundFrameLayout(Context context) {
        this(context,  null);
    }

    public RoundFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.delegate = new RoundViewDelegate(this, context, attributeSet);
    }

    public RoundViewDelegate getDelegate() {
        return this.delegate;
    }


    public void onMeasure(int i, int i2) {
        if (!this.delegate.isWidthHeightEqual() || getWidth() <= 0 || getHeight() <= 0) {
            super.onMeasure(i, i2);
            return;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(Math.max(getWidth(), getHeight()), 1073741824);
        super.onMeasure(makeMeasureSpec, makeMeasureSpec);
    }


    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.delegate.isRadiusHalfHeight()) {
            this.delegate.setCornerRadius(getHeight() / 2);
        } else {
            this.delegate.setBgSelector();
        }
    }
}
