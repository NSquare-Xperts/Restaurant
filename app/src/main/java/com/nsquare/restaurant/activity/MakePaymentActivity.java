package com.nsquare.restaurant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsquare.restaurant.adapter.OrderHistoryAdapter;
import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.fragment.HomeFragment;
import com.nsquare.restaurant.model.CartModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.MakePaymentCartItemsListAdapter;
import com.nsquare.restaurant.model.MyOrderDetails;
import com.nsquare.restaurant.model.MyOrderHistory;
import com.nsquare.restaurant.model.vegMenuList.VegMenuListModel;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;
/**
 * Created by Pushkar on 23-04-2018.
 * updated by Ritu Chavan on 12-12-2018
 */

public class MakePaymentActivity extends ParentActivity implements MakePaymentCartItemsListAdapter.UpdateValuesInterface {

    private RecyclerView fragment_recent_jobs_recycler_view;
    private Button button_add_more;
    private MakePaymentCartItemsListAdapter makePaymentCartItemsListAdapter;
    public static MakePaymentActivity cartPreviewActivity;
    private TextView activity_order_preview_textview_subtotal, activity_order_preview_textview_tax, activity_order_preview_textview_grand_total;
    private Button fragment_common_list_recycler_button_place_order;
    private double taxes = 0;
    private JSONObject jsonObjectCart;
    private ArrayList<MyOrderHistory> orderHistoryModelArrayList = new ArrayList<MyOrderHistory>();
    double subTotal = 0, grandTotal = 0, taxAmount = 0;
    private EditText activity_book_a_table_edittext_name, activity_book_a_table_edittext_mobile_number;
    private String mobileNumber, userName,sharedPreferencerole;
    private Activity activity;
    private Context context;
    private String order_id;
    RadioButton radio_mode;
    private LinearLayout linear_layout_to_hide;
    // private android.support.v4.app.FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.order_preview));
        findViewByIds();

