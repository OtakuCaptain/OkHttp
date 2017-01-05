package com.chen.okhttp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    private LayoutInflater mInflater;
    private Context mContext;
    //    private List<NewsMenu> mData;
    private ArrayList<NewsDetail> mData;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public NewsAdapter(Context context, ArrayList<NewsDetail> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.news_item_recview, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(view);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsDetail newsModel = mData.get(position + 1);
        holder.mTitle.setText(newsModel.getTitle());
        holder.mDescription.setText(newsModel.getDigest());
        Glide.with(mContext).load(newsModel.getImgsrc()).into(holder.mImageView);
        setUpItemEvent(holder);
    }

    @Override
    public int getItemCount() {
        return mData.size() - 1;
    }

    protected void setUpItemEvent(final NewsViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, layoutPosition);
                }
            });
        }
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTitle;
        public TextView mDescription;

        NewsViewHolder(View itemView) {
            super(itemView);
//            mTitle= (TextView) itemView.findViewById(R.id.id_tv);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_small);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mDescription = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }

}
