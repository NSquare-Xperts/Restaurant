package com.nsquare.restaurant.util.RobotoMedium;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.nsquare.restaurant.R;


public class EditTextRoboto extends EditText{
    private Context c;
    public EditTextRoboto(Context context) {
        super(context);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name_roboto_medium));
            setTypeface(tfs);

        }
    }

    public EditTextRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name_roboto_medium));
            setTypeface(tfs);

        }
    }

    public EditTextRoboto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name_roboto_medium));
            setTypeface(tfs);

        }
    }
}
