package com.example.roman.listofnews.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class AllNewsItem implements Serializable {

    //public static AllNewsItem create ()

    @NonNull
    private final String title;
    @Nullable
    private final String imageUrl;
    @NonNull
    private final String category;
    @NonNull
    private final String updatedDate;
    @NonNull
    private final String previewText;
    @NonNull
    private final String url;


    private AllNewsItem(@NonNull String title, @Nullable String imageUrl, @NonNull String category, @NonNull String updatedDate, @NonNull String previewText, @NonNull String url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.updatedDate = updatedDate;
        this.previewText = previewText;
        this.url = url;
    }

    public static AllNewsItem create(@NonNull String title,
                                  @Nullable String imageUrl,
                                  @NonNull String category,
                                  @NonNull String updatedDate,
                                  @NonNull String previewText,
                                  @NonNull String url) {
        return new AllNewsItem(title, imageUrl, category, updatedDate, previewText, url);
    }


    @NonNull
    public String getTitle(){return title;}

    @Nullable
    public String getImageUrl() {return imageUrl;}

    @NonNull
    public String getCategory() {return category;}

    @NonNull
    public String getUpdatedDate() {return updatedDate;}

    @NonNull
    public String getPreviewText() {return previewText;}

    @NonNull
    public String getUrl() {return url;}

}
