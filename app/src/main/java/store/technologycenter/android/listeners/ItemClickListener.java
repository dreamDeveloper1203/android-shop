package store.technologycenter.android.listeners;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick, RecyclerView.ViewHolder viewHolder);
}