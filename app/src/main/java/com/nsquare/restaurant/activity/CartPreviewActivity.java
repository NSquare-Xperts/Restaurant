package com.nsquare.restaurant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.nsquare.restaurant.adapter.VegMenuListAdapter;
import com.nsquare.restaurant.databasehelper.DatabaseHelper;
import com.nsquare.restaurant.model.CartModel;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.adapter.CartItemsListAdapter;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pushkar on 23-04-2018.
 */

public class CartPreviewActivity extends ParentActivity implements CartItemsListAdapter.UpdateValuesInterface {

    private RecyclerView fragment_recent_jobs_recycler_view;
    private ArrayList<CartModel> cartModelArrayList = new ArrayList<CartModel>();
    private DatabaseHelper databaseHelper;
    private CartItemsListAdapter cartItemsListAdapter;
    public static CartPreviewActivity cartPreviewActivity;
    private TextView activity_order_preview_textview_subtotal, activity_order_preview_textview_tax, activity_order_preview_textview_grand_total;
    private Button fragment_common_list_recycler_button_place_order;
    private double taxes = 0;
    private JSONObject jsonObjectCart;
    double subTotal = 0, grandTotal = 0, taxAmount = 0;
    private EditText activity_book_a_table_edittext_name, activity_book_a_table_edittext_mobile_number;
    private String mobileNumber, userName, customer_id = "", customerOrderId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.order_preview));
        findViewByIds();

        mobileNumber = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberMobileno),"");
        userName = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberUsername),"");
        customer_id = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerId),"");
        customerOrderId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");

        if(mobileNumber.equalsIgnoreCase(null) || mobileNumber.equalsIgnoreCase("")) {
            editor = sharedPreferencesRemember.edit();
            editor.putString(getResources().getString(R.string.sharedPreferenceRememberMobileno), activity_book_a_table_edittext_mobile_number.getText().toString());
            editor.putString(getResources().getString(R.string.sharedPreferenceRememberUsername), activity_book_a_table_edittext_name.getText().toString());
            //customer_id = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerId),"");
            //customerOrderId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId),"");
            editor.commit();
        }else {
            activity_book_a_table_edittext_name.setText(userName);
            activity_book_a_table_edittext_name.setEnabled(false);
            activity_book_a_table_edittext_mobile_number.setText(mobileNumber);
            activity_book_a_table_edittext_mobile_number.setEnabled(false);
        }

        cartPreviewActivity = this;
        getInstance();
        CartItemsListAdapter.updateValuesInterface = (CartItemsListAdapter.UpdateValuesInterface) cartPreviewActivity;

        databaseHelper = new DatabaseHelper(getApplicationContext());
        cartModelArrayList = databaseHelper.getAllCartItem(1);

        if(cartModelArrayList.size() > 0){
            fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
        }else{
            fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
        }

        cartItemsListAdapter = new CartItemsListAdapter(getApplicationContext(),cartModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        fragment_recent_jobs_recycler_view.setLayoutManager(linearLayoutManager);
        fragment_recent_jobs_recycler_view.setAdapter(cartItemsListAdapter);

        activity_order_preview_textview_tax.setText(taxes+"");

        if(cartModelArrayList.size() > 0){
            getMastervalues();
        }

//        if(internetConnection.isNetworkAvailable(getApplicationContext())){
//            getCartItems();
//        }else{
//            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
//        }

        fragment_common_list_recycler_button_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateForm()){
                    if (internetConnection.isNetworkAvailable(getApplicationContext())){
                        placeOrder();
                    }else{
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void findViewByIds(){
        fragment_recent_jobs_recycler_view = (RecyclerView) findViewById(R.id.fragment_recent_jobs_recycler_view);
        activity_order_preview_textview_subtotal = (TextView) findViewById(R.id.activity_order_preview_textview_subtotal);
        activity_order_preview_textview_tax = (TextView) findViewById(R.id.activity_order_preview_textview_tax);
        activity_order_preview_textview_grand_total = (TextView) findViewById(R.id.activity_order_preview_textview_grand_total);
        fragment_common_list_recycler_button_place_order = (Button) findViewById(R.id.fragment_common_list_recycler_button_place_order);
        activity_book_a_table_edittext_name = (EditText) findViewById(R.id.activity_book_a_table_edittext_name);
        activity_book_a_table_edittext_mobile_number = (EditText) findViewById(R.id.activity_book_a_table_edittext_mobile_number);
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

    public static CartPreviewActivity getInstance(){
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

        grandTotal = subTotal + taxes;

        activity_order_preview_textview_grand_total.setText(getResources().getString(R.string.Rs)+" "+grandTotal);

        if(cartModelArrayList.size() > 0){
            fragment_common_list_recycler_button_place_order.setVisibility(View.VISIBLE);
        }else{
            fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
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


    private void placeOrder() {
        int isFirstTime = 0;
        try {

            mobileNumber = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberMobileno),"");
            userName = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberUsername),"");

            if(mobileNumber.equalsIgnoreCase(null) || mobileNumber.equalsIgnoreCase("")) {
                editor = sharedPreferencesRemember.edit();
                editor.putString(getResources().getString(R.string.sharedPreferenceRememberMobileno), activity_book_a_table_edittext_mobile_number.getText().toString());
                editor.putString(getResources().getString(R.string.sharedPreferenceRememberUsername), activity_book_a_table_edittext_name.getText().toString());
                editor.commit();

                isFirstTime = 0;
            }else {
                isFirstTime = 1;
            }

            jsonObjectCart = new JSONObject();
            JSONArray jsonArrayCart = new JSONArray();
            for(int i = 0; i < cartModelArrayList.size(); i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(getResources().getString(R.string.database_menu_id), cartModelArrayList.get(i).getDatabase_menu_id());
                jsonObject.put(getResources().getString(R.string.database_menu_quantity), cartModelArrayList.get(i).getDatabase_menu_quantity());
                jsonObject.put(getResources().getString(R.string.database_menu_price), cartModelArrayList.get(i).getDatabase_menu_price());
                jsonObject.put(getResources().getString(R.string.database_menu_name), cartModelArrayList.get(i).getDatabase_menu_name());
                jsonObject.put(getResources().getString(R.string.database_user_id), cartModelArrayList.get(i).getDatabase_user_id());
                jsonObject.put(getResources().getString(R.string.database_menu_amount), (Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_quantity()) * Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_price()))+"");
                jsonArrayCart.put(jsonObject);
            }
            jsonObjectCart.put(getResources().getString(R.string.cart_items), jsonArrayCart);
            jsonObjectCart.put(getResources().getString(R.string.sub_total), subTotal);
            jsonObjectCart.put(getResources().getString(R.string.grand_total), grandTotal);
            jsonObjectCart.put(getResources().getString(R.string.user_name), activity_book_a_table_edittext_name.getText().toString());
            jsonObjectCart.put(getResources().getString(R.string.mobile_number_), activity_book_a_table_edittext_mobile_number.getText().toString());
            jsonObjectCart.put(getResources().getString(R.string.isFirstTime), isFirstTime);
            jsonObjectCart.put(getResources().getString(R.string.tableId), getResources().getString(R.string.tableIdValues));
            jsonObjectCart.put(getResources().getString(R.string.taxes_), taxAmount);
            jsonObjectCart.put(getResources().getString(R.string.customer_id), customer_id);
            jsonObjectCart.put(getResources().getString(R.string.customerOrderId), customerOrderId);
            jsonObjectCart.put(getResources().getString(R.string.isMakePayment), 0);

            System.out.println("placeOrder: "+jsonObjectCart.toString());
        } catch (Exception ignored) {
        }

        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put(getResources().getString(R.string.userOrder), jsonObjectCart.toString());

        showProcessingDialog();
        APIManager.requestPostMethod(this, getResources().getString(R.string.placeOrder), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        customer_id = jsonObject.getString(getResources().getString(R.string.customer_id));
                        customerOrderId = jsonObject.getString(getResources().getString(R.string.customerOrderId));

                        JSONArray orderMenuIds = jsonObject.getJSONArray(getResources().getString(R.string.orderMenuIds));


                        editor = sharedPreferencesRemember.edit();
                        editor.putString(getResources().getString(R.string.sharedPreferenceRememberCustomerId), customer_id);
                        editor.putString(getResources().getString(R.string.sharedPreferenceRememberCustomerOrderId), customerOrderId);
                        editor.apply();

                        for(int i = 0; i < cartModelArrayList.size(); i++){
                            JSONObject jsonObject1 = orderMenuIds.getJSONObject(i);
                            String order_menus_id = jsonObject1.getString(getResources().getString(R.string.order_menus_id));

                            //cartModelArrayList.get(i).setDatabase_menu_status("1");
                            CartModel cartModel = cartModelArrayList.get(i);
                            cartModel.setDatabase_menu_status("1");
                            cartModel.setDatabase_order_menus_id(order_menus_id);
                            databaseHelper.updateRecordById(Integer.parseInt(cartModel.getDatabase_menu_id()), cartModel.getDatabase_user_id(), cartModel);
                        }

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
            }
        });
    }

    private void getMastervalues() {

        String signUp = "getMastervalues";

        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest signUpRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.baseURL) + getResources().getString(R.string.getMastervalues), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    System.out.println("getMastervalues: " + s);
                    JSONObject object = new JSONObject(s);
                    if (object.getString(getResources().getString(R.string.status)).equalsIgnoreCase(getResources().getString(R.string.status200))) {

                        JSONArray jsonArray = object.getJSONArray(getResources().getString(R.string.masterType));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        taxes = Double.parseDouble(jsonObject.getString(getResources().getString(R.string.taxes_)));

                        for(int i = 0; i < cartModelArrayList.size(); i++){
                            subTotal = subTotal + (Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_quantity()) * Double.parseDouble(cartModelArrayList.get(i).getDatabase_menu_price()));
                        }

                        activity_order_preview_textview_subtotal.setText(getResources().getString(R.string.Rs)+" "+subTotal);

                        taxAmount = (subTotal * taxes / 100);

                        activity_order_preview_textview_tax.setText(getResources().getString(R.string.Rs)+" "+taxAmount);

                        grandTotal = subTotal + taxAmount;

                        activity_order_preview_textview_grand_total.setText(getResources().getString(R.string.Rs)+" "+grandTotal);

                        progressDialog.dismiss();

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    System.out.println("getMastervalues: " + e);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("getMastervalues: " + error);
                progressDialog.dismiss();
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        Toast.makeText(getApplicationContext(), R.string.timeout_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put(getResources().getString(R.string.masterType), getResources().getString(R.string.taxes_));

                System.out.println("getMastervalues: " + postParam);
                return postParam;
            }
        };

        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        signUpRequest.setRetryPolicy(policy);
        // Adding request to request queue
        APIController.getInstance().addToRequestQueue(signUpRequest, signUp);
    }


    private void getCartItems() {

        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put(getResources().getString(R.string.tableId), getResources().getString(R.string.tableIdValues));
        postParams.put(getResources().getString(R.string.customer_id), customer_id);
        postParams.put(getResources().getString(R.string.customerOrderId), customerOrderId);

        showProcessingDialog();
        APIManager.requestPostMethod(this, getResources().getString(R.string.getCartItems), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        JSONArray orderMenuIds = jsonObject.getJSONArray(getResources().getString(R.string.order_list));
                        if(orderMenuIds.length() > 0){

                            cartModelArrayList = new ArrayList<>();

                            for(int i = 0; i < orderMenuIds.length(); i++){
                                JSONObject orderList = orderMenuIds.getJSONObject(i);
                                customer_id = orderList.getString(getResources().getString(R.string.customer_id));
                                String customer_name = orderList.getString(getResources().getString(R.string.customer_name));
                                String mobile_number = orderList.getString(getResources().getString(R.string.mobile_number_));
                                String order_menus_id = orderList.getString(getResources().getString(R.string.order_menus_id));
                                String tableId = orderList.getString(getResources().getString(R.string.tableId));
                                customerOrderId = orderList.getString(getResources().getString(R.string.customer_order_id));
                                String menu_id = orderList.getString(getResources().getString(R.string.menu_id));
                                String itemName = orderList.getString(getResources().getString(R.string.itemName));
                                String menu_price = orderList.getString(getResources().getString(R.string.menu_price));
                                String imageName = orderList.getString(getResources().getString(R.string.imageName));
                                String menu_quantity = orderList.getString(getResources().getString(R.string.menu_quantity));
                                String menu_amount = orderList.getString(getResources().getString(R.string.menu_amount));
                                String is_processed = orderList.getString(getResources().getString(R.string.is_processed));

                                activity_book_a_table_edittext_name.setText(customer_name);
                                activity_book_a_table_edittext_name.setEnabled(false);
                                activity_book_a_table_edittext_mobile_number.setText(mobile_number);
                                activity_book_a_table_edittext_mobile_number.setEnabled(false);

                                cartModelArrayList.add(new CartModel("", order_menus_id, customer_id, menu_id, itemName, menu_price, imageName, menu_quantity, is_processed));
                            }

                            if(cartModelArrayList.size() > 0){
                                getMastervalues();
                            }else{
                                fragment_common_list_recycler_button_place_order.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismissProgressDialog();
            }
        });
    }
}
