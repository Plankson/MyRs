package com.example.hw2_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class PracticeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        Log.i("PracticeActivity", "onCreate");
    }
    protected void onStart(){
        super.onStart();
        Log.i("PracticeActivity", "onStart: ");

    }

    protected void onResume(){
        super.onResume();
        Log.i("PracticeActivity", "onResume: ");

    }
    protected void onPause(){
        super.onPause();
        Log.i("PracticeActivity", "onPause: ");

    }
    protected void onStop(){
        super.onStop();
        Log.i("PracticeActivity", "onStop: ");

    }
    protected void onDestory(){
        super.onDestroy();
        Log.i("PracticeActivity", "onDestory: ");

    }
}
