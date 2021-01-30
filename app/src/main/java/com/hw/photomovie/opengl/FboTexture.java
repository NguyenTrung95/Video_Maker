package com.hw.photomovie.opengl;

import android.opengl.GLES20;
import com.hw.photomovie.record.gles.GlUtil;

import javax.microedition.khronos.opengles.GL11;


public class FboTexture extends BasicTexture {

    private int mFrameBuffer;
    private boolean mIsFlipped;
    private int mRenderBuffer;

    public FboTexture() {
    }

    @Override
    protected boolean onBind(GLESCanvas canvas) {
        return true;
    }

    @Override
    protected int getTarget() {
        return GL11.GL_TEXTURE_2D;
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        prepareFrameBuffer(width, height);
    }

    public void setId(int id) {
        mId = id;
    }

    private void prepareFrameBuffer(int width, int height) {
        GlUtil.checkGlError("prepareFramebuffer start");

        int[] values = new int[1];

        GLES20.glGenTextures(1, values, 0);
        GlUtil.checkGlError("glGenTextures");
        int offscreenTexture = values[0];   // expected > 0
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, offscreenTexture);
        GlUtil.checkGlError("glBindTexture " + offscreenTexture);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
        GlUtil.checkGlError("glTexParameter");

        GLES20.glGenFramebuffers(1, values, 0);
        GlUtil.checkGlError("glGenFramebuffers");
        int framebuffer = values[0];    // expected > 0
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer);
        GlUtil.checkGlError("glBindFramebuffer " + framebuffer);

        GLES20.glGenRenderbuffers(1, values, 0);
        GlUtil.checkGlError("glGenRenderbuffers");
        mRenderBuffer = values[0];    // expected > 0
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mRenderBuffer);
        GlUtil.checkGlError("glBindRenderbuffer " + mRenderBuffer);

        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16,
                width, height);
        GlUtil.checkGlError("glRenderbufferStorage");

        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_RENDERBUFFER, mRenderBuffer);
        GlUtil.checkGlError("glFramebufferRenderbuffer");
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, offscreenTexture, 0);
        GlUtil.checkGlError("glFramebufferTexture2D");

        int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if (status != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer not complete, status=" + status);
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        GlUtil.checkGlError("prepareFramebuffer done");
        mFrameBuffer = framebuffer;
        mId = offscreenTexture;
    }

    public int getFrameBuffer() {
        return mFrameBuffer;
    }

    public void setIsFlippedVertically(boolean isFlipped) {
        mIsFlipped = isFlipped;
    }

    @Override
    public boolean isFlippedVertically() {
        return mIsFlipped;
    }

    public void release() {
        if (mId != 0 && GLES20.glIsTexture(mId)) {
            GLES20.glDeleteTextures(1, new int[]{mId}, 0);
        }
        if (mRenderBuffer != 0 && GLES20.glIsRenderbuffer(mRenderBuffer)) {
            GLES20.glDeleteRenderbuffers(1, new int[]{mRenderBuffer}, 0);
        }
        if (mFrameBuffer != 0 && GLES20.glIsFramebuffer(mFrameBuffer)) {
            GLES20.glDeleteFramebuffers(1, new int[]{mFrameBuffer}, 0);
            mFrameBuffer = 0;
        }
    }
}
