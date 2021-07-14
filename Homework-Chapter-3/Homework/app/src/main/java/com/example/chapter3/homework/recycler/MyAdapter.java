package com.example.chapter3.homework.recycler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chapter3.homework.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TestData> mDataset = new ArrayList<>();
    private IOnItemClickListener mItemClickListener;

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.onBind(position, mDataset.get(position));
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public interface IOnItemClickListener {
       void onItemCLick(int position, TestData data);
        void onItemLongCLick(int position, TestData data);
    }

    public MyAdapter(List<TestData> myDataset) {
        mDataset.addAll(myDataset);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView tv_ID;
        private TextView tv_NAME;
        private TextView tv_MESS;
        private View contentView;
        public MyViewHolder(View v) {
            super(v);
            contentView = v;
            tv_ID = v.findViewById(R.id.tv_id);
            tv_NAME = v.findViewById(R.id.tv_name);
            tv_MESS = v.findViewById(R.id.tv_mess);
        }
        public void onBind(int position, TestData data) {
            if(position%4==0)
                tv_ID.setImageResource(R.mipmap.akali);
            if(position%4==1)
                tv_ID.setImageResource(R.mipmap.camile);
            if(position%4==2)
                tv_ID.setImageResource(R.mipmap.viego);
            if(position%4==3)
                tv_ID.setImageResource(R.mipmap.gwen);
            tv_NAME.setText(data.name);
            tv_MESS.setText(data.Mess);
        }
    }
}
