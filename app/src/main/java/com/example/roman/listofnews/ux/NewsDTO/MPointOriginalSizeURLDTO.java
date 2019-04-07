package com.example.roman.listofnews.ux.NewsDTO;

import com.google.gson.annotations.SerializedName;

public class MPointOriginalSizeURLDTO {
    /*private Integer point;

    public MPointOriginalSizeURLDTO(Integer point) {
        this.point = point;
    }*/
    /*
    //private String s;
    public MPointOriginalSizeURLDTO <s>{
        //this.s = s;
    }
*/

    @SerializedName("url")
    private String url;

    public String getUrl() {
        return url;
    }
}
