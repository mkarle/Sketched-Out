package com.murnerapps.sketchedout.sketchedout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Mark on 5/29/2014.
 */
public class GuessFragment extends Fragment {

    ImageView next, sketchView;
    EditText guessInput;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_guess, container, false);


        setupGuessInput(rootView);

        //Next
        setupNext(rootView);


        setupSketch(rootView);
        return rootView;
    }


    private void setupGuessInput(View rootView){
        guessInput = (EditText) rootView.findViewById(R.id.guessInput);
        guessInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length()<1){
                    next.setVisibility(View.INVISIBLE);
                }
                else
                next.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setupNext( View rootView){
        next = (ImageView) rootView.findViewById(R.id.nextGuess);
        next.setVisibility(View.INVISIBLE);
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:

                        next.setImageDrawable(getResources().getDrawable(R.drawable.nextpressed));
                        break;

                    case MotionEvent.ACTION_UP:
                        next.setImageDrawable(getResources().getDrawable(R.drawable.next));
                        break;
                }
                return false;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    ((GameActivity) getActivity()).addGuess(guessInput.getText().toString());
                    ((GameActivity) getActivity()).nextFragment(v);



            }
        });
    }

    private void setupSketch(View rootView){
        sketchView = (ImageView) rootView.findViewById(R.id.sketchView);
        Bundle bundle = getArguments();
        String filename = bundle.getString(GameActivity.SKETCH_KEY);
        try {

            File f=new File(filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            sketchView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
