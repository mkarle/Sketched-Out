package com.murnerapps.sketchedout.sketchedout;

import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by Mark on 6/4/2014.
 */
public class RandomPrompt {

    public RandomPrompt(){
    }

    public static String getPrompt(Resources rs){
        InputStream is = rs.openRawResource(R.raw.wordlist);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String prompt = "";
        int max= 0;
        int randLine = 0;
        try {
             max = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random random = new Random();
        randLine = random.nextInt(max);
        try {
            for(int i = 0; (prompt = br.readLine()) != null && i < randLine; i++){
                //purposefully empty body
                //prompt will be the randomly chosen line at the end of loop
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prompt;
    }
}
