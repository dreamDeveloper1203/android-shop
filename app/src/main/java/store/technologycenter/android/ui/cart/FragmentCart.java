package store.technologycenter.android.ui.cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import store.technologycenter.android.FilterActivity;
import store.technologycenter.android.MainActivity;
import store.technologycenter.android.OrderActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.adapters.CartItemAdapter;
import store.technologycenter.android.models.Item;


import org.jetbrains.annotations.NotNull;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FragmentCart extends Fragment {
    private RecyclerView cartRecyclerView;
    private ImageView cartEmptyImageView;
    private CartItemAdapter cartItemAdapter;
    private Button btnGoToSubmitOrder;
    private Realm mRealm;
    private Context mContext;
    private RealmResults<Item> mItems;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        mRealm = Realm.getDefaultInstance();
        mContext = getContext();
        cartRecyclerView = view.findViewById(R.id.cart_rv);
        cartEmptyImageView = view.findViewById(R.id.cart_empty_iv);
        btnGoToSubmitOrder = view.findViewById(R.id.btn_go_to_submit_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, layoutManager.getOrientation());
        cartRecyclerView.addItemDecoration(dividerItemDecoration);
        setHasOptionsMenu(true);

        getCart();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Item item = cartItemAdapter.getItem(position);
                mRealm.executeTransaction(transactionRealm -> {
                    item.setInCart(false);
                    item.setQuantity(0);
                    item.setComment("");
                    item.setAddedToCartAt(null);
                });
                cartItemAdapter.notifyItemRemoved(position);
                if (mItems.size() == 0) {
                    cartEmptyImageView.setVisibility(View.VISIBLE);
                    cartRecyclerView.setVisibility(View.GONE);
                    btnGoToSubmitOrder.setVisibility(View.GONE);
                }
                configureSubmitButton();
                ((MainActivity) mContext).configureCartBadge();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(mContext, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(mContext, R.color.red))
                        .addActionIcon(R.drawable.ic_trash)
                        .setActionIconTint(ContextCompat.getColor(mContext, R.color.white))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(cartRecyclerView);

        btnGoToSubmitOrder.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, OrderActivity.class);
            orderActivityResultLauncher.launch(intent);
        });

        return view;
    }

    private void getCart() {
        cartEmptyImageView.setVisibility(View.GONE);
        btnGoToSubmitOrder.setVisibility(View.VISIBLE);
        mItems = mRealm.where(Item.class)
                .equalTo(Item.COL_IN_CART, true)
                .sort(Item.COL_ADDED_TO_CART_AT, Sort.DESCENDING)
                .limit(30)
                .findAll();

        if (mItems.isEmpty()) {
            cartEmptyImageView.setVisibility(View.VISIBLE);
            btnGoToSubmitOrder.setVisibility(View.GONE);
        }


        cartItemAdapter = new CartItemAdapter(getContext(), mItems);
        configureSubmitButton();
        cartRecyclerView.setAdapter(cartItemAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCart();
    }

    @SuppressLint("SetTextI18n")
    private void configureSubmitButton() {
        btnGoToSubmitOrder.setText("Continue To Checkout " + cartItemAdapter.getSubtotal());
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
            new ActivityResultContracts.StartActivityForResult(), result -> {
            });


    ActivityResultLauncher<Intent> orderActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
            });
}
