package store.technologycenter.android.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import store.technologycenter.android.R;
import store.technologycenter.android.listeners.ItemClickListener;

public class CategoryFilterRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private ItemClickListener mItemClickListener;
    public TextView textViewName;
    public CheckBox checkBox;

    public CategoryFilterRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewName = itemView.findViewById(R.id.category_name_filter_row_item);
        checkBox = itemView.findViewById(R.id.category_filter_checkbox);

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