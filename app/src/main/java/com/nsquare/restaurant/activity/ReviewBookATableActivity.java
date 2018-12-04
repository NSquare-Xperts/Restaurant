package com.nsquare.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nsquare.restaurant.R;

/**
 * Created by Pushkar on 08-09-2017.
 */

public class ReviewBookATableActivity extends ParentActivity {

    private Button activity_review_book_a_table_button_confirm, activity_review_book_a_table_button_edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_book_a_table);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.verify_booking));
        findViewByIds();

        activity_review_book_a_table_button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intentForgotPassword = new Intent(getApplicationContext(), VerifyOTPActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        activity_review_book_a_table_button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getApplicationContext(), BookATableActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void findViewByIds(){
        activity_review_book_a_table_button_confirm = (Button) findViewById(R.id.activity_review_book_a_table_button_confirm);
        activity_review_book_a_table_button_edit = (Button) findViewById(R.id.activity_review_book_a_table_button_edit);
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
