package com.nsquare.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.nsquare.restaurant.activity.admin.AdminConfigurationActivity;
import com.nsquare.restaurant.activity.waiter.WaiterMainActivity;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
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

public class LoginActivity extends ParentActivity implements View.OnClickListener{

    private TextView activity_login_textview_forgot_password;
    private EditText activity_login_edittext_password, activity_login_edittext_emailid;
    private Button activity_login_button_login;
    private String emailId = "", password = "", deviceId = "";
    private TextInputLayout activiity_login_textinputlayout_emailid, activiity_login_textinputlayout_password;
    private CheckBox activity_login_checkbox_rememberme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.button_login));
        findViewByIds();

        String email = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberemail), "");
        String password = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberpassword), "");

        if (email.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {
            activity_login_edittext_emailid.setText("");
            activity_login_edittext_password.setText("");
            activity_login_checkbox_rememberme.setChecked(false);
        } else {
            activity_login_edittext_emailid.setText(email);
            activity_login_edittext_password.setText(password);
            activity_login_checkbox_rememberme.setChecked(true);
        }

        activity_login_textview_forgot_password.setOnClickListener(this);
        activity_login_button_login.setOnClickListener(this);

        activity_login_edittext_emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    activiity_login_textinputlayout_emailid.setError(null);
                }else{
                    activiity_login_textinputlayout_emailid.setError(getResources().getString(R.string.error_email_id));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_login_edittext_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    activiity_login_textinputlayout_password.setError(null);
                }else{
                    activiity_login_textinputlayout_password.setError(getResources().getString(R.string.error_password));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void findViewByIds(){

        activity_login_textview_forgot_password = (TextView) findViewById(R.id.activity_login_textview_forgot_password);
        activity_login_button_login = (Button) findViewById(R.id.activity_login_button_login);
        activity_login_edittext_emailid = (EditText) findViewById(R.id.activity_login_edittext_emailid);
        activity_login_edittext_password = (EditText) findViewById(R.id.activity_login_edittext_password);
        activiity_login_textinputlayout_emailid = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_emailid);
        activiity_login_textinputlayout_password = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_password);
        activity_login_checkbox_rememberme = (CheckBox) findViewById(R.id.activity_login_checkbox_rememberme);

        activity_login_edittext_password.setTransformationMethod(new PasswordTransformationMethod());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.activity_login_textview_forgot_password:
                Intent intentForgotPassword = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.activity_login_button_login:

