package com.example.roman.listofnews.ux;

import android.support.annotation.Nullable;

public class DefaultResponse<T> {
    private T results;

    @Nullable
    public T getData(){
        return results;
    }
}
