package com.example.roman.listofnews.ui.adapter.spinner;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.roman.listofnews.R;
import com.example.roman.listofnews.data.Storage;
import com.example.roman.listofnews.ux.NewsCategory;


public final class CategoriesSpinnerAdapter extends ArrayAdapter<NewsCategory> {
    private static final int DEFAULT_SPINNER_ITEM = R.layout.categories_spinner_item;
    private static final int DEFAULT_DROP_DOWN_ITEM = R.layout.categories_drop_down_spinner_item;

    private static final String TAG_DROPDOWN = "DROPDOWN";
    private static final String TAG_NON_DROPDOWN = "NON_DROPDOWN";

    private NewsCategory selectedCategory = NewsCategory.findCategoryByPosition(Storage.getSelectedPositionCategory(getContext()));
    private final LayoutInflater inflater;
    private OnCategorySelectedListener categorySelectedListener;

    private CategoriesSpinnerAdapter(Context context, int resource, NewsCategory[] objects) {
        super(context, resource, objects); //
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    public static CategoriesSpinnerAdapter createDefault(@NonNull Context context,
                                                         @NonNull NewsCategory[] categories) {
        return new CategoriesSpinnerAdapter(context, DEFAULT_SPINNER_ITEM, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = checkConvertView(convertView, parent, TAG_NON_DROPDOWN, DEFAULT_SPINNER_ITEM);
        final NewsCategory item = getItem(position);
        if (item == null) {
            return convertView;
        }

        final TextView tvTitle = convertView.findViewById(R.id.tv_s_title);
        tvTitle.setText(item.displayValue());

        return convertView;
    }

    @Override
    public View getDropDownView(int position,
                                View convertView,
                                ViewGroup parent) {
        convertView = checkConvertView(convertView, parent, TAG_DROPDOWN, DEFAULT_DROP_DOWN_ITEM);
        final NewsCategory item = getItem(position);
        if (item == null) {
            return convertView;
        }

        final TextView tvDropDown = convertView.findViewById(R.id.tv_drop_down);
        tvDropDown.setText(item.displayValue());

        return convertView;
    }

    @NonNull
    private View checkConvertView(@NonNull View convertView,
                                  @NonNull ViewGroup parent,
                                  @NonNull String tag,
                                  @LayoutRes int layoutId) {
        if (convertView == null || !convertView.getTag().toString().equals(tag)) {
            convertView = inflater.inflate(layoutId, parent, false);
            convertView.setTag(tag);
        }
        return convertView;
    }

    public void setOnCategorySelectedListener(@Nullable OnCategorySelectedListener categorySelectedListener,
                                              @NonNull Spinner spinner) {
        this.categorySelectedListener = categorySelectedListener;
        if (categorySelectedListener == null) {
            spinner.setOnItemClickListener(null);
            return;
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //saving position to open in next time
                Storage.setSelectedPositionCategory(getContext(), position);
                final NewsCategory item = getItem(position);
                CategoriesSpinnerAdapter.this.selectedCategory = item;
                categorySelectedListener.onSelected(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @NonNull
    public NewsCategory getSelectedCategory() {
        return selectedCategory;
    }


    public interface OnCategorySelectedListener {
        void onSelected(@NonNull NewsCategory category);
    }
}
