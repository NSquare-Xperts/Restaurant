package com.nsquare.restaurant.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Pushkar on 29-08-2017.
 */

public class Constants {

   public static String color_code_dark="color_code_dark";

   public static String quantity="Quantity";
   public static String extra="Extra";
   public static String custom="Custom";
   public static String table_id="6";
   public static String status="status";
   public static String message="message";
   public static String status_422="422";
   public static String statusMakePayment="statusMakePayment";


   public static String order_history="order_history";

   public static String veg="Veg";
   public static String data="data";
   public static String yes="YES";
   public static String no="NO";
   public static String single_choice="s";
   public static String multi_choice="m";
   public static String color_code_light="color_code_light";
    public static Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_\\+-]+(\\.[a-zA-Z0-9_\\+-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.([a-zA-Z]{2,4})$");
    public static int SocketTimeout = 60000;
    public static ProgressDialog progressDialog;

    public static String deviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        Log.v("DeviceIMEI", "" + tmDevice);
//        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        Log.v("androidId CDMA devices", "" + androidId);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32));
//        | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        System.out.println("device" + deviceId);
//       String deviceId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        return deviceId;
    }

    public static String getDeviceDetails(){

        String deviceDetails = "";
        deviceDetails = "Device name:"+android.os.Build.MODEL +" brand:"+android.os.Build.BRAND +" OS version = "+android.os.Build.VERSION.RELEASE + " SDK version = " +android.os.Build.VERSION.SDK_INT;
        return deviceDetails;
    }
}
