package store.technologycenter.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import store.technologycenter.android.ItemDetailsActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.holders.ItemRecyclerViewHolder;
import store.technologycenter.android.listeners.ItemClickListener;
import store.technologycenter.android.models.Item;
import store.technologycenter.android.services.ImageService;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmResults;

public class ItemAdapter extends RecyclerView.Adapter<ItemRecyclerViewHolder> {

    private final RealmResults<Item> mItems;
    private final Context mContext;
    private final ItemClickListener mItemClickListener;

    public ItemAdapter(Context context, RealmResults<Item> items, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mItems = items;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    public ItemRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.product_row_item, parent, false);
        return new ItemRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NotNull final ItemRecyclerViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        assert item != null;

        configureFavButton(item, holder.fabFavorite);
        ImageService.setImage(item.getImageUrl() + "?w=500&h=500&fit=fill-max&bg=white&fm=webp", holder.imageView, ImageService.WIDTH_SM, ImageService.HEIGHT_SM);
        holder.textViewName.setText(item.getName());
        holder.fabFavorite.setOnClickListener(v -> {
            mItemClickListener.onClick(holder.itemView, position, false, holder);
            configureFavButton(item, holder.fabFavorite);
        });
        holder.setOnClickListener((view, position1, isLongClick, viewHolder) -> {
            Intent intent = new Intent(mContext, ItemDetailsActivity.class);
            intent.putExtra(Item.COL_ID, item.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void configureFavButton(Item item, FloatingActionButton floatingActionButton) {
        boolean isInFav = item.isInFavorites();
        int imgResId = isInFav ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline;
        int color = isInFav ? R.color.red : R.color.light;
        floatingActionButton.setImageResource(imgResId);
        floatingActionButton.setColorFilter(ContextCompat.getColor(mContext, color));
    }

    public Item getItem(int position) {
        return mItems.get(position);
    }
}

