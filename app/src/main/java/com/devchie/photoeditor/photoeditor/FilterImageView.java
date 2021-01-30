package com.devchie.photoeditor.photoeditor;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

class FilterImageView extends AppCompatImageView {
    private OnImageChangedListener mOnImageChangedListener;

    interface OnImageChangedListener {
        void onBitmapLoaded(Bitmap bitmap);
    }

    public FilterImageView(Context context) {
        super(context);
    }

    public FilterImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FilterImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setOnImageChangedListener(OnImageChangedListener onImageChangedListener) {
        this.mOnImageChangedListener = onImageChangedListener;
    }

    public void setImageBitmap(Bitmap bitmap) {
        FilterImageView.super.setImageBitmap(bitmap);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageIcon(Icon icon) {
        FilterImageView.super.setImageIcon(icon);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageMatrix(Matrix matrix) {
        FilterImageView.super.setImageMatrix(matrix);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageState(int[] iArr, boolean z) {
        FilterImageView.super.setImageState(iArr, z);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageTintList(ColorStateList colorStateList) {
        FilterImageView.super.setImageTintList(colorStateList);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageTintMode(PorterDuff.Mode mode) {
        FilterImageView.super.setImageTintMode(mode);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageDrawable(Drawable drawable) {
        FilterImageView.super.setImageDrawable(drawable);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageResource(int i) {
        FilterImageView.super.setImageResource(i);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageURI(Uri uri) {
        FilterImageView.super.setImageURI(uri);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }

    public void setImageLevel(int i) {
        FilterImageView.super.setImageLevel(i);
        OnImageChangedListener onImageChangedListener = this.mOnImageChangedListener;
        if (onImageChangedListener != null) {
            onImageChangedListener.onBitmapLoaded(getBitmap());
        }
    }


    public Bitmap getBitmap() {
        try {
            if (getDrawable() != null) {
                return ((BitmapDrawable) getDrawable()).getBitmap();
            }
            return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
