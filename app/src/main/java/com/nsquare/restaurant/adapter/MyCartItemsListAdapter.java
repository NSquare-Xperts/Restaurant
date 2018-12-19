package com.nsquare.restaurant.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.model.CartModel;
import com.nsquare.restaurant.model.MyCartData;
import com.nsquare.restaurant.model.MyOrderDetails;
import com.nsquare.restaurant.model.MyOrderHistory;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by Pushkar on 07-09-2017.
 * Updated by Ritu Chavan on 06-12-2018.
 */
public class MyCartItemsListAdapter extends RecyclerView.Adapter<MyCartItemsListAdapter.MyViewHolder> {

    private ArrayList<MyCartData> upcomingYourBookingModelArrayList;
    //private ArrayList<MyOrderHistory> orderHistoryModelArrayList = new ArrayList<>();
    private String[] quantityArray;
    private Context context;
    //private String isComingFrom;
    public static IsQuantityChanged isQuantityChanged;
    private int totalQuantityCount = 0;

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

        public TextView list_item_status_bottom;
        public ImageView list_item_selected_cart_menus_imageview_close_top;
        public TextView list_item_status;

        public MyViewHolder(View view) {
            super(view);
            list_item_veg_menu_list_imageview_profile_pic = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_profile_pic);
            list_item_veg_menu_list_textview_menu_name = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_name);
            list_item_veg_menu_list_textview_menu_price = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_price);
            list_item_veg_menu_list_textview_total = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_total);
            list_item_veg_menu_list_textview_menu_description = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_description);
            list_item_veg_menu_list_textview_quantity = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_quantity);

            list_item_status_bottom = (TextView) view.findViewById(R.id.list_item_status_bottom);
            list_item_selected_cart_menus_imageview_close_top = (ImageView) view.findViewById(R.id.list_item_selected_cart_menus_imageview_close_top);
            list_item_status = (TextView) view.findViewById(R.id.list_item_status);

            //list_item_veg_menu_list_imageview_add_general = (TextView) view.findViewById(R.id.list_item_veg_menu_list_imageview_add_general);
            //list_item_veg_menu_list_linearlayout_add_view = (LinearLayout) view.findViewById(R.id.list_item_veg_menu_list_linearlayout_add_view);
            list_item_veg_menu_list_linearlayout_count_view = (LinearLayout) view.findViewById(R.id.list_item_veg_menu_list_linearlayout_count_view);
            list_item_veg_menu_list_imageview_minus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_minus);
            list_item_veg_menu_list_imageview_plus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus);
            //list_item_veg_menu_list_imageview_plus_general = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus_general);
            list_item_selected_cart_menus_imageview_close = (ImageView) view.findViewById(R.id.list_item_selected_cart_menus_imageview_close);
        }
    }

    public MyCartItemsListAdapter(Context context, ArrayList<MyCartData> upcomingYourBookingModelArrayList) {
        this.upcomingYourBookingModelArrayList = upcomingYourBookingModelArrayList;
        this.context = context;
        //this.isComingFrom = isComingFrom;
        //this.orderHistoryModelArrayList = orderHistoryModelArrayList;
        quantityArray = new String[upcomingYourBookingModelArrayList.size()];
        totalQuantityCount = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_selected_cart_menus, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MyCartData issueItem = upcomingYourBookingModelArrayList.get(position);
        /*if (issueItem.getDatabase_menu_image().equalsIgnoreCase("")) {
        } else {
            Picasso.with(context).load(issueItem.getDatabase_menu_image()).placeholder(R.drawable.app_icon).into(holder.list_item_veg_menu_list_imageview_profile_pic);
        }*/
       /* if (isComingFrom.equalsIgnoreCase(Constants.order_history)){
            holder.list_item_veg_menu_list_imageview_minus.setEnabled(false);
            holder.list_item_veg_menu_list_imageview_plus.setEnabled(false);

            // status "is_processed": 0=Pending, 1=Cooking, 2=Cancelled, 3=Delivered
            if(orderHistoryModelArrayList.get(position).getArrayList_order_menu_details().get(position).getIs_processed().equalsIgnoreCase("0")){

                holder.list_item_selected_cart_menus_imageview_close_top.setVisibility(View.VISIBLE);
                holder.list_item_selected_cart_menus_imageview_close.setVisibility(View.GONE);
                holder.list_item_status.setVisibility(View.GONE);
                holder.list_item_status_bottom.setVisibility(View.VISIBLE);

            }else{

                holder.list_item_selected_cart_menus_imageview_close_top.setVisibility(View.GONE);
                holder.list_item_selected_cart_menus_imageview_close.setVisibility(View.GONE);
                holder.list_item_status.setVisibility(View.GONE);
                holder.list_item_status_bottom.setVisibility(View.VISIBLE);
            }



        }*/
        holder.list_item_veg_menu_list_textview_menu_name.setText(issueItem.getDish_name());
        holder.list_item_veg_menu_list_textview_menu_price.setText(context.getResources().getString(R.string.Rs) + " " + issueItem.getDish_price());
        holder.list_item_veg_menu_list_textview_quantity.setText(issueItem.getDish_quantity());
        holder.list_item_veg_menu_list_textview_total.setText(issueItem.getDish_total_price());
        //holder.list_item_veg_menu_list_textview_menu_description.setText(issueItem.getMe());

        final int[] quantityCount = { Integer.parseInt(issueItem.getDish_quantity())};
        totalQuantityCount = Integer.parseInt(issueItem.getDish_quantity());
        holder.list_item_veg_menu_list_imageview_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantityCount[0] = quantityCount[0] - 1;
                totalQuantityCount = totalQuantityCount - 1;

                quantityArray[position] = String.valueOf(quantityCount[0]);
                holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");
                if (quantityCount[0] == 1) {
                    //Toast.makeText(context, "Are you sure to remove this item", Toast.LENGTH_SHORT).show();
                    alertRemoveItem(issueItem.getDish_id());
                }else {
                   // holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    //holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");
                    //isQuantityChanged.checkQuantityIsChangedOrNot("1");
                    addMenuToCart(issueItem.getDish_id(), quantityCount[0] + "", Constants.table_id, issueItem.getDish_extra_id());
                }

            }
        });

        holder.list_item_veg_menu_list_imageview_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantityCount[0] = quantityCount[0] + 1;
                totalQuantityCount = totalQuantityCount + 1;

                quantityArray[position] = String.valueOf(quantityCount[0]);
                holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");
                //isQuantityChanged.checkQuantityIsChangedOrNot("1");

                addMenuToCart(issueItem.getDish_id(),quantityCount[0] + "", Constants.table_id,issueItem.getDish_extra_id());

            }
        });

        //remove Item from cart
        holder.list_item_selected_cart_menus_imageview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertRemoveItem(issueItem.getDish_id());
                //quantityArray[position] = "0";
                //updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());

               // notifyDataSetChanged();
            }
        });


       /* holder.list_item_veg_menu_list_imageview_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                *//*if (quantityCount[0] > 0) {
                    quantityCount[0] = quantityCount[0] - 1;
                    totalQuantityCount = totalQuantityCount - 1;

                    quantityArray[position] = String.valueOf(quantityCount[0]);

                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

                   // int total = Integer.parseInt(issueItem.getDatabase_menu_price()) * quantityCount[0];
                   // holder.list_item_veg_menu_list_textview_total.setText(total+"");
                   // issueItem.setDatabase_menu_quantity(quantityCount[0]+"");

                    if (quantityCount[0] == 0) {
                        holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                        holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
                        upcomingYourBookingModelArrayList.remove(position);

                        //updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());

                        notifyDataSetChanged();
                    }else{
                        //updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                    }
                } else {
                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);

                    //updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                }*//*
            }
        });


        holder.list_item_veg_menu_list_imageview_plus_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
                *//*if (quantityCount[0] == 0) {
                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);

                    quantityCount[0] = quantityCount[0] + 1;
                    totalQuantityCount = totalQuantityCount + 1;

                    quantityArray[position] = String.valueOf(quantityCount[0]);

                    holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.GONE);
                    holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.VISIBLE);
                    holder.list_item_veg_menu_list_textview_quantity.setText(quantityCount[0] + "");
                    holder.list_item_veg_menu_list_textview_quantity.setTag(quantityCount[0] + "");

                    //updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());
                } *//*
            }
        });
*/







