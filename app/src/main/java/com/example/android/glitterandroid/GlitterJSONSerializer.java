package com.example.android.glitterandroid;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by sergey on 8/20/15.
 * Helper class that serializes cards and their ingredients into JSON format
 */
public class GlitterJSONSerializer {
    private static final String TAG = "GlitterJSONSerializer";
    private static final String CARDS = "cards";
    private Context mContext;
    private String mFilename;

    public GlitterJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Card> cards)
            throws JSONException, IOException {
        //Build array in JSON
        JSONArray array = new JSONArray();
        for (Card card : cards) {
            array.put(card.toJSON());
        }
        JSONObject object = new JSONObject();
        object.put(CARDS, array);

        //write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext
                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            //writer.write(array.toString());
            writer.write(object.toString());
        } catch (IOException e) {
            Log.e(TAG,"Saving error: ", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
     }

    public ArrayList<Card> loadCards() throws IOException, JSONException {
        ArrayList<Card> cards = new ArrayList<>();
        BufferedReader reader = null;
        try {
            // open and read the file into a stringbuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line=reader.readLine()) != null) {
                //Line breaks are omitter and irrelevant
                jsonString.append(line);
            }

            //
            JSONArray array = (new JSONObject(jsonString.toString())).getJSONArray(CARDS);


            // Build the array of cards from JSONObjects
            for (int i=0; i<array.length();i++) {
                cards.add(new Card(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Ignore, happens when starting fresh
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return cards;
    }
}
