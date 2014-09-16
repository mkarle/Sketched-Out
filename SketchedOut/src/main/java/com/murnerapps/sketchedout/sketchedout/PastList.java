package com.murnerapps.sketchedout.sketchedout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mark on 6/6/2014.
 */
public class PastList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> prompts;
    private final ArrayList<String> dates;
    private final ArrayList<Bitmap> sketches;
    public PastList(Activity context, ArrayList<String> prompts, ArrayList<String> dates, ArrayList<Bitmap> sketches){
        super(context, R.layout.list_single,prompts);
        this.context = context;
        this.prompts = prompts;
        this.dates = dates;
        this.sketches = sketches;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single, null, true);
        TextView txtPrompt = (TextView) rowView.findViewById(R.id.pastPrompt);
        TextView txtDate = (TextView) rowView.findViewById(R.id.pastDate);
        ImageView imgSketch = (ImageView) rowView.findViewById(R.id.pastSketch);
        try {
            txtPrompt.setText(prompts.get(position));
            txtDate.setText(dates.get(position));
            imgSketch.setImageBitmap(sketches.get(position));
        }catch(Exception e){
            e.printStackTrace();
        }
        return rowView;
    }
}
