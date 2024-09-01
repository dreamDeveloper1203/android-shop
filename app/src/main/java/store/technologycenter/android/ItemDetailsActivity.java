package store.technologycenter.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import store.technologycenter.android.configurations.TechCenterConfiguration;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.services.FilterService;
import store.technologycenter.android.services.ImageService;
import store.technologycenter.android.services.Utility;

import com.squareup.picasso.Callback;

import java.util.Date;

import io.realm.Realm;

public class ItemDetailsActivity extends AppCompatActivity {

    private Context mContext;
    private Item item;
    private Realm mRealm;
    private String title;
    private ImageView itemImageView;
    private TextView itemNameTextView;
    private TextView itemCategoryNameTextView;
    private TextView itemDescriptionTextView;
    private TextView itemPriceTextView;
    private ProgressBar itemImageProgressBar;
    private ImageView minusBtn;
    private ImageView plusBtn;
    private TextView quantityText;
    private Button addToCartBtn;
    private EditText commentEditText;
    private Menu mOptionsMenu;
    private DisplayMetrics displayMetrics;
    private ActionBar mActionBar;
    private RelativeLayout mRelativeLayoutQuantity;
    private static final float ACTION_BAR_ELEVATION = 0;
    private static final String TAG = "ActivityItemDetailsBinding";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        mContext = this;
        mActionBar = getSupportActionBar();
        mRelativeLayoutQuantity = findViewById(R.id.rl_quantity);
        itemImageProgressBar = findViewById(R.id.activity_item_details_image_pb);
        itemCategoryNameTextView = findViewById(R.id.activity_item_details_tv_category_name);
        itemImageProgressBar.setVisibility(View.GONE);
        itemImageView = findViewById(R.id.activity_item_details_iv);
        itemNameTextView = findViewById(R.id.activity_item_details_tv_title);
        itemDescriptionTextView = findViewById(R.id.activity_item_details_tv_description);
        itemPriceTextView = findViewById(R.id.activity_item_details_tv_price);
        addToCartBtn = findViewById(R.id.activity_item_details_btn_add_to_cart);
        quantityText = findViewById(R.id.text_quantity);
        minusBtn = findViewById(R.id.ib_minus);
        plusBtn = findViewById(R.id.ib_plus);
        commentEditText = findViewById(R.id.et_comment);
        commentEditText.clearFocus();
        mRealm = Realm.getDefaultInstance();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        String itemSlug = getIntent().getStringExtra(Item.COL_ID);
        item = mRealm.where(Item.class).equalTo(Item.COL_ID, itemSlug).findFirst();
        if (item == null) {
            Toast.makeText(mContext, getString(R.string.item_not_found_error), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ItemDetailsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        Date now = new Date();
        mRealm.executeTransaction(v -> {
            item.setViewed(true);
            item.setViewedAt(now);
        });
        if (getIntent().hasExtra(Item.COL_IS_SEARCHED)) {
            if (!item.isSearched()) {
                mRealm.executeTransaction(v -> {
                    item.setSearched(true);
                    item.setSearchedAt(now);
                });
            }
        }

        title = Utility.actionBarTitle(mContext, item.getName());
        setTitle(title);
        itemNameTextView.setText(item.getName().toUpperCase());
        itemCategoryNameTextView.setText(item.getCategoryName());
        itemDescriptionTextView.setText(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        ? Html.fromHtml(item.getDescription(), Html.FROM_HTML_MODE_COMPACT)
                        : Html.fromHtml(item.getDescription())
        );

        itemImageProgressBar.setVisibility(View.VISIBLE);
        ImageService.setImage(item.getImageUrl() + "?w=600&h=600&fit=fill-max&bg=white&fm=webp", itemImageView, 1200, 1200, new Callback() {
            @Override
            public void onSuccess() {
                itemImageProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                itemImageProgressBar.setVisibility(View.GONE);
            }
        });

        itemImageView.setOnClickListener(v -> ImageService.showDialog(mContext, item.getImageUrl() + "?w=600&h=600&fit=fill-max&bg=white&fm=webp", displayMetrics));
        minusBtn.setOnClickListener(v -> setQuantity(false));
        plusBtn.setOnClickListener(v -> setQuantity(true));
        configureQuantityText();
        configureCartButton();
        configureActionBar();
        commentEditText.setText(item.getComment());


        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mRealm.executeTransaction(mRealm -> {
                    item.setComment(commentEditText.getText().toString());
                });
            }
        });

        addToCartBtn.setOnClickListener(v -> {
            mRealm.executeTransaction(mRealm -> {
                item.setAddedToCartAt(new Date());
                item.setQuantity(1);
                item.setInCart(true);
            });
            configureCartButton();
            Toast.makeText(mContext, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
        });
        configFavoriteButton();
    }

    private int quantity() {
        return Integer.parseInt(quantityText.getText().toString());
    }

    @SuppressLint("SetTextI18n")
    private void setQuantity(boolean increase) {
        int value = quantity();
        int valueToAdd = increase ? Math.min(1000, (value + 1)) : Math.max(0, (value - 1));
        quantityText.setText(String.valueOf(valueToAdd));
        mRealm.executeTransaction(mRealm -> item.setQuantity(valueToAdd));
        if (valueToAdd < 1) {
            configureCartButton();
            mRealm.executeTransaction(mRealm -> item.setInCart(false));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        configFavoriteButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void share() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = item.getUrl();
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));
    }

    private void addToFavorites() {
        mRealm.executeTransaction(transactionRealm -> {
            boolean isInFav = item.isInFavorites();
            item.setInFavorites(!isInFav);
            item.setFavoriteAt(new Date());
            configFavoriteButton();
        });
    }

    private void configFavoriteButton() {
        if (mOptionsMenu == null) return;
        MenuItem menuItem = mOptionsMenu.findItem(R.id.favorite_menu_item);
        int drawable = item.isInFavorites() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline;
        menuItem.setIcon(ContextCompat.getDrawable(mContext, drawable));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int color = item.isInFavorites() ? R.color.red : R.color.dark;
            menuItem.setIconTintList(ContextCompat.getColorStateList(mContext, color));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_details_action_bar_menu, menu);
        mOptionsMenu = menu;
        configFavoriteButton();
        return super.onCreateOptionsMenu(menu);
    }

    private void configureActionBar() {
        if (mActionBar == null) return;
        mActionBar.setElevation(ACTION_BAR_ELEVATION);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.share_menu_item:
                share();
                return true;
            case R.id.favorite_menu_item:
                addToFavorites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    private void configureCartButton() {
        addToCartBtn.setVisibility(item.isInCart() && item.getQuantity() > 0 ? View.GONE : View.VISIBLE);
        mRelativeLayoutQuantity.setVisibility(item.isInCart() && item.getQuantity() > 0 ? View.VISIBLE : View.GONE);
        addToCartBtn.setText(getString(R.string.add) + " " + item.getBasePriceString());
        itemPriceTextView.setText(item.getBasePriceString());
        configureQuantityText();
    }

    private void configureQuantityText() {
        quantityText.setText(String.valueOf(item.getQuantity()));
    }

}
