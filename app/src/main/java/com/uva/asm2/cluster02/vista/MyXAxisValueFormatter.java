package com.uva.asm2.cluster02.vista;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

class MyXAxisValueFormatter implements IAxisValueFormatter {

    private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MMM", new Locale("es", "ES"));

    public MyXAxisValueFormatter(long l, long l2) {
        long t = l;
        long t2 = l2;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // if (this.t == (long)value+1)
        //    return "";
        //if (this.t2 == (long)value)
        //    return "";
        long millis = TimeUnit.DAYS.toMillis((long) value);
        return mFormat.format(new Date(millis));
    }
}
