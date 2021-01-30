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

import android.opengl.GLES20;

import java.nio.ByteBuffer;

public class GeneratedTexture {
    //private static final String TAG = GlUtil.TAG;

    public enum Image { COARSE, FINE };

    // Basic colors, in little-endian RGBA.
    private static final int BLACK = 0x00000000;
    private static final int RED = 0x000000ff;
    private static final int GREEN = 0x0000ff00;
    private static final int BLUE = 0x00ff0000;
    private static final int MAGENTA = RED | BLUE;
    private static final int YELLOW = RED | GREEN;
    private static final int CYAN = GREEN | BLUE;
    private static final int WHITE = RED | GREEN | BLUE;
    private static final int OPAQUE = (int) 0xff000000L;
    private static final int HALF = (int) 0x80000000L;
    private static final int LOW = (int) 0x40000000L;
    private static final int TRANSP = 0;

    private static final int GRID[] = new int[] {    // must be 16 elements
        OPAQUE|RED,     OPAQUE|YELLOW,  OPAQUE|GREEN,   OPAQUE|MAGENTA,
        OPAQUE|WHITE,   LOW|RED,        LOW|GREEN,      OPAQUE|YELLOW,
        OPAQUE|MAGENTA, TRANSP|GREEN,   HALF|RED,       OPAQUE|BLACK,
        OPAQUE|CYAN,    OPAQUE|MAGENTA, OPAQUE|CYAN,    OPAQUE|BLUE,
    };

    private static final int TEX_SIZE = 64;         // must be power of 2
    private static final int FORMAT = GLES20.GL_RGBA;
    private static final int BYTES_PER_PIXEL = 4;   // RGBA

    // Generate test image data.  This must come after the other values are initialized.
    private static final ByteBuffer sCoarseImageData = generateCoarseData();
    private static final ByteBuffer sFineImageData = generateFineData();



    public static int createTestTexture(Image which) {
        ByteBuffer buf;
        switch (which) {
            case COARSE:
                buf = sCoarseImageData;
                break;
            case FINE:
                buf = sFineImageData;
                break;
            default:
                throw new RuntimeException("unknown image");
        }
        return GlUtil.createImageTexture(buf, TEX_SIZE, TEX_SIZE, FORMAT);
    }


    private static ByteBuffer generateCoarseData() {
        byte[] buf = new byte[TEX_SIZE * TEX_SIZE * BYTES_PER_PIXEL];
        final int scale = TEX_SIZE / 4;        // convert 64x64 --> 4x4

        for (int i = 0; i < buf.length; i += BYTES_PER_PIXEL) {
            int texRow = (i / BYTES_PER_PIXEL) / TEX_SIZE;
            int texCol = (i / BYTES_PER_PIXEL) % TEX_SIZE;

            int gridRow = texRow / scale;  // 0-3
            int gridCol = texCol / scale;  // 0-3
            int gridIndex = (gridRow * 4) + gridCol;  // 0-15

            int color = GRID[gridIndex];

            // override the pixels in two corners to check coverage
            if (i == 0) {
                color = OPAQUE | WHITE;
            } else if (i == buf.length - BYTES_PER_PIXEL) {
                color = OPAQUE | WHITE;
            }

            // extract RGBA; use "int" instead of "byte" to get unsigned values
            int red = color & 0xff;
            int green = (color >> 8) & 0xff;
            int blue = (color >> 16) & 0xff;
            int alpha = (color >> 24) & 0xff;

            // pre-multiply colors and store in buffer
            float alphaM = alpha / 255.0f;
            buf[i] = (byte) (red * alphaM);
            buf[i+1] = (byte) (green * alphaM);
            buf[i+2] = (byte) (blue * alphaM);
            buf[i+3] = (byte) alpha;
        }

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(buf.length);
        byteBuf.put(buf);
        byteBuf.position(0);
        return byteBuf;
    }


    private static ByteBuffer generateFineData() {
        byte[] buf = new byte[TEX_SIZE * TEX_SIZE * BYTES_PER_PIXEL];

        // top/left: single-pixel red/blue
        checkerPattern(buf, 0, 0, TEX_SIZE / 2, TEX_SIZE / 2,
                OPAQUE|RED, OPAQUE|BLUE, 0x01);
        // bottom/right: two-pixel red/green
        checkerPattern(buf, TEX_SIZE / 2, TEX_SIZE / 2, TEX_SIZE, TEX_SIZE,
                OPAQUE|RED, OPAQUE|GREEN, 0x02);
        // bottom/left: four-pixel blue/green
        checkerPattern(buf, 0, TEX_SIZE / 2, TEX_SIZE / 2, TEX_SIZE,
                OPAQUE|BLUE, OPAQUE|GREEN, 0x04);
        // top/right: eight-pixel black/white
        checkerPattern(buf, TEX_SIZE / 2, 0, TEX_SIZE, TEX_SIZE / 2,
                OPAQUE|WHITE, OPAQUE|BLACK, 0x08);

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(buf.length);
        byteBuf.put(buf);
        byteBuf.position(0);
        return byteBuf;
    }

    private static void checkerPattern(byte[] buf, int left, int top, int right, int bottom,
            int color1, int color2, int bit) {
        for (int row = top; row < bottom; row++) {
            int rowOffset = row * TEX_SIZE * BYTES_PER_PIXEL;
            for (int col = left; col < right; col++) {
                int offset = rowOffset + col * BYTES_PER_PIXEL;
                int color;
                if (((row & bit) ^ (col & bit)) == 0) {
                    color = color1;
                } else {
                    color = color2;
                }

                // extract RGBA; use "int" instead of "byte" to get unsigned values
                int red = color & 0xff;
                int green = (color >> 8) & 0xff;
                int blue = (color >> 16) & 0xff;
                int alpha = (color >> 24) & 0xff;

                // pre-multiply colors and store in buffer
                float alphaM = alpha / 255.0f;
                buf[offset] = (byte) (red * alphaM);
                buf[offset+1] = (byte) (green * alphaM);
                buf[offset+2] = (byte) (blue * alphaM);
                buf[offset+3] = (byte) alpha;
            }
        }
    }
}
