package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.MessageListResponse;
import com.example.myapplication.model.data;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.example.myapplication.Constants.token;

public class MainActivity extends AppCompatActivity {

    private static int PAGE_COUNT=2;
    private static int now_time=5;
    private FeedAdapter adapter = new FeedAdapter();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            TextView tv=findViewById(R.id.txv);
            String s=String.format("跳过|%d",msg.what);s=s+"s";
            if(msg.what==0)BacktoMain();
            tv.setText(s);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        View view=findViewById(R.id.btn_skip);
        view.getBackground().setAlpha(100);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                BacktoMain();
            }
        });
        Log.d("initial", "o");
        handler.sendEmptyMessageDelayed(now_time,0000);
        now_time--;
        handler.sendEmptyMessageDelayed(now_time,1000);
        now_time--;
        handler.sendEmptyMessageDelayed(now_time,2000);
        now_time--;
        handler.sendEmptyMessageDelayed(now_time,3000);
        now_time--;
        handler.sendEmptyMessageDelayed(now_time,4000);
        now_time--;
        handler.sendEmptyMessageDelayed(now_time,5000);
    }
    private void BacktoMain(){
        setContentView(R.layout.activity_main);
        Button vp=findViewById(R.id.video_post);
        vp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TempUploadActivity.class);
                startActivity(intent);
            }
        });
        ViewPager pager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if(i==0)return new PlaceholderFragment();
                else return new MyFragment();
            }
            @Override
            public int getCount() {
                return PAGE_COUNT;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                if(position==0)return "推荐";
                return "我的";
            }
        });
        tabLayout.setupWithViewPager(pager);
    }

}
