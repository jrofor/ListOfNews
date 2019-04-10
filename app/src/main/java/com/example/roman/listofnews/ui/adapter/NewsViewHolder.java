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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.roman.listofnews.R;
import com.example.roman.listofnews.ux.NewsDTO.NewsItemDTO;

import java.util.List;
import java.util.zip.Inflater;

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

    public interface OnItemClickListener {
        void OnItemClick(int position); }

     NewsViewHolder(@NonNull View itemView, RequestManager glideRequestManager) { //, @Nullable OnItemClickListener clickListener
        super(itemView);
        this.itemView = itemView;
        this.imageLoader = glideRequestManager;
        findViews(itemView);
    }

    public static NewsViewHolder create (@NonNull LayoutInflater inflater,
                                         @NonNull ViewGroup parent,
                                         @NonNull RequestManager glideRequestManager) { //, OnItemClickListener clickList
        final View view = inflater.inflate(LayuotItem, parent, false), clickListener;
        return new NewsViewHolder(view, glideRequestManager)  ; //, clickList

    }

    private void findViews (@NonNull View itemView) {
        progressBar = itemView.findViewById(R.id.progress_bar);
        tvSubsection = itemView.findViewById(R.id.tv_subsection);
        tvTitle = itemView.findViewById(R.id.tv_title);
        tvAbstract = itemView.findViewById(R.id.tv_abstract);
        ivMultimedia = itemView.findViewById(R.id.iv_multimedia);
        tvDate = itemView.findViewById(R.id.tv_date);
    }



    //public void bindItem (NewsItemDTO newsDTO) {
    public void bindItem (AllNewsItem newsDTO,
                          @NonNull NewsRecyclerAdapter.OnItemClickListener clickListener) {
        setupUx(newsDTO, clickListener);
        setupUi(newsDTO);
    }

    public void setupUi (AllNewsItem newsDTO) {

        progressBar.setVisibility(View.VISIBLE);

        tvSubsection.setText(newsDTO.getCategory());
        tvTitle.setText(newsDTO.getTitle());
        tvAbstract.setText(newsDTO.getPreviewText());
        tvDate.setText(newsDTO.getUpdatedDate().toString());
        imageLoader.load(newsDTO.getImageUrl()).listener( new RequestListener<Drawable>() {

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
        itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if (clickListener != null && position != RecyclerView.NO_POSITION) {
                clickListener.OnItemClick(allNewsItem);
            }
        });
    }
}
