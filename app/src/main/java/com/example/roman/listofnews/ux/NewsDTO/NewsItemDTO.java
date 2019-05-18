package com.example.roman.listofnews.ux.NewsDTO;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewsItemDTO{

    @SerializedName("subsection")
    @Expose
    private String subsection;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("abstract")
    @Expose
    private String _abstract;

    @SerializedName("updated_date")
    @Expose
    private Date updatedDate;

    @SerializedName("multimedia")
    @Expose
    private List<MultimediaDTO> multimedia ;

    @SerializedName("url")
    private String url;


    public String getSubsection() {
        return subsection;
    }

    public void setSubsection(String subsection) {
        this.subsection = subsection;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return _abstract;
    }

    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }


    public List<MultimediaDTO> getMultimedia() {
        return multimedia;
    }

    public String getUrl() {
        return url;
    }

 }
