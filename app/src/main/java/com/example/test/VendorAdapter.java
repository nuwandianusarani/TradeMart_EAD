package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Vendor;
import java.util.ArrayList;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.VendorViewHolder> {

    private Context context;
    private ArrayList<Vendor> vendorList;
    private onVendorListener vendorListener;

    public VendorAdapter(Context context, ArrayList<Vendor> vendorList, onVendorListener vendorListener) {
        this.context = context;
        this.vendorList = vendorList;
        this.vendorListener = vendorListener;
    }

    @NonNull
    @Override
    public VendorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vendor, parent, false); // Change to your layout file name
        return new VendorViewHolder(v, vendorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewHolder holder, int position) {
        Vendor vendor = vendorList.get(position);
        holder.vendorName.setText(vendor.getVendorName());
    }

    @Override
    public int getItemCount() {
        return vendorList.size();
    }

    public static class VendorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView vendorName;
        onVendorListener vendorListener;

        public VendorViewHolder(@NonNull View itemView, onVendorListener vendorListener) {
            super(itemView);
            vendorName = itemView.findViewById(R.id.vendorName); // Ensure this ID matches your layout
            this.vendorListener = vendorListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            vendorListener.onVendorClick(getAdapterPosition());
        }
    }

    public interface onVendorListener {
        void onVendorClick(int position);
    }
}
