package com.example.roman.listofnews.ui.adapter.pagedListAdapter;

import android.arch.paging.DataSource;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

public class NewsSourceFactory extends DataSource.Factory<Integer, AllNewsItem> {

    private final EmployeeStorage employeeStorage;

    public NewsSourceFactory(EmployeeStorage employeeStorage) {
        this.employeeStorage = employeeStorage;
    }

    @Override
    public DataSource<Integer, AllNewsItem> create() {
        return new MyPositionalDataSource(employeeStorage);
    }
}
