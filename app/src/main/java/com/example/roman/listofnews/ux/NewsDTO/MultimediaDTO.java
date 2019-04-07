package com.example.roman.listofnews.ux.NewsDTO;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public final class MultimediaDTO {

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;


    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

/*
//класс для обозначения джинерика
public class MultimediaDTO<T> {
    private T multimedia;
    private List<T> mult = new ArrayList<>();
    private MPointOriginalSizeURLDTO MPoint = new MPointOriginalSizeURLDTO (mult.size() - 1);
    //private MPointOriginalSizeURLDTO Mpoint = (mult.size() - 1);
    //private MPointOriginalSizeURLDTO MPoint = "ss";



    @Nullable
    public MPointOriginalSizeURLDTO getMultimediaPoint (){
        return  MPoint ;

    }*/
/*
    private MPointOriginalSizeURLDTO MUrl;

    @NonNull
    public MPointOriginalSizeURLDTO getMulti() {
        return
    }

*/
}
