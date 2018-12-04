package com.nsquare.restaurant.activity.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.nsquare.restaurant.R;
import com.nsquare.restaurant.activity.LoginActivity;
import com.nsquare.restaurant.activity.ParentActivity;
import com.nsquare.restaurant.activity.waiter.WaiterMainActivity;
import com.nsquare.restaurant.util.APIManager;
import com.nsquare.restaurant.util.Constants;

import org.json.JSONObject;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Pushkar on 28-08-2017.
 */

public class ScanQRActivity extends ParentActivity implements View.OnClickListener, ZXingScannerView.ResultHandler{

    private ZXingScannerView fragment_scan_qr_code_zing_scanner;
    private final static int PERMISSION_REQUEST_CODE104 = 104;
    private Button activity_admin_configuration_button_configure_device, activity_admin_configuration_button_reset_device;
    private String emailId = "", password = "", deviceId = "", scannedValue = "";
    private boolean isPermissionGranted, lastPermission, lastPermissionCamera, isPermissionGrantedCamera = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.scan_QR));
        findViewByIds();

        String email = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberemail), "");
        String password = sharedPreferencesRemember.getString(getResources().getString(R.string.sharedPreferenceRememberpassword), "");

    }

    private void findViewByIds(){
        fragment_scan_qr_code_zing_scanner = (ZXingScannerView) findViewById(R.id.scan_qr_code_zing_scanner);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.activity_admin_configuration_button_configure_device:

                if (isDeviceBuildVersionMarshmallow()) {
                    if (lastPermission == true && isPermissionGranted == true) {
                        QrScanner();
                    } else {
                        getSMSReadPermisson();
                    }
                } else {
                    QrScanner();
                }
                break;

            case R.id.activity_admin_configuration_button_reset_device:

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

    @Override
    public void handleResult(Result result) {
        if (!result.getText().equalsIgnoreCase("")) {
            scannedValue = result.getText();
            //Toast.makeText(getApplicationContext(), "Scanned value: "+scannedValue, Toast.LENGTH_LONG).show();
            //System.out.println("validateQRCode "+scannedValue);

            // QR code pattern is: 2/table 02/onTable
            String[] splittedValue = scannedValue.split("/");

            if(splittedValue.length != 3){
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_invalid_qr), Toast.LENGTH_LONG).show();
            }else{
                if (internetConnection.isNetworkAvailable(getApplicationContext())) {
                    validateQRCode(scannedValue);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }
            }


        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_invalid_qr), Toast.LENGTH_LONG).show();
        }
    }

    private void getSMSReadPermisson() {

        int permissionCheckCamera = ContextCompat.checkSelfPermission(ScanQRActivity.this, Manifest.permission.CAMERA);

        if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ScanQRActivity.this, Manifest.permission.CAMERA)) {

                // explanation needed
                ActivityCompat.requestPermissions(ScanQRActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE104);


            } else {
                // No explanation needed
                ActivityCompat.requestPermissions(ScanQRActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE104);
            }
        }

        if (permissionCheckCamera == 0) {
            isPermissionGranted = true;
            lastPermission = true;
            QrScanner();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_REQUEST_CODE104:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isPermissionGranted = true;
                    lastPermission = true;

                    QrScanner();
                } else {
                    lastPermission = false;
                    isPermissionGranted = false;
                    Toast.makeText(ScanQRActivity.this, getResources().getString(R.string.need_to_allow), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private boolean isDeviceBuildVersionMarshmallow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;

        return false;
    }

    @Override
    protected void onDestroy() {

        fragment_scan_qr_code_zing_scanner.stopCamera();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isDeviceBuildVersionMarshmallow()) {
            System.out.println("permisssion   ");
            if (lastPermission == true && isPermissionGranted == true) {
                QrScanner();
            } else {
                getSMSReadPermisson();
            }
        } else {

            QrScanner();
        }
    }

    public void QrScanner() {
        fragment_scan_qr_code_zing_scanner.setResultHandler(this); // Register ourselves as a handler for scan results.
        fragment_scan_qr_code_zing_scanner.startCamera();         // Start camera
    }

    @Override
    public void onPause() {
        super.onPause();
        fragment_scan_qr_code_zing_scanner.stopCamera();           // Stop camera on pause

    }

    private void validateQRCode(final String scannedValue) {

        // QR code pattern is: 2/table 02/onTable
        final String[] splittedValue = scannedValue.split("/");

        HashMap<String, String> postParams = new HashMap<>();
        postParams.put(getResources().getString(R.string.field_tableNo), splittedValue[0]);
        System.out.println("validateQRCode "+postParams.toString());

        (ScanQRActivity.this).showProcessingDialog();
        APIManager.requestPostMethod(ScanQRActivity.this, getResources().getString(R.string.validateQRCode), postParams, new APIManager.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    System.out.println("validateQRCode "+result.toString());
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString(getResources().getString(R.string.status)).equalsIgnoreCase(getResources().getString(R.string.status200))) {

                        editor = sharedPreferencesRemember.edit();
                        editor.putString(getResources().getString(R.string.sharedPreferencetableid), splittedValue[0]);
                        editor.putString(getResources().getString(R.string.sharedPreferencetablenumber), splittedValue[1]);
                        editor.putString(getResources().getString(R.string.sharedPreferencetabletype), splittedValue[2]);
                        editor.commit();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(getResources().getString(R.string.message)), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.out.println("API response exception: "+e.toString());
                    e.printStackTrace();
                }
                (ScanQRActivity.this).dismissProgressDialog();
            }
        });
    }
}
