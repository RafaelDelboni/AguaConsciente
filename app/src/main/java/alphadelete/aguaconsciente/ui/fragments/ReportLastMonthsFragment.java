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
import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.SumTypeItem;

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

        ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8);
        leftAxis.setValueFormatter(custom);

        mChart.getAxisRight().setEnabled(false);

        //mChart.setVisibility(View.VISIBLE);
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

        Calendar cal = Calendar.getInstance();

        // Open connection to timer database
        typeDatasource = new TypeDS(getActivity());
        typeDatasource.open();

        List<SumTypeItem> sumTypesMonth = new ArrayList<SumTypeItem>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {

            // get to the lowest day of that month
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            // lowest possible time in that day
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
            cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
            // Store it to use later
            long longIni = cal.getTimeInMillis();

            // get to the latest day of that month
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            // max possible time in that day
            cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
            cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
            // Store it to use later
            long longEnd = cal.getTimeInMillis();

            // Set Month name for X
            xVals.add(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));

            // Get totals from database
            sumTypesMonth = typeDatasource.getSumTypeTotal(longIni, longEnd);

            // Set Values for Y
            float[] yData = new float[sumTypesMonth.size()];
            for (int j = 0; j < sumTypesMonth.size(); j++) {
                yData[j] = sumTypesMonth.get(j).getLiter();
            }
            yVals.add(new BarEntry(yData, i));

            // subtract month
            cal.add(Calendar.MONTH, -1);
        }

        // Close connection to timer database
        typeDatasource.close();

        // Chart dataset
        BarDataSet dataSet = new BarDataSet(yVals, "");

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

        dataSet.setColors(colors);

        // Set Label data
        String[] labels = new String[sumTypesMonth.size()];
        for (int j = 0; j < sumTypesMonth.size(); j++) {
            labels[j] = sumTypesMonth.get(j).getDesc();
        }
        dataSet.setStackLabels(labels);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(dataSet);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());

        data.setDrawValues(false);

        mChart.setData(data);

        mChart.setHighlightEnabled(false);

        mChart.invalidate();

    }

    private void setDataFake(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count + 1; i++) {
            xVals.add(mMonths[i % mMonths.length]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count + 1; i++) {
            float mult = (range + 1);
            float val1 = (float) (Math.random() * mult) + mult / 3;
            float val2 = (float) (Math.random() * mult) + mult / 3;
            float val3 = (float) (Math.random() * mult) + mult / 3;

            yVals1.add(new BarEntry(new float[] {
                    val1, val2, val3
            }, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
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

        set1.setColors(colors);
        set1.setStackLabels(new String[] {
            "Escovar dentes", "Lavar Louça", "Tomar banho"
        });

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new MyValueFormatter());

        data.setDrawValues(false);

        mChart.setData(data);

        mChart.setHighlightEnabled(false);

        mChart.invalidate();

    }

    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("##,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);// + " $";
        }

    }

}
