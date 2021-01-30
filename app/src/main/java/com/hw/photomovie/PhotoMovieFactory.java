package com.hw.photomovie;

import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.segment.EndGaussianBlurSegment;
import com.hw.photomovie.segment.FitCenterScaleSegment;
import com.hw.photomovie.segment.FitCenterSegment;
import com.hw.photomovie.segment.GradientTransferSegment;
import com.hw.photomovie.segment.LayerSegment;
import com.hw.photomovie.segment.MoveTransitionSegment;
import com.hw.photomovie.segment.ScaleSegment;
import com.hw.photomovie.segment.ScaleTransSegment;
import com.hw.photomovie.segment.SingleBitmapSegment;
import com.hw.photomovie.segment.TestMovieSegment;
import com.hw.photomovie.segment.ThawSegment;
import com.hw.photomovie.segment.WindowSegment;
import com.hw.photomovie.segment.layer.GaussianBlurLayer;
import com.hw.photomovie.segment.layer.MovieLayer;
import java.util.ArrayList;
import java.util.Random;

public class PhotoMovieFactory {
    public static final int END_GAUSSIANBLUR_DURATION = 1500;

    public enum PhotoMovieType {
        RANDOM,
        THAW,
        SCALE,
        SCALE_TRANS,
        WINDOW,
        HORIZONTAL_TRANS,
        VERTICAL_TRANS,
        GRADIENT,
        TEST
    }


    public static PhotoMovie generatePhotoMovie(PhotoSource photoSource, PhotoMovieType photoMovieType, int i) {
        switch (photoMovieType) {
            case RANDOM:
                return randomPhotoMovie(photoSource, i);
            case THAW:
                return generateThawPhotoMovie(photoSource, i);
            case SCALE:
                return generateScalePhotoMovie(photoSource, i);
            case SCALE_TRANS:
                return generateScaleTransPhotoMovie(photoSource, i);
            case WINDOW:
                return generateWindowPhotoMovie(photoSource, i);
            case HORIZONTAL_TRANS:
                return generateHorizontalTransPhotoMovie(photoSource, i);
            case VERTICAL_TRANS:
                return generateVerticalTransPhotoMovie(photoSource, i);
            case GRADIENT:
                return genGradientPhotoMovie(photoSource, i);
            case TEST:
                return generateTestPhotoMovie(photoSource);
            default:
                return null;
        }
    }

