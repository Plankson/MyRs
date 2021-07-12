package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt1=findViewById(R.id.btn1);
        final TextView tv1=findViewById(R.id.textView);
        bt1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tv1.setText("yyds!");
                Log.d("button", "Click");
            }
        });
        bindViews();
        EditText et=findViewById(R.id.edit_text);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("edit_txt", "modifing");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RadioGroup rb=findViewById(R.id.RdGroup);
        final RadioButton cs=findViewById(R.id.CS);
        final RadioButton is=findViewById(R.id.IS);
        final RadioButton ieee=findViewById(R.id.IEEE);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.CS:Log.d("rb", "CS");break;
                    case R.id.IEEE:Log.d("rb", "IEEE");break;
                    case R.id.IS:Log.d("rb", "IS");break;
                }
            }
        });
    }
    private void bindViews(){
        SeekBar sb=findViewById(R.id.seekbar);
        final TextView txt=findViewById(R.id.textsb);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int num, boolean fromUser) {
                txt.setText("Now Process:"+num+"%");
                Log.d("seekBar", "moving to"+num+"%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
