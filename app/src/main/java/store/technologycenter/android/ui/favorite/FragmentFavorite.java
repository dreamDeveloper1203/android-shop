package store.technologycenter.android.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import store.technologycenter.android.FilterActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.adapters.ItemAdapter;
import store.technologycenter.android.models.Item;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class FragmentFavorite extends Fragment {
    private RecyclerView favoriteRecyclerView;
    private ImageView favoriteEmptyImageView;
    private Realm realm;
    private static final int MATERIAL_RECYCLER_VIEW_SPAN_COUNT = 2;
    private ItemAdapter itemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        setHasOptionsMenu(true);
        realm = Realm.getDefaultInstance();
        favoriteRecyclerView = view.findViewById(R.id.favorite_recycler_view);
        favoriteEmptyImageView = view.findViewById(R.id.favorite_empty_image_view);
        favoriteRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), MATERIAL_RECYCLER_VIEW_SPAN_COUNT));
        getFavoriteItems();
        return view;
    }

    private void getFavoriteItems() {
        RealmResults<Item> items = realm.where(Item.class)
                .equalTo(Item.COL_IN_FAVORITES, true)
                .sort(Item.COL_SEARCHED_AT, Sort.DESCENDING)
                .limit(30)
                .findAll();

        if (items.isEmpty()) {
            favoriteEmptyImageView.setVisibility(View.VISIBLE);
        }

        itemAdapter = new ItemAdapter(getContext(), items, (view, position, isLongClick, viewHolder) -> {
            final int _position = viewHolder.getAdapterPosition();
            final Item item = itemAdapter.getItem(_position);
            if (item == null) return;
            realm.executeTransaction(v -> {
                item.setInFavorites(!item.isInFavorites());
                item.setFavoriteAt(new Date());
            });
            itemAdapter.notifyItemRemoved(_position);
        });
        favoriteRecyclerView.setAdapter(itemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_item) {
            Intent intent = new Intent(getContext(), FilterActivity.class);
            filterActivityResultLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {});
}
