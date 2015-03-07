package alphadelete.aguaconsciente.ui.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.TypeItem;
import alphadelete.aguaconsciente.ui.fragments.TimerFragment;
import alphadelete.aguaconsciente.ui.fragments.TimerViewerFragment;


public class TimerActivity extends BaseParentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;

    private TypeDS typeDatasource;
    private long typeID;
    private TypeItem activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity based on XML layout
        setContentView(R.layout.activity_timer);

        // Override super method after the super
        super.onCreate(savedInstanceState);

        // Set the Tabs Manager
        pager = (ViewPager) findViewById(R.id.timer_pager);
        pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        // Set toolbar as Actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // Set back button and back action
            toolbar.setNavigationIcon(R.drawable.ic_back_button);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        typeID = Long.parseLong(message);

        // Set the Timer Type
        getTimerType(typeID);
    }

    public class MyAdapter extends FragmentPagerAdapter {
        private String[] titles = { getString(R.string.tab_timer_record),
                getString(R.string.tab_timer_saved_recordings) };

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:{
                    return TimerFragment.newInstance(position, activityType.getId());
                }
                case 1:{
                    return TimerViewerFragment.newInstance(position, activityType.getId());
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private void getTimerType(long typeId){
        // Open connection to timer database
        typeDatasource = new TypeDS(this);
        typeDatasource.open();

        activityType = typeDatasource.getType(typeId);

        // Close connection to timer database
        typeDatasource.close();

        // Set the MainActivity title
        this.setTitle(activityType.getDesc());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh screen data
        getTimerType(typeID);
    }

    /*
    @Override
    public void onPause() {

        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.button_start);
        b.setText("start");

    }*/

}
