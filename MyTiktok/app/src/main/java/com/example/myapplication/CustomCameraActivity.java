package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final static int PERMISSION_REQUEST_CODE = 1001;
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mHolder;
    private ImageView mImageView;
    private VideoView mVideoView;
    private Button mRecordButton;
    private boolean isRecording = false;

    private String mp4Path = "";
    private int time = 0;
    public Handler handler = new Handler();
    private String video_Name;
    private ProgressBar progressBar;
    public File mediaFile;
    private Button btn_jump;
    private Button btn_camera;


    public static void startUI(Context context) {
        Intent intent = new Intent(context, CustomCameraActivity.class);
        context.startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        mSurfaceView = findViewById(R.id.surfaceview);
        mImageView = findViewById(R.id.iv_img);
        mVideoView = findViewById(R.id.videoview);
        mRecordButton = findViewById(R.id.bt_record);
        progressBar = findViewById(R.id.progressbar);
        btn_jump=findViewById(R.id.btn_jump);
        btn_camera=findViewById(R.id.btn_pic);
        progressBar.setMax(10000);
        progressBar.setMin(0);

        mHolder = mSurfaceView.getHolder();
        try{
            requestPermission();
            initCamera();
        }catch (Exception e){
            Toast.makeText(this, "Initial fail, Error", Toast.LENGTH_SHORT).show();
        }
        mHolder.addCallback(this);
    }
    private void requestPermission() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission && hasAudioPermission) {

        } else {
            List<String> permission = new ArrayList<String>();
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.CAMERA);
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.RECORD_AUDIO);
            }
            ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), PERMISSION_REQUEST_CODE);
        }

    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(time<10000 && isRecording){
                progressBar.setVisibility(View.VISIBLE);
                time=time+16;
                progressBar.setProgress((10000-time));
                handler.postDelayed(this,0);
            }
            else if(isRecording&& time>=10000){
                mRecordButton.callOnClick();
            }
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            initCamera();
        }
        mCamera.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    private void initCamera() {
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.set("orientation", "portrait");
        parameters.set("rotation", 90);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // todo 3.1 设置 camera 和 holder 建立关联
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }
        //停止预览效果
        mCamera.stopPreview();
        //重新设置预览效果
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // todo 3.2 释放相机
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public void takePhoto(View view) {
        mCamera.takePicture(null, null, mPictureCallback);
    }

    //获取照片中的接口回调
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // todo 3.3 显示拍照所得图片
            FileOutputStream fos = null;
            String filepath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() +
                    File.separator + "pic.jpg";
            File file = new File(filepath);
            try{
                fos = new FileOutputStream(filepath);
                fos.write(data);
                fos.flush();
                Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                Bitmap rotateBitmap = PathUtils.rotateImage(bitmap,filepath);
                mImageView.setVisibility(View.VISIBLE);
                mVideoView.setVisibility(View.GONE);
                mImageView.setImageBitmap(rotateBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                mCamera.startPreview();
                if (fos != null){
                    try{
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };
    public void jump_to_upload(View view){
        Intent intent = new Intent(CustomCameraActivity.this,postvideo.class);
        Constants.Camera_upload = 1;
        Constants.mp4Path = mp4Path;
        startActivity(intent);
        finish();
    }

    public void record(View view) {
        if (isRecording) {
            // todo 3.4 开始录制
            mRecordButton.setText("錄制");
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();

            mVideoView.setVisibility(View.VISIBLE);
            btn_jump.setVisibility(View.VISIBLE);
            btn_camera.setVisibility(View.GONE);
            mRecordButton.setVisibility(View.GONE);

            mImageView.setVisibility(View.GONE);
            mVideoView.setVideoPath(mp4Path);
            mVideoView.start();

        } else {
            // todo 3.5 播放录制的视频
            if (prepareVideoRecorder()){
                time = 0;
                handler.post(runnable);
                mRecordButton.setText("暫停");
                mMediaRecorder.start();
            }
        }
        isRecording = !isRecording;
    }

    private boolean prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mp4Path = getOutputMediaPath();
        mMediaRecorder.setOutputFile(mp4Path);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mMediaRecorder.setOrientationHint(90);

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException | IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        // todo
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    private String getOutputMediaPath() {
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        video_Name="VIDEO"+System.currentTimeMillis();
        mediaFile = new File(mediaStorageDir, video_Name+ ".mp4");
        if (!mediaFile.exists()) {
            mediaFile.getParentFile().mkdirs();
        }
        return mediaFile.getAbsolutePath();
    }

}