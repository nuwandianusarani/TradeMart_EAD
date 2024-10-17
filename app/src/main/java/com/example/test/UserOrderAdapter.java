package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.test.model.Order;
import com.example.test.model.UserOrder;

import java.util.ArrayList;
public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<UserOrder> orderList;


    public UserOrderAdapter(Context context, ArrayList<UserOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_order, parent, false); // Use your order item layout
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        UserOrder userorder = orderList.get(position);
        holder.productName.setText(userorder.getProductName());
        holder.quantity.setText("Item Quantity :"+String.valueOf(userorder.getQuantity()));
        holder.orderTotal.setText("Total Amount :"+String.valueOf(userorder.getTotalPrice()));
        holder.status.setText("Order Status :"+getStatusText(userorder.getOrderItemStatus()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView email, productName, quantity, orderTotal, status;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email);
            productName = itemView.findViewById(R.id.productName);
            quantity = itemView.findViewById(R.id.quantity);
            orderTotal = itemView.findViewById(R.id.orderTotal);
            status = itemView.findViewById(R.id.status);
        }
    }

    private String getStatusText(int status) {
        switch (status) {
            case 0:
                return "Pending";
            case 1:
                return "Dispatched";
            case 2:
                return "Completed";
            case 3:
                return "Cancelled";
            case 4:
                return "Request to Cancel";
            case 5:
                return "Partially Delivered";
            default:
                return "Unknown";
        }
    }

}