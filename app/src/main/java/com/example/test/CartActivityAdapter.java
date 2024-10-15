package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartActivityAdapter extends RecyclerView.Adapter<CartActivityAdapter.CartViewHolder> {
    Context context;
    ArrayList<Product> productList;

    public CartActivityAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartActivityAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_cart_item, parent, false);
        return new CartActivityAdapter.CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.prodName.setText(product.getProductName());
        holder.prodPrice.setText(product.getPrice());
        holder.prodQuantity.setText(product.getAvailableQuantity());

        // If using Glide for image loading (optional)
        Picasso.get()
                .load(product.getImageUrl()) // You can use a placeholder if image URL is empty
                .into(holder.prodImage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder  {
        TextView prodName, prodPrice, prodQuantity;
        ImageView prodImage;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            prodName = itemView.findViewById(R.id.prodName);
            prodPrice = itemView.findViewById(R.id.prodPrice);
            prodQuantity = itemView.findViewById(R.id.prodQuantity);
            prodImage = itemView.findViewById(R.id.cartImg);
        }
    }
}

