package com.devchie.videomaker.view.radioview;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.devchie.videomaker.R;

public final class RatioLayoutDelegate<TARGET extends View & RatioMeasureDelegate> {
    private float mAspectRatio;
    private float mDatumHeight;
    private float mDatumWidth;
    private int mHeightMeasureSpec;
    private boolean mIsSquare;
    private RatioDatumMode mRatioDatumMode;
    private final TARGET mRatioTarget;
    private int mWidthMeasureSpec;

    private int resolveSize(int i, int i2) {
        return i;
    }

    public static <TARGET extends View & RatioMeasureDelegate> RatioLayoutDelegate obtain(TARGET target, AttributeSet attributeSet) {
        return obtain(target, attributeSet, 0);
    }

    public static <TARGET extends View & RatioMeasureDelegate> RatioLayoutDelegate obtain(TARGET target, AttributeSet attributeSet, int i) {
        return obtain(target, attributeSet, 0, 0);
    }

    public static <TARGET extends View & RatioMeasureDelegate> RatioLayoutDelegate obtain(TARGET target, AttributeSet attributeSet, int i, int i2) {
        return new RatioLayoutDelegate(target, attributeSet, i, i2);
    }

    @SuppressLint("ResourceType")
    private RatioLayoutDelegate(TARGET target, AttributeSet attributeSet, int i, int i2) {
        int[] ViewSizeCalculate = {R.attr.datumRatio, R.attr.heightRatio, R.attr.layoutAspectRatio, R.attr.layoutSquare, R.attr.widthRatio};
        this.mRatioTarget = target;
        TypedArray obtainStyledAttributes = target.getContext().obtainStyledAttributes(attributeSet, ViewSizeCalculate, i, i2);
        this.mRatioDatumMode = RatioDatumMode.valueOf(obtainStyledAttributes.getInt(0, 0));
        this.mDatumWidth = obtainStyledAttributes.getFloat(4, this.mDatumWidth);
        this.mDatumHeight = obtainStyledAttributes.getFloat(1, this.mDatumHeight);
        this.mIsSquare = obtainStyledAttributes.getBoolean(3, false);
        this.mAspectRatio = obtainStyledAttributes.getFloat(2, this.mAspectRatio);
        obtainStyledAttributes.recycle();
    }

    private RatioDatumMode shouldRatioDatumMode(ViewGroup.LayoutParams layoutParams) {
        RatioDatumMode ratioDatumMode = this.mRatioDatumMode;
        if (ratioDatumMode != null && ratioDatumMode != RatioDatumMode.DATUM_AUTO) {
            return this.mRatioDatumMode;
        }
        if (layoutParams.width > 0 || shouldLinearParamsWidth(layoutParams) || layoutParams.width == -1) {
            return RatioDatumMode.DATUM_WIDTH;
        }
        if (layoutParams.height > 0 || shouldLinearParamsHeight(layoutParams) || layoutParams.height == -1) {
            return RatioDatumMode.DATUM_HEIGHT;
        }
        return null;
    }

    private boolean shouldLinearParamsWidth(ViewGroup.LayoutParams layoutParams) {
        if (!(layoutParams instanceof LinearLayout.LayoutParams)) {
            return false;
        }
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
        if (layoutParams2.width != 0 || layoutParams2.weight <= 0.0f) {
            return false;
        }
        return true;
    }

    private boolean shouldLinearParamsHeight(ViewGroup.LayoutParams layoutParams) {
        if (!(layoutParams instanceof LinearLayout.LayoutParams)) {
            return false;
        }
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
        if (layoutParams2.height != 0 || layoutParams2.weight <= 0.0f) {
            return false;
        }
        return true;
    }

    public final void update(int i, int i2) {
        this.mWidthMeasureSpec = i;
        this.mHeightMeasureSpec = i2;
        RatioDatumMode shouldRatioDatumMode = shouldRatioDatumMode(this.mRatioTarget.getLayoutParams());
        int paddingLeft = this.mRatioTarget.getPaddingLeft() + this.mRatioTarget.getPaddingRight();
        int paddingTop = this.mRatioTarget.getPaddingTop() + this.mRatioTarget.getPaddingBottom();
        if (shouldRatioDatumMode == RatioDatumMode.DATUM_WIDTH) {
            int size = View.MeasureSpec.getSize(i);
            if (this.mIsSquare) {
                this.mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(resolveSize((size - paddingLeft) + paddingTop, i2), View.MeasureSpec.EXACTLY);
                return;
            }
            float f = this.mAspectRatio;
            if (f > 0.0f) {
                this.mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(resolveSize(Math.round((((float) (size - paddingLeft)) / f) + ((float) paddingTop)), i2), View.MeasureSpec.EXACTLY);
                return;
            }
            float f2 = this.mDatumWidth;
            if (f2 > 0.0f) {
                float f3 = this.mDatumHeight;
                if (f3 > 0.0f) {
                    this.mHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(resolveSize(Math.round(((((float) (size - paddingLeft)) / f2) * f3) + ((float) paddingTop)), i2), View.MeasureSpec.EXACTLY);
                }
            }
        } else if (shouldRatioDatumMode == RatioDatumMode.DATUM_HEIGHT) {
            int size2 = View.MeasureSpec.getSize(i2);
            if (this.mIsSquare) {
                this.mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(resolveSize((size2 - paddingTop) + paddingLeft, i), View.MeasureSpec.EXACTLY);
                return;
            }
            float f4 = this.mAspectRatio;
            if (f4 > 0.0f) {
                this.mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(resolveSize(Math.round((((float) (size2 - paddingTop)) / f4) + ((float) paddingLeft)), i), View.MeasureSpec.EXACTLY);
                return;
            }
            float f5 = this.mDatumWidth;
            if (f5 > 0.0f) {
                float f6 = this.mDatumHeight;
                if (f6 > 0.0f) {
                    this.mWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(resolveSize(Math.round(((((float) (size2 - paddingTop)) / f6) * f5) + ((float) paddingLeft)), i), View.MeasureSpec.EXACTLY);
                }
            }
        }
    }

    public final int getWidthMeasureSpec() {
        return this.mWidthMeasureSpec;
    }

    public final int getHeightMeasureSpec() {
        return this.mHeightMeasureSpec;
    }

    private void requestLayout() {
        this.mRatioTarget.requestLayout();
    }

    public final void setRatio(RatioDatumMode ratioDatumMode, float f, float f2) {
        this.mRatioDatumMode = ratioDatumMode;
        this.mDatumWidth = f;
        this.mDatumHeight = f2;
        requestLayout();
    }

    public final void setSquare(boolean z) {
        this.mIsSquare = z;
        requestLayout();
    }

    public final void setAspectRatio(float f) {
        this.mAspectRatio = f;
        requestLayout();
    }
}
