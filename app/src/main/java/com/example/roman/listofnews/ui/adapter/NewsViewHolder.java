package com.example.roman.listofnews.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.roman.listofnews.R;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private RequestManager imageLoader;
    private ProgressBar progressBar;
    private TextView tvSubsection;
    private TextView tvTitle;
    private TextView tvAbstract;
    private ImageView ivMultimedia;
    private TextView tvDate;
    private final View itemView;

    private static final int LayuotItem = R.layout.item_news;
    private static final String TAG = "myLogs";
    private static String createId  ="";

    private NewsViewHolder(@NonNull View itemView, RequestManager glideRequestManager) {
        super(itemView);
        this.itemView = itemView;
        this.imageLoader = glideRequestManager;
        findViews(itemView);
    }

    public static NewsViewHolder create (@NonNull LayoutInflater inflater,
                                         @NonNull ViewGroup parent,
                                         @NonNull RequestManager glideRequestManager) {
        final View view = inflater.inflate(LayuotItem, parent, false);
        return new NewsViewHolder(view, glideRequestManager)  ;

    }

    private void findViews (@NonNull View itemView) {
        progressBar = itemView.findViewById(R.id.progress_bar);
        tvSubsection = itemView.findViewById(R.id.tv_subsection);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvAbstract = itemView.findViewById(R.id.tv_abstract);
        ivMultimedia = itemView.findViewById(R.id.iv_multimedia);
        tvDate = itemView.findViewById(R.id.tv_date);
    }

    public void bindItem (AllNewsItem allNewsItem,
                          @NonNull NewsRecyclerAdapter.OnItemClickListener clickListener) {
        setupUi(allNewsItem);
        setupUx(allNewsItem, clickListener);
    }

    private void setupUi (AllNewsItem allNewsItem) {

        progressBar.setVisibility(View.VISIBLE);

        tvSubsection.setText(allNewsItem.getCategory());
        tvTitle.setText(allNewsItem.getTitle());
        tvAbstract.setText(allNewsItem.getPreviewText());
        tvDate.setText(allNewsItem.getUpdatedDate());
        imageLoader.load(allNewsItem.getImageUrl()).listener( new RequestListener<Drawable>() {

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
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivMultimedia);

    }

    private void setupUx(@NonNull AllNewsItem allNewsItem, NewsRecyclerAdapter.OnItemClickListener clickListener) {

        itemView.setOnClickListener((View view) -> {
            int position = getAdapterPosition();
            if (clickListener != null && position != RecyclerView.NO_POSITION) {
                // adding create id for database Entity
                createId = allNewsItem.getTitle().concat(allNewsItem.getUrl()) ;
                clickListener.OnItemClick(createId);
                Log.d(TAG, createId);
            }
        });
    }
}
