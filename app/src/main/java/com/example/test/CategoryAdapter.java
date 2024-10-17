package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Category;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> categoryList;
    private onCategoryListener categoryListener;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList, onCategoryListener categoryListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryListener = categoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.category, parent, false);
        return new CategoryViewHolder(v, categoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.categoryName.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryName;
        onCategoryListener categoryListener;

        public CategoryViewHolder(@NonNull View itemView, onCategoryListener categoryListener) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            this.categoryListener = categoryListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            categoryListener.onCategoryClick(getAdapterPosition());
        }
    }

    public interface onCategoryListener {
        void onCategoryClick(int position);
    }
}
