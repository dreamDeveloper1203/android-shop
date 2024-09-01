package store.technologycenter.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import store.technologycenter.android.ItemDetailsActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.configurations.TechCenterConfiguration;
import store.technologycenter.android.holders.CartRecyclerViewHolder;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.services.ImageService;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmResults;

public class CartItemAdapter extends RecyclerView.Adapter<CartRecyclerViewHolder>{

    private final RealmResults<Item> mItems;
    private final Context mContext;

    public CartItemAdapter(Context context, RealmResults<Item> items){
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    public CartRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_row_item, parent,false);
        return new CartRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NotNull final CartRecyclerViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        assert item != null;
        holder.name.setText(item.getName());
        ImageService.setImage(item.getImageUrl()+"?w=250&h=250&fit=fill-max&bg=white&fm=webp", holder.imageView, 200, 200);
        holder.category.setText(item.getCategoryName());
        holder.comment.setText(item.getComment());
        holder.subtotal.setText(item.getBasePriceString() +" x"+item.getQuantity() +" = " + item.getSubtotalString());

        holder.setOnClickListener((view, position1, isLongClick, i) -> {
            Intent intent = new Intent(mContext, ItemDetailsActivity.class);
            intent.putExtra(Item.COL_ID, item.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Item getItem(int position){
        return  mItems.get(position);
    }

    public String getSubtotal(){
        double total = 0;
        for (Item item: mItems){
            total += item.getSubtotal();
        }
        return TechCenterConfiguration.BLRFormat(total);
    }
}