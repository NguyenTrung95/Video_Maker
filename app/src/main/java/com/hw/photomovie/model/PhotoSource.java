package com.hw.photomovie.model;

import android.graphics.Bitmap;
import android.util.SparseArray;
import com.hw.photomovie.util.MLog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class PhotoSource implements PhotoData.OnDataLoadListener {
    protected static final int REPEAT_COUNT = 2;
    private static final String TAG = "PhotoSource";
    private AtomicInteger mDiskPrepared = new AtomicInteger(0);
    private Map<PhotoData, Integer> mErrorMap = new ConcurrentHashMap();
    private OnSourcePrepareListener mOnSourcePrepareListener;
    private List<PhotoData> mPhotoDataList = new Vector();
    private SparseArray<Float> mProgressArray = new SparseArray<>();
    private int mRequiredDiskPrepareNum;

    public interface OnSourcePrepareListener {
        void onError(PhotoSource photoSource, PhotoData photoData, ErrorReason errorReason);

        void onPrepared(PhotoSource photoSource, int i, List<PhotoData> list);

        void onPreparing(PhotoSource photoSource, float f);
    }

    public PhotoSource(List<PhotoData> list) {
        if (list != null) {
            this.mPhotoDataList.addAll(list);
        }
        this.mDiskPrepared.set(0);
    }

    public int size() {
        return this.mPhotoDataList.size();
    }

    public PhotoData get(int i) {
        if (i < 0 || i >= this.mPhotoDataList.size()) {
            return null;
        }
        return this.mPhotoDataList.get(i);
    }

    private List<PhotoData> getErrorList() {
        ArrayList arrayList = new ArrayList(this.mErrorMap.keySet());
        int size = arrayList.size();
        while (true) {
            size--;
            if (size >= arrayList.size() || size < 0) {
                return arrayList;
            }
            Integer num = this.mErrorMap.get(arrayList.get(size));
            if (num == null || num.intValue() < 2) {
                arrayList.remove(size);
            }
        }

    }

    public void prepare(int i) {
        this.mRequiredDiskPrepareNum = i;
        this.mProgressArray.clear();
        int i2 = 0;
        if (size() == 0) {
            OnSourcePrepareListener onSourcePrepareListener = this.mOnSourcePrepareListener;
            if (onSourcePrepareListener != null) {
                onSourcePrepareListener.onPreparing(this, 1.0f);
                this.mOnSourcePrepareListener.onPrepared(this, 0, (List<PhotoData>) null);
                return;
            }
            return;
        }
        this.mPhotoDataList.addAll(this.mErrorMap.keySet());
        this.mErrorMap.clear();
        this.mDiskPrepared.set(0);
        while (i2 < size() && i2 < i) {
            get(i2).prepareData(2, this);
            i2++;
        }
    }

    public void prepare() {
        prepare(size());
    }

    public void setOnSourcePreparedListener(OnSourcePrepareListener onSourcePrepareListener) {
        this.mOnSourcePrepareListener = onSourcePrepareListener;
    }

    private synchronized void notifyPrepareProgress() {
        int i = 0;
        float f = 0.0f;
        while (i < size() && i < this.mRequiredDiskPrepareNum) {
            f += (this.mProgressArray.get(get(i).hashCode(), Float.valueOf(0.0f)).floatValue() * 1.0f) / ((float) this.mRequiredDiskPrepareNum);
            i++;
        }
        if (this.mOnSourcePrepareListener != null) {
            this.mOnSourcePrepareListener.onPreparing(this, f);
            if (this.mDiskPrepared.get() >= this.mRequiredDiskPrepareNum) {
                this.mOnSourcePrepareListener.onPrepared(this, this.mDiskPrepared.get(), getErrorList());
                for (int i2 = this.mRequiredDiskPrepareNum; i2 < size(); i2++) {
                    get(i2).prepareData(2, (PhotoData.OnDataLoadListener) null);
                }
            }
        }
        MLog.i(TAG, "onDownloadProgressUpdate:" + f);
    }

    public void onDataLoaded(PhotoData photoData, Bitmap bitmap) {
        notifyPrepareProgress();
    }

    public void onDownloaded(PhotoData photoData) {
        this.mDiskPrepared.addAndGet(1);
        this.mProgressArray.put(photoData.hashCode(), Float.valueOf(1.0f));
        notifyPrepareProgress();
    }

    public List<PhotoData> getSourceData() {
        return new LinkedList(this.mPhotoDataList);
    }

    public void onDownloadProgressUpdate(PhotoData photoData, int i, int i2) {
        if (photoData != null) {
            this.mProgressArray.put(photoData.hashCode(), Float.valueOf(((float) i) / ((float) i2)));
            notifyPrepareProgress();
        }
    }

    public void onError(PhotoData photoData, ErrorReason errorReason) {
        int i = 1;
        if (this.mErrorMap.containsKey(photoData)) {
            i = 1 + this.mErrorMap.get(photoData).intValue();
        }
        Integer valueOf = Integer.valueOf(i);
        this.mErrorMap.put(photoData, valueOf);
        if (valueOf.intValue() >= 2) {
            MLog.e(TAG, photoData + " prepare error:" + valueOf + " 放弃加载。");
            this.mPhotoDataList.remove(photoData);
            notifyPrepareProgress();
            return;
        }
        photoData.prepareData(photoData.getTargetState(), this);
        MLog.e(TAG, photoData + " prepare error:" + valueOf);
    }
}
