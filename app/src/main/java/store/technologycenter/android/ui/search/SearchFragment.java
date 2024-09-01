package store.technologycenter.android.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import store.technologycenter.android.FilterActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.adapters.ItemSearchAdapter;
import store.technologycenter.android.models.Item;

import org.jetbrains.annotations.NotNull;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private ItemSearchAdapter itemSearchAdapter;
    private RecyclerView searchRecyclerView;
    private Realm realm;
    private Context mContext;
    private ImageView searchClearButton;
    private ImageView emptySearchResultsImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);
        mContext = getContext();
        realm = Realm.getDefaultInstance();
        searchEditText = view.findViewById(R.id.edit_text_search);
        emptySearchResultsImageView = view.findViewById(R.id.iv_empty_search_results);
        searchClearButton = view.findViewById(R.id.btn_search_clear);
        searchRecyclerView = view.findViewById(R.id.rv_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        searchRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, layoutManager.getOrientation());
        searchRecyclerView.addItemDecoration(dividerItemDecoration);


        searchClearButton.setOnClickListener(v -> {
            searchEditText.setText(null);
            configureSearchClearButton(searchEditText.getText().toString());
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                search(charSequence.toString());
                configureSearchClearButton(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search(searchEditText.getText().toString());
        configureSearchClearButton(searchEditText.getText().toString());
        return view;
    }

    private void search(String query) {
        RealmQuery<Item> realmQuery = realm.where(Item.class);
        RealmResults<Item> items;
        boolean isSearchList = false;
        if (query.isEmpty()) {
            items = realmQuery.equalTo(Item.COL_IS_SEARCHED, true)
                    .sort(Item.COL_SEARCHED_AT, Sort.DESCENDING)
                    .limit(30).findAll();
            isSearchList = true;
        } else {
            items = realmQuery.contains(Item.COL_SEARCH_NAME, query, Case.INSENSITIVE)
                    .sort(Item.COL_IN_STOCK, Sort.DESCENDING)
                    .limit(30).findAll();
        }

        if (items.isEmpty()) {
            emptySearchResultsImageView.setVisibility(View.VISIBLE);
        } else {
            emptySearchResultsImageView.setVisibility(View.GONE);
        }

        itemSearchAdapter = new ItemSearchAdapter(mContext, items);
        searchRecyclerView.setAdapter(itemSearchAdapter);
    }

    private void configureSearchClearButton(String value) {
        searchClearButton.setVisibility(value.isEmpty() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        itemSearchAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_item) {
            Intent intent = new Intent(mContext, FilterActivity.class);
            filterActivityResultLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {});
}