package com.nsquare.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by Pushkar on 28-08-2017.
 */

public class SignUpActivity extends ParentActivity implements View.OnClickListener {

    private EditText activity_signup_edittext_first_name, activity_signup_edittext_last_name, activity_signup_edittext_emailid, activity_signup_edittext_password, activity_signup_edittext_mobileno;
    private String firstName = "", lastName = "", emailId = "", password = "", mobileNumber = "", deviceId = "";
    private Button activity_signup_button_signup, activity_login_button_gmail;
    private TextInputLayout activiity_login_textinputlayout_first_name, activiity_login_textinputlayout_last_name, activiity_login_textinputlayout_emailid, activiity_login_textinputlayout_mobileno,
            activiity_login_textinputlayout_password;
    private CheckBox activity_signup_checkbox_terms;
    private Button activity_login_button_facebook;

    private String socialMediaProfileImage = "", social_strategy = "", socialaccesstoken = "", socialmedialink = "", fb_name = "", fb_profile_pic = "", fb_email = "", fb_id = "", selectedCountry = "", selectedCountryName = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.button_signup));
        findViewByIds();

        deviceId = Constants.deviceId(getApplicationContext());

        activity_signup_button_signup.setOnClickListener(this);

        activity_signup_edittext_first_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activiity_login_textinputlayout_first_name.setError(null);
                } else {
                    activiity_login_textinputlayout_first_name.setError(getResources().getString(R.string.error_first_name));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_signup_edittext_last_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activiity_login_textinputlayout_last_name.setError(null);
                } else {
                    activiity_login_textinputlayout_last_name.setError(getResources().getString(R.string.error_last_name));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_signup_edittext_emailid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activiity_login_textinputlayout_emailid.setError(null);
                } else {
                    activiity_login_textinputlayout_emailid.setError(getResources().getString(R.string.error_email_id));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_signup_edittext_mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activiity_login_textinputlayout_mobileno.setError(null);
                } else {
                    activiity_login_textinputlayout_mobileno.setError(getResources().getString(R.string.error_mobileno));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_signup_edittext_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    activiity_login_textinputlayout_password.setError(null);
                } else {
                    activiity_login_textinputlayout_password.setError(getResources().getString(R.string.error_password));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void findViewByIds() {
        activity_signup_edittext_first_name = (EditText) findViewById(R.id.activity_signup_edittext_first_name);
        activity_signup_edittext_last_name = (EditText) findViewById(R.id.activity_signup_edittext_last_name);
        activity_signup_edittext_emailid = (EditText) findViewById(R.id.activity_signup_edittext_emailid);
        activity_signup_edittext_mobileno = (EditText) findViewById(R.id.activity_signup_edittext_mobileno);
        activity_signup_edittext_password = (EditText) findViewById(R.id.activity_signup_edittext_password);
        activity_signup_button_signup = (Button) findViewById(R.id.activity_signup_button_signup);
        activity_login_button_facebook = (Button) findViewById(R.id.activity_login_button_facebook);
        activity_login_button_gmail = (Button) findViewById(R.id.activity_login_button_gmail);
        activiity_login_textinputlayout_first_name = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_first_name);
        activiity_login_textinputlayout_last_name = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_last_name);
        activiity_login_textinputlayout_emailid = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_emailid);
        activiity_login_textinputlayout_mobileno = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_mobileno);
        activiity_login_textinputlayout_password = (TextInputLayout) findViewById(R.id.activiity_login_textinputlayout_password);
        activity_signup_checkbox_terms = (CheckBox) findViewById(R.id.activity_signup_checkbox_terms);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.activity_signup_button_signup:
                if (validateForm()) {
                    if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                        social_strategy = getResources().getString(R.string.general);
                        signUp();
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean validateForm() {
        emailId = activity_signup_edittext_emailid.getText().toString().trim();
        firstName = activity_signup_edittext_first_name.getText().toString().trim();
        lastName = activity_signup_edittext_last_name.getText().toString().trim();
        password = activity_signup_edittext_password.getText().toString().trim();
        mobileNumber = activity_signup_edittext_mobileno.getText().toString().trim();
        Matcher mEmail = Constants.emailPattern.matcher(emailId);

        if (firstName.equalsIgnoreCase("")) {
            activity_signup_edittext_first_name.setError(getResources().getString(R.string.error_first_name));
            return false;
        }
        if (lastName.equalsIgnoreCase("")) {
            activity_signup_edittext_last_name.setError(getResources().getString(R.string.error_last_name));
            return false;
        }

        if (emailId.equalsIgnoreCase("")) {
            activiity_login_textinputlayout_emailid.setError(getResources().getString(R.string.error_email_id));
            return false;
        }
        if (!mEmail.matches()) {
            activiity_login_textinputlayout_emailid.setError(getResources().getString(R.string.error_invalid_email_id));
            return false;
        }

        if (mobileNumber.length() == 0) {
            return true;
        } else {
            if (activity_signup_edittext_mobileno.getText().toString().trim().length() < 10) {
                activiity_login_textinputlayout_mobileno.setError(getResources().getString(R.string.error_mobile_length));
                return false;
            }
//            if (activity_signup_edittext_mobileno.getText().toString().trim().equalsIgnoreCase("")) {
//                activiity_login_textinputlayout_mobileno.setError(getResources().getString(R.string.error_mobileno));
//                return false;
//            }
        }


        if (password.equalsIgnoreCase("")) {
            activiity_login_textinputlayout_password.setError(getResources().getString(R.string.error_password));
            return false;
        }
        if (password.length() < 8) {
            activiity_login_textinputlayout_password.setError(getResources().getString(R.string.error_password_lenght));
            return false;
        }
        if (!activity_signup_checkbox_terms.isChecked()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_terms), Toast.LENGTH_SHORT).show();
            return false;
        }
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
                        editor.commit();

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                        finish();
                        SignUpAndLoginDecisionActivity.signUpAndLoginDecisionActivity.finish();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
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
                postParam.put(getResources().getString(R.string.first_name), firstName);
                postParam.put(getResources().getString(R.string.last_name), lastName);
                postParam.put(getResources().getString(R.string.email_address), emailId);
                postParam.put(getResources().getString(R.string.password_), password);
                postParam.put(getResources().getString(R.string.contact_phone), mobileNumber);
                postParam.put(getResources().getString(R.string.deviceid), deviceId);
                postParam.put(getResources().getString(R.string.key), getResources().getString(R.string.api_key));
                postParam.put(getResources().getString(R.string.social_strategy), social_strategy);
                postParam.put(getResources().getString(R.string.socialaccesstoken), socialaccesstoken);
                postParam.put(getResources().getString(R.string.socialmedialink), socialmedialink);
                postParam.put(getResources().getString(R.string.profileimage), socialMediaProfileImage);

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

}
