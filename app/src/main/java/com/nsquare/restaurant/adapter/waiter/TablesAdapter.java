package com.nsquare.restaurant.adapter.waiter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.model.OrderHistoryModel;
import com.nsquare.restaurant.model.TablesItem;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.InternetConnection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.MyViewHolder> {

    private ArrayList<TablesItem> orderHistoryModelArrayList;
    private Context context;
    private Activity myActivity;
    private InternetConnection internetConnection = new InternetConnection();
    private SharedPreferences sharedPreferencesRemember;
    private SharedPreferences.Editor editor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_tables_table_number;

        public MyViewHolder(View view)
        {
            super(view);
            list_item_tables_table_number = (TextView) view.findViewById(R.id.list_item_tables_table_number);
        }
    }


    public TablesAdapter(Context context, ArrayList<TablesItem> orderHistoryModelArrayList, Activity myActivity) {
        this.orderHistoryModelArrayList = orderHistoryModelArrayList;
        this.context = context;
        this.myActivity = myActivity;
        sharedPreferencesRemember = myActivity.getSharedPreferences(myActivity.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_tables, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final TablesItem issueItem= orderHistoryModelArrayList.get(position);
        holder.list_item_tables_table_number.setText("Table No: - "+issueItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return orderHistoryModelArrayList.size();
    }
}
