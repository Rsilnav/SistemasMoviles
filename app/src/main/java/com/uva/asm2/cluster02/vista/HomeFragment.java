package com.uva.asm2.cluster02.vista;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.uva.asm2.cluster02.R;
import com.uva.asm2.cluster02.modelo.Movimiento;
import com.uva.asm2.cluster02.modelo.MovimientoDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private LineChart mChart;
    private TextView saldoTotal;

    public HomeFragment() { }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mChart = (LineChart) view.findViewById(R.id.chart1);
        saldoTotal = (TextView) view.findViewById(R.id.saldoTotal);

        setGrafica();
        return view;
    }


    private void setGrafica() {

        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        mChart.setBackgroundColor(Color.WHITE);
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        setData();

        mChart.invalidate();

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        //xAxis.setXOffset(1000f);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setGranularity(1f); // one day
        xAxis.setValueFormatter(new MyXAxisValueFormatter(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()), TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()-7*24*3600*1000)));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(mChart.getData().getYMin()/1.1f);
        leftAxis.setAxisMaximum(mChart.getData().getYMax()*1.1f);
        leftAxis.setYOffset(-5f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));
        leftAxis.setValueFormatter(new MyYAxisValueFormatter());

        mChart.setExtraBottomOffset(500f);
        //mChart.animateX(500);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setData() {

        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());

        ArrayList<Movimiento> listaMovimientos = movimientoDbHelper.getMovimientos();


        HashMap<Long, Float> mapa = new HashMap<>();
        Long lastDay = 0L;
        if (listaMovimientos.size() > 0)
            lastDay = TimeUnit.MILLISECONDS.toDays(listaMovimientos.get(0).getFecha().getTime().getTime());

        Long first = lastDay;
        Long last = lastDay;

        for (Movimiento m: listaMovimientos) {
            Long day = TimeUnit.MILLISECONDS.toDays(m.getFecha().getTime().getTime());
            if (day > last) last = day;
            if (day < first) first = day;
            Float cantidad = m.getCantidad();
            if (m.getTipo() == Movimiento.TIPO.GASTO)
                cantidad *= -1;

            if (mapa.containsKey(day)) {
                mapa.put(day, mapa.get(day) + cantidad);
            }
            else {
                mapa.put(day, cantidad);
            }
        }

        long now = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis());

        HashMap<Float,Float> sumadas = new HashMap<>();
        Float total = 0f;
        for (Long i = 0L; i <= last; i++) {
            if (mapa.containsKey(i)) {
                total += mapa.get(i);
            }
            sumadas.put(i.floatValue(), total);
        }

        ArrayList<Entry> entradas = new ArrayList<>();


        for (Float i = (float) (last - 29); i<= last; i++) {
            if (sumadas.containsKey(i)) {
                Entry e = new Entry(i, sumadas.get(i));
                entradas.add(e);
            }
        }


        String saldo = String.valueOf(total) +  getString(R.string.euro);
        saldoTotal.setText(saldo);

        LineDataSet set1 = new LineDataSet(entradas, getString(R.string.dinero));
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
        set1.setDrawValues(true);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);


        //set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        mChart.setData(data);


    }
}
