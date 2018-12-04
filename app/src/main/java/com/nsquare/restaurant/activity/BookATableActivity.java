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

public class BookATableActivity extends ParentActivity {

    private Button activity_book_a_table_button_book_a_table;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_a_table);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.book_a_table));
        findViewByIds();

        activity_book_a_table_button_book_a_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForgotPassword = new Intent(getApplicationContext(), ReviewBookATableActivity.class);
                startActivity(intentForgotPassword);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void findViewByIds(){
        activity_book_a_table_button_book_a_table = (Button) findViewById(R.id.activity_book_a_table_button_book_a_table);
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
