package com.hw.photomovie.opengl;

import android.opengl.GLES20;
import com.hw.photomovie.util.MLog;

import java.util.WeakHashMap;



public abstract class BasicTexture implements Texture {

    private static final String TAG = "BasicTexture";
    protected static final int UNSPECIFIED = -1;

    protected static final int STATE_UNLOADED = 0;
    protected static final int STATE_LOADED = 1;
    protected static final int STATE_ERROR = -1;

    private static final int MAX_TEXTURE_SIZE = 4096;

    protected int mId = -1;
    protected int mState;

    protected int mWidth = UNSPECIFIED;
    protected int mHeight = UNSPECIFIED;

    protected int mTextureWidth;
    protected int mTextureHeight;

    private boolean mHasBorder;

    protected GLESCanvas mCanvasRef = null;
    private static WeakHashMap<BasicTexture, Object> sAllTextures = new WeakHashMap<BasicTexture, Object>();
    private static ThreadLocal sInFinalizer = new ThreadLocal();

    protected BasicTexture(GLESCanvas canvas, int id, int state) {
        setAssociatedCanvas(canvas);
        mId = id;
        mState = state;
        synchronized (sAllTextures) {
            sAllTextures.put(this, null);
        }
    }

    protected BasicTexture() {
        this(null, 0, STATE_UNLOADED);
    }

    protected void setAssociatedCanvas(GLESCanvas canvas) {
        mCanvasRef = canvas;
    }


    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        mTextureWidth = width;
        mTextureHeight = height;
        if (mTextureWidth > MAX_TEXTURE_SIZE || mTextureHeight > MAX_TEXTURE_SIZE) {
            MLog.w(TAG, String.format("texture is too large: %d x %d",
                    mTextureWidth, mTextureHeight), new Exception());
        }
    }

    public boolean isFlippedVertically() {
        return false;
    }

    public int getId() {
        return mId;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    public int getTextureWidth() {
        return mTextureWidth;
    }

    public int getTextureHeight() {
        return mTextureHeight;
    }


    public boolean hasBorder() {
        return mHasBorder;
    }

    protected void setBorder(boolean hasBorder) {
        mHasBorder = hasBorder;
    }

    @Override
    public void draw(GLESCanvas canvas, int x, int y) {
        canvas.drawTexture(this, x, y, getWidth(), getHeight());
    }

    @Override
    public void draw(GLESCanvas canvas, int x, int y, int w, int h) {
        canvas.drawTexture(this, x, y, w, h);
    }


    protected abstract boolean onBind(GLESCanvas canvas);

    protected abstract int getTarget();

    public boolean isLoaded() {
        return mState == STATE_LOADED && GLES20.glIsTexture(mId);
    }

    public void recycle() {
        freeResource();
    }


    public void yield() {
        freeResource();
    }

    private void freeResource() {
        GLESCanvas canvas = mCanvasRef;
        if (canvas != null && mId != -1) {
            canvas.unloadTexture(this);
            mId = -1; // Don't free it again.
        }
        mState = STATE_UNLOADED;
        setAssociatedCanvas(null);
    }

    @Override
    protected void finalize() {
        sInFinalizer.set(BasicTexture.class);
        recycle();
        sInFinalizer.set(null);
    }


    public static boolean inFinalizer() {
        return sInFinalizer.get() != null;
    }

    public static void yieldAllTextures() {
        synchronized (sAllTextures) {
            for (BasicTexture t : sAllTextures.keySet()) {
                t.yield();
            }
        }
    }

    public static void invalidateAllTextures() {
        synchronized (sAllTextures) {
            for (BasicTexture t : sAllTextures.keySet()) {
                t.mState = STATE_UNLOADED;
                t.setAssociatedCanvas(null);
            }
        }
    }
}
