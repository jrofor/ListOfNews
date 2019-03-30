package com.example.roman.listofnews.ux;

import android.support.annotation.NonNull;

import com.example.roman.listofnews.ux.Endpoint.HomeTopStoriesEndpoint;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
    private static final String URL = "https://api.nytimes.com/";
    private static final String API_KEY = "xTvrIaqsho3wZBAPUcJP7H8KqBl9cSaG";

    private static final int TIMEOUT_IN_SECONDS = 2;
    private static RestApi myRestApi;

    private HomeTopStoriesEndpoint TSEndpoint;

    public static synchronized RestApi getInstanse() {
        if (myRestApi == null) {
            myRestApi = new RestApi();
        }
        return myRestApi;
    }

    private RestApi() {
        final OkHttpClient httpClient = buildOkHttpClient();
        final Retrofit retrofit = buildRetrofitClient(httpClient);

        TSEndpoint = retrofit.create(HomeTopStoriesEndpoint.class);
    }

    @NonNull
    private Retrofit buildRetrofitClient(@NonNull OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    private OkHttpClient buildOkHttpClient() {
        final HttpLoggingInterceptor networkLogInterceptor = new HttpLoggingInterceptor();
        networkLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);


        return new OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor.create(API_KEY))
                .addInterceptor(networkLogInterceptor)
                .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

}
