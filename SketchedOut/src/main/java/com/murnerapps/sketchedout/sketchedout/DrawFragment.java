package com.murnerapps.sketchedout.sketchedout;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Mark on 5/25/14.
 */
public class DrawFragment extends Fragment {

        //The custom view for drawing on
        private DrawingView drawView;

        //Views:
        //deleteDrawing - clears the sketch from the drawView
        //undoDrawing - removes the last touch on the drawView
        //currPaint - the selected paint color
        //save - saves the sketch
        //next - ends the drawing fragment and advances the game
        private ImageView deleteDrawing, undoDrawing, currPaint, save, next;

        //Pictures signifying a paint selected and unselected
        private Drawable paintPressed, paint;

        //tells the user what to draw
        private TextView promptText;

        public DrawFragment() {
        }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_draw, container, false);



            closeKeyboard(getActivity(), rootView.getWindowToken());

        //DrawView
            setupDrawView(rootView);

        //Paint selection
            setupPaintSelect(rootView);

            //DeleteDrawing
            setupDeleteDrawing(rootView);

            //UndoDrawing
            setupUndoDrawing(rootView);

            //Save
            setupSave(rootView);

            //Next
            setupNext(rootView);

            setPrompt(rootView);

            return rootView;
        }



        protected void setupDrawView(View rootView){
            drawView = (DrawingView) rootView.findViewById(R.id.drawing);
            drawView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        undoDrawing.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                        deleteDrawing.setVisibility(View.VISIBLE);
                        next.setVisibility(View.VISIBLE);

                    }
                    return false;
                }
            });
        }
        protected void setupPaintSelect(View rootView){
            //layout containingg paint selectors
            LinearLayout paintLayout = (LinearLayout)rootView.findViewById(R.id.paint_colors);
            paintPressed = getResources().getDrawable(R.drawable.paintpressed);
            paint = getResources().getDrawable(R.drawable.paint);
            currPaint = (ImageView) paintLayout.getChildAt(0);
            if (currPaint != null) {
                currPaint.setImageDrawable(paintPressed);
            }


            //add onclick listeners to each paint view
            for(int i = 0; i < paintLayout.getChildCount(); i++){
                ImageView paintView =(ImageView) paintLayout.getChildAt(i);
                if (paintView != null) {
                    paintView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(v != currPaint){
                                ImageView imgView = (ImageView)v;
                                String color = v.getTag().toString();
                                drawView.setColor(color);
                                imgView.setImageDrawable(paintPressed);
                                currPaint.setImageDrawable(paint);
                                currPaint=(ImageView)v;
                            }

                        }
                    });
                }
            }
        }

        protected void setupDeleteDrawing(View rootView){
            deleteDrawing = (ImageView) rootView.findViewById(R.id.deleteDrawing);
            deleteDrawing.setVisibility(View.GONE);
            deleteDrawing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.deleteDrawing();
                    undoDrawing.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    deleteDrawing.setVisibility(View.GONE);
                    next.setVisibility(View.INVISIBLE);
                }
            });
        }

    protected void setupUndoDrawing(View rootView){
        undoDrawing = (ImageView) rootView.findViewById(R.id.undoDrawing);
        undoDrawing.setVisibility(View.GONE);
        undoDrawing.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                drawView.undo();

                if(drawView.isEmpty()){
                    undoDrawing.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                    deleteDrawing.setVisibility(View.GONE);
                    next.setVisibility(View.INVISIBLE);
                }


            }
        });
    }

    protected void setupSave(final View rootView){
        save = (ImageView) rootView.findViewById(R.id.save);
        save.setVisibility(View.GONE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                drawView.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        rootView.getContext().getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(rootView.getContext().getApplicationContext(),
                            R.string.save, Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(rootView.getContext().getApplicationContext(),
                            R.string.unsaved, Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                drawView.destroyDrawingCache();
            }
        });
    }

    protected void setupNext(View rootView){
        next = (ImageView) rootView.findViewById(R.id.nextSketch);

        next.setVisibility(View.INVISIBLE);
        next.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){

                    case MotionEvent.ACTION_DOWN:
                        next.setImageDrawable(getResources().getDrawable(R.drawable.nextpressedgreen));
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
                drawView.setDrawingCacheEnabled(true);
                ((GameActivity)getActivity()).addSketch(saveToStorage(drawView.getDrawingCache()));
                drawView.destroyDrawingCache();
                ((GameActivity)getActivity()).nextFragment(v);
            }
        });
    }

    public void setPrompt(View rootView){

        promptText = (TextView) rootView.findViewById(R.id.sketchPrompt);
        Bundle bundle = getArguments();
        String prompt = bundle.getString(GameActivity.PROMPT_KEY);

        if(prompt != null)
        promptText.setText(prompt);

    }

    private String saveToStorage(Bitmap b){
        GameActivity activity = (GameActivity)getActivity();

        String filename = "SO_" + activity.roundNumber;

        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        // path to /data/data/yourapp/app_sketchDir/
        File directory = cw.getDir("sketchDir", Context.MODE_PRIVATE);
        directory = new File (directory + "/" + activity.timeStamp);
        directory.mkdirs();
        // Create imageDir
        File path=new File(directory,filename + ".png");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(path);

            // Use the compress method on the BitMap object to write image to the OutputStream
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return path.getAbsolutePath();
    }
}