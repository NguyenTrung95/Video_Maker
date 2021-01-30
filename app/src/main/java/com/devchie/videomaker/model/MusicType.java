package com.devchie.videomaker.model;

public class MusicType {
    public int imgMusic;
    public String nameMusic;

    public MusicType(int i, String str) {
        this.imgMusic = i;
        this.nameMusic = str;
    }

    public int getImgMusic() {
        return this.imgMusic;
    }

    public void setImgMusic(int i) {
        this.imgMusic = i;
    }

    public String getNameMusic() {
        return this.nameMusic;
    }

    public void setNameMusic(String str) {
        this.nameMusic = str;
    }
}
