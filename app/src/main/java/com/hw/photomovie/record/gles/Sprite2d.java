/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hw.photomovie.record.gles;

import android.opengl.Matrix;


public class Sprite2d {
    private static final String TAG = GlUtil.TAG;

    private Drawable2d mDrawable;
    private float mColor[];
    private int mTextureId;
    private float mAngle;
    private float mScaleX, mScaleY;
    private float mPosX, mPosY;

    private float[] mModelViewMatrix;
    private boolean mMatrixReady;

    private float[] mScratchMatrix = new float[16];

    public Sprite2d(Drawable2d drawable) {
        mDrawable = drawable;
        mColor = new float[4];
        mColor[3] = 1.0f;
        mTextureId = -1;

        mModelViewMatrix = new float[16];
        mMatrixReady = false;
    }


    private void recomputeMatrix() {
        float[] modelView = mModelViewMatrix;

        Matrix.setIdentityM(modelView, 0);
        Matrix.translateM(modelView, 0, mPosX, mPosY, 0.0f);
        if (mAngle != 0.0f) {
            Matrix.rotateM(modelView, 0, mAngle, 0.0f, 0.0f, 1.0f);
        }
        Matrix.scaleM(modelView, 0, mScaleX, mScaleY, 1.0f);
        mMatrixReady = true;
    }


    public float getScaleX() {
        return mScaleX;
    }

    public float getScaleY() {
        return mScaleY;
    }


    public void setScale(float scaleX, float scaleY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
        mMatrixReady = false;
    }


    public float getRotation() {
        return mAngle;
    }

    public void setRotation(float angle) {
        // Normalize.  We're not expecting it to be way off, so just iterate.
        while (angle >= 360.0f) {
            angle -= 360.0f;
        }
        while (angle <= -360.0f) {
            angle += 360.0f;
        }
        mAngle = angle;
        mMatrixReady = false;
    }


    public float getPositionX() {
        return mPosX;
    }


    public float getPositionY() {
        return mPosY;
    }


    public void setPosition(float posX, float posY) {
        mPosX = posX;
        mPosY = posY;
        mMatrixReady = false;
    }


    public float[] getModelViewMatrix() {
        if (!mMatrixReady) {
            recomputeMatrix();
        }
        return mModelViewMatrix;
    }


    public void setColor(float red, float green, float blue) {
        mColor[0] = red;
        mColor[1] = green;
        mColor[2] = blue;
    }


    public void setTexture(int textureId) {
        mTextureId = textureId;
    }


    public float[] getColor() {
        return mColor;
    }


    public void draw(FlatShadedProgram program, float[] projectionMatrix) {
        // Compute model/view/projection matrix.
        Matrix.multiplyMM(mScratchMatrix, 0, projectionMatrix, 0, getModelViewMatrix(), 0);

        program.draw(mScratchMatrix, mColor, mDrawable.getVertexArray(), 0,
                mDrawable.getVertexCount(), mDrawable.getCoordsPerVertex(),
                mDrawable.getVertexStride());
    }


    public void draw(Texture2dProgram program, float[] projectionMatrix) {
        // Compute model/view/projection matrix.
        Matrix.multiplyMM(mScratchMatrix, 0, projectionMatrix, 0, getModelViewMatrix(), 0);

        program.draw(mScratchMatrix, mDrawable.getVertexArray(), 0,
                mDrawable.getVertexCount(), mDrawable.getCoordsPerVertex(),
                mDrawable.getVertexStride(), GlUtil.IDENTITY_MATRIX, mDrawable.getTexCoordArray(),
                mTextureId, mDrawable.getTexCoordStride());
    }

    @Override
    public String toString() {
        return "[Sprite2d pos=" + mPosX + "," + mPosY +
                " scale=" + mScaleX + "," + mScaleY + " angle=" + mAngle +
                " color={" + mColor[0] + "," + mColor[1] + "," + mColor[2] +
                "} drawable=" + mDrawable + "]";
    }
}
