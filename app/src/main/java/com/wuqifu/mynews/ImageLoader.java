package com.wuqifu.mynews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wuqifu on 15/6/6.
 */
public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };


    public void showImageByThread(ImageView imageView, final String url) {
        mImageView = imageView;
        mUrl = url;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromURL(url);

                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();
    }

    public Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream is;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            Thread.sleep(500);
            connection.disconnect();
            is.close();
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }

        return null;
    }
}