        sharedPreferencerole = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencerole), "");
        //for staff show add more
        //check coming from
        if(getIntent().hasExtra(Constants.statusMakePayment)){

            if(sharedPreferencerole.equalsIgnoreCase("4")){
                fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
            }else{
                fragment_common_list_recycler_button_place_order.setText(getResources().getString(R.string.choose_payment_mode));
            }

            order_id = getIntent().getStringExtra("orderID");
            button_add_more.setVisibility(View.VISIBLE);
            linear_layout_to_hide.setVisibility(View.VISIBLE);
            //order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.table_wise_order_id),"");
        }else{

            fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
            mobileNumber = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberMobileno),"");
            userName = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberUsername),"");
            order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id),"");

            if(mobileNumber.equalsIgnoreCase(null) || mobileNumber.equalsIgnoreCase("")) {
                editor = sharedPreferencesRemember.edit();
                editor.putString(getResources().getString(R.string.sharedPreferenceRememberMobileno), activity_book_a_table_edittext_mobile_number.getText().toString());
                editor.putString(getResources().getString(R.string.sharedPreferenceRememberUsername), activity_book_a_table_edittext_name.getText().toString());
                editor.commit();
            }else {
                activity_book_a_table_edittext_name.setText(userName);
                activity_book_a_table_edittext_name.setEnabled(false);
                activity_book_a_table_edittext_mobile_number.setText(mobileNumber);
                activity_book_a_table_edittext_mobile_number.setEnabled(false);
            }

        }
        activity = MakePaymentActivity.this;
        cartPreviewActivity = this;
        context = this;
        getInstance();
        MakePaymentCartItemsListAdapter.updateValuesInterface = (MakePaymentCartItemsListAdapter.UpdateValuesInterface) cartPreviewActivity;

        // getOrder details
        if (internetConnection.isNetworkAvailable(getApplicationContext())) {
            //make payment
            getOrderListByCustomer();
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
        activity_order_preview_textview_tax.setText(taxes+"");

        fragment_common_list_recycler_button_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(validateForm()){
                if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                    //make payment
                    //open alert to show mode of transaction
                    alertToShowMakePaymentOption();
                }else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
                // }
            }
        });

        //add more items from menu
        button_add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MakePaymentActivity.this, MainActivity.class);
                intent.putExtra("Make","Make");
                startActivity(intent);
                /*android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HomeFragment fragment = new HomeFragment();

                Bundle args = new Bundle();
                args.putString("Make", "1");
                fragment.setArguments(args);

                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();*/
            }
        });

    }

    private void findViewByIds(){
        fragment_recent_jobs_recycler_view = (RecyclerView) findViewById(R.id.fragment_recent_jobs_recycler_view);
        activity_order_preview_textview_subtotal = (TextView) findViewById(R.id.activity_order_preview_textview_subtotal);
        activity_order_preview_textview_tax = (TextView) findViewById(R.id.activity_order_preview_textview_tax);
        activity_order_preview_textview_grand_total = (TextView) findViewById(R.id.activity_order_preview_textview_grand_total);
        fragment_common_list_recycler_button_place_order = (Button) findViewById(R.id.fragment_common_list_recycler_button_place_order);
        //fragment_common_list_recycler_button_place_order.setText(getResources().getString(R.string.payment));

        linear_layout_to_hide =  (LinearLayout) findViewById(R.id.relative_layout_to_hide);

        activity_book_a_table_edittext_name = (EditText) findViewById(R.id.activity_book_a_table_edittext_name);
        activity_book_a_table_edittext_mobile_number = (EditText) findViewById(R.id.activity_book_a_table_edittext_mobile_number);

        button_add_more = (Button)findViewById(R.id.button_add_more);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static MakePaymentActivity getInstance(){
        return cartPreviewActivity;
    }

    @Override
    public void onUpdateValuesInterface(String quantity, int position, int totalQuantity, CartModel issueItem, String menu_status) {

        CartModel cartModel = issueItem;

        int menuCount = databaseHelper.getCountOfMenuById(Integer.parseInt(cartModel.getDatabase_menu_id()), "111");

        if(menuCount == 0){
            databaseHelper.addInTo(cartModel);
        }else{
            databaseHelper.updateRecordById(Integer.parseInt(cartModel.getDatabase_menu_id()), "111", cartModel);
        }

        if(quantity.equalsIgnoreCase("0")){
            databaseHelper.deleteItem(Integer.parseInt(cartModel.getDatabase_menu_id()), "111");
        }else{
            cartModel.setDatabase_menu_quantity("1");
            databaseHelper.updateRecordById(Integer.parseInt(cartModel.getDatabase_menu_id()), "111", cartModel);
        }

        System.out.println("totalQuantity: "+totalQuantity);
        if(totalQuantity == 0){
            //fragment_common_list_recycler_button_view_cart.setVisibility(View.GONE);

            databaseHelper.deleteAll();
        }else{
            //fragment_common_list_recycler_button_view_cart.setVisibility(View.VISIBLE);
        }

        System.out.println("cart values: "+databaseHelper.getCartItemsByUserId("111").size());

        ArrayList<CartModel> cartModelArrayList = databaseHelper.getCartItemsByUserId("111");

        double subTotal = 0, grandTotal = 0;
        for(int i = 0; i < cartModelArrayList.size(); i++){
            subTotal = subTotal + (Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_quantity()) * Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_price()));
        }

        activity_order_preview_textview_subtotal.setText(getResources().getString(R.string.Rs)+" "+subTotal);

        taxAmount = (subTotal * taxes / 100);

        activity_order_preview_textview_tax.setText(getResources().getString(R.string.Rs)+" "+taxAmount);

        grandTotal = subTotal + taxAmount;

        activity_order_preview_textview_grand_total.setText(getResources().getString(R.string.Rs)+" "+grandTotal);

