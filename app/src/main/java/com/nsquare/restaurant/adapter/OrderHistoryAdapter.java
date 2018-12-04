package com.nsquare.restaurant.adapter;

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
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.OrderHistoryModel;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.InternetConnection;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private ArrayList<OrderHistoryModel> orderHistoryModelArrayList;
    private Context context;
    private Activity myActivity;
    private InternetConnection internetConnection = new InternetConnection();
    private SharedPreferences sharedPreferencesRemember;
    private SharedPreferences.Editor editor;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView list_item_order_history_textview_order_id;
        public TextView list_item_order_history_textview_amount;
        public TextView list_item_order_history_textview_status;
        public TextView list_item_order_history_textview_time;

        public MyViewHolder(View view)
        {
            super(view);
            list_item_order_history_textview_order_id = (TextView) view.findViewById(R.id.list_item_order_history_textview_order_id);
            list_item_order_history_textview_amount = (TextView) view.findViewById(R.id.list_item_order_history_textview_amount);
            list_item_order_history_textview_status = (TextView) view.findViewById(R.id.list_item_order_history_textview_status);
            list_item_order_history_textview_time = (TextView) view.findViewById(R.id.list_item_order_history_textview_time);
        }
    }


    public OrderHistoryAdapter(Context context, ArrayList<OrderHistoryModel> orderHistoryModelArrayList, Activity myActivity) {
        this.orderHistoryModelArrayList = orderHistoryModelArrayList;
        this.context = context;
        this.myActivity = myActivity;
        sharedPreferencesRemember = myActivity.getSharedPreferences(myActivity.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final OrderHistoryModel issueItem= orderHistoryModelArrayList.get(position);
        holder.list_item_order_history_textview_order_id.setText("Order id - "+issueItem.getOrderId());
        holder.list_item_order_history_textview_amount.setText("Bill - " +issueItem.getAmount());
        holder.list_item_order_history_textview_time.setText(issueItem.getTime());
        if(issueItem.getStatus().equalsIgnoreCase("0")){
            holder.list_item_order_history_textview_status.setVisibility(View.VISIBLE);
        }else if(issueItem.getStatus().equalsIgnoreCase("1")){
            holder.list_item_order_history_textview_status.setVisibility(View.INVISIBLE);
        }else if(issueItem.getStatus().equalsIgnoreCase("2")){
            holder.list_item_order_history_textview_status.setVisibility(View.INVISIBLE);
        }

        holder.list_item_order_history_textview_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setMessage(myActivity.getResources().getString(R.string.are_you_sure_cancel_order))
                        .setPositiveButton(myActivity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (internetConnection.isNetworkAvailable(myActivity)) {
                                    cancelOrder(position);
                                } else {
                                    Toast.makeText(myActivity, myActivity.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(myActivity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryModelArrayList.size();
    }

    private void cancelOrder(final int position) {

        String customerOrderId = sharedPreferencesRemember.getString(myActivity.getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(myActivity.getResources().getString(R.string.customerOrderId), customerOrderId);
        postParams.put(myActivity.getResources().getString(R.string.apiType), myActivity.getResources().getString(R.string.multiple_order)); //1 = Veg, 2 = Non Veg

        System.out.println("API response "+postParams.toString());
        ((ParentActivity) myActivity).showProcessingDialog();
        APIManager.requestPostMethod(myActivity, myActivity.getString(R.string.cancelOrder), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        orderHistoryModelArrayList.remove(position);
                        notifyDataSetChanged();

                        editor = sharedPreferencesRemember.edit();
                        editor.putString(myActivity.getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId), "");
                        editor.commit();

                        ((ParentActivity) myActivity).dismissProgressDialog();
                        Toast.makeText(myActivity, jsonObject.getString(myActivity.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    } else {
                        ((ParentActivity) myActivity).dismissProgressDialog();
                        Toast.makeText(myActivity, jsonObject.getString(myActivity.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((ParentActivity) myActivity).dismissProgressDialog();
            }
        });
    }
}
