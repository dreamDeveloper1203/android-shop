package store.technologycenter.android.models;

import store.technologycenter.android.configurations.TechCenterConfiguration;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_IMAGE_URL = "imageUrl";
    public static final String COL_BASE_PRICE = "basePrice";
    public static final String COL_VIEW_PRICE = "viewPrice";
    public static final String COL_HAS_DISCOUNT = "hasDiscount";
    public static final String COL_IS_NEW = "isNew";
    public static final String COL_IS_POPULAR = "isPopular";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_URL = "url";
    public static final String COL_CATEGORY_NAME = "categoryName";
    public static final String COL_IN_CART = "inCart";
    public static final String COL_IN_FAVORITES = "inFavorites";
    public static final String COL_IN_VIEWED = "isViewed";
    public static final String COL_SEARCH_NAME = "searchName";
    public static final String COL_IS_SEARCHED = "isSearched";
    public static final String COL_SEARCHED_AT = "searchedAt";
    public static final String COL_VIEWED_AT = "viewedAt";
    public static final String COL_FAVORITE_AT = "viewedAt";
    public static final String COL_ADDED_TO_CART_AT = "addedToCartAt";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_COMMENT = "comment";
    public static final String COL_IN_STOCK = "inStock";

    private @PrimaryKey String id;
    private String name;
    private String imageUrl;

    private double basePrice;
    private String viewPrice;
    private boolean hasDiscount;
    private boolean isNew;
    private boolean isPopular;
    private String description;
    private String url;
    private String categoryName;
    private boolean inCart;
    private String comment;
    private int quantity;
    private Date addedToCartAt;
    private boolean inFavorites;
    private Date favoriteAt;
    private boolean isViewed;
    private Date viewedAt;
    private boolean isSearched;
    private Date searchedAt;
    private String searchName;
    private int inStock;

    @LinkingObjects("items")
    private final RealmResults<Category> category = null;

    public Item(){}

    public Item(
            String id,
            String name,
            String imageUrl,
            double basePrice,
            String viewPrice,
            boolean hasDiscount,
            boolean isNew,
            boolean isPopular,
            String description,
            String url,
            String categoryName,
            boolean inCart,
            String comment,
            int quantity,
            Date addedToCartAt,
            boolean inFavorites,
            Date favoriteAt,
            boolean isViewed,
            Date viewedAt,
            boolean isSearched,
            Date searchedAt,
            String searchName,
            int inStock
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.basePrice = basePrice;
        this.viewPrice = viewPrice;
        this.hasDiscount = hasDiscount;
        this.isNew = isNew;
        this.isPopular = isPopular;
        this.description = description;
        this.url = url;
        this.categoryName = categoryName;
        this.inCart = inCart;
        this.comment = comment;
        this.quantity = quantity;
        this.addedToCartAt = addedToCartAt;
        this.inFavorites = inFavorites;
        this.favoriteAt = favoriteAt;
        this.isViewed = isViewed;
        this.viewedAt = viewedAt;
        this.isSearched = isSearched;
        this.searchedAt = searchedAt;
        this.searchName = searchName;
        this.inStock = inStock;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public double getBasePrice() {
        return basePrice;
    }

    public String getBasePriceString() {
        return TechCenterConfiguration.BLRFormat(this.getBasePrice());
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }


    public String getSubtotalString() {
        return TechCenterConfiguration.BLRFormat(this.getSubtotal());
    }

    public double getSubtotal() {
        return this.getQuantity() * this.getBasePrice();
    }



    public String getViewPrice() {
        return viewPrice;
    }

    public void setViewPrice(String viewPrice) {
        this.viewPrice = viewPrice;
    }

    public boolean isHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(boolean hasDiscount) {
        this.hasDiscount = hasDiscount;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public boolean isInFavorites() {
        return inFavorites;
    }

    public void setInFavorites(boolean inFavorites) {
        this.inFavorites = inFavorites;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public boolean isSearched() {
        return isSearched;
    }

    public void setSearched(boolean searched) {
        isSearched = searched;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getAddedToCartAt() {
        return addedToCartAt;
    }

    public void setAddedToCartAt(Date addedToCartAt) {
        this.addedToCartAt = addedToCartAt;
    }

    public Date getFavoriteAt() {
        return favoriteAt;
    }

    public void setFavoriteAt(Date favoriteAt) {
        this.favoriteAt = favoriteAt;
    }

    public Date getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Date viewedAt) {
        this.viewedAt = viewedAt;
    }

    public Date getSearchedAt() {
        return searchedAt;
    }

    public void setSearchedAt(Date searchedAt) {
        this.searchedAt = searchedAt;
    }
}
