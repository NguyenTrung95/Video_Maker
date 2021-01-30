package com.devchie.videomaker.model;

import com.hw.photomovie.PhotoMovieFactory;

public class TransferItem {
    public int imgRes;
    public String name;
    public PhotoMovieFactory.PhotoMovieType type;

    public TransferItem(int i, String str, PhotoMovieFactory.PhotoMovieType photoMovieType) {
        this.imgRes = i;
        this.name = str;
        this.type = photoMovieType;
    }

    public int getImgRes() {
        return this.imgRes;
    }

    public void setImgRes(int i) {
        this.imgRes = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public PhotoMovieFactory.PhotoMovieType getType() {
        return this.type;
    }

    public void setType(PhotoMovieFactory.PhotoMovieType photoMovieType) {
        this.type = photoMovieType;
    }
}
