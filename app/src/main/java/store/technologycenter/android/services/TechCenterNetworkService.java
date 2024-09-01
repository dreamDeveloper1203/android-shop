package store.technologycenter.android.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import store.technologycenter.android.configurations.TechCenterConfiguration;
import store.technologycenter.android.http.Request;
import store.technologycenter.android.http.Response;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import io.realm.Realm;
import io.realm.RealmList;

public class TechCenterNetworkService {

    public static final int READ_TIMEOUT = 60000;
    public static final int CONNECT_TIMEOUT = 60000;

    public static final String CATEGORIES_ITEMS = "categories/items/android";
    public static final String BANNERS = "bnrs";
    public static final String PAYMENT_METHODS = "payment-methods";
    public static final String AREAS = "areas";
    public static final String COUPONS = "coupon";
    public static final String STORE_STATUS = "store/status";
    public static final String ORDER = "order";

    public static String getCategoriesItems() {
        return getRequest(CATEGORIES_ITEMS);
    }
    public static String getBanners() {
        return getRequest(BANNERS);
    }
    public static String getPaymentMethods() {
        return getRequest(PAYMENT_METHODS);
    }

    public static String getAreas() {
        return getRequest(AREAS);
    }

    public static String getCoupon(String code, boolean isDelivery) {
        return postRequest(COUPONS, code, isDelivery);
    }

    public static boolean orderSubmit(JSONObject jsonObject) {
        return sendOrder(ORDER, jsonObject);
    }


    public static String getStoreStatus() {
        return getRequest(STORE_STATUS);
    }

    public static String getRequest(String uri) {
        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(route(uri));
            System.out.println(url);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setReadTimeout(READ_TIMEOUT);
            httpsURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpsURLConnection.setRequestMethod(Request.METHOD_GET);

            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public static ArrayList<Category> getCategoryArrayList(String response, Realm realm) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray dataJSONArray = jsonObject.getJSONArray(Response.DATA);

            ArrayList<Category> categoriesArrayList = new ArrayList<Category>();
            for (int i = 0; i < dataJSONArray.length(); i++) {
                JSONObject categoryJSONObject = dataJSONArray.getJSONObject(i);

                JSONArray itemsJSONArray = categoryJSONObject.getJSONArray("items");

                RealmList<Item> itemRealmList = new RealmList<>();
                String categoryName = categoryJSONObject.has("name") ? categoryJSONObject.getString("name") : "";

                for (int j = 0; j < itemsJSONArray.length(); j++) {
                    JSONObject itemJSONObject = itemsJSONArray.getJSONObject(j);

                    String id = itemJSONObject.getString("id");
                    String name = itemJSONObject.has("name") ? itemJSONObject.getString(Item.COL_NAME) : "";
                    String imageUrl = itemJSONObject.has("image_url") ? itemJSONObject.getString("image_url") : "";
                    double basePrice = itemJSONObject.has("retail_price") ? itemJSONObject.getDouble("retail_price") : 0.0;
                    String viewPrice = TechCenterConfiguration.BLRFormat(basePrice);
                    boolean hasDiscount = itemJSONObject.has("has_discount") && itemJSONObject.getBoolean("has_discount");
                    boolean isNew = itemJSONObject.has("is_new") && itemJSONObject.getBoolean("is_new");
                    boolean isPopular = itemJSONObject.has("is_popular") && itemJSONObject.getBoolean("is_popular");
                    String description = itemJSONObject.has("description") ? itemJSONObject.getString("description") : "";
                    int inStock = itemJSONObject.has("in_stock") ? itemJSONObject.getInt("in_stock") : 0;
                    String url = itemJSONObject.has("url") ? itemJSONObject.getString("url") : "";
                    String productSearchName = itemJSONObject.has("search_name") ? itemJSONObject.getString("search_name") : "";
                    String searchName = productSearchName + " " + categoryName;

                    boolean itemExists = false;
                    Item existingItem;
                    existingItem = realm.where(Item.class).equalTo("id", id).findFirst();
                    if (existingItem != null) {
                        itemExists = true;
                    }
                    boolean inCart = itemExists && existingItem.isInCart();
                    boolean inFavorites = itemExists && existingItem.isInFavorites();
                    boolean isViewed = itemExists && existingItem.isViewed();
                    boolean isSearched = itemExists && existingItem.isSearched();

                    Item item = new Item(
                            id,
                            name,
                            imageUrl,
                            basePrice,
                            viewPrice,
                            hasDiscount,
                            isNew,
                            isPopular,
                            description,
                            url,
                            categoryName,
                            inCart,
                            itemExists ? existingItem.getComment() : null,
                            itemExists ? existingItem.getQuantity() : 0,
                            itemExists ? existingItem.getAddedToCartAt() : null,
                            inFavorites,
                            itemExists ? existingItem.getFavoriteAt() : null,
                            isViewed,
                            itemExists ? existingItem.getViewedAt() : null,
                            isSearched,
                            itemExists ? existingItem.getSearchedAt() : null,
                            searchName,
                            inStock
                    );
                    itemRealmList.add(item);
                }
                Category category = new Category(
                        categoryJSONObject.has("id") ? categoryJSONObject.getString("id") : "",
                        categoryJSONObject.has("image_url") ? categoryJSONObject.getString("image_url") : "",
                        categoryName,
                        categoryJSONObject.has("sort_order") ? categoryJSONObject.getInt("sort_order") : 1,
                        itemRealmList
                );
                categoriesArrayList.add(category);
            }
            return categoriesArrayList;
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static String route(String uri) {
        return TechCenterConfiguration.API_URL() + "" + uri;
    }


    public static String postRequest(String uri, String code, boolean isDelivery) {

        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(route(uri));
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setReadTimeout(READ_TIMEOUT);
            httpsURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpsURLConnection.setRequestMethod(Request.METHOD_POST);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpsURLConnection.setRequestProperty("Accept", "application/json");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            OutputStream outputStream = httpsURLConnection.getOutputStream();

            JSONObject parameters = new JSONObject();
            try {
                parameters.put("coupon_code", code);
                parameters.put("is_delivery", isDelivery);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(String.valueOf(parameters));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean sendOrder(String uri, JSONObject jsonObject) {

        HttpsURLConnection httpsURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(route(uri));
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setReadTimeout(READ_TIMEOUT);
            httpsURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpsURLConnection.setRequestMethod(Request.METHOD_POST);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpsURLConnection.setRequestProperty("Accept", "application/json");
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            OutputStream outputStream = httpsURLConnection.getOutputStream();


            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(String.valueOf(jsonObject));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return false;
    }

}
