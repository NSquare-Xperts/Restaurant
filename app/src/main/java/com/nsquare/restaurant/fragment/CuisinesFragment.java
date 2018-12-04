package com.nsquare.restaurant.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nsquare.restaurant.R;


public class CuisinesFragment extends Fragment
{

    private HorizontalScrollView fragment_cuisines_horizontalview;
    private int imagesMenu[] = {R.drawable.menu_one, R.drawable.menu_two, R.drawable.menu_three, R.drawable.menu_one, R.drawable.menu_two, R.drawable.menu_three,
            R.drawable.menu_one, R.drawable.menu_two, R.drawable.menu_three};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_cuisines, container, false);
        findViewByIds(rootView);

        LinearLayout topLinearLayout = new LinearLayout(getActivity());
        // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < 9; i++){
            final ImageView imageView = new ImageView (getActivity());
            imageView.setTag(i);
            imageView.setPadding(10, 0, 10, 0);
            imageView.setImageResource(imagesMenu[i]);
            topLinearLayout.addView(imageView);

            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub
                    Log.e("Tag",""+imageView.getTag());
                }
            });
        }
        fragment_cuisines_horizontalview.addView(topLinearLayout);
        return rootView;
    }

    private void findViewByIds(View view){
        fragment_cuisines_horizontalview = (HorizontalScrollView) view.findViewById(R.id.fragment_cuisines_horizontalview);
    }
}
