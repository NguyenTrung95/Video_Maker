package com.devchie.photoeditor.photoeditor;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class TextureRenderer {
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final String FRAGMENT_SHADER = "precision mediump float;\nuniform sampler2D tex_sampler;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_FragColor = texture2D(tex_sampler, v_texcoord);\n}\n";
    private static final float[] POS_VERTICES = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    private static final float[] TEX_VERTICES = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private static final String VERTEX_SHADER = "attribute vec4 a_position;\nattribute vec2 a_texcoord;\nvarying vec2 v_texcoord;\nvoid main() {\n  gl_Position = a_position;\n  v_texcoord = a_texcoord;\n}\n";
    private int mPosCoordHandle;
    private FloatBuffer mPosVertices;
    private int mProgram;
    private int mTexCoordHandle;
    private int mTexHeight;
    private int mTexSamplerHandle;
    private FloatBuffer mTexVertices;
    private int mTexWidth;
    private int mViewHeight;
    private int mViewWidth;

    TextureRenderer() {
    }

    public void init() {
        int createProgram = GLToolbox.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        this.mProgram = createProgram;
        this.mTexSamplerHandle = GLES20.glGetUniformLocation(createProgram, "tex_sampler");
        this.mTexCoordHandle = GLES20.glGetAttribLocation(this.mProgram, "a_texcoord");
        this.mPosCoordHandle = GLES20.glGetAttribLocation(this.mProgram, "a_position");
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(TEX_VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTexVertices = asFloatBuffer;
        asFloatBuffer.put(TEX_VERTICES).position(0);
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(POS_VERTICES.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mPosVertices = asFloatBuffer2;
        asFloatBuffer2.put(POS_VERTICES).position(0);
    }

    public void tearDown() {
        GLES20.glDeleteProgram(this.mProgram);
    }

    public void updateTextureSize(int i, int i2) {
        this.mTexWidth = i;
        this.mTexHeight = i2;
        computeOutputVertices();
    }

    public void updateViewSize(int i, int i2) {
        this.mViewWidth = i;
        this.mViewHeight = i2;
        computeOutputVertices();
    }

    public void renderTexture(int i) {
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glUseProgram(this.mProgram);
        GLToolbox.checkGlError("glUseProgram");
        GLES20.glViewport(0, 0, this.mViewWidth, this.mViewHeight);
        GLToolbox.checkGlError("glViewport");
        GLES20.glDisable(3042);
        GLES20.glVertexAttribPointer(this.mTexCoordHandle, 2, 5126, false, 0, this.mTexVertices);
        GLES20.glEnableVertexAttribArray(this.mTexCoordHandle);
        GLES20.glVertexAttribPointer(this.mPosCoordHandle, 2, 5126, false, 0, this.mPosVertices);
        GLES20.glEnableVertexAttribArray(this.mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");
        GLES20.glActiveTexture(33984);
        GLToolbox.checkGlError("glActiveTexture");
        GLES20.glBindTexture(3553, i);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(this.mTexSamplerHandle, 0);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16384);
        GLES20.glDrawArrays(5, 0, 4);
    }

    private void computeOutputVertices() {
        float f;
        if (this.mPosVertices != null) {
            float f2 = (((float) this.mViewWidth) / ((float) this.mViewHeight)) / (((float) this.mTexWidth) / ((float) this.mTexHeight));
            float f3 = -1.0f;
            float f4 = 1.0f;
            if (f2 > 1.0f) {
                f4 = 1.0f / f2;
                f3 = -1.0f / f2;
                f2 = 1.0f;
                f = -1.0f;
            } else {
                f = -f2;
            }
            this.mPosVertices.put(new float[]{f3, f, f4, f, f3, f2, f4, f2}).position(0);
        }
    }
}
