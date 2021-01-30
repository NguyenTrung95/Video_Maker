package com.devchie.videomaker.model;

import java.io.Serializable;

public class Photo implements Serializable {
    public long id;
    public String paths;

    public Photo(long j, String str) {
        this.id = j;
        this.paths = str;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getPaths() {
        return this.paths;
    }

    public void setPaths(String str) {
        this.paths = str;
    }
}