//                Intent intentForgotPassword1 = new Intent(getApplicationContext(), AdminConfigurationActivity.class);
//                startActivity(intentForgotPassword1);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                if(validateForm()){
                    if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                        deviceId = Constants.deviceId(getApplicationContext());
                        //signUp();
                        user_authenticate();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
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
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private boolean validateForm(){
        emailId = activity_login_edittext_emailid.getText().toString().trim();
        password = activity_login_edittext_password.getText().toString().trim();
        Matcher mEmail = Constants.emailPattern.matcher(emailId);

        if(emailId.equalsIgnoreCase("")){
            activiity_login_textinputlayout_emailid.setError(getResources().getString(R.string.error_email_id));
            return false;
        }
//        if (!mEmail.matches()) {
//            activiity_login_textinputlayout_emailid.setError(getResources().getString(R.string.error_invalid_email_id));
//            return false;
//        }
        if(password.equalsIgnoreCase("")){
            activiity_login_textinputlayout_password.setError(getResources().getString(R.string.error_password));
            return false;
        }
//        if(password.length() < 6){
//            activiity_login_textinputlayout_password.setError(getResources().getString(R.string.error_password_lenght));
//            return false;
//        }
        return true;
    }

    private void signUp() {

        String signUp = "signUp";

        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        StringRequest signUpRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_main) + getResources().getString(R.string.url_auth), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    System.out.println("signUp: " + s);
                    JSONObject object = new JSONObject(s);
                    if (object.getString(getResources().getString(R.string.code)).equalsIgnoreCase(getResources().getString(R.string.code_1))) {

                        JSONObject jsonObjectDetails = object.getJSONObject(getResources().getString(R.string.details));
                        String userid = jsonObjectDetails.getString(getResources().getString(R.string.userid));
                        String profileimage = jsonObjectDetails.getString(getResources().getString(R.string.profileimage));
                        String first_name = jsonObjectDetails.getString(getResources().getString(R.string.first_name));
                        String last_name = jsonObjectDetails.getString(getResources().getString(R.string.last_name));
                        String email = jsonObjectDetails.getString(getResources().getString(R.string.email));

                        editor = sharedPreferencesRemember.edit();
                        editor.putString(getResources().getString(R.string.sharedPreferenceUserId), userid);
                        editor.putString(getResources().getString(R.string.sharedPreferenceprofileimage), profileimage);
                        editor.putString(getResources().getString(R.string.sharedPreferencefirst_name), first_name);
                        editor.putString(getResources().getString(R.string.sharedPreferencelast_name), last_name);
                        editor.putString(getResources().getString(R.string.sharedPreferenceemail), email);
                        editor.putString(getResources().getString(R.string.sharedPreferencepassword), password);

                        if(activity_login_checkbox_rememberme.isChecked()){
                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberemail), email);
                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberpassword), password);
                        }else{
                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberemail), "");
                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberpassword), "");
                        }
                        editor.commit();

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                        finish();
//                        SignUpAndLoginDecisionActivity.signUpAndLoginDecisionActivity.finish();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    System.out.println("signUp: " + e);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("signUp: " + error);
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
                postParam.put(getResources().getString(R.string.first_name), "");
                postParam.put(getResources().getString(R.string.last_name), "");
                postParam.put(getResources().getString(R.string.email_address), emailId);
                postParam.put(getResources().getString(R.string.password_), password);
                postParam.put(getResources().getString(R.string.contact_phone), "");
                postParam.put(getResources().getString(R.string.deviceid), deviceId);
                postParam.put(getResources().getString(R.string.key), getResources().getString(R.string.api_key));
                postParam.put(getResources().getString(R.string.social_strategy), getResources().getString(R.string.general));
                postParam.put(getResources().getString(R.string.socialaccesstoken), "");
                postParam.put(getResources().getString(R.string.socialmedialink), "");
                postParam.put(getResources().getString(R.string.profileimage), "");

                System.out.println("signUp: " + postParam);
                return postParam;
            }
        };

        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        signUpRequest.setRetryPolicy(policy);
        // Adding request to request queue
        APIController.getInstance().addToRequestQueue(signUpRequest, signUp);
    }

