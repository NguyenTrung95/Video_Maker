package com.hw.photomovie.opengl;


public class MGLES20Canvas extends GLES20Canvas{

    private int mSavedHashcode;
    private ShaderParameter[] parameters;
    @Override
    protected ShaderParameter[] prepareTexture(BasicTexture texture) {
        if(texture.hashCode() == mSavedHashcode){
            return parameters;
        }
        parameters = super.prepareTexture(texture);
        mSavedHashcode = texture.hashCode();
        return parameters;
    }
}
