package com.nsquare.restaurant.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.OrderHistoryModel;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.InternetConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pushkar on 07-09-2017.
 */

public class MakePaymentCartItemsListAdapter extends RecyclerView.Adapter<MakePaymentCartItemsListAdapter.MyViewHolder> {

    private ArrayList<CartModel> upcomingYourBookingModelArrayList;
    private String[] quantityArray;
    private Context context;
    public static UpdateValuesInterface updateValuesInterface;
    private int totalQuantityCount = 0;
    private Activity myActivity;
    private InternetConnection internetConnection = new InternetConnection();
    private SharedPreferences sharedPreferencesRemember;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView list_item_veg_menu_list_imageview_profile_pic;
        public TextView list_item_veg_menu_list_textview_menu_name;
        public TextView list_item_veg_menu_list_textview_menu_price;
        public TextView list_item_veg_menu_list_textview_total;
        public TextView list_item_veg_menu_list_textview_menu_description;
        public TextView list_item_veg_menu_list_textview_quantity;
        public TextView list_item_veg_menu_list_imageview_add_general;
        public LinearLayout list_item_veg_menu_list_linearlayout_add_view;
        public LinearLayout list_item_veg_menu_list_linearlayout_count_view;
        public ImageView list_item_veg_menu_list_imageview_minus, list_item_veg_menu_list_imageview_plus, list_item_veg_menu_list_imageview_plus_general,
                list_item_selected_cart_menus_imageview_close;

