package com.nsquare.restaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nsquare.restaurant.activity.InfoScreenActivity;

import com.nsquare.restaurant.R;

/**
 * Created by Pushkar on 23-08-2017.
 */

public class InfoScreenAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    Activity myActivity;

    public InfoScreenAdapter(Context context, Activity myActivity) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myActivity = myActivity;
    }

    @Override
    public int getCount() {
        return InfoScreenActivity.mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(InfoScreenActivity.mResources[position]);
        //textviewDes.setText(LevelMnemonicTechniqueActivity.mResourcesTagDes[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
