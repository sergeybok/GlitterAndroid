package com.example.android.glitterandroid;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

// USE THIS EXAMPLE http://api.giphy.com/v1/gifs/search?q=cute+puppy&api_key=dc6zaTOxFJmzC&limit=5&offset=1

/*
 * Created by Sergey based on Flickr Fetcher from Android Big Nerd Ranch programming book
 * Class utilizes Giphy API to download gifs from there
 */

public class GiphyFetcher {
    public static final String TAG = "GiphyFetcher";

    public static final String PREF_SEARCH_QUERY = "searchQuery";
    public static final String PREF_LAST_RESULT_ID = "lastResultId";

    private static final String ENDPOINT = "http://api.giphy.com/v1/gifs/";
    private static final String STICKER_ENDPOINT = "http://api.giphy.com/v1/stickers/";
    private static final String PARAM_API = "api_key=";
    private static final String API_KEY = "dc6zaTOxFJmzC";
    private static final String STICKER_API_KEY = "dc6zaTOxFJmzC";
    private static final String PARAM_SEARCH = "search?q=";
    private static final String PARAM_LIMIT = "&limit=";
    private static final String PARAM_OFFSET = "&offset=";

    private static final String PARAM_TEXT = "text";

    private static final String GIF_URL = "bitly_gif_url";



    public GiphyItem searchGifId(GiphyItem item) {
        String url = ENDPOINT + item.getId() + "?" + PARAM_API + API_KEY;

        Log.d(TAG, "url: " + url);
        return downloadGifItem(url,item);
    }

    public ArrayList<GiphyItem> searchGifs(String query, ArrayList<GifImageView> gifs) {
        int limit = gifs.size();
        String offset;
        offset="0";

        if (query.startsWith(" "))
            query = query.substring(1);
        if (query.endsWith(" "))
            query = query.substring(0,query.length()-1);
        query = query.toLowerCase();
        query = query.replace(' ','+');

        String url = ENDPOINT + PARAM_SEARCH + query + "&" + PARAM_API + API_KEY
                + PARAM_LIMIT + limit + PARAM_OFFSET + offset;

        return downloadGalleryItems(url, gifs);
    }

    public ArrayList<GiphyItem> searchSticker(String query, ArrayList<GifImageView> gifs) {
        int limit = gifs.size();
        String offset = "0";

        if (query.startsWith(" "))
            query = query.substring(1);
        if (query.endsWith(" "))
            query = query.substring(0,query.length()-1);
        query = query.toLowerCase();
        query = query.replace(' ','+');
        String url = STICKER_ENDPOINT + PARAM_SEARCH + query + "&" + PARAM_API +
                STICKER_API_KEY + PARAM_LIMIT + limit + PARAM_OFFSET + offset;

        return downloadGalleryItems(url, gifs);
    }

    public ArrayList<GiphyItem> downloadGalleryItems(String url, ArrayList<GifImageView> gifs) {
        ArrayList<GiphyItem> items = new ArrayList<>();

        try {
            URL u = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            u.openStream()));

            StringBuilder inputLine = new StringBuilder();
            String s = "";
            while ((s = in.readLine()) != null) {
                System.out.println(inputLine);
                inputLine.append(s);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(inputLine.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            parseGifItems(items,jsonArray,gifs);

        } catch (IOException e) {
            Log.e(TAG,"Can't get JSON from Giphy", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing exception", e);
        }

        return items;
    }

    public GiphyItem downloadGifItem(String url, GiphyItem item) {
        try {
            URL u = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            u.openStream()));

            StringBuilder inputLine = new StringBuilder();
            String s = "";
            while ((s = in.readLine()) != null) {
                System.out.println(inputLine);
                inputLine.append(s);
            }
            in.close();

            JSONObject jsonObject = new JSONObject(inputLine.toString());
            JSONObject json = (jsonObject.getJSONObject("data"));
            parseGif(item,json);

        } catch (IOException e) {
            Log.e(TAG,"Can't get JSON from Giphy", e);
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing exception", e);
        }

        return item;
    }

    void parseGif(GiphyItem item, JSONObject jsonObject) throws JSONException,IOException{
        String gifurl = ((jsonObject.getJSONObject("images"))
                .getJSONObject("fixed_width")).getString("url");
        item.setUrl(gifurl);
        Log.d(TAG, "Gif URL: " + gifurl);
        File file = new File(MainActivity.mCacheDir,item.getId()+".gif");
        downloadFile(item.getUrl(), file);
        item.getGif().setFile(file);

    }

    void parseGifItems(ArrayList<GiphyItem> items, JSONArray jsonArray, ArrayList<GifImageView> gifViews)
            throws JSONException, IOException {
        JSONObject jsonObject;

        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            GiphyItem item = new GiphyItem();
            item.setId(jsonObject.getString("id"));
            item.setUrl(((jsonObject.getJSONObject("images"))
                    .getJSONObject("fixed_width")).getString("url"));
            //Gif assignment
            File file = new File(GiphyGalleryFragment.mCacheDir,item.getId() + ".gif");

            downloadFile(item.getUrl(), file);
            GifImageView gifView = gifViews.get(i);
            gifView.setFile(file);
            item.setGif(gifView);

            items.add(item);
        }
    }


    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            Log.e(TAG,"File not found",e);
        } catch (IOException e) {
            Log.e(TAG,"I/O error",e);
        }
    }

}
