package com.hw.photomovie.filter;


public class GrayFilter extends MovieFilter {

    protected static final String FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     mediump vec4 color = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     mediump float gray = color.r*0.3+color.g*0.59+color.b*0.11;\n"+
            "     gl_FragColor = vec4(gray,gray,gray,1.0);\n"+
            "}";

    public GrayFilter() {
        super(VERTEX_SHADER,FRAGMENT_SHADER);
    }
}
