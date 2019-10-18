package com.example.roman.listofnews.ui.adapter.pagedListAdapter;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.List;

public class MyPositionalDataSource extends PositionalDataSource<AllNewsItem> {

    private final EmployeeStorage employeeStorage;

    private static final String TAG = "myLogs";

    private int loadRangeStart;

    public MyPositionalDataSource(EmployeeStorage employeeStorage) {
        this.employeeStorage = employeeStorage;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<AllNewsItem> callback) {
        Log.d(TAG, "loadInitial, requestedStartPosition = " + params.requestedStartPosition +
                ", requestedLoadSize = " + params.requestedLoadSize);
        List<AllNewsItem> result = employeeStorage.getData(params.requestedStartPosition, params.requestedLoadSize);
        if (params.placeholdersEnabled) {
            callback.onResult(result, params.requestedStartPosition, employeeStorage.outCount());
        } else {
            callback.onResult(result, params.requestedStartPosition); }
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<AllNewsItem> callback) {
        Log.d(TAG, "loadRange, startPosition = " + params.startPosition + ", loadSize = " + params.loadSize);
        loadRangeStart = params.startPosition;
        List<AllNewsItem> result = employeeStorage.getData(params.startPosition, params.loadSize);
        callback.onResult(result);
    }

    public int getLoadRangeStart() {
        return loadRangeStart;
    }
}

