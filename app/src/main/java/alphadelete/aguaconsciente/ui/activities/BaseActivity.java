package alphadelete.aguaconsciente.ui.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.ui.fragments.AboutFragment;

public class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String name = this.getClass().getSimpleName();

        switch (item.getItemId()) {
            case R.id.action_timer:
                openTimer(name);
                return true;
            case R.id.action_reports:
                openReports(name);
                return true;
            case R.id.action_settings:
                openConfig(name);
                return true;
            case R.id.action_about:
                openAbout(name);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openTimer(String previousName) {
        // Do something in response to button
        if (!previousName.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void openReports(String previousName) {
        // Do something in response to button
    }

    public void openConfig(String previousName) {
        // Do something in response to button
        if (!previousName.equals("ConfigActivity")) {
            Intent intent = new Intent(this, ConfigActivity.class);
            startActivity(intent);
        }
    }

    public void openAbout(String previousName) {
        // Do something in response to button
        AboutFragment aboutFragment = new AboutFragment();
        aboutFragment.show(getSupportFragmentManager().beginTransaction(), "dialog_about");
    }
}
