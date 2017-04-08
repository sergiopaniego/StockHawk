package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.DemoBase;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ChartActivity extends DemoBase  {

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private String symbol;
    private ArrayList<Entry> values;
    private List<Long> xAxisValues = new ArrayList<>();
    private float ymax;
    private float ymin=1000f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        // add data
        symbol = getIntent().getExtras().getString(EXTRA_MESSAGE);
        Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(symbol),
                null, null, null, null);
        cursor.moveToFirst();
        String history = cursor.getString(Contract.Quote.POSITION_HISTORY);
        String[] historyArray = history.split("\n");
        setData(historyArray);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date(xAxisValues.get((int)value));
                return new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
            }
        });

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);

        // // dont forget to refresh the drawing
        mChart.invalidate();
        leftAxis.setAxisMaximum(ymax+10f);
        leftAxis.setAxisMinimum(ymin-10f);
    }


    private void setData(String[] history) {

        values = new ArrayList<Entry>();

        for(int i = 0; i < history.length / 2; i++) {
            String temp = history[i];
            history[i] = history[history.length - i - 1];
            history[history.length - i - 1] = temp;
        }

        for (int i = 0; i < history.length; i++) {
            String array[] = history[i].split(",");
            Date date = new Date(Long.parseLong(array[0]));
            Entry c1e1 = new Entry(i, Float.parseFloat(array[1])); // 0 == quarter 1
            values.add(c1e1);
            System.out.println(Float.parseFloat(array[0])/100000 +" "+ Float.parseFloat(array[1])+" "+date);
            if (Float.parseFloat(array[1]) > ymax) {
                ymax=Float.parseFloat(array[1]);
            }else if(Float.parseFloat(array[1]) < ymin){
                ymin=Float.parseFloat(array[1]);
            }
            xAxisValues.add(Long.parseLong(array[0]));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, symbol);

            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.BLUE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }


}