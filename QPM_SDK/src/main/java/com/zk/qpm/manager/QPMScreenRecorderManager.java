package com.zk.qpm.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.zk.qpm.QPMConstant;
import com.zk.qpm.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class QPMScreenRecorderManager {

    private static final int REQUEST_CODE = 1001;

    private static volatile QPMScreenRecorderManager instance;

    private boolean isStart;
    private long startTime;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay display;

    private QPMScreenRecorderManager() {
    }

    public static QPMScreenRecorderManager getInstance() {
        if (instance == null) {
            synchronized (QPMScreenRecorderManager.class) {
                if (instance == null) {
                    instance = new QPMScreenRecorderManager();
                }
            }
        }
        return instance;
    }

    public void startRecorder(Activity activity) {
        if (isStart || activity == null || activity.isFinishing()) {
            return;
        }
        isStart = true;
        mediaProjectionManager = (MediaProjectionManager) QPMManager.getInstance().getContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent intent = mediaProjectionManager.createScreenCaptureIntent();
        PackageManager packageManager = activity.getPackageManager();
        if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            //存在录屏授权的Activity
            activity.startActivityForResult(intent, REQUEST_CODE);
        } else {
            onRecorderFailed(activity);
        }
    }

    public void stopRecorder() {
        if (!isStart) {
            return;
        }
        isStart = false;
        startTime = -1;
        if (display != null) {
            display.release();
            display = null;
        }
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
        if (mediaProjectionManager != null) {
            mediaProjectionManager = null;
        }
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    start(activity, resultCode, data);
                } catch (Exception e) {
                    e.printStackTrace();
                    onRecorderFailed(activity);
                }
            } else {
                onRecorderFailed(activity);
            }
            activity.finish();
        }
    }

    private void start(Activity activity, int resultCode, Intent data) {
        if (mediaProjectionManager == null) {
            return;
        }
        startTime = System.currentTimeMillis();
        try {
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;
            int density = metrics.densityDpi;

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(getSaveFile());
            mediaRecorder.setVideoSize(screenWidth, screenHeight);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setVideoEncodingBitRate(screenWidth * screenHeight);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.prepare();
            display = mediaProjection.createVirtualDisplay(QPMScreenRecorderManager.class.getSimpleName(),
                    screenWidth, screenHeight, density, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mediaRecorder.getSurface(), null, null);
            mediaRecorder.start();
        } catch (Exception e){
            e.printStackTrace();
            onRecorderFailed(activity);
        }
    }

    private void onRecorderFailed(Context context){
        Toast.makeText(context, R.string.jm_gt_screen_recorder_fail, Toast.LENGTH_SHORT).show();
        stopRecorder();
    }

    private String getSaveFile() throws Exception {
        File dir = new File(Environment.getExternalStorageDirectory(), QPMConstant.PATH_SCREEN_RECORDER);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new Exception("create screen recorder dir fail");
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String now = formatter.format(new Date());
        String fileName = now + ".mp4";
        File file = new File(dir, fileName);
        return file.getAbsolutePath();
    }

    public boolean isStart() {
        return isStart;
    }

    public long getStartTime() {
        return startTime;
    }
}
