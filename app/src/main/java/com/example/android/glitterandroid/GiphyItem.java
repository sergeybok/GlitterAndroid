package com.example.android.glitterandroid;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sergey on 8/12/15.
 * Wrapper class for GifImageView class that displays gifs
 * Used for saving/loading purposes
 */

public class GiphyItem {
    //Constants for JSON conversion
    public static final String JSON_ID = "gifId";
    public static final String JSON_X = "gifX";
    public static final String JSON_Y = "gifY";
    public static final String JSON_WIDTH = "gifWidth";
    public static final String JSON_HEIGHT = "gifHeight";
    public static final String JSON_ROTATION = "gifRotation";
    //maybe others?

    private GifImageView imageView;
    private String mId;
    private String mUrl;
    private float mX;
    private float mY;
    private int mWidth;
    private int mHeight;
    private float mRotation;

    public GiphyItem() {
        super();
    }

    public GiphyItem(JSONObject json) throws JSONException{
        super();
        updateFields();
        mId = json.getString(JSON_ID);
        mX = (float) json.getDouble(JSON_X);
        mY = (float) json.getDouble(JSON_Y);
        mWidth = json.getInt(JSON_WIDTH);
        mHeight = json.getInt(JSON_HEIGHT);
        mRotation = (float) json.getDouble(JSON_ROTATION);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_X, (double) mX);
        json.put(JSON_Y,(double) mY);
        json.put(JSON_WIDTH, mWidth);
        json.put(JSON_HEIGHT, mHeight);
        json.put(JSON_ROTATION, (double) mRotation);
        return json;
    }


    public GifImageView getGif() {
        return imageView;
    }
    public void setGif(GifImageView gif) {
        imageView = gif;
    }
    public String getId() {
        return mId;
    }
    public void setId(String l) {
        mId = l;
    }
    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String s) {
        mUrl = s;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public float getmY() {
        return mY;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public float getmRotation() {
        return mRotation;
    }

    public void setmRotation(float mRotation) {
        this.mRotation = mRotation;
    }

    public void updateFields() {
        if (imageView == null) return;
        mX = imageView.getX();
        mY = imageView.getY();
        mWidth = imageView.getWidth();
        mHeight = imageView.getHeight();
        mRotation = imageView.getRotation();
    }


}