/*
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
                holder.list_item_veg_menu_list_textview_total.setText(total+"");
                issueItem.setDatabase_menu_quantity(quantityCount[0]+"");

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
                    holder.list_item_veg_menu_list_textview_total.setText(total+"");
                    issueItem.setDatabase_menu_quantity(quantityCount[0]+"");

                    if (quantityCount[0] == 0) {
                        holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                        holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
                        upcomingYourBookingModelArrayList.remove(position);

                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());

                        notifyDataSetChanged();
                    }else{
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
                quantityArray[position] = "0";
                updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, issueItem.getDatabase_menu_status());

                notifyDataSetChanged();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return upcomingYourBookingModelArrayList.size();
    }

    /*public interface UpdateValuesInterface {

        public void onUpdateValuesInterface(String quantity, int position, int totalQuantity, CartModel issueItem, String menu_status);
    }*/


    //pop of remove item
    public void alertRemoveItem(final String dish_id){

        final AlertDialog alert11;
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);

        LayoutInflater factory = LayoutInflater.from(context);
        final View view1 = factory.inflate(R.layout.dialog_remove_item, null);
        builder1.setView(view1);
        builder1.setCancelable(true);

        alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert11.show();
        Button button_popup_no = (Button) view1.findViewById(R.id.button_popup_no);
        Button button_popup_yes = (Button) view1.findViewById(R.id.button_popup_yes);

        button_popup_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });

        button_popup_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
                if (((ParentActivity)context).internetConnection.isNetworkAvailable(context)) {
                    removeMenuFromCart(dish_id);
                } else {
                    Toast.makeText(context, context.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void removeMenuFromCart(String dish_id) {

        ((ParentActivity)context).showProcessingDialog();

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(context.getResources().getString(R.string.field_dish),dish_id);
        postParams.put(context.getResources().getString(R.string.field_table_id),Constants.table_id);

        APIManager.requestPostMethod(context, context.getResources().getString(R.string.removeCart), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        ((ParentActivity)context).dismissProgressDialog();
                        Toast.makeText(context, jsonObject.getString(context.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        //refresh page
                        /*if (((ParentActivity)context).internetConnection.isNetworkAvailable(context)) {
                            getMenuListByType();
                        } else {
                            Toast.makeText(context,context.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                        }*/
                    }else if (jsonObject.getInt("status") == 400) {
                        ((ParentActivity)context).dismissProgressDialog();
                        Toast.makeText(context, jsonObject.getString(context.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((ParentActivity)context).dismissProgressDialog();
                }
            }
        });
    }

    public void updateList(ArrayList<MyCartData> data ) {
        this.upcomingYourBookingModelArrayList = data;
        notifyDataSetChanged();
    }
  public interface IsQuantityChanged {

      public void checkQuantityIsChangedOrNot(String data);
  }

  //add to cart API
  // on evry +,_ update menu
  private void addMenuToCart(String dish_id, String dish_qty, String table_no, String dish_extra_id) {

      ((ParentActivity)context).showProcessingDialog();

      HashMap<String, String> postParams = new HashMap<>();
      postParams.put(context.getResources().getString(R.string.field_dish),dish_id);
      postParams.put(context.getResources().getString(R.string.field_dish_quantity),dish_qty);
      postParams.put(context.getResources().getString(R.string.field_table),"1");
      // send ids by comma seperated and it is optional
      postParams.put(context.getResources().getString(R.string.field_dishes_extra_id), dish_extra_id);

      APIManager.requestPostMethod(context, context.getString(R.string.addCart), postParams, new APIManager.VolleyCallback() {
          @Override
          public void onSuccess(String result) {
              try {
                  JSONObject jsonObject = new JSONObject(result);
                  if (jsonObject.getInt("status") == 200) {
                      ((ParentActivity)context).dismissProgressDialog();
                      Toast.makeText(context, jsonObject.getString(context.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                      //refresh
                      if (((ParentActivity)context).internetConnection.isNetworkAvailable(context)) {
                          //getMenuListByType();
                      } else {
                          Toast.makeText(context, context.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                      }

                  }else if (jsonObject.getInt("status") == 400) {
                      ((ParentActivity)context).dismissProgressDialog();
                      Toast.makeText(context, jsonObject.getString(context.getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                  }
              } catch (Exception e) {
                  e.printStackTrace();
                  ((ParentActivity)context).dismissProgressDialog();
              }
          }
      });
  }
}
