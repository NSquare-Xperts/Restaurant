package com.nsquare.restaurant.activity.admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
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
import com.google.zxing.Result;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.ForgotPasswordActivity;
import com.nsquare.restaurant.activity.LoginActivity;
import com.nsquare.restaurant.activity.MainActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.activity.waiter.WaiterMainActivity;
import com.nsquare.restaurant.util.APIController;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Pushkar on 28-08-2017.
 */

public class AdminConfigurationActivity extends ParentActivity implements View.OnClickListener {

    private Button activity_admin_configuration_button_configure_device, activity_admin_configuration_button_reset_device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_configuration);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.configure_device));
        findViewByIds();

        String email = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberemail), "");
        String password = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberpassword), "");

        activity_admin_configuration_button_configure_device.setOnClickListener(this);
        activity_admin_configuration_button_reset_device.setOnClickListener(this);

    }

    private void findViewByIds(){

        activity_admin_configuration_button_configure_device = (Button) findViewById(R.id.activity_admin_configuration_button_configure_device);
        activity_admin_configuration_button_reset_device = (Button) findViewById(R.id.activity_admin_configuration_button_reset_device);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.activity_admin_configuration_button_configure_device:
                Intent intentForgotPassword = new Intent(getApplicationContext(), ScanQRActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.activity_admin_configuration_button_reset_device:
                //clear saved QR code data
                SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                editor.remove(getResources().getString(R.string.sharedPreferencetableid));
                editor.remove(getResources().getString(R.string.sharedPreferencetablenumber));
                editor.remove(getResources().getString(R.string.sharedPreferencetabletype));
                editor.commit();
                Toast.makeText(AdminConfigurationActivity.this,"Data cleared",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }if (id == R.id.signout) {

            new android.app.AlertDialog.Builder(AdminConfigurationActivity.this)
                    .setMessage(getResources().getString(R.string.are_you_sure_you_want_logout))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                            editor.remove(getResources().getString(R.string.sharedPreferenceUserId));
                            editor.remove(getResources().getString(R.string.sharedPreferenceprofileimage));
                            editor.remove(getResources().getString(R.string.sharedPreferencefirst_name));
                            editor.remove(getResources().getString(R.string.sharedPreferencelast_name));
                            editor.remove(getResources().getString(R.string.sharedPreferenceemail));
                            editor.remove(getResources().getString(R.string.sharedPreferencepassword));
                            editor.remove(getResources().getString(R.string.sharedPreferencerole));
                            editor.remove(getResources().getString(R.string.sharedPreferencerole_title));
                            editor.commit();
                            try {
                                finish();
                                Intent intentLogout=new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intentLogout);
                            }catch (Exception e){
                                System.out.println("Exception: "+e);
                            }
                        }
                    }).setNegativeButton(getResources().getString(R.string.no), null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
