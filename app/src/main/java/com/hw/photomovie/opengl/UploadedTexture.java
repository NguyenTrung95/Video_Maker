package com.hw.photomovie.opengl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL11;
import java.util.HashMap;


public abstract class UploadedTexture extends BasicTexture {


    private static HashMap<BorderKey, Bitmap> sBorderLines = new HashMap<BorderKey, Bitmap>();
    private static BorderKey sBorderKey = new BorderKey();

    private static final String TAG = "Texture";
    private boolean mContentValid = true;

    private boolean mIsUploading = false;
    private boolean mOpaque = true;
    private boolean mThrottled = false;
    private static int sUploadedCount;
    private static final int UPLOAD_LIMIT = 100;

    protected Bitmap mBitmap;
    private int mBorder;

    protected UploadedTexture() {
        this(false);
    }

    protected UploadedTexture(boolean hasBorder) {
        super(null, 0, STATE_UNLOADED);
        if (hasBorder) {
            setBorder(true);
            mBorder = 1;
        }
    }

    public void setIsUploading(boolean uploading) {
        mIsUploading = uploading;
    }

    public boolean isUploading() {
        return mIsUploading;
    }

    private static class BorderKey implements Cloneable {
        public boolean vertical;
        public Config config;
        public int length;

        @Override
        public int hashCode() {
            int x = config.hashCode() ^ length;
            return vertical ? x : -x;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof BorderKey)) {
                return false;
            }
            BorderKey o = (BorderKey) object;
            return vertical == o.vertical
                    && config == o.config && length == o.length;
        }

        @Override
        public BorderKey clone() {
            try {
                return (BorderKey) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError(e);
            }
        }
    }

    protected void setThrottled(boolean throttled) {
        mThrottled = throttled;
    }

    private static Bitmap getBorderLine(
            boolean vertical, Config config, int length) {
        BorderKey key = sBorderKey;
        key.vertical = vertical;
        key.config = config;
        key.length = length;
        Bitmap bitmap = sBorderLines.get(key);
        if (bitmap == null) {
            bitmap = vertical
                    ? Bitmap.createBitmap(1, length, config)
                    : Bitmap.createBitmap(length, 1, config);
            sBorderLines.put(key.clone(), bitmap);
        }
        return bitmap;
    }

    private Bitmap getBitmap() {
        if (mBitmap == null) {
            mBitmap = onGetBitmap();
            int w = mBitmap.getWidth() + mBorder * 2;
            int h = mBitmap.getHeight() + mBorder * 2;
            if (mWidth == UNSPECIFIED) {
                setSize(w, h);
            }
        }
        return mBitmap;
    }

    private void freeBitmap() {
        if(mBitmap!=null) {
            onFreeBitmap(mBitmap);
            mBitmap = null;
        }
    }

    @Override
    public int getWidth() {
        if (mWidth == UNSPECIFIED) {
            getBitmap();
        }
        return mWidth;
    }

    @Override
    public int getHeight() {
        if (mWidth == UNSPECIFIED) {
            getBitmap();
        }
        return mHeight;
    }

    protected abstract Bitmap onGetBitmap();

    protected abstract void onFreeBitmap(Bitmap bitmap);

    protected void invalidateContent() {
        if (mBitmap != null) {
            freeBitmap();
        }
        mContentValid = false;
        mWidth = UNSPECIFIED;
        mHeight = UNSPECIFIED;
    }


    public boolean isContentValid() {
        return isLoaded() && mContentValid;
    }


    public void updateContent(GLESCanvas canvas) {
        if (!isLoaded()) {
            if (mThrottled && ++sUploadedCount > UPLOAD_LIMIT || canvas == null) {
                return;
            }
            uploadToCanvas(canvas);
        } else if (!mContentValid) {
            Bitmap bitmap = getBitmap();
            int format = GLUtils.getInternalFormat(bitmap);
            int type = GLUtils.getType(bitmap);
            canvas.texSubImage2D(this, mBorder, mBorder, bitmap, format, type);
            freeBitmap();
            mContentValid = true;
        }
    }

    public static void resetUploadLimit() {
        sUploadedCount = 0;
    }

    public static boolean uploadLimitReached() {
        return sUploadedCount > UPLOAD_LIMIT;
    }

    private void uploadToCanvas(GLESCanvas canvas) {

        Bitmap bitmap = getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                int bWidth = bitmap.getWidth();
                int bHeight = bitmap.getHeight();
                int width = bWidth + mBorder * 2;
                int height = bHeight + mBorder * 2;
                int texWidth = getTextureWidth();
                int texHeight = getTextureHeight();


                mId = canvas.getGLId().generateTexture();
                canvas.setTextureParameters(this);

                if (bWidth == texWidth && bHeight == texHeight) {
                    canvas.initializeTexture(this, bitmap);
                } else {
                    int format = GLUtils.getInternalFormat(bitmap);
                    int type = GLUtils.getType(bitmap);
                    Config config = bitmap.getConfig();

                    canvas.initializeTextureSize(this, format, type);
                    canvas.texSubImage2D(this, mBorder, mBorder, bitmap, format, type);

                    if (mBorder > 0) {
                        // Left border
                        Bitmap line = getBorderLine(true, config, texHeight);
                        canvas.texSubImage2D(this, 0, 0, line, format, type);

                        // Top border
                        line = getBorderLine(false, config, texWidth);
                        canvas.texSubImage2D(this, 0, 0, line, format, type);
                    }

                    // Right border
                    if (mBorder + bWidth < texWidth) {
                        Bitmap line = getBorderLine(true, config, texHeight);
                        canvas.texSubImage2D(this, mBorder + bWidth, 0, line, format, type);
                    }

                    // Bottom border
                    if (mBorder + bHeight < texHeight) {
                        Bitmap line = getBorderLine(false, config, texWidth);
                        canvas.texSubImage2D(this, 0, mBorder + bHeight, line, format, type);
                    }
                }
            } finally {
                freeBitmap();
            }
            // Update texture state.
            setAssociatedCanvas(canvas);
            mState = STATE_LOADED;
            mContentValid = true;
        } else {
            mState = STATE_ERROR;
            throw new RuntimeException("Texture loadSegmentsFromFile fail, no bitmap:" + bitmap);
        }
    }

    @Override
    protected boolean onBind(GLESCanvas canvas) {
        updateContent(canvas);
        return isContentValid();
    }

    @Override
    protected int getTarget() {
        return GL11.GL_TEXTURE_2D;
    }

    public void setOpaque(boolean isOpaque) {
        mOpaque = isOpaque;
    }

    @Override
    public boolean isOpaque() {
        return mOpaque;
    }

    @Override
    public void recycle() {
        super.recycle();
        if (mBitmap != null) {
            freeBitmap();
        }
    }
}
