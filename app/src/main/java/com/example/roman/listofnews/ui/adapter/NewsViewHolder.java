package com.example.roman.listofnews.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.roman.listofnews.R;
import com.example.roman.listofnews.ux.NewsDTO.NewsItemDTO;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private RequestManager imageLoader;
    private ProgressBar progressBar;
    private TextView tvSubsection;
    private TextView tvTitle;
    private TextView tvAbstract;
    private ImageView ivMultimedia;
    private TextView tvDate;

    private static final int LayuotItem = R.layout.item_news;

    public interface OnItemClickListener {
        void OnItemClick(int position); }

    public NewsViewHolder(@NonNull View itemView, RequestManager glideRequestManager, @Nullable OnItemClickListener clickListener) {
        super(itemView);
        this.imageLoader = glideRequestManager;
        findViews(itemView);
        itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if (clickListener != null && position != RecyclerView.NO_POSITION) {
                clickListener.OnItemClick(getAdapterPosition());
            }
        });

    }

    public static NewsViewHolder create (@NonNull ViewGroup parent, RequestManager glideRequestManager, OnItemClickListener clickList ) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(LayuotItem, parent, false ), clickListener;
        return new NewsViewHolder(view, glideRequestManager, clickList )  ;

    }

    private void findViews (@NonNull View itemView) {
        progressBar = itemView.findViewById(R.id.progress_bar);
        tvSubsection = itemView.findViewById(R.id.tv_subsection);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvAbstract = itemView.findViewById(R.id.tv_abstract);
        ivMultimedia = itemView.findViewById(R.id.iv_multimedia);
        tvDate = itemView.findViewById(R.id.tv_date);
    }

    public void bindItem (NewsItemDTO newsDTO) {

        progressBar.setVisibility(View.VISIBLE);

        tvSubsection.setText(newsDTO.getSubsection());
        tvTitle.setText(newsDTO.getTitle());
        tvAbstract.setText(newsDTO.getAbstract());
        tvDate.setText(newsDTO.getUpdatedDate().toString());
        imageLoader.load(newsDTO.getMultimediaURL()).listener( new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .thumbnail(0.3f)
                .into(ivMultimedia);
    }

}
