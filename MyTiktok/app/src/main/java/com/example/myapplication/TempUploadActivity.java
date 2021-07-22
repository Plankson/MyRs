package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TempUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_upload);
        Button bendi=findViewById(R.id.bendi);
        Button paishe=findViewById(R.id.paishe);
        bendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TempUploadActivity.this,postvideo.class);
                startActivity(intent);
            }
        });
        paishe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TempUploadActivity.this,CustomCameraActivity.class);
                startActivity(intent);
            }
        });
    }
}
