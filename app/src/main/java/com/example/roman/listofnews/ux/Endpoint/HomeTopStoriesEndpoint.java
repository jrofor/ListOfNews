package com.example.roman.listofnews.ux.Endpoint;

import android.support.annotation.NonNull;

import com.example.roman.listofnews.ux.NewsDTO.NewsItemDTO;
import com.example.roman.listofnews.ux.DefaultResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HomeTopStoriesEndpoint {
    @NonNull
    @GET("svc/topstories/v2/{section}.json")
    Call<DefaultResponse<List<NewsItemDTO>>>
    setSectionName  (@Path ("section") String sectionName);
//https://api.nytimes.com/svc/topstories/v2/home.json?api-key=xTvrIaqsho3wZBAPUcJP7H8KqBl9cSaG
}
