//package com.example.test;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.test.model.Order;
//import java.util.List;
//
//public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
//
//    private Context context;
//    private List<Order> orderList;
//    private onOrderListener orderListener;
//
//    // Constructor for initializing the adapter with context, order list, and click listener
//    public OrderAdapter(Context context, List<Order> orderList, onOrderListener orderListener) {
//        this.context = context;
//        this.orderList = orderList;
//        this.orderListener = orderListener;
//    }
//
//    @NonNull
//    @Override
//    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.user_order, parent, false);
//        return new OrderViewHolder(view, orderListener);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
//        Order order = orderList.get(position);
//        holder.email.setText(order.getEmail());
//        holder.deliveryAddress.setText(order.getDeliveryAddress());
//        holder.orderTotal.setText("Order Total: $" + order.getOrderTotal());
//        holder.status.setText("Status: " + order.getStatus());
//
//        // Optional: Display first product details if they exist
//        if (order.getOrderItems() != null && order.getOrderItems().size() > 0) {
//            holder.productName.setText("Product: " + order.getOrderItems().get(0).getProductName());
//            holder.quantity.setText("Quantity: " + order.getOrderItems().get(0).getQuantity());
//        } else {
//            holder.productName.setText("Product: N/A");
//            holder.quantity.setText("Quantity: N/A");
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return orderList.size();
//    }
//
//    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView email, deliveryAddress, orderTotal, productName, quantity, status;
//        onOrderListener orderListener;
//
//        // Constructor to initialize the UI components and listener
//        public OrderViewHolder(@NonNull View itemView, onOrderListener orderListener) {
//            super(itemView);
//            email = itemView.findViewById(R.id.email);
//            deliveryAddress = itemView.findViewById(R.id.deliveryAddress);
//            orderTotal = itemView.findViewById(R.id.orderTotal);
//            productName = itemView.findViewById(R.id.productName);
//            quantity = itemView.findViewById(R.id.quantity);
//            status = itemView.findViewById(R.id.status);
//            this.orderListener = orderListener;
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            orderListener.onOrderClick(getAdapterPosition());
//        }
//    }
//
//    // Interface to handle click events for each order
//    public interface onOrderListener {
//        void onOrderClick(int position);
//    }
//}