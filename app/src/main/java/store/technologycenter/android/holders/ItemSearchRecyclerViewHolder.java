package store.technologycenter.android.holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import store.technologycenter.android.R;
import store.technologycenter.android.listeners.ItemClickListener;

public class ItemSearchRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener mItemClickListener;
    public TextView textViewName;

    public ItemSearchRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.tv_name);
        itemView.setOnClickListener(this);
    }
    public void setOnClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onClick(view, getAdapterPosition(),false, null);
    }

}
