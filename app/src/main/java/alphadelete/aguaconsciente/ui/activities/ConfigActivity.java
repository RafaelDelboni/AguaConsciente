package alphadelete.aguaconsciente.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.ConfigDS;
import alphadelete.aguaconsciente.ui.adapters.TypeItemArrayEditAdapter;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.TypeItem;

public class ConfigActivity extends BaseParentActivity {
    List<TypeItem> allTimers;
    private TypeDS typeDatasource;
    private ConfigDS configDatasource;
    private TypeItemArrayEditAdapter ca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create Activity based on XML layout
        setContentView(R.layout.activity_config);

        // Override super method after the super
        super.onCreate(savedInstanceState);

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

        // Define Switch
        Switch mySwitch = (Switch) findViewById(R.id.config_switch_screen);
        // Get data from config table
        mySwitch.setChecked(getConfigScreenOn());
        // Define witch action do in switch
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    setConfigScreenOn("Y");
                } else {
                    setConfigScreenOn("N");
                }
            }
        });

        // Define RecyclerView LayoutManager
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        // Get Timers list from Database
        allTimers = listTimers();

        if (allTimers.size() > 0) {
            // Set and Fill ListView
            ca = new TypeItemArrayEditAdapter(this, allTimers);
            recList.setAdapter(ca);
        }
    }

    private boolean getConfigScreenOn(){
        Boolean value = false;

        // Open connection to database
        configDatasource = new ConfigDS(this);
        configDatasource.open();

        String configValue = configDatasource.getConfigValue("KEEP_SCREEN");
        if(configValue.equals("Y")){
            value = true;
        }

        // Close connection to timer database
        configDatasource.close();

        return value;
    }

    private void setConfigScreenOn(String value){
        // Open connection to database
        configDatasource = new ConfigDS(this);
        configDatasource.open();

        configDatasource.setConfigValue("KEEP_SCREEN", value);

        // Close connection to timer database
        configDatasource.close();
    }

    private List<TypeItem> listTimers(){
        List<TypeItem> _allTimers;

        // Open connection to database
        typeDatasource = new TypeDS(this);
        typeDatasource.open();

        //allTimers.clear();
        _allTimers = typeDatasource.getType();

        // Close connection to timer database
        typeDatasource.close();

        return _allTimers;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Clear and Refresh the grid
        allTimers.clear();
        allTimers.addAll(listTimers());
        ca.notifyDataSetChanged();
    }

}
