package com.murnerapps.sketchedout.sketchedout;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link RecapFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecapFragment extends Fragment {



    private ArrayList<String> guesses;
    private ArrayList<String> sketches;

    //private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecapFragment.
     */

    public static RecapFragment newInstance(ArrayList<String> param1, ArrayList<String> param2) {
        RecapFragment fragment = new RecapFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(GameActivity.PROMPT_KEY, param1);
        args.putStringArrayList(GameActivity.SKETCH_KEY, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public RecapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            guesses = getArguments().getStringArrayList(GameActivity.PROMPT_KEY);
            sketches = getArguments().getStringArrayList(GameActivity.SKETCH_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recap, container, false);

        setViews(rootView);

        return rootView;
    }
    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
*/
    private void setViews(View rootView){
        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.recap_layout);
        for(int i = 0; i < layout.getChildCount() && i <= GameActivity.MAX_ROUNDS; i++){
            View child = layout.getChildAt(i);
            if(child.getTag().toString().equals("guess")){
                ((TextView) child).setText(guesses.get(i / 2));
                child.setVisibility(View.VISIBLE);
            }
            else if (child.getTag().toString().equals("sketch")){

                try {
                    String filename = sketches.get(i / 2);
                    File f=new File(filename);
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

                    ((ImageView)child).setImageBitmap(b);
                    child.setVisibility(View.VISIBLE);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        }
    }


}
