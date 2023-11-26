package com.takeascreenshortScreenShotApp;
import android.os.Handler;

import com.takeascreenshortScreenShotApp.data.SharedData;

public class PeriodicTaskExample {

    private static final int DELAY_MILLISECONDS = 5000; // 5 seconds delay

    private Handler handler = new Handler();
    private Runnable periodicTask = new Runnable() {
        @Override
        public void run() {
            SharedData.takeAScreenShot();
            handler.postDelayed(this, DELAY_MILLISECONDS);
        }
    };

    public void startPeriodicTask() {
        handler.postDelayed(periodicTask, DELAY_MILLISECONDS);
    }

    public void stopPeriodicTask() {
        handler.removeCallbacks(periodicTask);
    }


}
