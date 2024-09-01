package store.technologycenter.android.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import store.technologycenter.android.R;
import store.technologycenter.android.listeners.ItemClickListener;

public class CategoryRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener mItemClickListener;
    public ImageView imageView;
    public TextView textViewName;
    public CardView cardView;

    public CategoryRecyclerViewHolder(@NonNull View view) {
        super(view);
        imageView = view.findViewById(R.id.iv_category);
        textViewName = view.findViewById(R.id.tv_category_name);
        cardView = view.findViewById(R.id.cv_category);

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
