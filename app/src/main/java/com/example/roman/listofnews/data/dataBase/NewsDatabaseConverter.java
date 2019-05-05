package com.example.roman.listofnews.data.dataBase;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsDatabaseConverter {

    public List<NewsEntity> toDatabase(List<AllNewsItem> NewsItems) {

        final List<NewsEntity> newsEntities = new ArrayList<>();

        for (AllNewsItem Item : NewsItems) {
            final NewsEntity nEntity = ConvertItem(Item);
            newsEntities.add(nEntity);
        }
        return Collections.unmodifiableList(newsEntities);
    }

    private static NewsEntity ConvertItem(AllNewsItem Item) {

        final String title = Item.getTitle();
        final String imageUrl = Item.getImageUrl();
        final String category = Item.getCategory();
        final String updatedDate = Item.getUpdatedDate();
        final String previewText = Item.getPreviewText();
        final String url = Item.getUrl();

        final String id = title.concat(url);

        return NewsEntity.create(id,title,
                imageUrl,
                category,
                updatedDate,
                previewText,
                url);
    }

    public List<AllNewsItem> fromDatabase(List<NewsEntity> newsEntities ) {

        final List<AllNewsItem> NewsItems = new ArrayList<>();

        for (NewsEntity Entity : newsEntities) {
            final AllNewsItem Item = ConvertEntity(Entity);
            NewsItems.add(Item);
        }
        return Collections.unmodifiableList(NewsItems);
    }

    private static AllNewsItem ConvertEntity(NewsEntity Entity) {

        final String title = Entity.getTitle();
        final String imageUrl = Entity.getImageUrl();
        final String category = Entity.getCategory();
        final String updatedDate = Entity.getUpdatedDate();
        final String previewText = Entity.getPreviewText();
        final String url = Entity.getUrl();

        return AllNewsItem.create(title, imageUrl, category, updatedDate, previewText, url);
    }
}
