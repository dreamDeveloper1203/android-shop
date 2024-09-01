package store.technologycenter.android.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import store.technologycenter.android.R;
import store.technologycenter.android.listeners.ItemClickListener;

public class CartRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener mItemClickListener;
    public ImageView imageView;
    public TextView name;
    public TextView category;
    public TextView comment;
    public TextView subtotal;

    public CartRecyclerViewHolder(@NonNull View view) {
        super(view);
        imageView = view.findViewById(R.id.iv_cart_item);
        name = view.findViewById(R.id.text_cart_item_name);
        category = view.findViewById(R.id.text_cart_item_category_name);
        comment = view.findViewById(R.id.text_cart_item_comment);
        subtotal = view.findViewById(R.id.text_cart_item_subtotal);

        view.setOnClickListener(this);
    }
    public void setOnClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onClick(view, getAdapterPosition(),false, null);
    }

}
