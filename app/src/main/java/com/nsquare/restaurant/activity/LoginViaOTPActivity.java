package com.nsquare.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nsquare.restaurant.R;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginViaOTPActivity extends ParentActivity {

    private Button button_login_login_activity, activity_login_button_send_otp, activity_login_button_verify_otp;
    private RelativeLayout activity_login_relative_layout_otp;
    private EditText activitylogin_edittextview_mobileno,  activitylogin_edittextview_otp,activitylogin_edittextview_username;
    private String mobileno, userId, otpValue,username;
    //private SharedPreferences sharedPreferencesRemember;
    private SharedPreferences.Editor editor;
    private TextView activity_login_textView_resend_otp, activity_login_textView_label_verifyotp, activitylogin_textview;
    private CountDownTimer countDownTimer;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private String otpStr="";
    private String order_id="";
    private String cart_Ids="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_via_otp);
        setStatusBar();

        /*Changes by : *Ritu Chavan
            Date :6-12-2018
         */
        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.verify));
        findViewById();
        //sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(LoginViaOTPActivity.this);

        cart_Ids = getIntent().getStringExtra("cartIds");

        activity_login_button_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobileno = activitylogin_edittextview_mobileno.getText().toString();
                username = activitylogin_edittextview_username.getText().toString();
                if (validateform()) {
                    activity_login_button_send_otp.setVisibility(View.GONE);
                    activity_login_button_verify_otp.setVisibility(View.VISIBLE);
                    activity_login_relative_layout_otp.setVisibility(View.VISIBLE);
                    activitylogin_textview.setVisibility(View.GONE);
                    sendUserOTP();
                }
            }
        });

        activity_login_button_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mobileno = activitylogin_edittextview_mobileno.getText().toString();
               // username = activitylogin_edittextview_username.getText().toString();
                if (validateform()) {
                    sendUserOTP();
                }
            }
        });

        activity_login_button_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mobileno = activitylogin_edittextview_mobileno.getText().toString();
                otpValue = activitylogin_edittextview_otp.getText().toString();

                if (otpValue.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_password), Toast.LENGTH_SHORT).show();
                } else {
                    if (validateform()) {
                        verifyOTP();
                    }
                }
            }
        });

        activity_login_textView_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //activitylogin_edittextview_otp.setError(null);
                long actualTime;
                activity_login_textView_resend_otp.setEnabled(false);
                if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                    // resentOTPMethodNew();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
                actualTime = TimeUnit.MINUTES.toMillis(Long.parseLong("3"));
                countDownTimer = new CountDownTimer(actualTime, 1000) {
                    public void onTick(long millisUntilFinished) {

                        long millis = millisUntilFinished;
                        String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                        activity_login_textView_resend_otp.setText(hms);
                    }
                    public void onFinish() {
                        activity_login_textView_resend_otp.setText(getResources().getString(R.string.resend));
                        activity_login_textView_resend_otp.setEnabled(true);
                    }

                }.start();
            }
        });

    }
    public void findViewById() {

        activity_login_button_send_otp = (Button) findViewById(R.id.activity_login_button_send_otp);
        activity_login_button_verify_otp = (Button) findViewById(R.id.activity_login_button_verify_otp);
        activity_login_textView_resend_otp = (TextView) findViewById(R.id.activity_login_textView_resend_otp);
        activitylogin_textview = (TextView) findViewById(R.id.activitylogin_textview);

        activity_login_textView_label_verifyotp = (TextView) findViewById(R.id.activity_login_textView_label_verifyotp);
        activitylogin_edittextview_username = (EditText) findViewById(R.id.activitylogin_edittextview_username);
        activitylogin_edittextview_mobileno = (EditText) findViewById(R.id.activitylogin_edittextview_mobileno);
        activitylogin_edittextview_otp = (EditText) findViewById(R.id.activitylogin_edittextview_otp);
        activity_login_relative_layout_otp = (RelativeLayout) findViewById(R.id.activity_login_relative_layout_otp);
    }

    private boolean validateform() {

        if (activitylogin_edittextview_mobileno.getText().toString().trim().equalsIgnoreCase("")) {
            activitylogin_edittextview_mobileno.setError(getResources().getString(R.string.error_mobile));
            return false;
        }
        if (activitylogin_edittextview_mobileno.getText().toString().trim().length() < 10) {
            activitylogin_edittextview_mobileno.setError(getResources().getString(R.string.error_mobile_length));
            return false;
        }
        if(activitylogin_edittextview_username.getText().toString().trim().isEmpty()){
            activitylogin_edittextview_username.setError(getResources().getString(R.string.error_username));
            return false;
        }

        return true;
    }


    @Override
    protected void onResume() {
        //System.out.println("onresume login");
        //LocalBroadcastManager.getInstance(this).registerReceiver(receiver1, new IntentFilter("otp"));
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
    }

    public void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(new View(LoginViaOTPActivity.this).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //call OTP
    private void sendUserOTP() {

        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();

        postParams.put(getResources().getString(R.string. field_table), Constants.table_id);
        postParams.put(getResources().getString(R.string. field_mobile), mobileno);
        postParams.put(getResources().getString(R.string. field_fullname), username);

        APIManager.requestPostMethod(LoginViaOTPActivity.this, getResources().getString(R.string.sendUserOTP), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        order_id = jsonObject.getString("order_id");

                        editor = sharedPreferencesRemember.edit();
                        editor.putString(getResources().getString(R.string.order_id), order_id);
                        editor.commit();
                        //save order
                        Toast.makeText(LoginViaOTPActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(LoginViaOTPActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //verify Otp
    private void verifyOTP() {
        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();


        String order_id_ = sharedPreferencesRemember.getString(getResources().getString(R.string.order_id), "");

        postParams.put(getResources().getString(R.string. field_mobile), mobileno);
        postParams.put(getResources().getString(R.string. field_otp), otpValue);
        postParams.put(getResources().getString(R.string. field_order_id),order_id_);

        APIManager.requestPostMethod(LoginViaOTPActivity.this, getResources().getString(R.string.verifyotp), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        placeOrder();
                        Toast.makeText(LoginViaOTPActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(LoginViaOTPActivity.this, jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //order confirmed pop up

    public void alertOrderConfirmed(){

        final AlertDialog alert11;
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginViaOTPActivity.this);

        LayoutInflater factory = LayoutInflater.from(LoginViaOTPActivity.this);
        final View view1 = factory.inflate(R.layout.dialog_oder_confirmed, null);
        builder1.setView(view1);
        builder1.setCancelable(false);

        alert11 = builder1.create();
        alert11.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert11.show();
        Button button_popup_ok = (Button) view1.findViewById(R.id.button_popup_ok);

        button_popup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.dismiss();
                Intent intent = new Intent(LoginViaOTPActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void placeOrder() {
        // swipe_refresh_layout.setRefreshing(true);
        HashMap<String, String> postParams = new HashMap<>();

        postParams.put(getResources().getString(R.string.field_order_id), order_id);
        postParams.put(getResources().getString(R.string.field_table), Constants.table_id);
        postParams.put(getResources().getString(R.string.field_cart_id),cart_Ids);

        APIManager.requestPostMethod(LoginViaOTPActivity.this, getResources().getString(R.string.placeOrder_), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 200) {
                        alertOrderConfirmed();
                    }else if (jsonObject.getInt("status") == 400) {
                        Toast.makeText(LoginViaOTPActivity.this, jsonObject.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //swipe_refresh_layout.setRefreshing(false);
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

}
