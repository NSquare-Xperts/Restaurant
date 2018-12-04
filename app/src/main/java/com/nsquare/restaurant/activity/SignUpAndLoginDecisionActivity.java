package com.nsquare.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Pushkar on 28-08-2017.
 */

public class SignUpAndLoginDecisionActivity extends ParentActivity implements View.OnClickListener {

    private Button activity_signup_login_decision_button_staff_login, activity_signup_login_decision_textview_guest_user;
    public static Activity signUpAndLoginDecisionActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_sinup_login_decision);


        signUpAndLoginDecisionActivity = this;
//        getSupportActionBar().hide();
        setStatusBar();
        findViewByIds();

        activity_signup_login_decision_textview_guest_user.setOnClickListener(this);
        activity_signup_login_decision_button_staff_login.setOnClickListener(this);

    }

    private void findViewByIds(){
        activity_signup_login_decision_textview_guest_user = (Button) findViewById(R.id.activity_signup_login_decision_textview_guest_user);
        activity_signup_login_decision_button_staff_login = (Button) findViewById(R.id.activity_signup_login_decision_button_staff_login);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.activity_signup_login_decision_button_staff_login:
                Intent intentForgotPassword = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.activity_signup_login_decision_textview_guest_user:
                finish();
                Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMainActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
