package com.murnerapps.sketchedout.sketchedout;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class RecapActivity extends ActionBarActivity {

   Fragment currFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);
        Intent intent = getIntent();
        currFrag = RecapFragment.newInstance(intent.getStringArrayListExtra(GameActivity.PROMPT_KEY), intent.getStringArrayListExtra(GameActivity.SKETCH_KEY));
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.recap_container, currFrag)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
