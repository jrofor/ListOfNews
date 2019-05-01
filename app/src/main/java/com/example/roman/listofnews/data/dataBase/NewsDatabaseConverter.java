package com.example.roman.listofnews.data.dataBase;

import com.example.roman.listofnews.ui.adapter.AllNewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsDatabaseConverter {

    private void toDatabase(List<AllNewsItem> NewsItems) {

        final List<NewsEntity> newsEntity = new ArrayList<>();

        for (AllNewsItem Item : NewsItems) {
            final NewsEntity nEntity = ConvertItem(Item);
            newsEntity.add(nEntity);
        }
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

    private void fromDatabase(List<NewsEntity> newsEntities ) {

        final List<AllNewsItem> NewsItems = new ArrayList<>();

        for (NewsEntity Entity : newsEntities) {
            final AllNewsItem Item = ConvertEntity(Entity);
            NewsItems.add(Item);
        }
    }

    private static AllNewsItem ConvertEntity(NewsEntity Entity) {

        final String title = Entity.getTitle();
        final String imageUrl = Entity.getImageUrl();
        final String category = Entity.getCategory();
        final String updatedDate = Entity.getUpdatedDate();
        final String previewText = Entity.getPreviewText();
        final String url = Entity.getUrl();

        //final String id = title.concat(url);

        return AllNewsItem.create(title, imageUrl, category, updatedDate, previewText, url);


    }
}
