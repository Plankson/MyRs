package com.example.myapplication;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.model.MessageListResponse;
import com.example.myapplication.model.data;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static android.os.Looper.getMainLooper;
import static com.example.myapplication.Constants.STUDENT_ID;
import static com.example.myapplication.Constants.token;

public class MyFragment extends Fragment implements FeedAdapter.IOnItemClickListener {

    private LottieAnimationView animationView;
    private RecyclerView recyclerView;
    private FeedAdapter mAdapter;
    private myLayoutManager lyManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_placeholder, container, false);
        animationView=view.findViewById(R.id.animation_view2);
        recyclerView=view.findViewById(R.id.recycler);
        lyManager = new myLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
        initView();
        return view;
    }
    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lyManager);
        mAdapter = new FeedAdapter();
        mAdapter.setOnItemClickListener(this);
        getData("3190105419");
        /*if(data_type==0)getData("");
        else getData(STUDENT_ID);*/
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(3000);
        recyclerView.setItemAnimator(animator);
    }
    @Override
    public void onItemCLick(int position,data datas) {
        Intent intent=new Intent(MyApplication.getContext(),VideoPlay.class);
        intent.putExtra("Videourl",datas.getvideo_url());
        startActivity(intent);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        animationView.playAnimation();
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行=
                ObjectAnimator obj1= ObjectAnimator.ofFloat(animationView,"alpha",1f,0f);
                obj1.setRepeatCount(0);
                ObjectAnimator obj2= ObjectAnimator.ofFloat(recyclerView,"alpha",0f,1f);
                obj2.setRepeatCount(0);
                obj1.start();
                obj2.start();
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
            }
        }, 1000);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    private void getData(final String stundent){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<data> msg = baseGetReposFromRemote(stundent,token);
                if (msg != null && !msg.isEmpty()) {
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setData(msg);
                        }
                    });
                }
            }
        }).start();
    }
    public List<data> baseGetReposFromRemote(String stu_id,String TOKEN) {
        String urlStr =
                String.format("https://api-android-camp.bytedance.com/zju/invoke/video?student_id=%s", stu_id);
        MessageListResponse result = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("token", TOKEN);
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                result = new Gson().fromJson(reader, MessageListResponse.class);
                reader.close();
                in.close();
            } else {
                // 错误处理
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.feeds;
    }
}
