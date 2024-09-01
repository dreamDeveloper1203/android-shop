package store.technologycenter.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import store.technologycenter.android.configurations.AndroidBug5497Workaround;
import store.technologycenter.android.configurations.TechCenterConfiguration;
import store.technologycenter.android.http.Response;
import store.technologycenter.android.models.Area;
import store.technologycenter.android.models.Banner;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.models.PaymentMethod;
import store.technologycenter.android.services.CustomerService;
import store.technologycenter.android.services.TechCenterNetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;


public class SplashActivity extends AppCompatActivity {

    private ExecutorService mExecutor;
    private Handler mHandler;
    private Realm mRealm;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        TextView appVersionTextView = findViewById(R.id.app_version_text_view);
        appVersionTextView.setText(getString(R.string.app_version, TechCenterConfiguration.versionName()));
        mExecutor = Executors.newSingleThreadExecutor();
        mContext = this;
        mRealm = Realm.getDefaultInstance();
        CustomerService.createCustomer(mRealm);

        if (TechCenterNetworkService.isConnected(mContext)) {
            Category categoryFoTesting = mRealm.where(Category.class).findFirst();
            if (categoryFoTesting == null) {
                downloadData();
            } else {
                new Handler().postDelayed(this::startMainActivity, 1000);
            }
        } else {
            new Handler().postDelayed(this::startMainActivity, 1000);
        }
    }


    private void downloadData() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getCategoriesItems();
            mHandler.post(() -> {
                if (Response.isNull(response)) {
                    startMainActivity();
                } else {
                    updateCategoryItem(response);
                }
            });
        });
    }

    private void updateCategoryItem(String response) {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            mRealm = Realm.getDefaultInstance();
            ArrayList<Category> categoryArrayList = TechCenterNetworkService.getCategoryArrayList(response, mRealm);
            if (categoryArrayList != null) {
                mRealm.executeTransaction(r -> {
                    mRealm.delete(Category.class);
                    mRealm.delete(Item.class);
                });
                mRealm.executeTransaction(transactionRealm -> {
                    try {
                        transactionRealm.insert(categoryArrayList);
                    } catch (Exception ignored) {
                    }
                });
            }
            mRealm.close();
            mHandler.post(this::getBanners);
        });
    }
    private void getBanners() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getBanners();
            mHandler.post(() -> {
                if (Response.isNull(response)) {
                    startMainActivity();
                } else {
                    updateBanners(response);
                }
            });
        });
    }
    private void getPaymentMethods() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getPaymentMethods();
            mHandler.post(() -> {
                if (Response.isNull(response)) {
                    startMainActivity();
                } else {
                    updatePaymentMethods(response);
                }
            });
        });
    }
    private void updateBanners(String response) {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            try {
                mRealm = Realm.getDefaultInstance();

                mRealm.executeTransaction(r -> {
                    mRealm.delete(Banner.class);
                });

                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataJSONArray = jsonObject.getJSONArray(Response.DATA);
                System.out.println(dataJSONArray);
                ArrayList<Banner> bannerArrayList = new ArrayList<Banner>();
                for (int i = 0; i < dataJSONArray.length(); i++) {
                    JSONObject bannerJSONObject = dataJSONArray.getJSONObject(i);
                    Banner banner = new Banner(
                            bannerJSONObject.getString("id"),
                            bannerJSONObject.has("image_url") ? bannerJSONObject.getString("image_url") : "",
                            bannerJSONObject.has("sort_order") ? bannerJSONObject.getInt("sort_order") : 1
                    );
                    bannerArrayList.add(banner);
                }
                System.out.println(bannerArrayList);
                mRealm.executeTransaction(transactionRealm -> {
                    try {
                        transactionRealm.insertOrUpdate(bannerArrayList);
                    } catch (Exception ignored) {
                    }
                });
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            mRealm.close();
            mHandler.post(this::getPaymentMethods);
        });
    }
    private void updatePaymentMethods(String response) {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            try {
                mRealm = Realm.getDefaultInstance();

                mRealm.executeTransaction(r -> {
                    mRealm.delete(PaymentMethod.class);
                });

                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataJSONArray = jsonObject.getJSONArray(Response.DATA);
                System.out.println(dataJSONArray);
                ArrayList<PaymentMethod> paymentMethodArrayList = new ArrayList<PaymentMethod>();
                for (int i = 0; i < dataJSONArray.length(); i++) {
                    JSONObject paymentMethodJSONObject = dataJSONArray.getJSONObject(i);
                    PaymentMethod paymentMethod = new PaymentMethod(
                            paymentMethodJSONObject.getString("id"),
                            paymentMethodJSONObject.has("name") ? paymentMethodJSONObject.getString("name") : ""
                    );
                    paymentMethodArrayList.add(paymentMethod);
                }
                mRealm.executeTransaction(transactionRealm -> {
                    try {
                        transactionRealm.insertOrUpdate(paymentMethodArrayList);
                    } catch (Exception ignored) {
                    }
                });
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            mRealm.close();
            mHandler.post(this::getAreas);
        });
    }

    private void getAreas() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getAreas();
            mHandler.post(() -> {
                if (Response.isNull(response)) {
                    startMainActivity();
                } else {
                    updateAreas(response);
                }
            });
        });
    }

    private void updateAreas(String response) {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            try {
                mRealm = Realm.getDefaultInstance();
                mRealm.executeTransaction(r -> {
                    mRealm.delete(Area.class);
                });
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataJSONArray = jsonObject.getJSONArray(Response.DATA);
                System.out.println(dataJSONArray);

                ArrayList<Area> areaArrayList = new ArrayList<Area>();
                for (int i = 0; i < dataJSONArray.length(); i++) {
                    JSONObject areaJSONObject = dataJSONArray.getJSONObject(i);
                    Area area = new Area(
                            areaJSONObject.getString("id"),
                            areaJSONObject.has("name") ? areaJSONObject.getString("name") : "",
                            areaJSONObject.has("fee") ? areaJSONObject.getDouble("fee") : 0.0,
                            areaJSONObject.has("view_fee") ? areaJSONObject.getString("view_fee") : "",
                            areaJSONObject.has("time") ? areaJSONObject.getInt("time") : 0,
                            areaJSONObject.has("view_time") ? areaJSONObject.getString("view_time") : ""
                    );
                    areaArrayList.add(area);
                }
                mRealm.executeTransaction(transactionRealm -> {
                    try {
                        transactionRealm.insertOrUpdate(areaArrayList);
                    } catch (Exception ignored) {
                    }
                });
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
            mRealm.close();
            mHandler.post(this::startMainActivity);
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}