package com.nsquare.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.model.vegMenuList.DishExtrasModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuListModel;
import com.nsquare.restaurant.model.vegMenuList.VegMenuList_NewModel;
import com.nsquare.restaurant.util.Constants;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Pushkar on 07-09-2017.
 * Updated by Ritu on 01-08-2018.
 */
public class VegMenuListAdapter extends RecyclerView.Adapter<VegMenuListAdapter.MyViewHolder> {

    private ArrayList<VegMenuList_NewModel> upcomingYourBookingModelArrayList;
    private String[] quantityArray;
    private Context context;
    public static UpdateValuesInterface updateValuesInterface;
    private int totalQuantityCount = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView list_item_veg_menu_list_imageview_profile_pic;
        public TextView selected_quantity;
        public TextView list_item_veg_menu_list_textview_menu_name;
        public TextView list_item_veg_menu_list_textview_menu_price;
        public TextView list_item_veg_menu_list_textview_menu_description;
        public TextView list_item_veg_menu_list_textview_quantity;
        public TextView list_item_veg_menu_list_textview_customize;
        public LinearLayout adapter_vegmenu_linearlayout;
        public TextView list_item_veg_menu_list_imageview_add_general;
        public LinearLayout list_item_veg_menu_list_linearlayout_add_view;
        public LinearLayout list_item_veg_menu_list_linearlayout_count_view;
        public ImageView list_item_veg_menu_list_imageview_minus, list_item_veg_menu_list_imageview_plus, list_item_veg_menu_list_imageview_plus_general;

        public MyViewHolder(View view) {
            super(view);
            list_item_veg_menu_list_imageview_profile_pic = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_profile_pic);
            list_item_veg_menu_list_textview_menu_name = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_name);
            list_item_veg_menu_list_textview_menu_price = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_price);
            list_item_veg_menu_list_textview_menu_description = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_menu_description);
            list_item_veg_menu_list_textview_quantity = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_quantity);
            list_item_veg_menu_list_textview_customize = (TextView) view.findViewById(R.id.list_item_veg_menu_list_textview_customize);
           // list_item_veg_menu_list_imageview_add_general = (TextView) view.findViewById(R.id.list_item_veg_menu_list_imageview_add_general);
           // list_item_veg_menu_list_linearlayout_add_view = (LinearLayout) view.findViewById(R.id.list_item_veg_menu_list_linearlayout_add_view);
            list_item_veg_menu_list_linearlayout_count_view = (LinearLayout) view.findViewById(R.id.list_item_veg_menu_list_linearlayout_count_view);
            list_item_veg_menu_list_imageview_minus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_minus);
            list_item_veg_menu_list_imageview_plus = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus);
            adapter_vegmenu_linearlayout = (LinearLayout) view.findViewById(R.id.adapter_vegmenu_linearlayout);
            selected_quantity = (TextView) view.findViewById(R.id.selected_quantity);

           // list_item_veg_menu_list_imageview_plus_general = (ImageView) view.findViewById(R.id.list_item_veg_menu_list_imageview_plus_general);
        }
    }


    public VegMenuListAdapter(Context context, ArrayList<VegMenuList_NewModel> upcomingYourBookingModelArrayList) {
        this.upcomingYourBookingModelArrayList = upcomingYourBookingModelArrayList;
        this.context = context;
        quantityArray = new String[upcomingYourBookingModelArrayList.size()];
        totalQuantityCount = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_veg_menu_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final VegMenuList_NewModel issueItem = upcomingYourBookingModelArrayList.get(position);

        holder.selected_quantity.setText(issueItem.getSelected_dish_quantity());
        holder.selected_quantity.setTag(position);
        //add visibly status for customization
            if(issueItem.isDish_customizable()){
                //show label
                holder.list_item_veg_menu_list_textview_customize.setVisibility(View.VISIBLE);
            }else {
                //hide label
                holder.list_item_veg_menu_list_textview_customize.setVisibility(View.GONE);
            }

            if (issueItem.getDish_image().equalsIgnoreCase("")) {
            } else {
                Picasso.with(context).load(issueItem.getDish_image()).placeholder(R.drawable.app_icon).into(holder.list_item_veg_menu_list_imageview_profile_pic);
            }
            holder.list_item_veg_menu_list_textview_menu_name.setText(issueItem.getDish_name());
            holder.list_item_veg_menu_list_textview_menu_price.setText(context.getResources().getString(R.string.Rs) + " " + issueItem.getDish_price());
            holder.list_item_veg_menu_list_textview_menu_description.setText(issueItem.getDish_desc());


            holder.adapter_vegmenu_linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            final int[] quantityCount = {0};
           /* holder.list_item_veg_menu_list_imageview_plus_general.setOnClickListener(new View.OnClickListener() {
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

                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, "");
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

                        updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, "");
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

                    updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, "");

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

                        if (quantityCount[0] == 0) {
                            holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                            holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
                        }
                    } else {
                        holder.list_item_veg_menu_list_linearlayout_add_view.setVisibility(View.VISIBLE);
                        holder.list_item_veg_menu_list_linearlayout_count_view.setVisibility(View.GONE);
                    }

                    updateValuesInterface.onUpdateValuesInterface(quantityArray[position], position, totalQuantityCount, issueItem, "");
                }
            });
*/
          /*  holder.list_item_veg_menu_list_linearlayout_add_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add customisable view to edit menu
                    //check menu is already added or not
                    // for already added products open popup
                    // boolean isSelected = false;
                    //  if(isSelected) {
                }
            });*/
        }
        @Override
        public int getItemCount() {
            return upcomingYourBookingModelArrayList.size();
        }
        public interface UpdateValuesInterface {

            public void onUpdateValuesInterface(String quantity, int position, int totalQuantity, VegMenuListModel issueItem, String menu_status);
        }

    }
