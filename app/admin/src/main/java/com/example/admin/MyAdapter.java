package com.example.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Customer> list;

    public MyAdapter(Context context, ArrayList<Customer> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.items,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Customer customer = list.get(position);

        holder.Token.setText(customer.getToken());
        holder.Email.setText(customer.getEmail());
        holder.phoneNumber.setText(customer.phoneNumber);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView Token,Email,phoneNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Token = itemView.findViewById(R.id.token);
            Email = itemView.findViewById(R.id.email);
            phoneNumber = itemView.findViewById(R.id.phone);


        }
    }
}