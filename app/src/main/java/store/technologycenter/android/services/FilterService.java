package store.technologycenter.android.services;

import store.technologycenter.android.models.Category;
import store.technologycenter.android.models.Item;

import java.util.HashSet;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class FilterService {

    public static boolean isOn = false;
    public static double startPrice = 0.0;
    public static double upToPrice = 0.0;
    public static boolean hasDiscount = false;
    public static boolean isNew = false;
    public static boolean isPopular = false;
    public static HashSet<String> categories = new HashSet<>();

    public static void reset(){
        isOn = false;
        startPrice = 0;
        upToPrice = 0;
        hasDiscount = false;
        isNew = false;
        isPopular = false;
        categories = new HashSet<>();
    }
    public static boolean isDefault(){
        return !hasDiscount
                && !isNew
                && !isPopular
                && startPrice == 0
                && upToPrice == 0
                && categories.size() == 0;
    }

    public static RealmQuery<Item> filteredItems(Realm realm){
        RealmQuery<Item> itemRealmQuery = realm.where(Item.class);

        if(startPrice > 0){
            itemRealmQuery.greaterThanOrEqualTo(Item.COL_BASE_PRICE, startPrice);
        }
        if(upToPrice > 0){
            itemRealmQuery.lessThanOrEqualTo(Item.COL_BASE_PRICE, upToPrice);
        }

        if(hasDiscount){
            itemRealmQuery.equalTo(Item.COL_HAS_DISCOUNT, true);
        }
        if(isNew){
            itemRealmQuery.equalTo(Item.COL_IS_NEW, true);
        }
        if(isPopular){
            itemRealmQuery.equalTo(Item.COL_IS_POPULAR, true);
        }

        if(categories.size() > 0){
            if(categories.size() > 1){
                itemRealmQuery.beginGroup();
                for (String category : categories){
                    itemRealmQuery.or().equalTo(Item.COL_CATEGORY_NAME, category);
                }
                itemRealmQuery.endGroup();
            }
            else{
                itemRealmQuery.equalTo(Item.COL_CATEGORY_NAME, categories.iterator().next());
            }
        }
        return itemRealmQuery;
    }


    public static RealmResults<Category> filteredCategories(Realm realm){
        RealmQuery<Category> categoryRealmQuery = realm.where(Category.class);
        if(categories.size() > 0){
            if(categories.size() > 1){
                categoryRealmQuery.beginGroup();
                for (String category : categories){
                    categoryRealmQuery.or().equalTo(Category.COL_NAME, category);
                }
                categoryRealmQuery.endGroup();
            }
            else{
                categoryRealmQuery.equalTo(Category.COL_NAME, categories.iterator().next());
            }
        }
        return categoryRealmQuery.sort(Category.COL_SORT_ORDER, Sort.ASCENDING).findAll();
    }

}
