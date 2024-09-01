package store.technologycenter.android.services;

import android.text.TextUtils;

import store.technologycenter.android.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CartService {

    public static RealmResults<Item> items(Realm realm) {
        return realm.where(Item.class)
                .equalTo(Item.COL_IN_CART, true)
                .sort(Item.COL_ADDED_TO_CART_AT, Sort.ASCENDING)
                .findAll();
    }

    public static void reset(Realm realm) {
        RealmResults<Item> CartItems = CartService.items(realm);
        realm.executeTransaction(r -> {
            for (Item item : CartItems) {
                item.setInCart(false);
                item.setAddedToCartAt(null);
            }
        });
    }

    public static int getTotalItems(Realm realm) {
        return CartService.items(realm).size();
    }

    public static double getTotal(Realm realm) {
        RealmResults<Item> CartItems = CartService.items(realm);
        double total = 0;
        for (Item item : CartItems) {
            total += item.getSubtotal();
        }
        return total;
    }

    public static JSONArray getJsonArray(Realm realm) throws JSONException {
        RealmResults<Item> CartItems = CartService.items(realm);
        JSONArray jsonArray = new JSONArray();
        for (Item item : CartItems) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", item.getId());
            jsonObject.put("quantity", item.getQuantity());
            jsonObject.put("comments", TextUtils.isEmpty(item.getComment()) ? "" : item.getComment());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

}
