package com.example.hw2_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity", "onCreate: ");
        Button BTN0=findViewById(R.id.btn0);
        BTN0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Notice!", Toast.LENGTH_SHORT).show();
            }
        });
        Button BTN1=findViewById(R.id.btn1);
        BTN1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent im = new Intent();
                im.setAction(Intent.ACTION_DIAL);
                startActivity(im);
            }
        });
        Button BTN2=findViewById(R.id.btn2);
        BTN2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent im = new Intent(Intent.ACTION_VIEW);
                im.setData(Uri.parse("http://www.baidu.com"));
                startActivity(im);
            }
        });
        Button BTN3=findViewById(R.id.btn3);
        BTN3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent im=new Intent(MainActivity.this,PracticeActivity.class);
                startActivity(im);
            }
        });
        Button BTN4=findViewById(R.id.btn4);
        BTN4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent im=new Intent(MainActivity.this,RecyclerViewActivity.class);
                startActivity(im);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity", "onDestory: ");
    }

    protected void onStart(){
        super.onStart();
        Log.i("MainActivity", "onStart: ");

    }
    protected void onResume(){
        super.onResume();
        Log.i("MainActivity", "onResume: ");

    }
    protected void onPause(){
        super.onPause();
        Log.i("MainActivity", "onPause: ");

    }
    protected void onStop(){
        super.onStop();
        Log.i("MainActivity", "onStop: ");
    }
}
