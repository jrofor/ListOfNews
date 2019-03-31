package com.example.roman.listofnews.ux.NewsDTO;

import com.google.gson.annotations.SerializedName;

public class OriginalSizeDTO {
    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
