package com.example.hw2_1.recycler;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.hw2_1.R;

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
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemCLick(position, mDataset.get(position));
                }
            }
        });
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemLongCLick(position, mDataset.get(position));
                }
                return false;
            }

        });
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

    public void setOnItemClickListener(IOnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void addData(int position, TestData data) {
        mDataset.add(position, data);
        notifyItemInserted(position);
        if (position != mDataset.size()) {
            notifyItemRangeChanged(position, mDataset.size() - position);
        }
    }
    public void removeData(int position) {
        if (null != mDataset && mDataset.size() > position) {
            mDataset.remove(position);
            notifyItemRemoved(position);
            if (position != mDataset.size()) {
                notifyItemRangeChanged(position, mDataset.size() - position);
            }
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_ID;
        private TextView tv_NAME;
        private TextView tv_HOT;
        private View contentView;
        public MyViewHolder(View v) {
            super(v);
            contentView = v;
            tv_ID = v.findViewById(R.id.tv_id);
            tv_NAME = v.findViewById(R.id.tv_name);
            tv_HOT = v.findViewById(R.id.tv_hot);
        }
        public void onBind(int position, TestData data) {
            tv_ID.setText(new StringBuilder().append(position).append(".  ").toString());
            tv_NAME.setText(data.name);
            tv_HOT.setText(data.hot);
            if (position >= 3) {
                tv_ID.setTextColor(Color.parseColor("#000000"));
            } else {
                if(position==0)
                    tv_ID.setTextColor(Color.parseColor("#FF0000"));
                if(position==1)
                    tv_ID.setTextColor(Color.parseColor("#FF8C00"));
                if(position==2)
                    tv_ID.setTextColor(Color.parseColor("#FFD700"));
            }
        }
        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                contentView.setOnClickListener(listener);
            }
        }
        public void setOnLongClickListener(View.OnLongClickListener listener) {
            if (listener != null) {
                contentView.setOnLongClickListener(listener);
            }
        }
    }
}
