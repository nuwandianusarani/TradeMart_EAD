package com.example.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.model.User;

import java.util.List;

public class UserDetailsAdaptor extends RecyclerView.Adapter<UserDetailsAdaptor.UserDetailsHolder> {

    private List<User > userList;
    private Context context;

    public UserDetailsAdaptor(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_ui, parent, false);
        return new UserDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailsHolder holder, int position) {
        Log.i("gggg", "onBindViewHolder: ");
        holder.email.setText(userList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserDetailsHolder extends RecyclerView.ViewHolder {

        private TextView email;

        public UserDetailsHolder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email);
        }
    }
}
