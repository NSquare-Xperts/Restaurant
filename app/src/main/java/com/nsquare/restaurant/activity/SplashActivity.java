package com.nsquare.restaurant.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.nsquare.restaurant.activity.admin.AdminConfigurationActivity;
import com.nsquare.restaurant.activity.waiter.WaiterMainActivity;
import com.nsquare.restaurant.util.Constants;

import com.nsquare.restaurant.R;

public class SplashActivity extends ParentActivity {

    private Handler myHandler;
    private static int SPLASH_TIME_OUT = 2000;
    private final static int PERMISSION_REQUEST_CODE104 = 104;
    private boolean isPermissionGranted, lastPermission = false;
    private String deviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setStatusBar();

        if (isDeviceBuildVersionMarshmallow()) {
            if (lastPermission == true && isPermissionGranted == true) {
                myHandler = new Handler();
                myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
            }else {
                getSMSReadPermisson();
            }
        }else{
            myHandler = new Handler();
            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
        }

    }


    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            //sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
            String sharedPreferenceIsInfoVisited = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceIsInfoVisited), "");
            String sharedPreferenceUserId = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceUserId), "");
            String sharedPreferencerole = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferencerole), "");

            if (sharedPreferenceIsInfoVisited.equalsIgnoreCase("1")){

                if(sharedPreferenceUserId == null || sharedPreferenceUserId.equalsIgnoreCase("")){
                    finish();
                    Intent intent = new Intent(SplashActivity.this, SignUpAndLoginDecisionActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else{
                    if(sharedPreferencerole.equalsIgnoreCase("1")){
                        finish();
                        Intent intentMainActivity = new Intent(getApplicationContext(), AdminConfigurationActivity.class);
                        startActivity(intentMainActivity);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else if(sharedPreferencerole.equalsIgnoreCase("2")){

                    }else if(sharedPreferencerole.equalsIgnoreCase("3")){

                    }else if(sharedPreferencerole.equalsIgnoreCase("4")){
                        finish();
                        Intent intentMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentMainActivity);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
//                    finish();
//                    Intent intent = new Intent(SplashActivity.this, WaiterMainActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }else{
                if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                    //registerDevice();
                    editor = sharedPreferencesRemember.edit();
                    editor.putString(getResources().getString(R.string.sharedPreferenceIsInfoVisited), "1");
                    editor.commit();

                    finish();
                    Intent intentMainActivity = new Intent(getApplicationContext(), InfoScreenActivity.class);
                    startActivity(intentMainActivity);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private boolean isDeviceBuildVersionMarshmallow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;

        return false;
    }

    private void getSMSReadPermisson() {

        int permissionCheckCamera = ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, Manifest.permission.READ_PHONE_STATE)) {

                // explanation needed
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSION_REQUEST_CODE104);


            } else {
                // No explanation needed
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        PERMISSION_REQUEST_CODE104);
            }
        }

        if (permissionCheckCamera == 0) {
            isPermissionGranted = true;
            lastPermission = true;
            deviceId = Constants.deviceId(getApplicationContext());
            System.out.println("deviceid1****" + deviceId);
            myHandler = new Handler();
            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_REQUEST_CODE104:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;
                    lastPermission = true;
                    deviceId = Constants.deviceId(getApplicationContext());
                    System.out.println("deviceid****4" + deviceId);
                    myHandler = new Handler();
                    myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
                } else {
                    lastPermission = false;
                    isPermissionGranted = false;
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.need_to_allow), Toast.LENGTH_SHORT).show();
                    if (isDeviceBuildVersionMarshmallow()) {
                        if (lastPermission == true && isPermissionGranted == true) {
                            myHandler = new Handler();
                            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
                        }else {
                            getSMSReadPermisson();
                        }
                    }else{
                        myHandler = new Handler();
                        myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
                    }
                }
                break;
        }
    }


//    private void registerDevice() {
//
//        String login = "registerDevice";
//
//        progressDialog.setMessage(getResources().getString(R.string.please_wait));
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//        StringRequest loginRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url_main)+getResources().getString(R.string.url_deviceregistration), new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    System.out.println("registerDevice: " + s);
//                    JSONObject object = new JSONObject(s);
////                    String requestArray = object.getString(getResources().getString(R.string.request));
////                    JSONArray jsonArrayRequest = object.getJSONArray(requestArray);
//                    if(object.getString(getResources().getString(R.string.code)).equalsIgnoreCase(getResources().getString(R.string.code_1))){
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
//
//                        editor = sharedPreferencesRemember.edit();
//                        editor.putString(getResources().getString(R.string.sharedPreferenceIsInfoVisited), "1");
//                        editor.commit();
//
//                        finish();
//                        Intent intentMainActivity = new Intent(getApplicationContext(), InfoScreenActivity.class);
//                        startActivity(intentMainActivity);
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                    }else{
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), object.getString(getResources().getString(R.string.msg)), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    System.out.println("registerDevice: " + e);
//                    progressDialog.dismiss();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                System.out.println("registerDevice: " + error);
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
//                postParam.put(getResources().getString(R.string.deviceid), deviceId);
////                postParam.put(getResources().getString(R.string.os), Constants.getDeviceDetails());
//                postParam.put(getResources().getString(R.string.os), "Android OS version = "+android.os.Build.VERSION.RELEASE);
//                postParam.put(getResources().getString(R.string.key), getResources().getString(R.string.api_key));
//                System.out.println("registerDevice: " + postParam);
//                return postParam;
//            }
//        };
//
//        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        loginRequest.setRetryPolicy(policy);
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(loginRequest, login);
//    }
}
