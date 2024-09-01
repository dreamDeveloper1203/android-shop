package store.technologycenter.android.listeners;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface CategoryClickListener {
    void onClick(View view, int position, boolean isLongClick, RecyclerView.ViewHolder viewHolder);
}