package com.nsquare.restaurant.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsquare.restaurant.R;

/**
 * Created by Pushkar on 28-08-2017.
 */

public class YourBookingDetailsActivity extends ParentActivity implements View.OnClickListener{

    private TextView activity_upcoming_booking_details_textview_modify_booking, activity_upcoming_booking_details_textview_cancel_booking,
            activity_upcoming_booking_details_textview_booking_status;
    private String intentStatus = "", intentFlag = "";
    private LinearLayout activity_upcoming_booking_details_linearlayout, activity_upcoming_booking_details_linearlayout_call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_booking_details);

        intentStatus = getIntent().getStringExtra(getResources().getString(R.string.key_status));
        intentFlag = getIntent().getStringExtra(getResources().getString(R.string.key_flag));

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.booking_details));
        findViewByIds();

        activity_upcoming_booking_details_textview_modify_booking.setOnClickListener(this);
        activity_upcoming_booking_details_textview_cancel_booking.setOnClickListener(this);

        if (intentStatus.equalsIgnoreCase("1")){
            activity_upcoming_booking_details_textview_booking_status.setText(getResources().getString(R.string.confirmed));
            activity_upcoming_booking_details_textview_booking_status.setTextColor(getResources().getColor(R.color.green_light));
        }else if (intentStatus.equalsIgnoreCase("0")){
            activity_upcoming_booking_details_textview_booking_status.setText(getResources().getString(R.string.pending));
            activity_upcoming_booking_details_textview_booking_status.setTextColor(getResources().getColor(R.color.yellow_pending));
        }else if (intentStatus.equalsIgnoreCase("2")){
            activity_upcoming_booking_details_textview_booking_status.setText(getResources().getString(R.string.canceled));
            activity_upcoming_booking_details_textview_booking_status.setTextColor(getResources().getColor(R.color.red_canceled));
        }

        if(intentFlag.equalsIgnoreCase("0")){
            activity_upcoming_booking_details_linearlayout.setVisibility(View.VISIBLE);
            activity_upcoming_booking_details_linearlayout_call.setVisibility(View.VISIBLE);
        }else{
            activity_upcoming_booking_details_linearlayout.setVisibility(View.GONE);
            activity_upcoming_booking_details_linearlayout_call.setVisibility(View.GONE);
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
//        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void findViewByIds(){
        activity_upcoming_booking_details_textview_modify_booking = (TextView) findViewById(R.id.activity_upcoming_booking_details_textview_modify_booking);
        activity_upcoming_booking_details_textview_cancel_booking = (TextView) findViewById(R.id.activity_upcoming_booking_details_textview_cancel_booking);
        activity_upcoming_booking_details_textview_booking_status = (TextView) findViewById(R.id.activity_upcoming_booking_details_textview_booking_status);
        activity_upcoming_booking_details_linearlayout = (LinearLayout) findViewById(R.id.activity_upcoming_booking_details_linearlayout);
        activity_upcoming_booking_details_linearlayout_call = (LinearLayout) findViewById(R.id.activity_upcoming_booking_details_linearlayout_call);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.activity_upcoming_booking_details_textview_modify_booking:
                openModifyBookingDialog(getResources().getString(R.string.modify_booking), getResources().getString(R.string.modify_booking_dialog_message));
                break;

            case R.id.activity_upcoming_booking_details_textview_cancel_booking:
                openModifyBookingDialog(getResources().getString(R.string.cancel_booking), getResources().getString(R.string.cancel_booking_dialog_message));
                break;
        }
    }

    private void openModifyBookingDialog(String title, String message){
        LayoutInflater inflater = getLayoutInflater();
        View viewGesture = inflater.inflate(R.layout.dialog_modify_booking, null);

        final Dialog dialogForgotPassword = new Dialog(YourBookingDetailsActivity.this);
        dialogForgotPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogForgotPassword.setContentView(viewGesture);
        dialogForgotPassword.setCanceledOnTouchOutside(false);
        dialogForgotPassword.setCancelable(false);
        dialogForgotPassword.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialogForgotPassword.show();

        TextView dialog_modify_booking_textview_dialog_title = (TextView) dialogForgotPassword.findViewById(R.id.dialog_modify_booking_textview_dialog_title);
        TextView dialog_modify_booking_textview_dialog_message = (TextView) dialogForgotPassword.findViewById(R.id.dialog_modify_booking_textview_dialog_message);
        Button dialog_modify_booking_button_yes = (Button) dialogForgotPassword.findViewById(R.id.dialog_modify_booking_button_yes);
        Button dialog_modify_booking_button_no = (Button) dialogForgotPassword.findViewById(R.id.dialog_modify_booking_button_no);

        dialog_modify_booking_textview_dialog_title.setText(title);
        dialog_modify_booking_textview_dialog_message.setText(message);

        dialog_modify_booking_button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPassword.dismiss();
            }
        });

        dialog_modify_booking_button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPassword.dismiss();
            }
        });
    }
}
