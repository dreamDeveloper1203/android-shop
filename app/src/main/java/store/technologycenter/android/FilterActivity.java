package store.technologycenter.android;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import store.technologycenter.android.adapters.CategoryFilterAdapter;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.services.FilterService;
import store.technologycenter.android.services.Utility;

import io.realm.Realm;
import io.realm.RealmResults;

public class FilterActivity extends AppCompatActivity {
    private Realm mRealm;
    private Context mContext;
    private RecyclerView categoriesFilterRecyclerView;
    private Dialog categoriesDialog;
    private CategoryFilterAdapter categoryFilterAdapter;
    private Button applyFilterButton;
    private TextView selectedCategoriesTextView;
    private SwitchMaterial hasDiscountSwitch;
    private SwitchMaterial isNewSwitch;
    private SwitchMaterial isPopularSwitch;
    private EditText priceStartEditText;
    private EditText priceUpToEditText;
    private LinearLayout categorySelectLinearLayout;
    private RealmResults<Category> categories;
    private ActionBar actionBar;
    private static final float ACTION_BAR_ELEVATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        categoriesFilterRecyclerView = findViewById(R.id.categories_filter_recycler_view);
        applyFilterButton = findViewById(R.id.apply_filter_button);
        selectedCategoriesTextView = findViewById(R.id.selected_categories_text_view);
        priceStartEditText = findViewById(R.id.price_start_edit_text);
        priceUpToEditText = findViewById(R.id.price_up_to_edit_text);

        hasDiscountSwitch = findViewById(R.id.has_discount_switch);
        isNewSwitch = findViewById(R.id.is_new_switch);
        isPopularSwitch = findViewById(R.id.is_popular_switch);
        categorySelectLinearLayout = findViewById(R.id.category_select_linear_layout);
        mRealm = Realm.getDefaultInstance();
        mContext = this;
        categories = categories();
        setTitle(" ");
        actionBar = getSupportActionBar();

        applyFilterButton.setOnClickListener(v -> {
            FilterService.isOn = !FilterService.isDefault();
            onBackPressed();
        });

        categorySelectLinearLayout.setOnClickListener(v -> {
            showCategoriesDialog();
        });


        priceStartEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                FilterService.startPrice = Utility.editTextDoubleValue(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {
                configureApplyButton();
            }
        });


        priceUpToEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                FilterService.upToPrice = Utility.editTextDoubleValue(charSequence);
            }

            @Override
            public void afterTextChanged(Editable s) {
                configureApplyButton();
            }
        });


        hasDiscountSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FilterService.hasDiscount = isChecked;
            configureApplyButton();
        });
        isNewSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FilterService.isNew = isChecked;
            configureApplyButton();
        });
        isPopularSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            FilterService.isPopular = isChecked;
            configureApplyButton();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        configureActionBar();
        configureApplyButton();
        setSelectedCategoriesTextView();
        configureFilterSwitches();
        configurePriceEditText();
    }

    private RealmResults<Category> categories() {
        return mRealm.where(Category.class).findAll();
    }

    private void showCategoriesDialog() {
        categoriesDialog = new Dialog(mContext, R.style.Bottom_Sheet_Dialog_Theme);
        categoriesDialog.setContentView(R.layout.categories_filter_dialog);
        categoriesDialog.setCancelable(true);
        Button categoriesFilterReadyButton = categoriesDialog.findViewById(R.id.categories_filter_ready_button);
        categoriesFilterReadyButton.setOnClickListener(v -> {
            setSelectedCategoriesTextView();
            configureApplyButton();
            categoriesDialog.dismiss();
        });
        categoriesDialog.setOnCancelListener(dialog -> {
            setSelectedCategoriesTextView();
            configureApplyButton();
        });
        categoriesFilterRecyclerView = categoriesDialog.findViewById(R.id.categories_filter_recycler_view);
        categoryFilterAdapter = new CategoryFilterAdapter(mContext, categories);
        categoriesFilterRecyclerView.setAdapter(categoryFilterAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        categoriesFilterRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, layoutManager.getOrientation());
        categoriesFilterRecyclerView.addItemDecoration(dividerItemDecoration);
        categoriesDialog.show();
    }

    private void setSelectedCategoriesTextView() {
        if (FilterService.categories.isEmpty()) {
            selectedCategoriesTextView.setVisibility(View.GONE);
            selectedCategoriesTextView.setText(null);
        } else {
            selectedCategoriesTextView.setText(TextUtils.join(", ", FilterService.categories));
            selectedCategoriesTextView.setVisibility(View.VISIBLE);
        }
    }

    private void configureFilterSwitches() {
        hasDiscountSwitch.setChecked(FilterService.hasDiscount);
        isNewSwitch.setChecked(FilterService.isNew);
        isPopularSwitch.setChecked(FilterService.isPopular);
    }

    private void configurePriceEditText() {
        if (FilterService.startPrice > 0) {
            priceStartEditText.setText(String.valueOf(FilterService.startPrice));
        } else {
            priceStartEditText.setText(null);
        }
        if (FilterService.upToPrice > 0) {
            priceUpToEditText.setText(String.valueOf(FilterService.upToPrice));
        } else {
            priceUpToEditText.setText(null);
        }
    }

    @SuppressLint("SetTextI18n")
    private void configureApplyButton() {
        int totalItems = (int) FilterService.filteredItems(mRealm).count();
        applyFilterButton.setText(getString(R.string.show) + " " + totalItems + " " +
                (totalItems == 1 ? getString(R.string.item) : getString(R.string.items)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_filter) {
            resetFilter();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetFilter() {
        FilterService.reset();
        configureApplyButton();
        setSelectedCategoriesTextView();
        configureFilterSwitches();
        configurePriceEditText();
    }

    private void configureActionBar() {
        if (actionBar == null) return;
        actionBar.setElevation(ACTION_BAR_ELEVATION);
    }


}
