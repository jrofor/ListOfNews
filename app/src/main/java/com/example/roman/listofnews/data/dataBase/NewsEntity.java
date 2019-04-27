package com.example.roman.listofnews.data.dataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

@Entity (primaryKeys = {"title", "url"})

public class NewsEntity {
    public NewsEntity() {
        //id = String.format(title, url);
    }

    @Ignore
    public NewsEntity(@NonNull String title, @Nullable String imageUrl, @NonNull String category,
                      @NonNull String updatedDate, @NonNull String previewText, @NonNull String url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.updatedDate = updatedDate;
        this.previewText = previewText;
        this.url = url;

    }



    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "updatedDate")
    private String updatedDate;
    @ColumnInfo(name = "previewText")
    private String previewText;

    /*@NonNull
    public String getId() {
        return id;
    }
    public void setId(@NonNull String id) { this.id = id; }*/

    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    @NonNull
    public String getTitle() {
        return title;
    }
    @NonNull
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getPreviewText() {
        return previewText;
    }
    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    @NonNull
    public String getUrl() {
        return url;
    }
    @NonNull
    public void setUrl(String previewText) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "News{" +
                //"id='" + id + '\'' +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category='" + category + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                ", previewText='" + previewText + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
