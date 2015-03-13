package alphadelete.aguaconsciente.ui.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.ConfigDS;
import alphadelete.aguaconsciente.dao.TimerDS;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.TimerItem;
import alphadelete.aguaconsciente.models.TypeItem;
import alphadelete.aguaconsciente.utils.StringFormat;
import alphadelete.aguaconsciente.utils.Chronometer;

public class TimerFragment extends Fragment {
    // the fragment initialization parameters
    private static final String ARG_POSITION = "position";
    private static final String ARG_TYPE = "type";

    private int position;
    private long type;
    private boolean keepScreenOn = false;

    // Database stuff
    private TypeItem activityType;
    private ConfigDS configDatasource;

    //Recording controls
    private FloatingActionButton mRecordButton = null;

    private TextView mRecordingPrompt;
    private int mRecordPromptCount = 0;

    private boolean mStartRecording = true;

    private Chronometer mChronometer = null;

    /**
     * @param position Tab Position.
     * @param type Timer Type.
     * @return A new instance of fragment TimerFragment.
     */
    public static TimerFragment newInstance(int position, long type) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putLong(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public TimerFragment() {
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
        View recordView = inflater.inflate(R.layout.fragment_timer, container, false);

        getTimerType(this.type);

        // Use Chronometer from layout
        mChronometer = (Chronometer) recordView.findViewById(R.id.chronometer);
        //update recording prompt text
        mRecordingPrompt = (TextView) recordView.findViewById(R.id.recording_status_text);

        mRecordButton = (FloatingActionButton) recordView.findViewById(R.id.btnRecord);
        mRecordButton.setColorNormal(getResources().getColor(R.color.primary));
        mRecordButton.setColorPressed(getResources().getColor(R.color.primary_dark));
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecord(mStartRecording);
                mStartRecording = !mStartRecording;
            }
        });

        // Get config to check if it will keep screen on
        getConfigScreenOn();

        return recordView;
    }

    private void onRecord(boolean start){

        if (start) {
            // Start recording
            mRecordButton.setImageResource(R.drawable.ic_media_stop);
            Toast.makeText(getActivity(), R.string.toast_recording_start, Toast.LENGTH_SHORT).show();

            // Start Chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer chronometer) {
                    long millis = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    mRecordingPrompt.setText(
                        StringFormat.literToString(activityType.getLiter(),millis) +
                        " " + getString(R.string.record_in_progress)
                    );
                }
            });

            //keep screen on while recording
            if(keepScreenOn) {
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

            mRecordingPrompt.setText("0.00 " + getString(R.string.record_in_progress));

        } else {
            // Stop recording
            mRecordButton.setImageResource(R.drawable.ic_timer_set);
            Toast.makeText(getActivity(), R.string.toast_recording_finish, Toast.LENGTH_SHORT).show();

            mChronometer.stop();

            // Save the time on database
            long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
            saveTimer(elapsedMillis);

            // Resets chronometer
            mChronometer.setBase(SystemClock.elapsedRealtime());

            mRecordingPrompt.setText(getString(R.string.timer_prompt));

            //allow the screen to turn off again once recording is finished
            if(keepScreenOn) {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }

    private void saveTimer(long millisTimer){
        // Open connection to timer database
        TimerDS timerDatasource = new TimerDS(getActivity());
        timerDatasource.open();

        // Add Item to Database and auto add the adapter
        TimerItem newTimer = timerDatasource.createTimer(
                activityType.getId(),
                activityType.getLiter(),
                millisTimer,
                System.currentTimeMillis()
        );

        //Way to get TagName which generated by FragmentPagerAdapter
        String tagName = "android:switcher:" + R.id.timer_pager + ":" + 1; // Your pager name & tab no of Second Fragment

        //Get SecondFragment object from FirstFragment
        TimerViewerFragment fragTV = (TimerViewerFragment)getActivity().getSupportFragmentManager().findFragmentByTag(tagName);

        //Then call your wish method from SecondFragment to update appropriate list
        fragTV.timersUpdate();

        // Close connection to timer database
        timerDatasource.close();
    }

    private void getConfigScreenOn(){
        // Open connection to database
        configDatasource = new ConfigDS(getActivity());
        configDatasource.open();

        String configValue = configDatasource.getConfigValue("KEEP_SCREEN");
        if(configValue.equals("Y")){
            keepScreenOn = true;
        }

        // Close connection to timer database
        configDatasource.close();
    }

    private void getTimerType(long typeId){
        // Open connection to timer database
        TypeDS typeDatasource = new TypeDS(getActivity());
        typeDatasource.open();

        activityType = typeDatasource.getType(typeId);

        // Close connection to timer database
        typeDatasource.close();
    }

}
