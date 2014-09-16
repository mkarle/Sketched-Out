package com.murnerapps.sketchedout.sketchedout;

import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.os.Build;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*
 Todo list:
 Timer
 start game
 past games
 multiplayer
 share
 better palettes
 "Pass to next player" between fragments
 collages
 tutorial
 */

public class GameActivity extends ActionBarActivity {


    protected static final int MAX_ROUNDS = 8;
    protected static final String PROMPT_KEY = "com.murnerapps.prompt";
    protected static final String SKETCH_KEY = "com.murnerapps.sketch";


    public int roundNumber;
    public String timeStamp;

    Fragment currFrag;
    Bundle fragBundle = new Bundle();

    //list of guesses from user
    ArrayList<String> guesses = new ArrayList<String>();
    //list of filenames of sketches
    ArrayList<String> sketches = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        currFrag = new DrawFragment();

        //put the prompt for the drawfrag here
        String origPrompt = getNewPrompt();
        guesses.add(origPrompt);
        fragBundle.putString(PROMPT_KEY, origPrompt);
        currFrag.setArguments(fragBundle);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.game_container, currFrag)
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        roundNumber = 1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
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

    @Override
    protected void onStop(){
        super.onStop();
        if (roundNumber <= MAX_ROUNDS){
            if (sketches.size() > 0){
                File file = new File(sketches.get(0));
                file.getParentFile().delete();
            }
        }
        finish();
    }

    public void nextFragment(View view){
        roundNumber++;
        if(roundNumber>MAX_ROUNDS){
            saveGame();
            endGame();
            finish();
        }
        else if(view.getTag().toString().equals("sketch")){
            createGuessFrag();

        }
        else if(view.getTag().toString().equals("guess")){
            createDrawFrag();

        }
    }

    public void addGuess(String guess){
        guesses.add(guess);
    }
    public void addSketch(String sketch){ sketches.add(sketch); }

    private void endGame(){
        //start an activity showing the game progression
        Intent intent = new Intent(this, RecapActivity.class);
        intent.putStringArrayListExtra(PROMPT_KEY, guesses);
        intent.putStringArrayListExtra(SKETCH_KEY, sketches);
        startActivity(intent);

    }

    private void createGuessFrag(){
        getSupportFragmentManager().beginTransaction().remove(currFrag).commit();
        currFrag = new GuessFragment();

        fragBundle.putString(SKETCH_KEY, sketches.get(sketches.size() - 1));
        currFrag.setArguments(fragBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.game_container, currFrag ).commit();
    }

    private void createDrawFrag(){
        getSupportFragmentManager().beginTransaction().remove(currFrag).commit();
        currFrag = new DrawFragment();

        fragBundle.putString(PROMPT_KEY, guesses.get(guesses.size() - 1));
        currFrag.setArguments(fragBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.game_container, currFrag ).commit();
    }

    private String getNewPrompt(){
        return RandomPrompt.getPrompt(getResources());
    }

    private void saveGame(){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_sketchDir/
        File directory = cw.getDir("guessDir", Context.MODE_PRIVATE);
        directory = new File (directory + "/" + timeStamp);
        directory.mkdirs();
        for(int i = 0; i < guesses.size(); i++) {
            String filename = "SO_" + i;
            File path = new File(directory, filename);

            FileOutputStream stream;
            try {
                stream = new FileOutputStream(path);

                stream.write(guesses.get(i).getBytes());

                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
