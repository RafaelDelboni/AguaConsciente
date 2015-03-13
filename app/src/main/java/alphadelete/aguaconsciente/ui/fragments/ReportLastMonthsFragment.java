package alphadelete.aguaconsciente.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TimerDS;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.SumTypeItem;
import alphadelete.aguaconsciente.models.TimerItem;

public class ReportLastMonthsFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_POSITION = "position";
    private int position;

    // Database
    private TypeDS typeDatasource;

    // Chart
    protected BarChart mChart;
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    public static ReportLastMonthsFragment newInstance(int position) {
        ReportLastMonthsFragment fragment = new ReportLastMonthsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportLastMonthsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View recordView = inflater.inflate(R.layout.fragment_report_last_months, container, false);

        // Creates the chart
        mChart = (BarChart) recordView.findViewById(R.id.chart1);
        //mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(true);
        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8);

        mChart.getAxisRight().setEnabled(false);

        mChart.setHighlightEnabled(false);

        setData();
        //setDataFake(12, 1000);

        animateChart();

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        return recordView;
    }

    public void animateChart () {
        mChart.animateY(3000);
    }

    private void setData() {

        // Open connection to timer database
        typeDatasource = new TypeDS(getActivity());
        typeDatasource.open();

        List<SumTypeItem> sumTypesMonth = new ArrayList<SumTypeItem>();
        ArrayList<String> xMonthVals = new ArrayList<String>();
        ArrayList<BarEntry> yMonthVals = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            Calendar cal = Calendar.getInstance();
            // subtract month
            cal.add(Calendar.MONTH, -i);

            // get to the lowest day of that month
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            // lowest possible time in that day
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
            cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
            // Store it to use later
            long longIni = cal.getTimeInMillis();
            Date ini = new Date(longIni);

            // get to the latest day of that month
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            // max possible time in that day
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
            cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
            // Store it to use later
            long longEnd = cal.getTimeInMillis();
            Date end = new Date(longEnd);

            // Set Month name for X
            xMonthVals.add(
                cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                + "/" +
                cal.get(Calendar.YEAR)
            );

            // Get totals from database
            sumTypesMonth = typeDatasource.getSumTypeTotal(longIni, longEnd);

            // Set Values for Y
            float[] yData = new float[sumTypesMonth.size()];
            for (int j = 0; j < sumTypesMonth.size(); j++) {
                yData[j] = sumTypesMonth.get(j).getLiter();
            }
            yMonthVals.add(new BarEntry(yData, i));

            //insertFakeData(longIni);
        }

        // Close connection to timer database
        typeDatasource.close();

        // Chart dataset
        BarDataSet dataSetMonth = new BarDataSet(yMonthVals, "");

        // Colors
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        int[] ALPHA_COLORS = {
                Color.rgb(35, 77, 208), Color.rgb(37, 129, 218), Color.rgb(43, 160, 195),
                Color.rgb(37, 218, 212), Color.rgb(35, 208, 153)
        };

        for (int c : ALPHA_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSetMonth.setColors(colors);

        // Set Label data
        String[] labels = new String[sumTypesMonth.size()];
        for (int j = 0; j < sumTypesMonth.size(); j++) {
            labels[j] = sumTypesMonth.get(j).getDesc();
        }
        dataSetMonth.setStackLabels(labels);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(dataSetMonth);

        BarData data = new BarData(xMonthVals, dataSets);

        data.setDrawValues(false);

        mChart.setData(data);

        mChart.invalidate();

    }

    private void insertFakeData(long data){
        // Open connection to timer database
        TimerDS timerDatasource = new TimerDS(getActivity());
        timerDatasource.open();
        TimerItem newTimer;

        // Add Item to Database and auto add the adapter
        newTimer = timerDatasource.createTimer(
                1,
                10,
                (float) Math.random() * 10000,
                data
        );

        newTimer = timerDatasource.createTimer(
                2,
                20,
                (float) Math.random() * 10000,
                data
        );

        newTimer = timerDatasource.createTimer(
                3,
                30,
                (float) Math.random() * 10000,
                data
        );

        newTimer = timerDatasource.createTimer(
                4,
                40,
                (float) Math.random() * 10000,
                data
        );

        // Close connection to timer database
        timerDatasource.close();
    }

}
