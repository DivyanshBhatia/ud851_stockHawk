package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private String quoteSymbol;
    private String quoteHistory;
    @BindView(R.id.chart_id)
    LineChart quoteChart;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        actionBar=this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            quoteSymbol=bundle.getString(getApplicationContext().getResources().getString(R.string.stock_quote_symbol));
            quoteHistory=bundle.getString(getApplicationContext().getResources().getString(R.string.stock_quote_history));

            if(actionBar!=null && quoteSymbol!=null)
            actionBar.setTitle(getResources().getString(R.string.details_placeholder)+" "+quoteSymbol.toUpperCase());
            addGraph(quoteHistory);
        }
    }

    private void addGraph(String quoteHistory) {
        List<Entry> entries = new ArrayList<Entry>();
        final List<String> dateList=new ArrayList<String>();
        String[] quoteLine=quoteHistory.split("\n");
        int quotePostion=quoteLine.length;
        int quoteVar=quotePostion-1;
        int index=0;
        while(quoteVar>=0){
            dateList.add(String.valueOf(DateFormat.format("dd/MM/yyyy",Long.parseLong(quoteLine[quoteVar].replace(" ","").split(",")[0]))));
            Entry entry=new Entry(index++, Float.parseFloat(quoteLine[quoteVar].replace(" ","").split(",")[1]));
            entries.add(entry);
            quoteVar--;
        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (String) dateList.toArray()[(int) value];
            }
        };
        LineDataSet dataSet = new LineDataSet(entries, getResources().getString(R.string.label_entry));
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.GREEN);
        dataSet.setCircleColor(Color.CYAN); // styling
        LineData lineData = new LineData(dataSet);
        quoteChart.setData(lineData);
        quoteChart.setNoDataTextColor(Color.WHITE);
        quoteChart.getXAxis().setTextColor(Color.GREEN);
        quoteChart.getAxisRight().setTextColor(Color.GREEN);
        quoteChart.getAxisLeft().setTextColor(Color.GREEN);
        XAxis xAxis = quoteChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        quoteChart.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
