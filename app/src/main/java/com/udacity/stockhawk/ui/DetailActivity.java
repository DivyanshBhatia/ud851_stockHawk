package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateFormat;
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

public class DetailActivity extends Activity {

    private String quoteSymbol;
    private String quoteHistory;
    @BindView(R.id.quote_symbol_id)
    TextView quoteSymbolView;
    @BindView(R.id.quote_history_id)
    TextView quoteHistoryView;
    @BindView(R.id.chart_id)
    LineChart quoteChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            quoteSymbol=bundle.getString("StockQuoteSymbol");
            quoteHistory=bundle.getString("StockQuoteHistory");
            quoteSymbolView.setText(quoteSymbol);
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
        LineDataSet dataSet = new LineDataSet(entries, "Label");
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
}
