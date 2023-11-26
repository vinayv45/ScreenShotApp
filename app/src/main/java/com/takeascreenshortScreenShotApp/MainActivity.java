package com.takeascreenshortScreenShotApp;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.takeascreenshortScreenShotApp.data.SharedData;
import com.takeascreenshortScreenShotApp.services.ScreenshotService;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MEDIA_PROJECTION = 1000;
    MediaProjectionManager mediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        SharedData.sharedContext  = this;
        startProjection();
    }

    private void startProjection() {
        mediaProjectionManager =
                (MediaProjectionManager) getSystemService(Context. MEDIA_PROJECTION_SERVICE);

        if (mediaProjectionManager != null) {
            Intent permissionIntent = mediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(permissionIntent, REQUEST_MEDIA_PROJECTION);
        } else {
            Toast.makeText(this, "MediaProjectionManager is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION && resultCode == RESULT_OK && data != null) {
            Intent serviceIntent = new Intent(this, ScreenshotService.class);
            serviceIntent.putExtra("data", data);
            serviceIntent.putExtra("result-code", resultCode);
                startService(serviceIntent);
                startScreenRecording();
            }
    }

    private void startScreenRecording() {
        new PeriodicTaskExample().startPeriodicTask();
    }


}
