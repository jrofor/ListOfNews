package com.example.roman.listofnews.ux.NewsDTO;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopStoriesResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private List<NewsItemDTO> news;

    public String getStatus() { return status;}

    public List<NewsItemDTO> getNews() {
        return news;
    }
}
