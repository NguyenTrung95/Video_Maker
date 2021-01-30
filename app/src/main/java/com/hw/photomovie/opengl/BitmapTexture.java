package com.hw.photomovie.opengl;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import static com.hw.photomovie.util.GLUtil.NO_TEXTURE;


public class BitmapTexture extends UploadedTexture {
    protected Bitmap mContentBitmap;
    protected boolean mIsRecycled;

    protected boolean mRecycleDirectly = true;

    public BitmapTexture(Bitmap bitmap) {
        this(bitmap, false);
    }

    public BitmapTexture(Bitmap bitmap, boolean hasBorder) {
        super(hasBorder);

        mIsRecycled = true;
        mContentBitmap = bitmap;
        mIsRecycled = false;
    }

    @Override
    public void recycle() {
        mIsRecycled = true;
        if(mRecycleDirectly) {
            if (mId != NO_TEXTURE && GLES20.glIsTexture(mId)) {
                GLES20.glDeleteTextures(1, new int[]{mId}, 0);
                mId = NO_TEXTURE;
            }
        }
        super.recycle();
    }

    public void setRecycleDirectly(boolean recycleDirectly) {
        this.mRecycleDirectly = recycleDirectly;
    }

    @Override
    public void updateContent(GLESCanvas canvas) {
        // 当bitmap已经被释放，onGLIdle时还在上传队列中的该对象，崩溃
        if (mIsRecycled) {
            return;
        }
        super.updateContent(canvas);
    }

    @Override
    protected void onFreeBitmap(Bitmap bitmap) {
        // Do nothing.
    }

    @Override
    protected Bitmap onGetBitmap() {
        return mContentBitmap;
    }

    public Bitmap getBitmap() {
        return mContentBitmap;
    }
}
