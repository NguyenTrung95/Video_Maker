package com.devchie.videomaker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.devchie.videomaker.R;

public class RoundViewDelegate {
    private int backgroundColor;
    private int backgroundPressColor;
    private Context context;
    private int cornerRadius;
    private int cornerRadius_BL;
    private int cornerRadius_BR;
    private int cornerRadius_TL;
    private int cornerRadius_TR;
    private GradientDrawable gd_background = new GradientDrawable();
    private GradientDrawable gd_background_press = new GradientDrawable();
    private boolean isRadiusHalfHeight;
    private boolean isRippleEnable;
    private boolean isWidthHeightEqual;
    private float[] radiusArr = new float[8];
    private int strokeColor;
    private int strokePressColor;
    private int strokeWidth;
    private int textPressColor;
    private View view;

    public RoundViewDelegate(View view2, Context context2, AttributeSet attributeSet) {
        this.view = view2;
        this.context = context2;
        obtainAttributes(context2, attributeSet);
    }

    @SuppressLint("ResourceType")
    private void obtainAttributes(Context context2, AttributeSet attributeSet) {
        int[] RoundTextView = {R.attr.rt_backgroundColor, R.attr.rt_backgroundPressColor, R.attr.rt_cornerRadius, R.attr.rt_cornerRadius_BL, R.attr.rt_cornerRadius_BR, R.attr.rt_cornerRadius_TL, R.attr.rt_cornerRadius_TR, R.attr.rt_isRadiusHalfHeight, R.attr.rt_isRippleEnable, R.attr.rt_isWidthHeightEqual, R.attr.rt_strokeColor, R.attr.rt_strokePressColor, R.attr.rt_strokeWidth, R.attr.rt_textPressColor};
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, RoundTextView);
        this.backgroundColor = obtainStyledAttributes.getColor(0, 0);
        this.backgroundPressColor = obtainStyledAttributes.getColor(1, Integer.MAX_VALUE);
        this.cornerRadius = obtainStyledAttributes.getDimensionPixelSize(2, 0);
        this.strokeWidth = obtainStyledAttributes.getDimensionPixelSize(12, 0);
        this.strokeColor = obtainStyledAttributes.getColor(10, 0);
        this.strokePressColor = obtainStyledAttributes.getColor(11, Integer.MAX_VALUE);
        this.textPressColor = obtainStyledAttributes.getColor(13, Integer.MAX_VALUE);
        this.isRadiusHalfHeight = obtainStyledAttributes.getBoolean(7, false);
        this.isWidthHeightEqual = obtainStyledAttributes.getBoolean(9, false);
        this.cornerRadius_TL = obtainStyledAttributes.getDimensionPixelSize(5, 0);
        this.cornerRadius_TR = obtainStyledAttributes.getDimensionPixelSize(6, 0);
        this.cornerRadius_BL = obtainStyledAttributes.getDimensionPixelSize(3, 0);
        this.cornerRadius_BR = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        this.isRippleEnable = obtainStyledAttributes.getBoolean(8, true);
        obtainStyledAttributes.recycle();
    }

    public void setBackgroundColor(int i) {
        this.backgroundColor = i;
        setBgSelector();
    }

    public void setBackgroundPressColor(int i) {
        this.backgroundPressColor = i;
        setBgSelector();
    }

    public void setCornerRadius(int i) {
        this.cornerRadius = dp2px((float) i);
        setBgSelector();
    }

    public void setStrokeWidth(int i) {
        this.strokeWidth = dp2px((float) i);
        setBgSelector();
    }

    public void setStrokeColor(int i) {
        this.strokeColor = i;
        setBgSelector();
    }

    public void setStrokePressColor(int i) {
        this.strokePressColor = i;
        setBgSelector();
    }

    public void setTextPressColor(int i) {
        this.textPressColor = i;
        setBgSelector();
    }

    public void setIsRadiusHalfHeight(boolean z) {
        this.isRadiusHalfHeight = z;
        setBgSelector();
    }

    public void setIsWidthHeightEqual(boolean z) {
        this.isWidthHeightEqual = z;
        setBgSelector();
    }

    public void setCornerRadius_TL(int i) {
        this.cornerRadius_TL = i;
        setBgSelector();
    }

    public void setCornerRadius_TR(int i) {
        this.cornerRadius_TR = i;
        setBgSelector();
    }

    public void setCornerRadius_BL(int i) {
        this.cornerRadius_BL = i;
        setBgSelector();
    }

    public void setCornerRadius_BR(int i) {
        this.cornerRadius_BR = i;
        setBgSelector();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public int getBackgroundPressColor() {
        return this.backgroundPressColor;
    }

    public int getCornerRadius() {
        return this.cornerRadius;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    public int getStrokePressColor() {
        return this.strokePressColor;
    }

    public int getTextPressColor() {
        return this.textPressColor;
    }

    public boolean isRadiusHalfHeight() {
        return this.isRadiusHalfHeight;
    }

    public boolean isWidthHeightEqual() {
        return this.isWidthHeightEqual;
    }

    public int getCornerRadius_TL() {
        return this.cornerRadius_TL;
    }

    public int getCornerRadius_TR() {
        return this.cornerRadius_TR;
    }

    public int getCornerRadius_BL() {
        return this.cornerRadius_BL;
    }

    public int getCornerRadius_BR() {
        return this.cornerRadius_BR;
    }


    public int dp2px(float f) {
        return (int) ((f * this.context.getResources().getDisplayMetrics().density) + 0.5f);
    }


    public int sp2px(float f) {
        return (int) ((f * this.context.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    private void setDrawable(GradientDrawable gradientDrawable, int i, int i2) {
        gradientDrawable.setColor(i);
        if (this.cornerRadius_TL > 0 || this.cornerRadius_TR > 0 || this.cornerRadius_BR > 0 || this.cornerRadius_BL > 0) {
            float[] fArr = this.radiusArr;
            int i3 = this.cornerRadius_TL;
            fArr[0] = (float) i3;
            fArr[1] = (float) i3;
            int i4 = this.cornerRadius_TR;
            fArr[2] = (float) i4;
            fArr[3] = (float) i4;
            int i5 = this.cornerRadius_BR;
            fArr[4] = (float) i5;
            fArr[5] = (float) i5;
            int i6 = this.cornerRadius_BL;
            fArr[6] = (float) i6;
            fArr[7] = (float) i6;
            gradientDrawable.setCornerRadii(fArr);
        } else {
            gradientDrawable.setCornerRadius((float) this.cornerRadius);
        }
        gradientDrawable.setStroke(this.strokeWidth, i2);
    }

    public void setBgSelector() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (Build.VERSION.SDK_INT < 21 || !this.isRippleEnable) {
            setDrawable(this.gd_background, this.backgroundColor, this.strokeColor);
            stateListDrawable.addState(new int[]{-16842919}, this.gd_background);
            if (!(this.backgroundPressColor == Integer.MAX_VALUE && this.strokePressColor == Integer.MAX_VALUE)) {
                GradientDrawable gradientDrawable = this.gd_background_press;
                int i = this.backgroundPressColor;
                if (i == Integer.MAX_VALUE) {
                    i = this.backgroundColor;
                }
                int i2 = this.strokePressColor;
                if (i2 == Integer.MAX_VALUE) {
                    i2 = this.strokeColor;
                }
                setDrawable(gradientDrawable, i, i2);
                stateListDrawable.addState(new int[]{16842919}, this.gd_background_press);
            }
            if (Build.VERSION.SDK_INT >= 16) {
                this.view.setBackground(stateListDrawable);
            } else {
                this.view.setBackgroundDrawable(stateListDrawable);
            }
        } else {
            setDrawable(this.gd_background, this.backgroundColor, this.strokeColor);
            this.view.setBackground(new RippleDrawable(getPressedColorSelector(this.backgroundColor, this.backgroundPressColor), this.gd_background, (Drawable) null));
        }
        View view2 = this.view;
        if ((view2 instanceof TextView) && this.textPressColor != Integer.MAX_VALUE) {
            ((TextView) this.view).setTextColor(new ColorStateList(new int[][]{new int[]{-16842919}, new int[]{16842919}}, new int[]{((TextView) view2).getTextColors().getDefaultColor(), this.textPressColor}));
        }
    }

    private ColorStateList getPressedColorSelector(int i, int i2) {
        return new ColorStateList(new int[][]{new int[]{16842919}, new int[]{16842908}, new int[]{16843518}, new int[0]}, new int[]{i2, i2, i2, i});
    }
}
