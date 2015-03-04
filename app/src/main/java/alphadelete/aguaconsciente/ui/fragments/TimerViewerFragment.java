package alphadelete.aguaconsciente.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TimerDS;
import alphadelete.aguaconsciente.models.TimerItem;
import alphadelete.aguaconsciente.ui.adapters.TimerItemArrayEditAdapter;

public class TimerViewerFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_POSITION = "position";
    private static final String ARG_TYPE = "type";
    private TimerDS timerDatasource;
    private List<TimerItem> allTimers;
    private TimerItemArrayEditAdapter ca;

    private int position;
    private long type;

    /**
     * @param position Tab Position.
     * @param type Timer Type.
     * @return A new instance of fragment TimerFragment.
     */
    public static TimerViewerFragment newInstance(int position, long type) {
        TimerViewerFragment fragment = new TimerViewerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putLong(ARG_TYPE, type);

        fragment.setArguments(args);

        return fragment;
    }

    public TimerViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            type = getArguments().getLong(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timer_viewer, container, false);

        RecyclerView recList = (RecyclerView) v.findViewById(R.id.timerEditList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        // Get Timers list from Database
        allTimers = listTimers(this.type);

        // Set and Fill ListView
        ca = new TimerItemArrayEditAdapter(getActivity(), allTimers);
        recList.setAdapter(ca);

        return v;

    }

    private List<TimerItem> listTimers(long itemType) {
        List<TimerItem> _allTimers;

        // Open connection to timer database
        timerDatasource = new TimerDS(getActivity());
        timerDatasource.open();

        _allTimers = timerDatasource.getAllTimerItems(itemType);

        // Close connection to timer database
        timerDatasource.close();

        return _allTimers;
    }

    public void timersUpdate(){
        allTimers.clear();
        allTimers.addAll(listTimers(this.type));
        ca.notifyDataSetChanged();
    }

}
