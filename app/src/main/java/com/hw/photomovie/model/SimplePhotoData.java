package com.hw.photomovie.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hw.photomovie.model.PhotoData;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimplePhotoData extends PhotoData {

    public Context mContext;

    public Handler mHandler = new Handler(Looper.getMainLooper());
    private ExecutorService mPool = Executors.newFixedThreadPool(4);

    public SimplePhotoData(Context context, String str, int i) {
        super(str, i);
        this.mContext = context.getApplicationContext();
    }

    public void prepareData(int i, final PhotoData.OnDataLoadListener onDataLoadListener) {
        this.mTargetState = i;
        int i2 = this.mState;
        if (i2 == -1 || i2 == 0) {
            this.mPool.submit(new Runnable() {
                public void run() {
                    SimplePhotoData.this.mState = 3;
                    SimplePhotoData simplePhotoData = SimplePhotoData.this;
                    Bitmap unused = simplePhotoData.loadBitmap(simplePhotoData.getUri());
                    SimplePhotoData.this.mState = 4;
                }
            });
        } else if (i2 != 2) {
            if (i2 == 4) {
                if (i == 4 && onDataLoadListener != null) {
                    onDataLoadListener.onDataLoaded(this, getBitmap());
                } else if (i == 2 && onDataLoadListener != null) {
                    onDataLoadListener.onDownloaded(this);
                }
            }
        } else if (i == 4) {
            this.mPool.submit(new Runnable() {
                public void run() {
                    SimplePhotoData.this.mState = 3;
                    Glide.with(SimplePhotoData.this.mContext).asBitmap().load(SimplePhotoData.this.getUri()).into(new CustomTarget<Bitmap>() {
                        public void onLoadCleared(Drawable drawable) {
                        }

                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            SimplePhotoData.this.mBitmap = bitmap;
                            if (SimplePhotoData.this.mBitmap != null) {
                                if (SimplePhotoData.this.mTargetState == 2) {
                                    SimplePhotoData.this.mState = 2;
                                } else if (SimplePhotoData.this.mTargetState == 4) {
                                    SimplePhotoData.this.mState = 4;
                                }
                                if (onDataLoadListener != null) {
                                    SimplePhotoData.this.mHandler.post(new Runnable() {
                                        public void run() {
                                            if (SimplePhotoData.this.mTargetState >= 2) {
                                                onDataLoadListener.onDownloaded(SimplePhotoData.this);
                                            }
                                            if (SimplePhotoData.this.mTargetState == 4) {
                                                onDataLoadListener.onDataLoaded(SimplePhotoData.this, SimplePhotoData.this.mBitmap);
                                            }
                                        }
                                    });
                                    return;
                                }
                                return;
                            }
                            SimplePhotoData.this.mState = -1;
                            if (onDataLoadListener != null) {
                                SimplePhotoData.this.mHandler.post(new Runnable() {
                                    public void run() {
                                        onDataLoadListener.onError(SimplePhotoData.this, (ErrorReason) null);
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else if (i == 2 && onDataLoadListener != null) {
            onDataLoadListener.onDownloaded(this);
        }
    }



    public Bitmap loadBitmap(String str) {
        InputStream inputStream;
        if (str.startsWith("drawable://")) {
            return BitmapFactory.decodeResource(this.mContext.getResources(), Integer.parseInt(str.substring(11)));
        } else if (str.startsWith("file://")) {
            return BitmapFactory.decodeFile(str.substring(7));
        } else {
            if (!str.startsWith("http")) {
                return BitmapFactory.decodeFile(str);
            }
            Bitmap bitmap = null;
            try {
                inputStream = ((HttpURLConnection) new URL(str).openConnection()).getInputStream();
                try {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e = e;
                        }
                    }
                } catch (Exception e2) {

                    try {
                        e2.printStackTrace();
                        if (inputStream != null) {
                        }
                        return bitmap;
                    } catch (Throwable th) {
                        th = th;
                        if (inputStream != null) {
                        }
                        throw th;
                    }
                }
            } catch (Exception e3) {

                inputStream = null;
                e3.printStackTrace();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e4) {

                    }
                }
                return bitmap;
            } catch (Throwable th2) {

                inputStream = null;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }

            }
            return bitmap;
        }

    }
}
