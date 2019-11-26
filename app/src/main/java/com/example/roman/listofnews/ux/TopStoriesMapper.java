package com.example.roman.listofnews.ux;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;
import com.example.roman.listofnews.ux.NewsDTO.MultimediaDTO;
import com.example.roman.listofnews.ux.NewsDTO.NewsItemDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TopStoriesMapper {

    private static final String MULTIMEDIA_TYPE_IMAGE = "image";

    private TopStoriesMapper() {
        throw new AssertionError("Must be no instance");
    }

    public static List<AllNewsItem> map(@NonNull List<NewsItemDTO> dtos) {

        final List<AllNewsItem> items = new ArrayList<>();

        for (NewsItemDTO dto : dtos) {
            final AllNewsItem newsItem = mapItem(dto);
            items.add(newsItem);
        }
        return Collections.unmodifiableList(items);
    }

    private static AllNewsItem mapItem(@NonNull NewsItemDTO dto) {
        final String title = dto.getTitle();

        final List<MultimediaDTO> multimedia = dto.getMultimedia();
        final String imageUrl = mapImage(multimedia);

        final String category = dto.getSubsection();
        final String updatedDate = dto.getUpdatedDate().toString();
        final String preview = dto.getAbstract();
        final String url = dto.getUrl();

        return AllNewsItem.create(title,
                imageUrl,
                category,
                updatedDate,
                preview,
                url);
    }

    @Nullable
    private static String mapImage(@Nullable List<MultimediaDTO> multimedias) {

        if (multimedias == null || multimedias.isEmpty()) {
            return null;
        }

        final int imageImMaximumQuantityIndex = multimedias.size() - 1;
        final MultimediaDTO multimedia = multimedias.get(imageImMaximumQuantityIndex);

        if (!multimedia.getType().equals(MULTIMEDIA_TYPE_IMAGE)) {
            return null;
        }

        return multimedia.getUrl();
    }
}
