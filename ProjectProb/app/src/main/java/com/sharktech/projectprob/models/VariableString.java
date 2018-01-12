package com.sharktech.projectprob.models;


import com.sharktech.projectprob.customtable.Cell.ICell;
import com.sharktech.projectprob.customtable.Variable.IVariable;

import java.util.ArrayList;

public class VariableString implements IVariable {

    private String mTitle;
    private ArrayList<ICell> mValues;

    public VariableString(String mTitle) {
        this.mTitle = mTitle;
        this.mValues = new ArrayList<>();
    }

    public void add(String value){
        mValues.add(new ValueString(value));
    }

    public void add(Character value){
        add(String.valueOf(value));
    }

    public void add(char value){
        add(String.valueOf(value));
    }

    public void add(String[] values){
        for(String s : values){
            add(s);
        }
    }

    public void add(Character[] values){
        for(Character c : values){
            add(c);
        }
    }

    public void add(char[] values){
        for(char c : values){
            add(c);
        }
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int nElements() {
        return mValues.size();
    }

    @Override
    public ArrayList<ICell> getElements() {
        return mValues;
    }

    @Override
    public ICell getElement(int index) {
        return mValues.get(index);
    }

    @Override
    public void setElement(ICell value, int index) {
        mValues.add(index,  value);
    }

    protected static class ValueString implements ICell<String> {

        private String value;

        private ValueString(String value) {
            this.value = value;
        }

        @Override
        public String getTitle() {
            return value;
        }

        @Override
        public String getElement() {
            return value;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public Float asFloat() {
            return 1f;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}