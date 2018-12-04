package com.nsquare.restaurant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.Constants;

import com.nsquare.restaurant.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by Pushkar on 28-08-2017.
 */

public class ForgotPasswordActivity extends ParentActivity implements View.OnClickListener{

    private EditText activity_forgot_password_edittext_email_id;
    private Button activity_forgot_password_button_submit;
    private String emailId = "";
    private TextInputLayout activity_forgot_password_textinputlayout_emailid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.forgot_password));
        findViewByIds();

        activity_forgot_password_button_submit.setOnClickListener(this);
        activity_forgot_password_edittext_email_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    activity_forgot_password_textinputlayout_emailid.setError(null);
                }else{
                    activity_forgot_password_textinputlayout_emailid.setError(getResources().getString(R.string.error_email_id));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    private void findViewByIds(){

        activity_forgot_password_edittext_email_id = (EditText) findViewById(R.id.activity_forgot_password_edittext_email_id);
        activity_forgot_password_button_submit = (Button) findViewById(R.id.activity_forgot_password_button_submit);
        activity_forgot_password_textinputlayout_emailid = (TextInputLayout) findViewById(R.id.activity_forgot_password_textinputlayout_emailid);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.activity_forgot_password_button_submit :
                if(validateForm()){
                    if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                        forgotPassword();

                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean validateForm(){
        emailId = activity_forgot_password_edittext_email_id.getText().toString().trim();
        Matcher mEmail = Constants.emailPattern.matcher(emailId);

        if(emailId.equalsIgnoreCase("")){
            activity_forgot_password_textinputlayout_emailid.setError(getResources().getString(R.string.error_email_id));
            return false;
        }
        if (!mEmail.matches()) {
            activity_forgot_password_textinputlayout_emailid.setError(getResources().getString(R.string.error_invalid_email_id));
            return false;
        }
        return true;
    }

    private void forgotPassword() {

        String forgot_password = "forgotPassword";

        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest signInRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_main)+getResources().getString(R.string.url_forgotpassword), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    System.out.println("forgotPassword: " + s);
                    JSONObject object = new JSONObject(s);
//                    String requestArray = object.getString(getResources().getString(R.string.request));
//                    JSONArray jsonArrayRequest = object.getJSONArray(requestArray);
                    if(object.getString(getResources().getString(R.string.code)).equalsIgnoreCase(getResources().getString(R.string.code_1))){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    System.out.println("forgotPassword: " + e);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("forgotPassword: " + error);
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
                postParam.put(getResources().getString(R.string.email_address), emailId);
                postParam.put(getResources().getString(R.string.key), getResources().getString(R.string.api_key));

                System.out.println("forgotPassword: " + postParam);
                return postParam;
            }
        };

        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        signInRequest.setRetryPolicy(policy);
        // Adding request to request queue
        APIController.getInstance().addToRequestQueue(signInRequest, forgot_password);


    }
}
