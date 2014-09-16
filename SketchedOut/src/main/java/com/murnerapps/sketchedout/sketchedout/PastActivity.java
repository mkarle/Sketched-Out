package com.murnerapps.sketchedout.sketchedout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class PastActivity extends Activity {



    ArrayAdapter<File> adapter;

    ArrayList<ArrayList<String>> allGuesses;
    ArrayList<String> prompts;
    ArrayList<String> dates;
    ArrayList<Bitmap> sketches;
    RecapFragment recap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past);
        //Create progress bar while list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        final ListView list = (ListView) findViewById(R.id.past_list);

        list.setEmptyView(progressBar);

        //must add bar to root of layout
        ViewGroup root = (ViewGroup) findViewById(R.id.past_layout);
        root.addView(progressBar);



        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_sketchDir/
        File sketchDirectory = cw.getDir("sketchDir", Context.MODE_PRIVATE);
        final File[] sketchFiles = sketchDirectory.listFiles();

        File guessDirectory = cw.getDir("guessDir", Context.MODE_PRIVATE);
        File[] guessFiles = guessDirectory.listFiles();

        allGuesses = extractGuesses(guessFiles);


        //prompts = extractPrompts(allGuesses);
        prompts = extractPrompts(allGuesses);

        dates = extractDates(guessFiles);
        sketches = extractSketches(sketchFiles);


        PastList adapter = new PastList(PastActivity.this, prompts, dates, sketches);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                recap = RecapFragment.newInstance(allGuesses.get(position), extractSketchFiles(sketchFiles[position]));
                getFragmentManager().beginTransaction().add(R.id.past_layout, recap ).commit();
                ((ListView) findViewById(R.id.past_list)).setVisibility(View.GONE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.past, menu);
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
    public void onBackPressed() {
        View list = findViewById(R.id.past_list);
        if(list.getVisibility() == View.GONE){
            list.setVisibility(View.VISIBLE);
            getFragmentManager().beginTransaction().remove(recap).commit();
        }
        else
            super.onBackPressed();
    }

    ArrayList<ArrayList<String>> extractGuesses(File files[]){
        ArrayList<ArrayList<String>> allGuesses = new ArrayList<ArrayList<String>>();

        for(File e : files){
            ArrayList<String> guesses = new ArrayList<String>();
            for(File file : e.listFiles()){
                int length = (int) file.length();

                byte[] bytes = new byte[length];


                try {
                    FileInputStream in = new FileInputStream(file);
                    in.read(bytes);
                } catch (Exception except) {
                    except.printStackTrace();
                }

                String contents = new String(bytes);

                guesses.add(contents);

            }
            allGuesses.add(guesses);
        }
        return allGuesses;
    }

    ArrayList<String> extractPrompts(ArrayList<ArrayList<String>> allGuesses){
        ArrayList<String> prompts = new ArrayList<String>();
        for(ArrayList<String> e: allGuesses){
            if(e.size() > 0)
            prompts.add(e.get(0));
        }
        return prompts;
    }

    ArrayList<String> extractDates(File files[]){
        ArrayList<String> dates = new ArrayList<String>();

        for(int i = 0; i < files.length; i++){
            String date = files[i].getName();
            String day = date.substring(0, 2);
            day = Integer.toString(Integer.parseInt(day));
            int monthInt = Integer.parseInt(date.substring(2, 4));
            String month;
            String year = date.substring(4, 8);
            String hour = date.substring(9, 11);
            int hourInt = Integer.parseInt(hour);
            if(hourInt > 12)
                hourInt = hourInt - 12;
            hour = Integer.toString(hourInt);
            String minute = date.substring(11, 13);
            switch (monthInt){
                case 1: month = "Jan";
                    break;
                case 2: month = "Feb";
                    break;
                case 3: month = "Mar";
                    break;
                case 4: month = "Apr";
                    break;
                case 5: month = "May";
                    break;
                case 6: month = "Jun";
                    break;
                case 7: month = "Jul";
                    break;
                case 8: month = "Aug";
                    break;
                case 9: month = "Sep";
                    break;
                case 10: month = "Oct";
                    break;
                case 11: month = "Nov";
                    break;
                case 12: month = "Dec";
                    break;
                default : month = Integer.toString(monthInt);
            }


            date = hour + ":" + minute + "\t" + month + " " + day;
            dates.add(date);
        }

        return dates;
    }

    ArrayList<Bitmap> extractSketches(File files[]){
        ArrayList<Bitmap> sketches = new ArrayList<Bitmap>();

        try {
            for(File e : files){
                File f = new File (e, "SO_1.png");

                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

                sketches.add(b);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return sketches;
    }

    ArrayList<String> extractSketchFiles(File file){
        ArrayList<String> sketchFiles = new ArrayList<String>();

        for(File e : file.listFiles()){
            sketchFiles.add(e.getPath());
        }

        return sketchFiles;
    }

}