//        activity_order_preview_textview_subtotal.setText(getResources().getString(R.string.Rs)+" "+subTotal);
//
//        grandTotal = subTotal + taxes;
//
//        activity_order_preview_textview_grand_total.setText(getResources().getString(R.string.Rs)+" "+grandTotal);

        if(cartModelArrayList.size() > 0){
            //fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
        }else{
            //fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
        }

    }

    private boolean validateForm(){

        if(activity_book_a_table_edittext_name.getText().toString().trim().equalsIgnoreCase("")){
            activity_book_a_table_edittext_name.setError(getResources().getString(R.string.error_name));
            return false;
        }
        if(activity_book_a_table_edittext_mobile_number.getText().toString().trim().equalsIgnoreCase("")){
            activity_book_a_table_edittext_mobile_number.setError(getResources().getString(R.string.error_mobileno));
            return false;
        }
        return true;
    }
    private void getOrderListByCustomer() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_order_id), order_id);

        showProcessingDialog();
        APIManager.requestPostMethod(MakePaymentActivity.this, getResources().getString(R.string.getOrderDetails), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        String jsonOutput = jsonObject.getJSONArray(Constants.data).toString();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<MyOrderHistory>>() {
                        }.getType();
                        orderHistoryModelArrayList = gson.fromJson(jsonOutput, listType);
                        if (orderHistoryModelArrayList.size() > 0) {

                            //swipe_refresh_layout.setRefreshing(false);
                            if(sharedPreferencerole.equalsIgnoreCase("4")){
                                fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
                            }else{
                                fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
                            }

                            activity_order_preview_textview_subtotal.setText(orderHistoryModelArrayList.get(0).getBill_amount() + "");
                            activity_order_preview_textview_tax.setText(orderHistoryModelArrayList.get(0).getTax_amount() + "");
                            activity_order_preview_textview_grand_total.setText(orderHistoryModelArrayList.get(0).getTotal_amount() + "");

                            if(orderHistoryModelArrayList.get(0).getArrayList_order_menu_details().size() > 0){
                                makePaymentCartItemsListAdapter = new MakePaymentCartItemsListAdapter(context, orderHistoryModelArrayList, MakePaymentActivity.this,order_id);
                                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MakePaymentActivity.this);
                                linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
                                fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
                                fragment_recent_jobs_recycler_view.setAdapter(makePaymentCartItemsListAdapter);

                            }
                            fragment_recent_jobs_recycler_view.setEnabled(true);
                            //swipe_refresh_layout.setRefreshing(false);
                        } else {

                            fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
                            Toast.makeText(MakePaymentActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  swipe_refresh_layout.setRefreshing(false);
                dismissProgressDialog();
            }
        });

    }
    //Dialog to show make payment option

    public void alertToShowMakePaymentOption() {

        final android.support.v7.app.AlertDialog alert11;
        final android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(MakePaymentActivity.this);
        LayoutInflater factory = LayoutInflater.from(context);
        final View view1 = factory.inflate(R.layout.dialogbox_payment_mode, null);
        builder1.setView(view1);
        builder1.setCancelable(true);

        alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        //alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        if (!((Activity) context).isFinishing()) {
            //show dialog
            alert11.show();
        }

        Button button_submit = (Button) view1.findViewById(R.id.button_popup_submit);
        Button button_cancel = (Button) view1.findViewById(R.id.button_popup_no);

        final RadioGroup radio_grp_select=(RadioGroup)view1.findViewById(R.id.radio_grp_select);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=radio_grp_select.getCheckedRadioButtonId();
                radio_mode=(RadioButton)view1.findViewById(selectedId);
                //call payment API
                if (internetConnection.isNetworkAvailable(MakePaymentActivity.this)) {
                    selectPaymentMode();
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
            }
        });
    }

    private void selectPaymentMode() {

        // String order_id = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id),"");
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_order_id), order_id);
        showProcessingDialog();

        APIManager.requestPostMethod(activity, getString(R.string.makePayment), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        dismissProgressDialog();
                        Toast.makeText(activity, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    } else if(jsonObject.getInt("status") == 202){
                        /*SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                        editor.putString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId), "");
                        editor.commit();*/
                        dismissProgressDialog();
                        Toast.makeText(activity, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        dismissProgressDialog();
                        Toast.makeText(activity, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
            }
        });
    }
}
