package store.technologycenter.android.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import io.realm.RealmResults;
import store.technologycenter.android.R;
import store.technologycenter.android.holders.CategoryRecyclerViewHolder;
import store.technologycenter.android.models.Category;
import store.technologycenter.android.services.ImageService;
import store.technologycenter.android.ui.home.CategoryFragmentDirections;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryRecyclerViewHolder> {

    private final RealmResults<Category> mCategories;
    private final Context mContext;

    public CategoryAdapter(Context context, RealmResults<Category> categories) {
        this.mContext = context;
        this.mCategories = categories;
    }

    @NonNull
    public CategoryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_row_item, parent, false);
        return new CategoryRecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NotNull final CategoryRecyclerViewHolder holder, final int position) {
        final Category category = mCategories.get(position);
        assert category != null;

        ImageService.setImage(category.getImageUrl() + "?w=500&h=500&fit=crop-center&bg=white&fm=webp", holder.imageView, ImageService.WIDTH_SM, ImageService.HEIGHT_SM);
        holder.textViewName.setText(category.getName());

        holder.setOnClickListener((view, position1, isLongClick, viewHolder) -> {
            try {
                CategoryFragmentDirections.ActionNavigationCategoryToNavigationProducts action =
                        CategoryFragmentDirections.actionNavigationCategoryToNavigationProducts(category.getSlug());
                Navigation.findNavController(view).navigate((NavDirections) action);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public Category getItem(int position) {
        return mCategories.get(position);
    }
}