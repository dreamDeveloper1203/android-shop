package store.technologycenter.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import store.technologycenter.android.configurations.TechCenterConfiguration;
import store.technologycenter.android.databinding.ActivityMainBinding;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.services.FilterService;
import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private Realm mRealm;
    private Context mContext;
    private Menu mOptionsMenu;
    private ActionBar mActionBar;
    private TextView mBadgeText;
    private static final float ACTION_BAR_ELEVATION = 0;
    private static final String TAG = "ActivityMainBinding";
    private static final int CART_INDEX = 2;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupBeam();
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mRealm = Realm.getDefaultInstance();
        mActionBar = getSupportActionBar();
        mContext = this;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(CART_INDEX);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        View badge = LayoutInflater.from(this)
                .inflate(R.layout.cart_badge, itemView, true);
        mBadgeText = findViewById(R.id.text_cart_items_count);



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_category,
                R.id.navigation_search,
                R.id.navigation_cart,
                R.id.navigation_favorite,
                R.id.navigation_contact
        )
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, mNavController);


        configureActionBar();
        configureFilterIcon();
        configureCartBadge();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        mOptionsMenu = menu;
        configureFilterIcon();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        configureFilterIcon();
        configureCartBadge();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.qr_scanner_menu_item) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            integrator.setPrompt("");
            integrator.setCameraId(0);
            integrator.initiateScan();
            integrator.setBeepEnabled(false);
            return true;
        }else if(item.getItemId() == R.id.filter_menu_item){
            Intent intent = new Intent(mContext, FilterActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Item item = mRealm.where(Item.class)
                        .equalTo(Item.COL_ID, result.getContents().replace(TechCenterConfiguration.URL + "/p/", ""))
                        .findFirst();
                if (item != null) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(Item.COL_ID, item.getId());
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, getString(R.string.item_not_found_error), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp() || super.onSupportNavigateUp();
    }

    private void configureFilterIcon() {
        if (mOptionsMenu == null) return;
        MenuItem item = mOptionsMenu.findItem(R.id.filter_menu_item);
        int drawable = FilterService.isOn ? R.drawable.ic_hero_filter_solid : R.drawable.ic_hero_filter_outline;
        item.setIcon(ContextCompat.getDrawable(mContext, drawable));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int color = FilterService.isOn ? R.color.orange : R.color.dark;
            item.setIconTintList(ContextCompat.getColorStateList(mContext, color));
        }
    }

    private void configureActionBar() {
        if (mActionBar == null) return;
        mActionBar.setElevation(ACTION_BAR_ELEVATION);
    }

    @SuppressLint("SetTextI18n")
    public void configureCartBadge() {
        int count = (int) mRealm.where(Item.class).equalTo(Item.COL_IN_CART, true).count();
        mBadgeText.setText(String.valueOf(count));
        int visibility = count > 0 ? View.VISIBLE : View.GONE;
        mBadgeText.setVisibility(visibility);
    }

    private void setupBeam() {
//        try {
//            PushNotifications.start(getApplicationContext(), TechCenterConfiguration.INSTANCE_ID);
//            PushNotifications.addDeviceInterest(TechCenterConfiguration.DEVICE_INTEREST);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
    }

}



