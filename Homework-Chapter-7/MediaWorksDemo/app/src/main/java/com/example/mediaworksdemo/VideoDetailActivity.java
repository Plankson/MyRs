package com.example.mediaworksdemo;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoDetailActivity extends AppCompatActivity {

    String target_url = "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        VideoView v = findViewById(R.id.vv_detail);
        v.setVideoURI(Uri.parse(target_url));
        v.setMediaController(new MediaController(this));
        v.start();
    }
}