        public MyViewHolder(View view) {
            super(view);
            list_item_veg_menu_list_imageview_profile_pic = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_profile_pic);
            list_item_veg_menu_list_textview_menu_name = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_name);
            list_item_veg_menu_list_textview_menu_price = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_price);
            list_item_veg_menu_list_textview_total = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_total);
            list_item_veg_menu_list_textview_menu_description = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_description);
            list_item_veg_menu_list_textview_quantity = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_quantity);
            //list_item_veg_menu_list_imageview_add_general = (TextView) view.findViewById(R.id.list_item_veg_menu_list_imageview_add_general);
           // list_item_veg_menu_list_linearlayout_add_view = (LinearLayout) view.findViewById(R.id.list_item_veg_menu_list_linearlayout_add_view);
            list_item_veg_menu_list_linearlayout_count_view = (LinearLayout) view.findViewById(R.id.list_item_veg_menu_list_linearlayout_count_view);
            list_item_veg_menu_list_imageview_minus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_minus);
            list_item_veg_menu_list_imageview_plus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus);
            //list_item_veg_menu_list_imageview_plus_general = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus_general);
            list_item_selected_cart_menus_imageview_close = (ImageView) view.findViewById(R.id.list_item_selected_cart_menus_imageview_close);
        }
    }


    public MakePaymentCartItemsListAdapter(Context context, ArrayList<CartModel> upcomingYourBookingModelArrayList, Activity myActivity) {
        this.upcomingYourBookingModelArrayList = upcomingYourBookingModelArrayList;
        this.context = context;
        this.myActivity = myActivity;
        quantityArray = new String[upcomingYourBookingModelArrayList.size()];
        totalQuantityCount = 0;
        sharedPreferencesRemember = myActivity.getSharedPreferences(myActivity.getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_selected_cart_menus, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CartModel issueItem = upcomingYourBookingModelArrayList.get(position);
        if (issueItem.getDatabase_menu_image().equalsIgnoreCase("")) {
        } else {
            Picasso.with(context).load(issueItem.getDatabase_menu_image()).placeholder(R.drawable.app_icon).into(holder.list_item_veg_menu_list_imageview_profile_pic);
        }
        holder.list_item_veg_menu_list_textview_menu_name.setText(issueItem.getDatabase_menu_name());
        holder.list_item_veg_menu_list_textview_menu_price.setText(context.getResources().getString(R.string.Rs) + " " + issueItem.getDatabase_menu_price());
        holder.list_item_veg_menu_list_textview_quantity.setText(issueItem.getDatabase_menu_quantity());
        int total = Integer.parseInt(issueItem.getDatabase_menu_price()) * Integer.parseInt(issueItem.getDatabase_menu_quantity());
        holder.list_item_veg_menu_list_textview_total.setText(total + "");
        //holder.list_item_veg_menu_list_textview_menu_description.setText(issueItem.getMe());

        holder.list_item_veg_menu_list_imageview_minus.setEnabled(false);
        holder.list_item_veg_menu_list_imageview_plus.setEnabled(false);
        holder.list_item_veg_menu_list_imageview_plus_general.setEnabled(false);

        System.out.println("getDatabase_menu_status " + issueItem.getDatabase_menu_status());
        if (issueItem.getDatabase_menu_status().equalsIgnoreCase("1")) {
            holder.list_item_selected_cart_menus_imageview_close.setEnabled(true);
            holder.list_item_selected_cart_menus_imageview_close.setVisibility(View.VISIBLE);
        } else {
            holder.list_item_selected_cart_menus_imageview_close.setEnabled(false);
            holder.list_item_selected_cart_menus_imageview_close.setVisibility(View.GONE);
        }

        holder.list_item_veg_menu_list_imageview_add_general.setEnabled(false);

        final int[] quantityCount = {0};

        quantityCount[0] = Integer.parseInt(issueItem.getDatabase_menu_quantity());


        if (quantityCount[0] > 0) {
            //quantityCount[0] = quantityCount[0] + 1;
            totalQuantityCount = totalQuantityCount + quantityCount[0];

            quantityArray[position] = String.valueOf(quantityCount[0]);

            holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
            holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
            holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
            holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

            if (quantityCount[0] == 0) {
                holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
            }
        } else {
            holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
            holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
        }

        holder.list_item_veg_menu_list_imageview_plus_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityCount[0] == 0) {
                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);

                    quantityCount[0] = quantityCount[0] + 1;
                    totalQuantityCount = totalQuantityCount + 1;

                    quantityArray[position] = String.valueOf(quantityCount[0]);

                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

                    updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                }
            }
        });

        holder.list_item_veg_menu_list_imageview_add_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityCount[0] == 0) {
                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);

                    quantityCount[0] = quantityCount[0] + 1;
                    totalQuantityCount = totalQuantityCount + 1;

                    quantityArray[position] = String.valueOf(quantityCount[0]);

                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

                    updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                }
            }
        });

        holder.list_item_veg_menu_list_imageview_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(quantityCount[0] > 0){
                quantityCount[0] = quantityCount[0] + 1;
                totalQuantityCount = totalQuantityCount + 1;

                quantityArray[position] = String.valueOf(quantityCount[0]);

                holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
                holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

                int total = Integer.parseInt(issueItem.getDatabase_menu_price()) * quantityCount[0];
                holder.list_item_veg_menu_list_textview_total.setText(total + "");
                issueItem.setDatabase_menu_quantity(quantityCount[0] + "");

                updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());

                //}
            }
        });

        holder.list_item_veg_menu_list_imageview_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityCount[0] > 0) {
                    quantityCount[0] = quantityCount[0] - 1;
                    totalQuantityCount = totalQuantityCount - 1;

                    quantityArray[position] = String.valueOf(quantityCount[0]);

                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

                    int total = Integer.parseInt(issueItem.getDatabase_menu_price()) * quantityCount[0];
                    holder.list_item_veg_menu_list_textview_total.setText(total + "");
                    issueItem.setDatabase_menu_quantity(quantityCount[0] + "");

                    if (quantityCount[0] == 0) {
                        holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                        holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
                        upcomingYourBookingModelArrayList.remove(position);

                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());

                        notifyDataSetChanged();
                    } else {
                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                    }
                } else {
                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);

                    updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                }
            }
        });

        holder.list_item_selected_cart_menus_imageview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setMessage(myActivity.getResources().getString(R.string.are_you_sure_cancel_order))
                .setPositiveButton(myActivity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (internetConnection.isNetworkAvailable(myActivity)) {
                            cancelOrder(position, issueItem);
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
        return upcomingYourBookingModelArrayList.size();
    }

    public interface UpdateValuesInterface {

        public void onUpdateValuesInterface(String quantity, int position, int totalQuantity, CartModel issueItem, String menu_status);
    }

    private void cancelOrder(final int position, final CartModel cartModel) {

        String customerOrderId = sharedPreferencesRemember.getString(myActivity.getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(myActivity.getResources().getString(R.string.customerOrderId), customerOrderId);
        postParams.put(myActivity.getResources().getString(R.string.order_menus_id), cartModel.getDatabase_order_menus_id());
        postParams.put(myActivity.getResources().getString(R.string.apiType), myActivity.getResources().getString(R.string.single_order)); //1 = Veg, 2 = Non Veg

        System.out.println("API response "+postParams.toString());
        ((ParentActivity) myActivity).showProcessingDialog();
        APIManager.requestPostMethod(myActivity, myActivity.getString(R.string.cancelOrder), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {

                        quantityArray[position] = "0";
                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, cartModel, cartModel.getDatabase_menu_status());
                        upcomingYourBookingModelArrayList.remove(position);
                        notifyDataSetChanged();
                        ((ParentActivity) myActivity).dismissProgressDialog();
                        Toast.makeText(myActivity, jsonObject.getString(myActivity.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getInt("status") == 201) {
                        //quantityArray[position] = "0";
                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, cartModel, cartModel.getDatabase_menu_status());

                        notifyDataSetChanged();
                        ((ParentActivity) myActivity).dismissProgressDialog();
                        Toast.makeText(myActivity, jsonObject.getString(myActivity.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }else if(jsonObject.getInt("status") == 202){
                        SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                        editor.putString(myActivity.getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId), "");
                        editor.commit();

                        ((ParentActivity) myActivity).dismissProgressDialog();
                        Toast.makeText(myActivity, jsonObject.getString(myActivity.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                    else {
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
