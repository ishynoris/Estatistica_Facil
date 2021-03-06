package com.sharktech.projectprob.analyse;

import android.util.Log;

import java.util.ArrayList;

import static com.sharktech.projectprob.analyse.DataAnalyse.Average;
import static com.sharktech.projectprob.analyse.DataAnalyse.Sum;

class DataAnalyseCalc {

    private DataAnalyseCalc() {
    }

    static Double get(Average key, DataAnalyseResult values) {
        if (key == Average.ARITHMETIC) {

            double sumValues = values.get(Sum.SUM_VALUES);
            int n = values.sort().size();
            return sumValues / n;

        } else if (key == Average.ARITHMETIC_POUND) {

            double sumValues = values.get(Sum.SUM_VAL_MULTI_FREQ);
            double sumFreq = values.get(Sum.SUM_FREQUENCY);
            return sumValues / sumFreq;

        } else if (key == Average.GEOMETRIC) {

            int n = values.sort().size();
            if (n == 0) return 0d;
            double val = values.get(Sum.PROD_VALUES);
            return Math.pow(val, (1 / n));

        } else if (key == Average.GEOMETRIC_POUND) {

            double freq = values.get(Sum.SUM_FREQUENCY);
            if (freq == 0) return 0d;
            double val = values.get(Sum.PROD_VAL_POW_FREQ);
            return Math.pow(val, (1 / freq));

        } else if (key == Average.WEIGHTED) {

            int n = values.sort().size();
            double val = values.get(Sum.SUM_ONE_DIV_VAL);
            return val == 0 ? 0 : n / val;

        } else if (key == Average.WEIGHTED_POUND) {

            double freq = values.get(Sum.SUM_FREQUENCY);
            double val = values.get(Sum.SUM_FREQ_DIV_VAL);
            return val == 0 ? 0 : freq / val;

        } else if (key == Average.QUADRATIC) {

            double val = values.get(Sum.SUM_SQRT_VAL);
            return Math.sqrt(val);

        } else if (key == Average.QUADRATIC_POUND) {

            double val = values.get(Sum.SUM_SQRT_VAL_MULTI_FREQ);
            return Math.sqrt(val);

        } else {
            throw new IllegalArgumentException("You must use enum " + Average.class.getName());
        }
    }

    static Double asymmetry(ArrayList<DataAnalyseValue> values) {

        double sumFrequency = sumFrequency(values);
        Double fstQuart = elemBeforeValue(values, indexFirstQuart(sumFrequency));
        Double secQuart = elemBeforeValue(values, indexSecondQuart(sumFrequency));
        Double trdQuart = elemBeforeValue(values, indexThirdQuart(sumFrequency));
        return (fstQuart == null || secQuart == null || trdQuart == null) ? null
                : (trdQuart + fstQuart - 2 * secQuart) / (trdQuart - fstQuart);
    }

    static Double kurtosis(ArrayList<DataAnalyseValue> values) {
        double sumFrequency = sumFrequency(values);
        Double fstQuart = elemBeforeValue(values, indexFirstQuart(sumFrequency));
        Double trdQuart = elemBeforeValue(values, indexThirdQuart(sumFrequency));
        Double fstTenth = elemBeforeValue(values, indexFirstTenth(sumFrequency));
        Double nthTenth = elemBeforeValue(values, indexNinthTenth(sumFrequency));
        return (fstQuart == null || trdQuart == null || fstTenth == null || nthTenth == null) ? null
                : (trdQuart - fstQuart) / (2 * (nthTenth - fstTenth));
    }

    static void intervalAverage(DataAnalyse.IntervalConfidenceValues values){
        try {
            if (values.isEmpty()) {
                values.onError();
                return;
            }
            double correction = 1;
            if (!values.isNull(values.populationSize) && !values.isNull(values.sampleSize) && values.sampleSize > 0) {
                double dif = (values.populationSize - values.sampleSize) / (values.populationSize - 1);
                correction = Math.sqrt(dif);
            }
            float z = TableDistribution.normal((float) (values.confidence / 200));
            if (z < 0) {
                values.onError();
                return;
            }
            double error = z * (values.deviation / (Math.sqrt(values.sampleSize)) * correction);
            values.onSuccess(values.sampleAvg - error, values.sampleAvg + error, error, z, values);
        }catch(Exception e){
            values.onError();
        }
    }

    static void intervalProportion(DataAnalyse.IntervalConfidenceValues values){
        try {
            if (values.isEmpty()) {
                values.onError();
                return;
            }
            float z = TableDistribution.normal((float) (values.confidence / 200));
            if (z < 0) {
                values.onError();
                return;
            }
            float success = (float) (values.success / values.sampleSize);
            float fail = 1f - success;
            float deviation = (float) Math.sqrt((success * fail) / values.sampleSize);
            double error = z * deviation;
            values.onSuccess((success - error), (success + error), error, z, values);
        } catch(Exception e) {
            values.onError();
        }
    }

    static void intervalVariance(DataAnalyse.IntervalConfidenceValues values){
        try {
            if (values.isEmpty()) {
                values.onError();
                return;
            }

            float limitInf = (float) ((100 - values.confidence) / 2) / 100f;
            float limitSup = limitInf + (float) (values.confidence / 100);
            int degree = values.sampleSize.intValue() - 1;

            float quiInf = TableDistribution.quiQuadratic(limitInf, degree);
            float quiSup = TableDistribution.quiQuadratic(limitSup, degree);
            if (quiInf < 0 || quiSup < 0) {
                values.onError();
                return;
            }

            double prod = (degree * values.variance.floatValue());
            values.onSuccess((prod / quiInf), (prod / quiSup), prod, -1, values);
        } catch (Exception e){
            values.onError();
        }
    }

    private static double sumFrequency(ArrayList<DataAnalyseValue> values) {
        double sumFrequency = 0;
        for (DataAnalyseValue value : values) {
            sumFrequency += value.getFrequency();
        }
        return sumFrequency;
    }

    private static double indexFirstQuart(double sumFrequency) {
        return (sumFrequency + 1) / 4;
    }

    private static double indexSecondQuart(double sumFrequency) {
        return (sumFrequency + 1) / 2;
    }

    private static double indexThirdQuart(double sumFrequency) {
        return 3 * indexFirstQuart(sumFrequency);
    }

    private static double indexFirstTenth(double sumFrequency) {
        return (sumFrequency + 1) / 4;
    }

    private static double indexNinthTenth(double sumFrequency) {
        return (sumFrequency + 1) / 2;
    }

    private static Double elemBeforeValue(ArrayList<DataAnalyseValue> values, double value) {
        double accumulated = 0;

        for (DataAnalyseValue data : values) {

            if (data.isNumber()) {
                accumulated += data.getFrequency();
                if (value <= accumulated) {
                    return data.asNumber();
                }
            }
        }
        return null;
    }
}
