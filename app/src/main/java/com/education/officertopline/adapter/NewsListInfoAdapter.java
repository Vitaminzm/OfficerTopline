package com.education.officertopline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.education.officertopline.R;
import com.education.officertopline.app.ConstantData;
import com.education.officertopline.entity.ToplineNewsListInfo;
import com.education.officertopline.http.ImageLoaderUtils;
import com.education.officertopline.interfaces.MyItemClickListener;
import com.education.officertopline.interfaces.MyItemLongClickListener;
import com.education.officertopline.utils.StringUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jianghejie on 15/11/26.
 */
public class NewsListInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<ToplineNewsListInfo> datas = null;
    public Context mContext = null;
    private static MyItemLongClickListener mItemLongClickListener;
    private static MyItemClickListener mItemClickListener;

    public static final int NO_PIC = 1;
    public static final int SINGLE_PIC = 2;
    public static final int MORE_PIC = 3;
    public NewsListInfoAdapter(Context context, ArrayList<ToplineNewsListInfo> datas, MyItemClickListener mItemClickListener) {
        this.datas = datas;
        this.mItemClickListener = mItemClickListener;
        mContext = context;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == SINGLE_PIC){
            return new ViewHolderSinglepic(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_single_pic,viewGroup,false), mItemClickListener);
        }else if(viewType == MORE_PIC){
            return new ViewHolderMorepic(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_more_pic,viewGroup,false), mItemClickListener);
        }else{
            return new ViewHolderNopic(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_no_pic,viewGroup,false), mItemClickListener);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderMorepic){
            ((ViewHolderMorepic)viewHolder).text_title.setText(datas.get(position).getTitle());
            if(!StringUtil.isEmpty(datas.get(position).getCommentNum())){
                ((ViewHolderMorepic)viewHolder).text_command_count.setText(datas.get(position).getCommentNum());
            }
            ((ViewHolderMorepic)viewHolder).text_time.setText(datas.get(position).getFirstTime());
            if(datas.get(position).getPic()!= null){
                final String[] codes = datas.get(position).getPic().split("\\|");
                if(codes.length>0){
                    ImageLoaderUtils.getInstance().loadCommonImage(mContext, ((ViewHolderMorepic) viewHolder).image_one, codes[0]);
                }
                if(codes.length>1){
                    ImageLoaderUtils.getInstance().loadCommonImage(mContext, ((ViewHolderMorepic) viewHolder).image_two, codes[1]);
                }
                if(codes.length>2){
                    ImageLoaderUtils.getInstance().loadCommonImage(mContext, ((ViewHolderMorepic) viewHolder).image_three, codes[2]);
                }
            }

        }else if (viewHolder instanceof ViewHolderSinglepic){
            ((ViewHolderSinglepic)viewHolder).text_title.setText(datas.get(position).getTitle());
            if(!StringUtil.isEmpty(datas.get(position).getCommentNum())){
                ((ViewHolderSinglepic)viewHolder).text_command_count.setText(datas.get(position).getCommentNum());
            }
            ((ViewHolderSinglepic)viewHolder).text_time.setText(datas.get(position).getFirstTime());
            ImageLoaderUtils.getInstance().loadCommonImage(mContext, ((ViewHolderSinglepic) viewHolder).image_one, datas.get(position).getPic());
        }else{
            ((ViewHolderNopic)viewHolder).text_title.setText(datas.get(position).getTitle());
            if(!StringUtil.isEmpty(datas.get(position).getCommentNum())) {
                ((ViewHolderNopic) viewHolder).text_command_count.setText(datas.get(position).getCommentNum());
            }
            ((ViewHolderNopic)viewHolder).text_time.setText(datas.get(position).getFirstTime());
        }
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(ConstantData.NEWS_TYPE_SINGLE_PIC.equals(datas.get(position).getNewsType())){
            return SINGLE_PIC;
        }else if(ConstantData.NEWS_TYPE_MORE_PIC.equals(datas.get(position).getNewsType())){
            return MORE_PIC;
        }else{
            return NO_PIC;
        }
    }

    public static class ViewHolderNopic extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        @Bind(R.id.text_title) TextView text_title;
        @Bind(R.id.text_command_count) TextView text_command_count;
        @Bind(R.id.text_time) TextView text_time;
        @Bind(R.id.imageview_type) ImageView imageview_type;
        @Bind(R.id.imageview_close) ImageView imageview_close;
        private MyItemClickListener mListener;
        public ViewHolderNopic(View view, MyItemClickListener listener){
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            ButterKnife.bind(this, view);//framgent
            mListener = listener;
            imageview_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemInClick(v, getLayoutPosition() - 1);
                    }
                }
            });
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

    public static class ViewHolderSinglepic extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        @Bind(R.id.text_title) TextView text_title;
        @Bind(R.id.text_command_count) TextView text_command_count;
        @Bind(R.id.text_time) TextView text_time;
        @Bind(R.id.imageview_type) ImageView imageview_type;
        @Bind(R.id.imageview_close) ImageView imageview_close;
        @Bind(R.id.image_one) ImageView image_one;
        private MyItemClickListener mListener;
        public ViewHolderSinglepic(View view, MyItemClickListener listener){
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            ButterKnife.bind(this, view);//framgent
            mListener = listener;
            imageview_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemInClick(v, getLayoutPosition()-1);
                    }
                }
            });
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

    public static class ViewHolderMorepic extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        @Bind(R.id.text_title) TextView text_title;
        @Bind(R.id.text_command_count) TextView text_command_count;
        @Bind(R.id.text_time) TextView text_time;
        @Bind(R.id.imageview_type) ImageView imageview_type;
        @Bind(R.id.imageview_close) ImageView imageview_close;
        @Bind(R.id.image_one) ImageView image_one;
        @Bind(R.id.image_two) ImageView image_two;
        @Bind(R.id.image_three) ImageView image_three;
        private MyItemClickListener mListener;
        public ViewHolderMorepic(View view, MyItemClickListener listener){
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            ButterKnife.bind(this, view);//framgent
            mListener = listener;
            imageview_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemInClick(v, getLayoutPosition()-1);
                    }
                }
            });
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
