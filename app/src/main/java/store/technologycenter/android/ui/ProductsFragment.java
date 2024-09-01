package store.technologycenter.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import store.technologycenter.android.FilterActivity;
import store.technologycenter.android.MainActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.adapters.ItemAdapter;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.services.FilterService;

public class ProductsFragment extends Fragment {
    ItemAdapter itemAdapter;
    Context context;
    Realm realm;
    ImageView NoContent;
    RecyclerView itemsRecyclerView;
    String categoryId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        categoryId = ProductsFragmentArgs.fromBundle(getArguments()).getCategoryId();

        realm = Realm.getDefaultInstance();
        itemsRecyclerView = view.findViewById(R.id.products_recycler_view);
        NoContent = view.findViewById(R.id.no_products_image_view);
        context = getContext();
        Category category = realm.where(Category.class).equalTo("slug", categoryId).findFirst();

        if (category != null) {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(category.getName());
        }
        setProducts();

        return view;
    }

    public void setProducts() {
        RealmResults<Item> items = FilterService.isOn ? FilterService.filteredItems(realm).equalTo("category.slug", categoryId)
                .sort(Item.COL_IN_STOCK, Sort.DESCENDING)
                .findAll()
                : realm.where(Item.class).equalTo("category.slug", categoryId)
                .sort(Item.COL_IN_STOCK, Sort.DESCENDING)
                .findAll();

        NoContent.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        itemsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        itemAdapter = new ItemAdapter(getContext(), items, (view2, position, isLongClick, i) -> {
            Item item = items.get(position);
            if (item != null) {
                realm.executeTransaction(v -> {
                    item.setInFavorites(!item.isInFavorites());
                    item.setFavoriteAt(new Date());
                });
            }
        });
        itemsRecyclerView.setAdapter(itemAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        setProducts();
    }
}