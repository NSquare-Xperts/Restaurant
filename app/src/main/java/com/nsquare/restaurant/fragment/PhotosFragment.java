package com.nsquare.restaurant.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.nsquare.restaurant.adapter.PhotosRecyclerAdapter;
import com.nsquare.restaurant.model.PhotosModel;

import com.nsquare.restaurant.R;

import java.util.ArrayList;


public class PhotosFragment extends Fragment
{
    private ArrayList<PhotosModel> photosModelArrayList = new ArrayList<PhotosModel>();
//    private ExpandableHeightGridView fragment_pub_details_photos_gridView_photos;
    private PhotosRecyclerAdapter photosAdapter;
    private RecyclerView fragment_reviews_recycler_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_pub_details_photos, container, false);
        findViewByIds(rootView);

        setListValues();

        return rootView;
    }

    private void findViewByIds(View view){
        fragment_reviews_recycler_view = (RecyclerView) view.findViewById(R.id.fragment_reviews_recycler_view);
    }

    private void setListValues(){

        photosModelArrayList.clear();

        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));
        photosModelArrayList.add(new PhotosModel("1", "fsfssdfdsdf"));

//        photosAdapter = new PhotosAdapter(getActivity(), photosModelArrayList);
//        fragment_pub_details_photos_gridView_photos.setAdapter(photosAdapter);
//        fragment_pub_details_photos_gridView_photos.setExpanded(true);

        photosAdapter = new PhotosRecyclerAdapter(getActivity(),photosModelArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        fragment_reviews_recycler_view.setLayoutManager(linearLayoutManager);
        fragment_reviews_recycler_view.setAdapter(photosAdapter);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragment_reviews_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            fragment_reviews_recycler_view.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        }

    }

    private int getScreenHeight(Context context) {
        int measuredHeight;
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(size);
            measuredHeight = size.y;
        } else {
            Display d = wm.getDefaultDisplay();
            measuredHeight = d.getHeight();
        }

        return measuredHeight;
    }
}
