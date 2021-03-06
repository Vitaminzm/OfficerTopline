package com.education.officertopline.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.interfaces.MyItemClickListener;
import com.education.officertopline.interfaces.MyItemLongClickListener;
import com.education.officertopline.log.LogUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianghejie on 15/11/26.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolders> {
    public ArrayList<String> datas = null;
    private static MyItemLongClickListener mItemLongClickListener;
    private static MyItemClickListener mItemClickListener;

    public MyAdapter(ArrayList<String> datas, MyItemClickListener mItemClickListener) {
        this.datas = datas;
        this.mItemClickListener = mItemClickListener;
    }
    public void remove(int position, int position2) {
        if(position < 0  || position >= datas.size()){
            return;
        }
        datas.remove(position);
        //notifyDataSetChanged();
        notifyItemRemoved(position + 1 + position2);
    }
    @Override
    public ViewHolders onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        return new ViewHolders(view, mItemClickListener);
    }
    @Override
    public void onBindViewHolder(ViewHolders viewHolder, int position) {
        viewHolder.mTextView.setText(datas.get(position));
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    public static class ViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        @Bind(R.id.text) TextView mTextView;
        private MyItemClickListener mListener;
        public ViewHolders(View view, MyItemClickListener listener){
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            ButterKnife.bind(this, view);
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v, getLayoutPosition()-1);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mItemLongClickListener != null){
                mItemLongClickListener.onLongItemClick(v, getLayoutPosition()-1);
            }
            return true;
        }
    }
}
