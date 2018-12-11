package com.nsquare.restaurant.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nsquare.restaurant.activity.ParentActivity;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import com.nsquare.restaurant.R;

/**
 * Created by pratik on 3/5/18
 */
public class APIManager {
    public static void requestGetMethod(final Context context, String url, final VolleyCallback callback) {
        if (InternetConnection.isNetworkAvailable(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, context.getResources().getString(R.string.baseURL)+url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getInt("status") == 200) {
                                        callback.onSuccess(jsonObject.toString());
                                    } else if (jsonObject.getInt("status") == 400) {
                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Constants.progressDialog.dismiss();
                    if (error.networkResponse == null) {
                        if (error.getClass().equals(NoConnectionError.class)) {
                            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_LONG).show();
                        } else if (error.getClass().equals(TimeoutError.class)) {
                            // Show timeout error message
                            Toast.makeText(context, R.string.timeout_error, Toast.LENGTH_LONG).show();
                        } else {
                            // Show timeout error message
                            Toast.makeText(context, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
//                    String creds = String.format("%s:%s", context.getResources().getString(R.string.basic_auth_username), context.getResources().getString(R.string.basic_auth_password));
//                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            // Adding JsonObject request to request queue
            APIController.getInstance().addToRequestQueue(stringRequest, "GET_COUNTRY_LIST");
        } else {
            Constants.progressDialog.dismiss();
            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * fetch data from server
     * @param context:
     * @param url:
     * @param postParams:
     * @param callback:
     */
    public static void requestPostMethod(final Context context, String url, final Map<String, String> postParams, final VolleyCallback callback) {
        // Check if no view has focus:
        View view = ((ParentActivity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (InternetConnection.isNetworkAvailable(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.baseURL) + url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("API response "+response.toString());
                            if (response != null) {
                                callback.onSuccess(response);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(Constants.progressDialog == null){
                    }else {
                        Constants.progressDialog.dismiss();
                    }
                    if (error.networkResponse == null) {
                        if (error.getClass().equals(NoConnectionError.class)) {
                            Toast.makeText(context, R.string.server_not_reachable, Toast.LENGTH_SHORT).show();
                        } else if (error.getClass().equals(TimeoutError.class)) {
                            // Show timeout error message
                            Toast.makeText(context, R.string.timeout_error, Toast.LENGTH_SHORT).show();
                        } else {
                            // Show timeout error message
                            Toast.makeText(context, R.string.error_something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return postParams;
                }

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
//                    String creds = String.format("%s:%s", context.getResources().getString(R.string.basic_auth_username), context.getResources().getString(R.string.basic_auth_password));
//                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                    headers.put("Authorization", auth);
//                    headers.put("Content-Type","application/x-www-form-urlencoded");
                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding JsonObject request to request queue
            APIController.getInstance().addToRequestQueue(stringRequest, "GET_COUNTRY_LIST");
        } else {
            Constants.progressDialog.dismiss();
            Toast.makeText(context, R.string.internet_connection, Toast.LENGTH_SHORT).show();
        }
    }


    public interface VolleyCallback {
        void onSuccess(String result);
    }
}