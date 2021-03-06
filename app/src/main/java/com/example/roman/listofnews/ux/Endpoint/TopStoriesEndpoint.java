package com.example.roman.listofnews.ux.Endpoint;

import android.support.annotation.NonNull;

import com.example.roman.listofnews.ux.NewsDTO.TopStoriesResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TopStoriesEndpoint {
    @NonNull
    @GET("svc/topstories/v2/{section}.json")
    Single<TopStoriesResponse>
    setSectionName(@Path("section") @NonNull String sectionName);
//https://api.nytimes.com/svc/topstories/v2/home.json?api-key=xTvrIaqsho3wZBAPUcJP7H8KqBl9cSaG
}
