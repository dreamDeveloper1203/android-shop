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
import store.technologycenter.android.holders.ItemSearchRecyclerViewHolder;
import store.technologycenter.android.models.Item;

import org.jetbrains.annotations.NotNull;

import io.realm.RealmResults;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchRecyclerViewHolder>{

    private final RealmResults<Item> mItems;
    Context mContext;

    public ItemSearchAdapter(Context context, RealmResults<Item> items){
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    public ItemSearchRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_row_item, parent,false);
        return new ItemSearchRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NotNull final ItemSearchRecyclerViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        assert item != null;
        holder.textViewName.setText(item.getName().toUpperCase()+" ("+item.getCategoryName().toUpperCase()+")");
        int textDrawable = item.isSearched() ? R.drawable.ic_history : R.drawable.ic_search_outline;
        holder.textViewName.setCompoundDrawablesWithIntrinsicBounds(textDrawable, 0, 0, 0);
        holder.setOnClickListener((view, position1, isLongClick, i) -> {
            Intent intent = new Intent(mContext, ItemDetailsActivity.class);
            intent.putExtra(Item.COL_ID, item.getId());
            intent.putExtra(Item.COL_IS_SEARCHED, true);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
