package store.technologycenter.android.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {

    public static final String COL_SLUG = "slug";
    public static final String COL_IMAGE_URL = "imageUrl";
    public static final String COL_NAME = "name";
    public static final String COL_SORT_ORDER = "sortOrder";
    public static final String COL_ITEMS = "items";

    private @PrimaryKey String slug;
    private String imageUrl;
    private String name;
    private int sortOrder;
    private RealmList<Item> items;

    public Category(){}

    public Category(String slug, String imageUrl, String name, int sortOrder, RealmList<Item> items) {
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.name = name;
        this.sortOrder = sortOrder;
        this.items = items;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(RealmList<Item> items) {
        this.items = items;
    }
}
