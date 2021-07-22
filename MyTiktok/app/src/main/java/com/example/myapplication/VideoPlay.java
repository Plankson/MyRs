package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class VideoPlay extends AppCompatActivity {
    String extra=null;
    SurfaceView surfaceView;
    MediaPlayer mediaPlayer;
    SurfaceHolder holdr;
    Uri address;
    SeekBar seekBar;
    ImageView stop_ig;
    int count;
    ImageButton not_like;
    ImageButton is_like;
    TextView pro;
    static int maxx;
    long mCutTime=0;
    long mLastTime=0;
    ObjectAnimator no_ani_x=null;
    ObjectAnimator is_ani_x=null;
    ObjectAnimator no_ani_y=null;
    ObjectAnimator is_ani_y=null;
    private static String TAG="VideoPlay";
    private final String SP_DEMO = "sp_demo";
    private Handler her=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1://单击
                    ++count;
                    if(count%2==1){
                        stop_ig.setVisibility(View.VISIBLE);
                        mediaPlayer.pause();
                    }
                    else {
                        mediaPlayer.start();
                        stop_ig.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 2://双击
                    SharedPreferences sp = VideoPlay.this.getSharedPreferences(SP_DEMO, MODE_PRIVATE);
                    int value = sp.getInt(extra,0);
                    Log.e(TAG, "onClick: ");
                    if(value==0){
                        Log.d(TAG, "findnow:value=0");
                        not_like.setAlpha(0.0f);
                        is_like.setAlpha(1.0f);
                        is_ani_x=ObjectAnimator.ofFloat(is_like,"scaleX",1.0f,1.3f);
                        is_ani_y=ObjectAnimator.ofFloat(is_like,"scaleY",1.0f,1.3f);
                        is_ani_x.setRepeatCount(1);is_ani_x.setRepeatMode(ValueAnimator.REVERSE);
                        is_ani_y.setRepeatCount(1);is_ani_y.setRepeatMode(ValueAnimator.REVERSE);
                        AnimatorSet animatorSet=new AnimatorSet();
                        animatorSet.playTogether(is_ani_x,is_ani_y);
                        animatorSet.start();
                        value=1;
                    }
                    else {
                        Log.d(TAG, "findnow:value=1");
                        not_like.setAlpha(1.0f);
                        is_like.setAlpha(0.0f);
                        no_ani_x=ObjectAnimator.ofFloat(not_like,"scaleX",1.0f,1.3f);
                        no_ani_y=ObjectAnimator.ofFloat(not_like,"scaleY",1.0f,1.3f);
                        no_ani_x.setRepeatCount(1);no_ani_x.setRepeatMode(ValueAnimator.REVERSE);
                        no_ani_y.setRepeatCount(1);no_ani_y.setRepeatMode(ValueAnimator.REVERSE);
                        AnimatorSet animatorSet=new AnimatorSet();
                        animatorSet.playTogether(no_ani_x,no_ani_y);
                        animatorSet.start();
                        value=0;
                    }
                    SharedPreferences.Editor edt = sp.edit();
                    edt.putInt(extra, value);
                    edt.apply();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count=0;
        setContentView(R.layout.activity_video_play);
        stop_ig=findViewById(R.id.ig_stop);
        not_like=findViewById(R.id.not_liked);
        is_like=findViewById(R.id.is_liked);
        pro=findViewById(R.id.video_process);
        if(count==0)stop_ig.setVisibility(View.INVISIBLE);
        extra=getIntent().getStringExtra("Videourl");
        initView();
        setListener();
        is_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = VideoPlay.this.getSharedPreferences(SP_DEMO, MODE_PRIVATE);
                int value = sp.getInt(extra,0);
                Log.e(TAG, "onClick: ");
                if(value==0){
                    Log.d(TAG, "findnow:value=0");
                    is_like.setAlpha(1.0f);
                    not_like.setAlpha(0.0f);
                    is_ani_x=ObjectAnimator.ofFloat(is_like,"scaleX",1.0f,1.3f);
                    is_ani_y=ObjectAnimator.ofFloat(is_like,"scaleY",1.0f,1.3f);
                    is_ani_x.setRepeatCount(1);is_ani_x.setRepeatMode(ValueAnimator.REVERSE);
                    is_ani_y.setRepeatCount(1);is_ani_y.setRepeatMode(ValueAnimator.REVERSE);
                    AnimatorSet animatorSet=new AnimatorSet();
                    animatorSet.playTogether(is_ani_x,is_ani_y);
                    animatorSet.start();
                    value=1;
                }
                else {
                    Log.d(TAG, "findnow:value=1");
                    not_like.setAlpha(1.0f);
                    is_like.setAlpha(0.0f);
                    no_ani_x=ObjectAnimator.ofFloat(not_like,"scaleX",1.0f,1.3f);
                    no_ani_y=ObjectAnimator.ofFloat(not_like,"scaleY",1.0f,1.3f);
                    no_ani_x.setRepeatCount(1);no_ani_x.setRepeatMode(ValueAnimator.REVERSE);
                    no_ani_y.setRepeatCount(1);no_ani_y.setRepeatMode(ValueAnimator.REVERSE);
                    AnimatorSet animatorSet=new AnimatorSet();
                    animatorSet.playTogether(no_ani_x,no_ani_y);
                    animatorSet.start();
                    value=0;
                }
                SharedPreferences.Editor edt = sp.edit();
                edt.putInt(extra, value);
                edt.apply();
            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLastTime=mCutTime;
                mCutTime=System.currentTimeMillis();
                if(mCutTime-mLastTime<300){//双击
                    mCutTime=0;mLastTime=0;
                    her.removeMessages(1);
                    her.sendEmptyMessage(2);
                }else {
                    her.sendEmptyMessageDelayed(1,310);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
        mediaPlayer.release();
    }

    private void setListener(){
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String all_len=String.format("%02d:%02d:%02d",maxx/3600000,maxx/60000,maxx/1000);
                String now_len=String.format("%02d:%02d:%02d",i/3600000,i/60000,i/1000);
                String pt=now_len+'/'+all_len;
                pro.setText(pt);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { handler.removeCallbacks(run);}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.postDelayed(run, 1000);
            }
        });
    }
    Handler handler = new Handler();
    Runnable run = new Runnable() {
        @Override
        public void run() {
            //player.getCurrentPosition()获取音乐的当前进度
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(run, 100);
        }
    };
    private void initView(){
        SharedPreferences sp = VideoPlay.this.getSharedPreferences(SP_DEMO, MODE_PRIVATE);
        int value = sp.getInt(extra,0);
        Log.d(TAG, "in");
        if(value==0){
            Log.d(TAG, "findinit:value=0");
            is_like.setAlpha(0.0f);
            not_like.setAlpha(1.0f);
            SharedPreferences.Editor edt = sp.edit();
            edt.putInt(extra, value);
            edt.apply();
        }
        else {
            Log.d(TAG, "findinit:value=1");
            not_like.setAlpha(0.0f);
            is_like.setAlpha(1.0f);
        }
        handler.postDelayed(run, 100);
        seekBar=findViewById(R.id.sb);
        surfaceView=findViewById(R.id.suv);
        mediaPlayer=new MediaPlayer();
        address=Uri.parse(extra);
        try {
            mediaPlayer.setDataSource(VideoPlay.this, address);
            holdr=surfaceView.getHolder();
            holdr.addCallback(new PlayerCallBack());
            mediaPlayer.prepare();
            maxx=mediaPlayer.getDuration();
            seekBar.setMax(maxx);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private class PlayerCallBack implements SurfaceHolder.Callback{
        @Override
        public void surfaceCreated(SurfaceHolder holder){
            mediaPlayer.setDisplay(holder);
            }
        @Override
        public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder){
        }
    }
    }
