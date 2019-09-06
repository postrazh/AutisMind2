package com.autismindd.utilities;

/**
 * Created by RAFI on 12/1/2016.
 */
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class MyValueFormatter implements ValueFormatter {
   /* private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("########0.0"); // use one decimal
    }
    @Override
    public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {

        return mFormat.format(v) + " %";
    }*/
   protected DecimalFormat mFormat;

    public MyValueFormatter() {
//        mFormat = new DecimalFormat("###,###,##0.0");
        mFormat = new DecimalFormat("#########");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public MyValueFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // IValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value) + " %";
    }

 /*   // IAxisValueFormatter
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " %";
    }*/

    public int getDecimalDigits() {
        return 1;
    }


}
