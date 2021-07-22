package com.example.myapplication;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class myLayoutManager extends LinearLayoutManager {

    private int mDrift;//位移，用来判断方向

    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;
    private Context context;
    private RecyclerView RV;

    public myLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    public myLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
        this.context = context;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mPagerSnapHelper.attachToRecyclerView(view);
        RV = view;
        RV.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    public void setOnViewPagerListener(OnViewPagerListener mOnViewPagerListener) {
        this.mOnViewPagerListener = mOnViewPagerListener;
    }

    @Override
    public void onScrollStateChanged(int state) {
        switch (state){
            case RecyclerView.SCROLL_STATE_IDLE:
                View viewIdle = mPagerSnapHelper.findSnapView(this);
                int positionIdle = getPosition(viewIdle);
                if(mOnViewPagerListener != null && getChildCount() == 1){
                    mOnViewPagerListener.onPageSelected(positionIdle,positionIdle == getItemCount()-1);
                }
                break;
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if(mOnViewPagerListener != null && getChildCount() == 1){
                mOnViewPagerListener.onInitComplete();
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            if(mDrift >= 0){
                if(mOnViewPagerListener != null) mOnViewPagerListener.onPageRelease(true,getPosition(view));

            }else{
                if(mOnViewPagerListener != null) mOnViewPagerListener.onPageRelease(false, getPosition(view));
            }
        }
    };

}
