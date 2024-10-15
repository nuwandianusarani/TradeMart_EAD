package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import com.example.test.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private ArrayList<Product> products;
    private Context context;



    public ProductAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the product at the current position
        Product product = products.get(position);

        // Set product data to the corresponding views
        if (product.getProductName() != null) {
            holder.productName.setText(product.getProductName());
        }

        if (product.getPrice() != null) {
            holder.productPrice.setText("Price: " + product.getPrice());
        }

//        if (product.getAvailableQuantity() != null) {
//            holder.productAvailableQuantity.setText("Available: " + product.getAvailableQuantity());
//        }

//        if (product.getDescription() != null) {
//            holder.productDescription.setText(product.getDescription());
//        }

        Picasso.get()
                .load(product.getImageUrl()) // Load product image URL dynamically
//                .placeholder(R.drawable.placeholder_image) // Placeholder image
//                .error(R.drawable.error_image) // Error image if URL fails
                .into(holder.productImg); // ImageView in the layout


        // Load image using Glide
//        Glide.with(context)
//                .load("http://127.0.0.1:3000/images/" + product.getImage()) // Adjust the base URL as needed
//                .into(holder.productImg);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productAvailableQuantity, productDescription;
        ImageView productImg;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
//            productAvailableQuantity = itemView.findViewById(R.id.productAvailableQuantity);
//            productDescription = itemView.findViewById(R.id.productDescription);
            productImg = itemView.findViewById(R.id.productImg);
        }
    }
}