package com.example.jaewonkim.pubsubimage;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Class for drawable objects each bound to a {@link BitmapWorkerTask} object.
 * (Source: https://developer.android.com/training/displaying-bitmaps/process-bitmap.html)
 *
 * Created by nyangkun on 28/11/2016.
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
                         BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return bitmapWorkerTaskReference.get();
    }
}
