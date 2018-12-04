package com.nsquare.restaurant.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nsquare.restaurant.R;
import com.nsquare.restaurant.model.CustomeOrderItems;
import com.nsquare.restaurant.util.RobotoRegular.CheckBoxTextRoboto;

import android.support.design.widget.BottomSheetDialogFragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private Context context;
    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);


    }



    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}