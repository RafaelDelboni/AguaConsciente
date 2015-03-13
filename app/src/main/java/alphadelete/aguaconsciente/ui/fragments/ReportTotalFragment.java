package alphadelete.aguaconsciente.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.SumTypeItem;

public class ReportTotalFragment extends Fragment implements OnChartValueSelectedListener {
    // the fragment initialization parameters
    private static final String ARG_POSITION = "position";
    private int position;

    // Database
    private TypeDS typeDatasource;

    // Chart
    protected PieChart mChart;

    public static ReportTotalFragment newInstance(int position) {
        ReportTotalFragment fragment = new ReportTotalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public ReportTotalFragment() {
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
        View recordView = inflater.inflate(R.layout.fragment_report_total, container, false);


        mChart = (PieChart) recordView.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);

        // change the color of the center-hole
        mChart.setHoleColorTransparent(true);

        mChart.setHoleRadius(60f);

        mChart.setDescription("");

        mChart.setDrawCenterText(true);

        mChart.setDrawHoleEnabled(true);

        mChart.setRotationAngle(0);

        // disable rotation of the chart by touch
        mChart.setRotationEnabled(false);

        // disable X axis text
        mChart.setDrawSliceText(!mChart.isDrawSliceTextEnabled());

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        // Set data and Get total Liters
        float total = setData();

        mChart.setCenterText(
                getActivity().getString(R.string.chart_total)
                + "\n" +
                String.format("%.2f", total) + " " + getActivity().getString(R.string.msg_liter));

        animateChart();

        // Set Legend
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);

        return recordView;
    }

    public void animateChart () {
        mChart.animateXY(1500, 1500);
    }

    private float setData() {
        float totalLiters = 0;

        // Open connection to timer database
        typeDatasource = new TypeDS(getActivity());
        typeDatasource.open();

        // Get Totals from database
        List<SumTypeItem> Total;
        Total = typeDatasource.getSumTypeTotal();

        // Close connection to timer database
        typeDatasource.close();

        // Set Chart Axis Data
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        for (SumTypeItem item : Total) {
            yVals.add(new Entry(item.getLiter(), (int)item.getId()));
            xVals.add(item.getDesc());
            totalLiters += item.getLiter();
        }

        PieDataSet dataSet = new PieDataSet(yVals, "");
        dataSet.setSliceSpace(3f);

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

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();

        return totalLiters;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
    }

    @Override
    public void onNothingSelected() {

    }

}
