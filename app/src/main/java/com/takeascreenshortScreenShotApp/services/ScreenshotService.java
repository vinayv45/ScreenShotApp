package com.takeascreenshortScreenShotApp.services;
import static com.takeascreenshortScreenShotApp.data.SharedData.imageReader;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;



public class ScreenshotService extends Service {

    private static final long DELAY = 5000;
    private boolean shouldContinueCapturing = true;
    private Thread captureThread;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;


    private Handler handler;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent resultData = intent.getParcelableExtra("data");
        int resultCode = intent.getIntExtra("result-code",-1);
        mediaProjection = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE)).getMediaProjection(resultCode,resultData);
        startImageReader();
        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        shouldContinueCapturing = false;
        if (captureThread != null) {
            captureThread.interrupt();
        }
    }



    private void startImageReader() {
        if (mediaProjection != null) {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 10);
            mediaProjection.createVirtualDisplay(
                    "ScreenCapture",
                    screenWidth,
                    screenHeight,
                    getResources().getDisplayMetrics().densityDpi,
                    0,
                    imageReader.getSurface(),
                    null,
                    null
            );

        }
    }
}
