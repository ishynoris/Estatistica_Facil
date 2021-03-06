package com.sharktech.projectprob.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sharktech.projectprob.R;
import com.sharktech.projectprob.controllers.VariableTableController;

public class VariableTableView extends Fragment {

    private VariableTableController mController;
    private ViewGroup mContentTable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        FragmentActivity activity = getActivity();
        if(activity != null) activity.setTitle(getString(R.string.txt_variables));

        mController = new VariableTableController(this);
        View view = inflater.inflate(R.layout.fragment_vars_table, container, false);

        Button btnCmd = view.findViewById(R.id.btn_cmd);
        mContentTable = view.findViewById(R.id.content_table);

        btnCmd.setOnClickListener(mController.getClickListener());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup table = mController.buildTable();
        mContentTable.addView(table);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vars_table, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}