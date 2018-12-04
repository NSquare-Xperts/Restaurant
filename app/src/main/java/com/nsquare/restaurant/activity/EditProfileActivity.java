package com.nsquare.restaurant.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import com.nsquare.restaurant.R;

/**
 * Created by Pushkar on 08-09-2017.
 */

public class EditProfileActivity extends ParentActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setStatusBar();
        setActionBarCustomWithBackLeftText(getResources().getString(R.string.edit_profile));
        findViewByIds();
    }

    private void findViewByIds(){
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
