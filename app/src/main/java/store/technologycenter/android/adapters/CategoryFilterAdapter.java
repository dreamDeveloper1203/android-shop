package store.technologycenter.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import store.technologycenter.android.R;
import store.technologycenter.android.holders.CategoryFilterRecyclerViewHolder;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.services.FilterService;
import org.jetbrains.annotations.NotNull;
import io.realm.RealmResults;

public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterRecyclerViewHolder>{

    private final RealmResults<Category> mCategories;
    Context mContext;

    public CategoryFilterAdapter(Context context, RealmResults<Category> categories){
        this.mContext = context;
        this.mCategories = categories;
    }

    @NonNull
    public CategoryFilterRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.categories_filter_row_item, parent,false);
        return new CategoryFilterRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NotNull final CategoryFilterRecyclerViewHolder holder, final int position) {
        final Category category = mCategories.get(position);

        assert category != null;

        holder.textViewName.setText(category.getName());

        holder.checkBox.setChecked(FilterService.categories.contains(category.getName()));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (FilterService.categories.contains(category.getName())) {
                FilterService.categories.remove(category.getName());
            }else{
                FilterService.categories.add(category.getName());
            }
        });

        holder.setOnClickListener((view, position1, isLongClick, i) -> {
            holder.checkBox.setChecked(!holder.checkBox.isChecked());
            if(holder.checkBox.isChecked()){
                FilterService.categories.add(category.getName());
            }else{
                FilterService.categories.remove(category.getName());
            }
        });
    }
    @Override
    public void onViewRecycled(@NonNull CategoryFilterRecyclerViewHolder holder) {
        holder.checkBox.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
    }
    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
