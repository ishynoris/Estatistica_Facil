package com.sharktech.projectprob.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sharktech.projectprob.R;
import com.sharktech.projectprob.alert.DefaultAlert;
import com.sharktech.projectprob.analyse.DataAnalyse;
import com.sharktech.projectprob.customtable.TableColumn;
import com.sharktech.projectprob.customview.ItemConfidenceInterval;
import com.sharktech.projectprob.persistence.VariablePersistence;

import java.util.Locale;

public class IntervalVarianceController {

    private Fragment mFragment;
    private Listener mListener;

    public IntervalVarianceController(Fragment fragment) {
        mFragment = fragment;
        mListener = new Listener();
    }

    public Listener getListeners() {
        return mListener;
    }

    private void setVarsAsSample(boolean isChecked){
        setItemValue(R.id.ci_sample_size,"");

        FragmentActivity activity = mFragment.getActivity();
        if (activity != null) {
            int height = isChecked ? LinearLayout.LayoutParams.WRAP_CONTENT : 0;
            LinearLayout layout = activity.findViewById(R.id.lay_var_sample);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        }
    }

    private void setVariable(int position){
        if (position < 0) {
            setItemValue(R.id.ci_sample_size,"");
            setItemValue(R.id.ci_sample_variance,"");
        } else {
            TableColumn.IVariable variable = VariablePersistence.getInstance().getVariable(position);

            setItemValue(R.id.ci_sample_size, variable.nElements() + "");
            setItemValue(R.id.ci_sample_variance, "123456789");
        }
    }

    private void setItemValue(int id, String text) {

        FragmentActivity activity = mFragment.getActivity();
        if (activity != null) {
            ((ItemConfidenceInterval) activity.findViewById(id)).setValue(text);
        }
    }

    private void calculate(){
        FragmentActivity activity = mFragment.getActivity();
        if (activity != null) {

            ItemConfidenceInterval sampleSize = activity.findViewById(R.id.ci_sample_size);
            ItemConfidenceInterval sampleVariance = activity.findViewById(R.id.ci_sample_variance);
            ItemConfidenceInterval confidenceLevel = activity.findViewById(R.id.ci_confidence);

            DataAnalyse.IntervalConfidenceValues values = new DataAnalyse.IntervalConfidenceValues();
            values.setOnResult(getListeners());

            values.setSampleSize(sampleSize.isChecked() ? asDouble(sampleSize.getValue()) : null);
            values.setVariance(sampleVariance.isChecked() ? asDouble(sampleVariance.getValue()) : null);
            values.setConfidence(confidenceLevel.isChecked() ? asDouble(confidenceLevel.getValue()) : null);

            DataAnalyse.intervalQuiQuadratic(values);
        }
    }

    private Double asDouble(String s){
        return Double.valueOf(s.replace(",", "."));
    }

    private class Listener implements Switch.OnCheckedChangeListener, AdapterView.OnItemSelectedListener,
            View.OnClickListener, DataAnalyse.IntervalConfidenceResult{

        //Switch.OnCheckedChangeListener
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setVarsAsSample(isChecked);
        }

        //AdapterView.OnItemSelectedListener
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setVariable(position - 1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}

        //View.OnClickListener
        @Override
        public void onClick(View v) {
            calculate();
        }

        //DataAnalyse.IntervalConfidenceResult
        @Override
        public void onSuccess(Double min, Double max, Double error, float z, DataAnalyse.IntervalConfidenceValues values) {

            String text = String.format(Locale.getDefault(), "P = (%s < %s² < %s) = %s",
                    format(min), "o", format(max), format(values.getConfidence() / 100));
            text += "\nou\n";
            text += String.format(Locale.getDefault(), "IC (%s², %s) = (%s; %s)",
                    "o", format(values.getConfidence() / 100), format(min), format(max));

            FragmentActivity activity = mFragment.getActivity();
            if(activity != null) {
                TextView valueInterval = activity.findViewById(R.id.txt_value_ic_var);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) valueInterval.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                valueInterval.setLayoutParams(params);
                valueInterval.setText(text);
            }
        }

        @Override
        public void onError() {
            DefaultAlert alert = new DefaultAlert(mFragment.getActivity());
            alert.setTitle("Erro")
                    .setMessage("Não foi possível realizar o cálculo.\nVerifique os valores informados e tente novamente")
                    .setPositiveListener("OK", (dialog, which) -> dialog.dismiss());
            alert.show();
        }

        private String format(Double d){
            return String.format(Locale.getDefault(), "%.2f", d.floatValue());
        }
    }
}
