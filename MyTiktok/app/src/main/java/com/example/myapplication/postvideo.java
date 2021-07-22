package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.model.UploadResponse;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class postvideo extends AppCompatActivity {
    private final static int PERMISSION_REQUEST_CODE = 1001;
    private static final String TAG = "Upload";
    private static final long MAX_FILE_SIZE = 30 * 1024 * 1024;
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final int REQUEST_CODE_VIDEO = 202;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private static final String VIDEO_TYPE = "video/*";

    private static final int MSG_START_COMPRESS = 0;
    private static final int MSG_END_COMPRESS = 1;

    private IApi api;
    private Uri coverImageUri;
    private Uri videoUri;
    public String compressPath;
    public Boolean flag = false;

    private SimpleDraweeView coverSD;
    private SimpleDraweeView videoSD;
    private EditText ContentEditText;
    private static Retrofit retrofit;
    private Button btn_submit;
    private LottieAnimationView animationView;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetwork();
        setContentView(R.layout.activity_postvideo);
        coverSD = findViewById(R.id.sd_cover);
        videoSD = findViewById(R.id.sd_video);
        btn_submit = findViewById(R.id.btn_submit);
        animationView=findViewById(R.id.animation_view);
        lottieAnimationView=findViewById(R.id.lottie_view);
        ContentEditText = findViewById(R.id.et_extra_content);
        findViewById(R.id.btn_cover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");
            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_VIDEO, VIDEO_TYPE, "选择视频");
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        requestPermission();
        Camera_Info();
    }
    private void Camera_Info(){
        if (Constants.Camera_upload==1) {
            String videoPath = Constants.mp4Path;
            videoUri = Uri.parse("file://" + videoPath);
            coverImageUri = getVideoThumb(this,videoUri);
            coverSD.setImageURI(coverImageUri);
            videoSD.setImageURI(coverImageUri);
            Constants.Camera_upload = 0;
            Constants.mp4Path = "";
        }
    }
    private void requestPermission() {
        boolean hasCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean hasAudioPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (hasCameraPermission && hasAudioPermission) {

        } else {
            List<String> permission = new ArrayList<String>();
            if (!hasCameraPermission) {
                permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!hasAudioPermission) {
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            ActivityCompat.requestPermissions(this, permission.toArray(new String[permission.size()]), PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private Uri getVideoThumb(Context context, Uri uri) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(context,uri);
        Bitmap cover = media.getFrameAtTime();
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String coverName = "COVER_"+ System.currentTimeMillis() + ".jpg";
        File coverFile = new File(mediaStorageDir, coverName);
        try {
            FileOutputStream fos = new FileOutputStream(coverFile);
            cover.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
// 其次把文件插入到系统图库
        String path = coverFile.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, coverName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri coverUri = Uri.fromFile(coverFile);
        intent.setData(coverUri);
        context.sendBroadcast(intent);
        return coverUri;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                coverSD.setImageURI(coverImageUri);
            }
        }
        if (REQUEST_CODE_VIDEO == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                videoUri = data.getData();
                videoSD.setImageURI(getVideoThumb(this,videoUri));
            }
        }
    }



    private void initNetwork() {
        //TODO 3
        // 创建Retrofit实例
        // 生成api对象
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }
    private void submit_recover(){
        btn_submit.setText("上传");
        findViewById(R.id.btn_submit).setEnabled(true);
    }


    private void submit() {
        byte[] coverImageData = readDataFromUri(coverImageUri);
        byte[] videoData = readDataFromUri(videoUri);

        btn_submit.setText("上传中");
        findViewById(R.id.btn_submit).setEnabled(false);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        Runnable button_recover = new Runnable() {
            @Override
            public void run() {
                btn_submit.setText("重新上传");
                findViewById(R.id.btn_submit).setEnabled(true);
                lottieAnimationView.setVisibility(View.INVISIBLE);
                lottieAnimationView.pauseAnimation();
            }
        };

        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            submit_recover();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.pauseAnimation();
            return;
        }
        if (videoData == null || videoData.length == 0) {
            Toast.makeText(this, "视频不能为空白", Toast.LENGTH_SHORT).show();
            submit_recover();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.pauseAnimation();
            return;
        }

        String content = ContentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入视频备注", Toast.LENGTH_SHORT).show();
            submit_recover();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.pauseAnimation();
            return;
        }

        if (coverImageData.length + videoData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "文件过大，请重新选择", Toast.LENGTH_SHORT).show();
            submit_recover();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            lottieAnimationView.pauseAnimation();
            return;
        }
        // 使用api.submitMessage()方法提交留言
        MultipartBody.Part image_part = MultipartBody.Part.createFormData(
                "cover_image",
                "cover.png",
                RequestBody.create(MediaType.parse("multipart/form_data"), coverImageData)
        );
        MultipartBody.Part video_part = MultipartBody.Part.createFormData(
                "video",
                "video_file.mp4",
                RequestBody.create(MediaType.parse("multipart/form_data"), videoData)
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<UploadResponse> call = api.submitMessage(
                        Constants.STUDENT_ID,
                        Constants.USER_NAME,
                        content,
                        image_part,
                        video_part);
                try {
                    Response<UploadResponse> response = call.execute();
                    if (response.isSuccessful() && response.body().success){
                        Log.d("upload", "run: back");
                        finish();
                    }
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
