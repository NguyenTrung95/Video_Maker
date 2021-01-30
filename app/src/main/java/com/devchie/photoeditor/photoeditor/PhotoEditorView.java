package com.devchie.photoeditor.photoeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.devchie.videomaker.R;

public class PhotoEditorView extends RelativeLayout {
    private static final String TAG = "PhotoEditorView";
    private static final int brushSrcId = 2;
    private static final int glFilterId = 3;
    private static final int imgSrcId = 1;
    private BrushDrawingView mBrushDrawingView;

    public ImageFilterView mImageFilterView;

    public FilterImageView mImgSource;

    public PhotoEditorView(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public PhotoEditorView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(attributeSet);
    }

    @SuppressLint("ResourceType")
    private void init(AttributeSet attributeSet) {
        Drawable drawable;
        FilterImageView filterImageView = new FilterImageView(getContext());
        this.mImgSource = filterImageView;
        filterImageView.setId(1);
        this.mImgSource.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(13, -1);
        int[] PhotoEditorView = {R.attr.photo_src};
        if (!(attributeSet == null || (drawable = getContext().obtainStyledAttributes(attributeSet,PhotoEditorView).getDrawable(0)) == null)) {
            this.mImgSource.setImageDrawable(drawable);
        }
        BrushDrawingView brushDrawingView = new BrushDrawingView(getContext());
        this.mBrushDrawingView = brushDrawingView;
        brushDrawingView.setVisibility(GONE);
        this.mBrushDrawingView.setId(2);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(13, -1);
        layoutParams2.addRule(6, 1);
        layoutParams2.addRule(8, 1);
        ImageFilterView imageFilterView = new ImageFilterView(getContext());
        this.mImageFilterView = imageFilterView;
        imageFilterView.setId(3);
        this.mImageFilterView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams3.addRule(13, -1);
        layoutParams3.addRule(6, 1);
        layoutParams3.addRule(8, 1);
        this.mImgSource.setOnImageChangedListener(new FilterImageView.OnImageChangedListener() {
            public void onBitmapLoaded(Bitmap bitmap) {
                PhotoEditorView.this.mImageFilterView.setFilterEffect(PhotoFilter.NONE);
                PhotoEditorView.this.mImageFilterView.setSourceBitmap(bitmap);
                Log.d("PhotoEditorView", "onBitmapLoaded() called with: sourceBitmap = [" + bitmap + "]");
            }
        });
        addView(this.mImgSource, layoutParams);
        addView(this.mImageFilterView, layoutParams3);
        addView(this.mBrushDrawingView, layoutParams2);
    }

    public ImageView getSource() {
        return this.mImgSource;
    }


    public BrushDrawingView getBrushDrawingView() {
        return this.mBrushDrawingView;
    }


    public void saveFilter(final OnSaveBitmap onSaveBitmap) {
        if (this.mImageFilterView.getVisibility() == VISIBLE) {
            this.mImageFilterView.saveBitmap(new OnSaveBitmap() {
                public void onBitmapReady(Bitmap bitmap) {
                    Log.e(PhotoEditorView.TAG, "saveFilter: " + bitmap);
                    PhotoEditorView.this.mImgSource.setImageBitmap(bitmap);
                    PhotoEditorView.this.mImageFilterView.setVisibility(GONE);
                    onSaveBitmap.onBitmapReady(bitmap);
                }

                public void onFailure(Exception exc) {
                    onSaveBitmap.onFailure(exc);
                }
            });
        } else {
            onSaveBitmap.onBitmapReady(this.mImgSource.getBitmap());
        }
    }


    public void setFilterEffect(PhotoFilter photoFilter) {
        this.mImageFilterView.setVisibility(VISIBLE);
        this.mImageFilterView.setSourceBitmap(this.mImgSource.getBitmap());
        this.mImageFilterView.setFilterEffect(photoFilter);
    }


    public void setFilterEffect(CustomEffect customEffect) {
        this.mImageFilterView.setVisibility(VISIBLE);
        this.mImageFilterView.setSourceBitmap(this.mImgSource.getBitmap());
        this.mImageFilterView.setFilterEffect(customEffect);
    }
}
