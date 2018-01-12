package com.sharktech.projectprob.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.sharktech.projectprob.R;
import com.sharktech.projectprob.adapters.SpinAdapter;
import com.sharktech.projectprob.controllers.DataDetailsController;
import com.sharktech.projectprob.customtable.Variable;

public class DataDetailsView extends Fragment implements DataAnalyseView.ChangeVariableListener{

    private DataDetailsController mController;
    private Variable.IVariable mVariable;

    public static DataDetailsView newInstance(Variable.IVariable variable) {

        DataDetailsView fragment = new DataDetailsView();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataAnalyseView.ANALYSES, variable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if(bundle != null) mVariable = (Variable.IVariable) bundle.getSerializable(DataAnalyseView.ANALYSES);

        mController = new DataDetailsController(this, mVariable);
        View view = inflater.inflate(R.layout.fragment_data_details, container, false);
        Spinner spnGraphs = view.findViewById(R.id.spn_graphs);

        SpinAdapter adapter = new SpinAdapter(getContext());
        spnGraphs.setAdapter(adapter.getAdapter(R.array.graphs));

        spnGraphs.setOnItemSelectedListener(mController.getItemSelectedListener());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.calculate();
    }

    @Override
    public void onChangeVariable(Variable.IVariable variable) {
        mController.changeVariable(variable);
        onResume();
    }
}