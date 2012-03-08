package com.allplayers.android.widget;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.allplayers.android.R;
import com.allplayers.util.BitmapMemoryCache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageLoader {

    BitmapMemoryCache memoryCache=new BitmapMemoryCache();
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    public ImageLoader(Context context){
        executorService=Executors.newFixedThreadPool(5);
    }

    final int stub_id=R.drawable.stub;
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        } else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) throws IOException
    {
        Bitmap bitmap = null;

        //from web

        URL imageUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
        connection.setInstanceFollowRedirects(true);
        connection.setUseCaches(true);

        InputStream is = new BufferedInputStream(connection.getInputStream());

        bitmap = decodeFile(is);
        is.close();
        return bitmap;
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(InputStream is) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=is.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        byte[] response = out.toByteArray();

        //decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(response, 0, response.length, o);

        //Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE=70;
        int width_tmp=o.outWidth, height_tmp=o.outHeight;
        int scale=1;
        while(true){
            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE) {
                break;
            }
            width_tmp/=2;
            height_tmp/=2;
            scale*=2;
        }

        //decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        return BitmapFactory.decodeByteArray(response, 0, response.length, o2);
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u;
            imageView=i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        public void run() {
            if(imageViewReused(photoToLoad)) {
                return;
            }

            Bitmap bmp;
            try {
                bmp = getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
            } catch (IOException e) {
                // Probably a 404 or temp network failure.
                return;
            }


            if(imageViewReused(photoToLoad)) {
                return;
            }
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url)) {
            return true;
        }
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad)) {
                return;
            }
            if(bitmap!=null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            } else {
                photoToLoad.imageView.setImageResource(stub_id);
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
    }

}