//    private void login() {
//
//        String login = "login";
//
//        progressDialog.setMessage(getResources().getString(R.string.please_wait));
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//        StringRequest loginRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_main)+getResources().getString(R.string.url_login), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    System.out.println("login: " + s);
//                    JSONObject object = new JSONObject(s);
////                    String requestArray = object.getString(getResources().getString(R.string.request));
////                    JSONArray jsonArrayRequest = object.getJSONArray(requestArray);
//                    if(object.getString(getResources().getString(R.string.code)).equalsIgnoreCase(getResources().getString(R.string.code_1))){
//
//                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
//
//                        editor = sharedPreferencesRemember.edit();
//
//                        JSONObject jsonObjectDetails = object.getJSONObject(getResources().getString(R.string.details));
//                        String userid = jsonObjectDetails.getString(getResources().getString(R.string.user_id));
//                        String profileimage = jsonObjectDetails.getString(getResources().getString(R.string.profile));
//                        String first_name = jsonObjectDetails.getString(getResources().getString(R.string.first_name));
//                        String last_name = jsonObjectDetails.getString(getResources().getString(R.string.last_name));
//                        String email = jsonObjectDetails.getString(getResources().getString(R.string.email));
//
//                        editor.putString(getResources().getString(R.string.sharedPreferenceUserId), userid);
//                        editor.putString(getResources().getString(R.string.sharedPreferenceprofileimage), profileimage);
//                        editor.putString(getResources().getString(R.string.sharedPreferencefirst_name), first_name);
//                        editor.putString(getResources().getString(R.string.sharedPreferencelast_name), last_name);
//                        editor.putString(getResources().getString(R.string.sharedPreferenceemail), email);
//                        editor.putString(getResources().getString(R.string.sharedPreferencepassword), password);
//
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
//
//                        if (activity_login_checkbox_rememberme.isChecked()) {
//                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberemail), emailId);
//                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberpassword), password);
//                            editor.commit();
//                            activity_login_checkbox_rememberme.setChecked(true);
//
//                        } else {
//                            sharedPreferencesRemember.edit().remove(getResources().getString(R.string.sharedPreferenceRememberemail)).commit();
//                            sharedPreferencesRemember.edit().remove(getResources().getString(R.string.sharedPreferenceRememberpassword)).commit();
//                            activity_login_checkbox_rememberme.setChecked(false);
//                        }
//
//                        editor.commit();
//                        progressDialog.dismiss();
//                        finish();
//                        Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intentMainActivity);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    }else{
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    System.out.println("login: " + e);
//                    progressDialog.dismiss();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("login: " + error);
//                progressDialog.dismiss();
//                if (error.networkResponse == null) {
//                    if (error.getClass().equals(TimeoutError.class)) {
//                        // Show timeout error message
//                        Toast.makeText(getApplicationContext(), R.string.timeout_error,
//                                Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> postParam = new HashMap<String, String>();
//                postParam.put(getResources().getString(R.string.email_address), emailId);
//                postParam.put(getResources().getString(R.string.password_), password);
//                postParam.put(getResources().getString(R.string.deviceid), deviceId);
//                postParam.put(getResources().getString(R.string.key), getResources().getString(R.string.api_key));
//
//                System.out.println("login: " + postParam);
//                return postParam;
//            }
//        };
//
//        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        loginRequest.setRetryPolicy(policy);
//        // Adding request to request queue
//        APIController.getInstance().addToRequestQueue(loginRequest, login);
//    }


    private void user_authenticate() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_username), emailId);
        postParams.put(getResources().getString(R.string.field_password), password); //1 = Veg, 2 = Non Veg
        //postParams.put(getResources().getString(R.string.apiType), getResources().getString(R.string.order_list)); //1 = Veg, 2 = Non Veg

        System.out.println("API response "+postParams.toString());

        (LoginActivity.this).showProcessingDialog();
        APIManager.requestPostMethod(LoginActivity.this, getResources().getString(R.string.user_authenticate), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString(getResources().getString(R.string.status)).equalsIgnoreCase(getResources().getString(R.string.status200))) {

                        editor = sharedPreferencesRemember.edit();

                        JSONObject jsonObjectDetails = jsonObject.getJSONObject(getResources().getString(R.string.user));
                        String id = jsonObjectDetails.getString(getResources().getString(R.string.id));
                        String first_name = jsonObjectDetails.getString(getResources().getString(R.string.first_name));
                        String last_name = jsonObjectDetails.getString(getResources().getString(R.string.last_name));
                        String role = jsonObjectDetails.getString(getResources().getString(R.string.role));
                        String role_title = jsonObjectDetails.getString(getResources().getString(R.string.role_title));

                        editor.putString(getResources().getString(R.string.sharedPreferenceUserId), id);
                        editor.putString(getResources().getString(R.string.sharedPreferencewaiterName), first_name+ " "+last_name);
                        editor.putString(getResources().getString(R.string.sharedPreferencewaiterUniqueId), id);
                        editor.putString(getResources().getString(R.string.sharedPreferencepassword), password);
                        editor.putString(getResources().getString(R.string.sharedPreferencerole), role);
                        editor.putString(getResources().getString(R.string.sharedPreferencerole_title), role_title);

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                        if (activity_login_checkbox_rememberme.isChecked()) {
                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberemail), emailId);
                            editor.putString(getResources().getString(R.string.sharedPreferenceRememberpassword), password);
                            editor.commit();
                            activity_login_checkbox_rememberme.setChecked(true);

                        } else {
                            sharedPreferencesRemember.edit().remove(getResources().getString(R.string.sharedPreferenceRememberemail)).commit();
                            sharedPreferencesRemember.edit().remove(getResources().getString(R.string.sharedPreferenceRememberpassword)).commit();
                            activity_login_checkbox_rememberme.setChecked(false);
                        }

                        editor.commit();
                        progressDialog.dismiss();

                        if(role.equalsIgnoreCase("1")){


                        }else if(role.equalsIgnoreCase("2")){

                        }else if(role.equalsIgnoreCase("3")){

                            //to scan table number
                            finish();
                            Intent intentMainActivity = new Intent(getApplicationContext(), AdminConfigurationActivity.class);
                            startActivity(intentMainActivity);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }else if(role.equalsIgnoreCase("4")){
                            //staff login
                            finish();
                            Intent intentMainActivity = new Intent(getApplicationContext(), WaiterMainActivity.class);
                            startActivity(intentMainActivity);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                (LoginActivity.this).dismissProgressDialog();
            }
        });
    }
}
