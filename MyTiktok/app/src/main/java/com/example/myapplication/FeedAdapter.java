package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.model.data;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.VideoViewHolder>{
    private List<data> dat;
    private IOnItemClickListener mItemClickListener;
    public interface IOnItemClickListener {
        void onItemCLick(int position,data datas);
    }
    public void setData(List<data> messageList){
        dat = messageList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new VideoViewHolder(root);
    }
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(dat.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemCLick(position, dat.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dat==null?0:dat.size();
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        mItemClickListener = listener;
    }
    public static class VideoViewHolder extends RecyclerView.ViewHolder{
        private TextView userTV;
        private ImageView user_face;
        private View contentView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView=itemView;
            userTV = itemView.findViewById(R.id.poster);
            user_face=itemView.findViewById(R.id.imag_face);
        }
        public void bind(data dat){
            userTV.setText("Post By: "+dat.getuser_name());

            Glide.with(MyApplication.getContext()).load(dat.getImageUrl()).into(user_face);
        }
        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                contentView.setOnClickListener(listener);
            }
        }
    }
}