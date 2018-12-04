package com.nsquare.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsquare.restaurant.model.MyAddressesModel;

import com.nsquare.restaurant.R;

import java.util.ArrayList;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class MyAddressesAdapter extends RecyclerView.Adapter<MyAddressesAdapter.MyViewHolder> {

    private ArrayList<MyAddressesModel> myAddressesModelArrayList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_my_addresses_textview_address;

        public MyViewHolder(View view)
        {
            super(view);
            list_item_my_addresses_textview_address = (TextView) view.findViewById(R.id.list_item_my_addresses_textview_address);
        }
    }


    public MyAddressesAdapter(Context context, ArrayList<MyAddressesModel> myAddressesModelArrayList) {
        this.myAddressesModelArrayList = myAddressesModelArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_my_addresses, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        MyAddressesModel issueItem= myAddressesModelArrayList.get(position);
        holder.list_item_my_addresses_textview_address.setText(issueItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return myAddressesModelArrayList.size();
    }
}
