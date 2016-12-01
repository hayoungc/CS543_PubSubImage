package com.example.jaewonkim.pubsubimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * AsyncTask implementation for bitmap images in the feed tab.
 * Customized the sample from Android Developers Training documents.
 * (Source: https://developer.android.com/training/displaying-bitmaps/process-bitmap.html)
 *
 * Created by nyangkun on 28/11/2016.
 */
public class BitmapWorkerTask extends AsyncTask<URL, Void, Bitmap> {

    private static String TAG = "BitmapWorkerTask";

    private WeakReference<ImageView> imgViewRef;
    private URL imgUrl;
    static private int reqWidth = 0;

    public BitmapWorkerTask(ImageView imgView) {
        imgViewRef = new WeakReference<>(imgView);

    }

    // Set the sample width of bitmaps.
    public static void setReqWidth(int width) {
        reqWidth = width;
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(URL... urls) {
        imgUrl = urls[0];
        return decodeSampledBitmapFromUrl(imgUrl, reqWidth);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (null != imgViewRef && null != bitmap) {
            final ImageView imgView = imgViewRef.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imgView);
            if (this == bitmapWorkerTask && null != imgView) {
                ViewGroup.LayoutParams layoutParams = imgView.getLayoutParams();
                int imgHeight = bitmap.getHeight();
                layoutParams.height = bitmap.getHeight();
                imgView.setLayoutParams(layoutParams);
                imgView.setImageBitmap(bitmap);
            }
        }
    }

    public Bitmap decodeSampledBitmapFromUrl(URL url, int reqWidth) {
        // Open an input stream with the URL.
        InputStream is;
        try {
            is = url.openStream();
        } catch (IOException e) {
            Log.e(TAG, "failed to open input stream for the image", e);
            return null;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        // Close the input stream.
        try {
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "failed to close input stream for the image", e);
            return null;
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth);

        // Open the input stream again. This time, the image will be decoded and sampled.
        try {
            is = url.openStream();
        } catch (IOException e) {
            Log.e(TAG, "failed to open the second input stream for the image", e);
            return null;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap sampled = BitmapFactory.decodeStream(is, null, options);

        double ratio = 1.0 * sampled.getHeight() / sampled.getWidth();
        Bitmap resized_bit = Bitmap.createScaledBitmap(sampled, 600, (int) (600 * ratio), true);

        // Close the input stream.
        try {
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "failed to close the second input stream for the image", e);
            return null;
        }

        return resized_bit;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth) {
        // Raw height and width of image
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static boolean cancelPotentialWork(URL imgUrl, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final URL bitmapImgUrl = bitmapWorkerTask.imgUrl;
            // If bitmapImgUrl is not yet set or it differs from the new data
            if (null != bitmapImgUrl || !bitmapImgUrl.equals(imgUrl)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