    private static PhotoMovie generateHorizontalTransPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < photoSource.size(); i2++) {
            arrayList.add(new FitCenterSegment(i).setBackgroundColor(-16777216));
            arrayList.add(new MoveTransitionSegment(MoveTransitionSegment.DIRECTION_HORIZON, i));
        }
        arrayList.remove(arrayList.size() - 1);
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateVerticalTransPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < photoSource.size(); i2++) {
            arrayList.add(new FitCenterSegment(i).setBackgroundColor(-16777216));
            arrayList.add(new MoveTransitionSegment(MoveTransitionSegment.DIRECTION_VERTICAL, i));
        }
        arrayList.remove(arrayList.size() - 1);
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateTestPhotoMovie(PhotoSource photoSource) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TestMovieSegment(5555));
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateWindowPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList(7);
        arrayList.add(new SingleBitmapSegment(i));
        arrayList.add(new WindowSegment(2.1f, 1.0f, 2.1f, -1.0f, -1.1f).removeFirstAnim());
        arrayList.add(new WindowSegment(-1.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f));
        arrayList.add(new WindowSegment(-1.0f, -2.1f, 1.0f, -2.1f, 1.1f).removeFirstAnim());
        arrayList.add(new WindowSegment(0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f));
        arrayList.add(new WindowSegment(-1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f));
        arrayList.add(new EndGaussianBlurSegment(1500));
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateScalePhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList(photoSource.size() + 1);
        for (int i2 = 0; i2 < photoSource.size(); i2++) {
            arrayList.add(new ScaleSegment(i, 10.0f, 1.0f));
        }
        arrayList.add(new EndGaussianBlurSegment(i));
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateScaleTransPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList(photoSource.size() + 1);
        for (int i2 = 0; i2 < photoSource.size() - 1; i2++) {
            arrayList.add(new ScaleTransSegment());
        }
        arrayList.add(new LayerSegment(new MovieLayer[]{new GaussianBlurLayer()}, i));
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateThawPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < photoSource.size() - 1; i3++) {
            int i4 = i2 + 1;
            arrayList.add(new ThawSegment(i, i2));
            i2 = i4 == 3 ? 0 : i4;
        }
        arrayList.add(new ScaleSegment(i, 1.0f, 1.1f));
        arrayList.add(new EndGaussianBlurSegment(1500));
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie genGradientPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList(photoSource.size());
        for (int i2 = 0; i2 < photoSource.size(); i2++) {
            if (i2 == 0) {
                arrayList.add(new FitCenterScaleSegment(i, 1.0f, 1.1f));
            } else {
                arrayList.add(new FitCenterScaleSegment(i, 1.05f, 1.1f));
            }
            if (i2 < photoSource.size() - 1) {
                arrayList.add(new GradientTransferSegment(i, 1.1f, 1.15f, 1.0f, 1.05f));
            }
        }
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie generateCustomPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SingleBitmapSegment(i));
        arrayList.add(new WindowSegment(2.1f, 1.0f, 2.1f, -1.0f, -1.1f).removeFirstAnim());
        arrayList.add(new FitCenterScaleSegment(i, 1.0f, 1.1f));
        arrayList.add(new ScaleSegment(i, 10.0f, 1.0f));
        arrayList.add(new FitCenterSegment(i).setBackgroundColor(-16777216));
        arrayList.add(new MoveTransitionSegment(MoveTransitionSegment.DIRECTION_HORIZON, i));
        arrayList.add(new FitCenterSegment(i).setBackgroundColor(-16777216));
        arrayList.add(new MoveTransitionSegment(MoveTransitionSegment.DIRECTION_VERTICAL, i));
        arrayList.add(new FitCenterSegment(i).setBackgroundColor(-16777216));
        arrayList.add(new EndGaussianBlurSegment(i / 2));
        arrayList.add(new ThawSegment(i, 1));
        return new PhotoMovie(photoSource, arrayList);
    }

    private static PhotoMovie randomPhotoMovie(PhotoSource photoSource, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < photoSource.size(); i2++) {
            switch (new Random().nextInt(7)) {
                case 0:
                    arrayList.add(new FitCenterScaleSegment(i, 1.0f, 1.1f));
                    arrayList.add(new WindowSegment(-1.0f, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f));
                    break;
                case 1:
                    arrayList.add(new FitCenterScaleSegment(i, 1.0f, 1.1f));
                    arrayList.add(new WindowSegment(-1.0f, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f));
                    break;
                case 2:
                    arrayList.add(new FitCenterScaleSegment(i, 1.0f, 1.1f));
                    arrayList.add(new WindowSegment(0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f));
                    break;
                case 3:
                    int i3 = i / 2;
                    arrayList.add(new FitCenterScaleSegment(i3, 1.0f, 1.1f));
                    arrayList.add(new GradientTransferSegment(i, 1.1f, 1.15f, 1.0f, 1.05f));
                    arrayList.add(new FitCenterScaleSegment(i3, 1.05f, 1.0f));
                    break;
                case 4:
                    int i4 = i / 2;
                    arrayList.add(new FitCenterScaleSegment(i4, 1.0f, 1.1f));
                    arrayList.add(new MoveTransitionSegment(MoveTransitionSegment.DIRECTION_HORIZON, i));
                    arrayList.add(new FitCenterScaleSegment(i4, 1.0f, 1.05f));
                    break;
                case 5:
                    int i5 = i / 2;
                    arrayList.add(new FitCenterScaleSegment(i5, 1.0f, 1.1f));
                    arrayList.add(new MoveTransitionSegment(MoveTransitionSegment.DIRECTION_VERTICAL, i));
                    arrayList.add(new FitCenterScaleSegment(i5, 1.0f, 1.05f));
                    break;
                case 6:
                    arrayList.add(new FitCenterScaleSegment(i, 1.0f, 1.1f));
                    arrayList.add(new ThawSegment(i, 1));
                    break;
            }
        }
        return new PhotoMovie(photoSource, arrayList);
    }
}
