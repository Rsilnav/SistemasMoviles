package com.uva.asm2.cluster02.vista;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

class MyYAxisValueFormatter implements IAxisValueFormatter {

    public MyYAxisValueFormatter() {
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return String.valueOf(value) + " €";
    }

}