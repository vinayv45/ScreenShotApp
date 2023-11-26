package com.takeascreenshortScreenShotApp.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SharedData {
    public static ImageReader imageReader;
    public static Context sharedContext;

    public static boolean isCapturing = false;
    public static TessBaseAPI tessBaseAPI = new TessBaseAPI();

    public static void takeAScreenShot() {


        Log.d("Vinay", "Taking screenshot now..");
        if (imageReader != null) {
            Image image = imageReader.acquireLatestImage();

            if (image == null) {
                image = imageReader.acquireLatestImage();
            }
            if (image == null) {
                image = imageReader.acquireLatestImage();
            }
            if (image == null) {
                image = imageReader.acquireNextImage();
            }


            if (image == null)
                return;

            try {
                Image.Plane[] planes = image.getPlanes();

                Image.Plane plane = planes[0];
                ByteBuffer buffer = plane.getBuffer();
                int pixelStride = plane.getPixelStride();
                int rowStride = plane.getRowStride();
                int rowPadding = rowStride - pixelStride * image.getWidth();

                Bitmap bitmap = Bitmap.createBitmap(
                        image.getWidth() + rowPadding / pixelStride,
                        image.getHeight(),
                        Bitmap.Config.ARGB_8888
                );

                bitmap.copyPixelsFromBuffer(buffer);
                saveBitmapToFile(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                image.close();
            }

        }
    }

    private static void saveBitmapToFile(Bitmap bitmap) {
        String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/sc.jpg";
        File fileScreenshot = new File(imagePath);
        if (fileScreenshot.exists()) {
            fileScreenshot.delete();
        } else {
            try {
                fileScreenshot.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Log.d("Vinay 5", "Screenshot saved to: " + fileScreenshot.getAbsolutePath());

        String tessdataPath = sharedContext.getFilesDir().getPath();
        File tessdataDir = new File(tessdataPath);

        if (!tessdataDir.exists()) {
            tessdataDir.mkdirs();
        }

        tessBaseAPI.init(tessdataPath, "eng");
        tessBaseAPI.setImage(bitmap);
        String recognizedText = tessBaseAPI.getUTF8Text();
        Log.d("Vinay 7", recognizedText);
        tessBaseAPI.end();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
