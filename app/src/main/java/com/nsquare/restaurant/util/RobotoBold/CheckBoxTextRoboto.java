package com.nsquare.restaurant.util.RobotoBold;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.nsquare.restaurant.R;


public class CheckBoxTextRoboto extends CheckBox{
    private Context c;
    public CheckBoxTextRoboto(Context context) {
        super(context);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name_roboto_bold));
            setTypeface(tfs);

        }
    }

    public CheckBoxTextRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name_roboto_bold));
            setTypeface(tfs);

        }
    }

    public CheckBoxTextRoboto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name_roboto_bold));
            setTypeface(tfs);

        }
    }
}
