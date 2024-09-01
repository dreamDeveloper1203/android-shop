package store.technologycenter.android.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import store.technologycenter.android.R;
import store.technologycenter.android.adapters.CategoryAdapter;
import store.technologycenter.android.http.Response;
import store.technologycenter.android.models.Area;
import store.technologycenter.android.models.Banner;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.models.PaymentMethod;
import store.technologycenter.android.services.FilterService;
import store.technologycenter.android.services.TechCenterNetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CategoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private CategoryAdapter categoryAdapter;
    private RecyclerView mCategoryRecyclerView;
    private ExecutorService mExecutor;
    private Handler mHandler;
    private Realm mRealm;
    private SwipeRefreshLayout categoriesSwipeRefreshLayout;
    private Context context;
    private ImageView noDataImageView;
    private static final int MATERIAL_RECYCLER_VIEW_SPAN_COUNT = 3;
    private ImageSlider mImageSlider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoriesSwipeRefreshLayout = view.findViewById(R.id.categories_swipe_refresh_layout);
        mCategoryRecyclerView = view.findViewById(R.id.categories_recycler_view);
        noDataImageView = view.findViewById(R.id.no_data_image_view);
        mImageSlider = view.findViewById(R.id.image_slider);

        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), MATERIAL_RECYCLER_VIEW_SPAN_COUNT));
        mRealm = Realm.getDefaultInstance();
        mExecutor = Executors.newSingleThreadExecutor();
        context = getActivity();
        categoriesSwipeRefreshLayout.setOnRefreshListener(this);
        setHasOptionsMenu(true);
        setCatalog();
        setBanners();
        return view;
    }

    public void setCatalog() {
        mRealm = Realm.getDefaultInstance();

        RealmResults<Category> categoryRealmResults = FilterService.isOn
                ? FilterService.filteredCategories(mRealm)
                : mRealm.where(Category.class).sort(Category.COL_SORT_ORDER, Sort.ASCENDING).findAll();

        categoryAdapter = new CategoryAdapter(getContext(), categoryRealmResults);
        mCategoryRecyclerView.setAdapter(categoryAdapter);
        noDataImageView.setVisibility(categoryRealmResults.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void setBanners() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        RealmResults<Banner> bannerRealmResults =  mRealm.where(Banner.class).sort(Banner.COL_SORT_ORDER, Sort.ASCENDING).findAll();
        if(bannerRealmResults.size() > 0){
            List<SlideModel> slideModels = new ArrayList<>();

            for (Banner bannerRealmResult: bannerRealmResults){
                if (bannerRealmResult != null) {
                    String url = bannerRealmResult.getImageUrl() + "?w=855&h=285&fit=crop&fm=webp";
                    slideModels.add(new SlideModel(url, ScaleTypes.FIT));
                }
            }

            mImageSlider.setImageList(slideModels, ScaleTypes.FIT);
        }else{
            mImageSlider.setVisibility(View.GONE);
        }



    }



    @Override
    public void onRefresh() {
        if (FilterService.isOn) {
            categoriesSwipeRefreshLayout.setRefreshing(false);
            categoriesSwipeRefreshLayout.setEnabled(false);
            return;
        } else {
            categoriesSwipeRefreshLayout.setEnabled(true);
        }

        categoriesSwipeRefreshLayout.setRefreshing(true);
        categoriesSwipeRefreshLayout.setEnabled(true);

        if (!TechCenterNetworkService.isConnected(context)) {
            categoriesSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, getString(R.string.no_internet_connection_error), Toast.LENGTH_SHORT).show();
            return;
        }
        downloadData();
    }




    private void downloadData() {
        mHandler = new Handler(Looper.getMainLooper());
        mExecutor.execute(() -> {
            String response = TechCenterNetworkService.getCategoriesItems();
            mHandler.post(() -> {
                if (Response.isNull(response)) {
                    categoriesSwipeRefreshLayout.setRefreshing(false);
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
                    categoriesSwipeRefreshLayout.setRefreshing(false);
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
                    categoriesSwipeRefreshLayout.setRefreshing(false);
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
                System.out.println("dataJSONArray "+dataJSONArray);
                System.out.println("dataJSONArray "+dataJSONArray.length());
                ArrayList<Banner> bannerArrayList = new ArrayList<Banner>();

                for (int i = 0; i < dataJSONArray.length(); i++) {
                    JSONObject bannerJSONObject = dataJSONArray.getJSONObject(i);
                    Banner banner = new Banner(
                            bannerJSONObject.getString("id"),
                            bannerJSONObject.has("image_url") ? bannerJSONObject.getString("image_url") : "",
                            bannerJSONObject.has("sort_order") ? bannerJSONObject.getInt("sort_order") : 1
                    );
                    bannerArrayList.add(banner);
                    System.out.println("banner "+ banner);

                }
                System.out.println("bannerArrayList "+ bannerArrayList);

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
                    categoriesSwipeRefreshLayout.setRefreshing(false);
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
            mHandler.post(()->{
                categoriesSwipeRefreshLayout.setRefreshing(false);
                setCatalog();
                setBanners();
            });
        });
    }



    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        setCatalog();
    }


}