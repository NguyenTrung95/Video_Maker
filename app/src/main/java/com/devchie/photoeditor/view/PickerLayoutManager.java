package com.devchie.photoeditor.view;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PickerLayoutManager extends LinearLayoutManager {
    private boolean changeAlpha = true;
    private onScrollStopListener onScrollStopListener;
    private RecyclerView recyclerView;
    private float scaleDownBy = 0.4f;
    private float scaleDownDistance = 0.5f;

    public interface onScrollStopListener {
        void selectedView(int i);
    }

    public PickerLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        PickerLayoutManager.super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getOrientation() != RecyclerView.HORIZONTAL) {
            return 0;
        }
        int scrollHorizontallyBy = PickerLayoutManager.super.scrollHorizontallyBy(i, recycler, state);
        scaleDownView();
        return scrollHorizontallyBy;
    }

    private void scaleDownView() {
        float width = ((float) getWidth()) / 2.0f;
        float f = this.scaleDownDistance * width;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            float min = (((this.scaleDownBy * -1.0f) * Math.min(f, Math.abs(width - (((float) (getDecoratedLeft(childAt) + getDecoratedRight(childAt))) / 2.0f)))) / f) + 1.0f;
            childAt.setScaleX(min);
            childAt.setScaleY(min);
            if (this.changeAlpha) {
                childAt.setAlpha(min);
            }
        }
    }

    public void onAttachedToWindow(RecyclerView recyclerView2) {
        PickerLayoutManager.super.onAttachedToWindow(recyclerView2);
        this.recyclerView = recyclerView2;
    }

    private int getRecyclerViewCenterX() {
        return ((this.recyclerView.getRight() - this.recyclerView.getLeft()) / 2) + this.recyclerView.getLeft();
    }

    public void onScrollStateChanged(int i) {
        int abs;
        PickerLayoutManager.super.onScrollStateChanged(i);
        if (i == 0) {
            int recyclerViewCenterX = getRecyclerViewCenterX();
            int width = this.recyclerView.getWidth();
            int i2 = -1;
            for (int i3 = 0; i3 <= this.recyclerView.getChildCount(); i3++) {
                View childAt = this.recyclerView.getChildAt(i3);
                if (childAt != null && (abs = Math.abs((getDecoratedLeft(childAt) + ((getDecoratedRight(childAt) - getDecoratedLeft(childAt)) / 2)) - recyclerViewCenterX)) < width) {
                    i2 = this.recyclerView.getChildLayoutPosition(childAt);
                    width = abs;
                }
            }
            this.onScrollStopListener.selectedView(i2);
        }
    }

    public float getScaleDownBy() {
        return this.scaleDownBy;
    }

    public void setScaleDownBy(float f) {
        this.scaleDownBy = f;
    }

    public float getScaleDownDistance() {
        return this.scaleDownDistance;
    }

    public void setScaleDownDistance(float f) {
        this.scaleDownDistance = f;
    }

    public boolean isChangeAlpha() {
        return this.changeAlpha;
    }

    public void setChangeAlpha(boolean z) {
        this.changeAlpha = z;
    }

    public void setOnScrollStopListener(onScrollStopListener onscrollstoplistener) {
        this.onScrollStopListener = onscrollstoplistener;
    }
}
