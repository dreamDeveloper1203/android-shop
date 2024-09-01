package store.technologycenter.android.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Banner extends RealmObject {

    public static final String COL_ID = "id";
    public static final String COL_IMAGE_URL = "imageUrl";
    public static final String COL_SORT_ORDER = "sortOrder";
    private @PrimaryKey
    String id;
    private String imageUrl;
    private int sortOrder;
    public Banner(){}


    public Banner(String id, String imageUrl, int sortOrder) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
