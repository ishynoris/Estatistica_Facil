package com.sharktech.projectprob.parser;

import com.sharktech.projectprob.customtable.TableCell;
import com.sharktech.projectprob.customtable.TableColumn;
import com.sharktech.projectprob.models.VariableNumber;
import com.sharktech.projectprob.models.VariableString;
import com.sharktech.projectprob.persistence.VariablePersistence;

import lexer.Token;
import parser.ParserAdd;
import parser.ParserDelete;
import parser.ParserEdit;
import parser.ParserNew;

class VariableOperation {

    private VariableParser.IParserResult mResult;

    VariableOperation(VariableParser.IParserResult result) {
        this.mResult = result;
    }

    void finish(ParserAdd parser) {
        Token lastToken;
        VariablePersistence persistence = VariablePersistence.getInstance();

        int col = indexOf(lastToken = parser.getValue(Token.COLUMN));
        if(col < 0 || col >= persistence.size()){
            result(lastToken, VariableParser.Error.ERR_COL_INDEX);
            return;
        }
        lastToken = parser.getValue(Token.VAL);

        TableColumn.IVariable variable = VariablePersistence.getInstance().getVariable(col);
        VariableParser.Error err = type(lastToken, variable);

        if (err == VariableParser.Error.MATCH_NUMBER) addNumber(variable, lastToken.getText());
        else if (err == VariableParser.Error.MATCH_TEXT) addString(variable, lastToken.getText());
        else if (err == VariableParser.Error.MATCH_MANY_VALUES) {

            String[] values = lastToken.getText().replaceAll(" ", "").split(",");
            VariableParser.Error type = type(values, variable);
            if ((err = type) == VariableParser.Error.MATCH_NUMBER) addNumber(variable, values);
            else if ((err = type) == VariableParser.Error.MATCH_TEXT) addString(variable, values);
            else err = VariableParser.Error.ERR_GENERAL;
        }

        result(lastToken, err);
    }

    void finish(ParserNew parser){
        String title = parser.getValue(Token.TEXT).getText();
        Token lastToken = parser.getValue(Token.VAL);

        String[] values = lastToken.getText().split(",");
        VariableParser.Error type = type(values);

        TableColumn.IVariable var = null;
        if(type == VariableParser.Error.MATCH_NUMBER) {
            var = new VariableNumber(title);
            addNumber(var, values);
        } else if(type == VariableParser.Error.MATCH_TEXT) {
            var = new VariableString(title);
            addString(var, values);
        }

        if(type == VariableParser.Error.MATCH_NUMBER || type == VariableParser.Error.MATCH_TEXT) {
            VariablePersistence.getInstance().add(var);
        }

        result(lastToken, type);
    }

    void finish(ParserEdit parser){

        VariablePersistence persistence = VariablePersistence.getInstance();
        Token lastToken;

        int col = indexOf(lastToken = parser.getValue(Token.COLUMN));
        if(col < 0 || col >= persistence.size()){
            result(lastToken, VariableParser.Error.ERR_COL_INDEX);
            return;
        }
        TableColumn.IVariable variable = persistence.getVariable(col);
        int row = indexOf(lastToken = parser.getValue(Token.ROW));
        if(row < 0 || row >= variable.nElements()){
            result(lastToken, VariableParser.Error.ERR_ROW_INDEX);
            return;
        }

        lastToken = parser.getValue(Token.VAL);
        VariableParser.Error error = type(lastToken, variable);
        TableCell.ICell cell = null;

        if(error == VariableParser.Error.MATCH_NUMBER) {
            Integer valNum = Integer.parseInt(lastToken.getText());
            cell = new VariableNumber.ValueInteger(valNum);
        } else if(error == VariableParser.Error.MATCH_TEXT) {
            cell = new VariableString.ValueString(lastToken.getText());
        }

        if(cell != null) {
            variable.setElement(cell, row);
        }
        result(lastToken, error);
    }

    void finish(ParserDelete parser) {

        VariablePersistence persistence = VariablePersistence.getInstance();
        Token lastToken;
        int col = indexOf(lastToken = parser.getValue(Token.COLUMN));
        if(col < 0 || col >= persistence.size()){
            result(lastToken, VariableParser.Error.ERR_COL_INDEX);
            return;
        }

        lastToken = parser.getValue(Token.ROW);
        if(lastToken.getType() != Token.EMPTY){
            TableColumn.IVariable variable = persistence.getVariable(col);
            int row = indexOf(lastToken);
            if(row < 0 || row >= variable.nElements()){
                result(lastToken, VariableParser.Error.ERR_ROW_INDEX);
                return;
            }
            variable.getElements().remove(row);
        }else{
            lastToken = parser.getValue(Token.COLUMN);
            persistence.remove(col);
        }

        result(lastToken, VariableParser.Error.MATCH_TEXT);
    }

    private void result(Token lastToken, VariableParser.Error err) {

        if (err == VariableParser.Error.MATCH_TEXT || err == VariableParser.Error.MATCH_NUMBER) {
            mResult.onSuccess();
        } else {
            mResult.onError(err, lastToken);
        }
    }

    private void addNumber(TableColumn.IVariable variable, String value) {
        ((VariableNumber) variable).add(Integer.valueOf(value));
    }

    private void addNumber(TableColumn.IVariable variable, String[] values) {
        Number[] ints = new Number[values.length];
        int index = 0;
        for (String s : values) {
            ints[index++] = Integer.parseInt(s);
        }
        ((VariableNumber) variable).add(ints);
    }

    private void addString(TableColumn.IVariable variable, String value) {
        ((VariableString) variable).add(value);
    }

    private void addString(TableColumn.IVariable variable, String[] values) {
        ((VariableString) variable).add(values);
    }

    private VariableParser.Error type(String[] values) {
        if (values.length > 0) {
            return isNumber(values[0])
                    ? VariableParser.Error.MATCH_NUMBER
                    : VariableParser.Error.MATCH_TEXT;
        }
        return VariableParser.Error.ERR_GENERAL;
    }

    private VariableParser.Error type(String[] values, TableColumn.IVariable variable) {
        if (values.length > 0) {
            boolean isNumber = isNumber(values[0]);
            Class cls = variable.getClass();

            if(isNumber && cls == VariableNumber.class) return VariableParser.Error.MATCH_NUMBER;
            else if(!isNumber && cls == VariableString.class) return VariableParser.Error.MATCH_TEXT;
            else if(isNumber && cls != VariableNumber.class) return VariableParser.Error.ERR_NUMBER_TEXT;
            else if(!isNumber && cls != VariableString.class) return VariableParser.Error.ERR_TEXT_NUMBER;
        }
        return VariableParser.Error.ERR_GENERAL;
    }

    private VariableParser.Error type(Token token, TableColumn.IVariable var) {
        int type = token.getType();
        Class cls = var.getClass();

        if (type == Token.NUMBER && cls == VariableNumber.class) return VariableParser.Error.MATCH_NUMBER;
        else if (type == Token.TEXT && cls == VariableString.class) return VariableParser.Error.MATCH_TEXT;
        else if (type == Token.NUMBER && cls != VariableNumber.class) return VariableParser.Error.ERR_NUMBER_TEXT;
        else if (type == Token.TEXT && cls != VariableString.class) return VariableParser.Error.ERR_TEXT_NUMBER;
        else if (type == Token.VALUES) return VariableParser.Error.MATCH_MANY_VALUES;
        return VariableParser.Error.ERR_GENERAL;
    }

    private boolean isNumber(String values){
        return values.matches("[0-9]+");
    }

    private int indexOf(Token token){
        return Integer.parseInt(token.getText()) - 1;
    }
}
