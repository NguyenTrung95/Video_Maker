package com.hw.photomovie.moviefilter;

import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.opengl.FboTexture;


public interface IMovieFilter {
    void doFilter(PhotoMovie photoMovie,int elapsedTime, FboTexture inputTexture, FboTexture outputTexture);
    void release();
}
