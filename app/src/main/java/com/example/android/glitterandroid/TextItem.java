package com.example.android.glitterandroid;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wrapper class for GlitterCustomTextView
 * Use this for saving and loading saved cards
 */
public class TextItem {
    //Constants for JSON serialization
    public static final String JSON_TEXT = "textIngredient";
    public static final String JSON_X = "textX";
    public static final String JSON_Y = "textY";
    public static final String JSON_WIDTH = "textWidth";
    public static final String JSON_HEIGHT = "textHeight";
    public static final String JSON_ROTATION = "textRotation";
    public static final String JSON_TEXT_SIZE = "textSize";


    String textIngredient;
    float mX;
    float mY;
    int mWidth;
    int mHeight;
    float mRotation;
    float mTextSize;
    GlitterCustomTextView textView;

    public TextItem() {
        super();
    }

    public TextItem(JSONObject json) throws JSONException {
        super();
        textIngredient = json.getString(JSON_TEXT);
        mX = (float) json.getDouble(JSON_X);
        mY = (float) json.getDouble(JSON_Y);
        mWidth = json.getInt(JSON_WIDTH);
        mHeight = json.getInt(JSON_HEIGHT);
        mRotation = (float) json.getDouble(JSON_ROTATION);
        mTextSize = (float) json.getDouble(JSON_TEXT_SIZE);

    }


    public void updateFields() {
        textIngredient = textView.getText().toString();
        mX = textView.getX();
        mY = textView.getY();
        mWidth = textView.getWidth();
        mHeight = textView.getHeight();
        mRotation = textView.getRotation();
        mTextSize = textView.getTextSize();
    }

    public JSONObject toJSON() throws JSONException {
        updateFields();
        JSONObject json = new JSONObject();
        json.put(JSON_TEXT, textIngredient);
        json.put(JSON_X, (double) mX);
        json.put(JSON_Y, (double) mY);
        json.put(JSON_WIDTH, mWidth);
        json.put(JSON_HEIGHT, mHeight);
        json.put(JSON_ROTATION, (double) mRotation);
        json.put(JSON_TEXT_SIZE, (double) mTextSize);
        return json;
    }

    public float getmTextSize() {
        return mTextSize;
    }
    public void setmTextSize(float f) {
        mTextSize = f;
    }

    public String getTextIngredient() {
        return textIngredient;
    }

    public void setTextIngredient(String textIngredient) {
        this.textIngredient = textIngredient;
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

    public GlitterCustomTextView getTextView() {
        return textView;
    }

    public void setTextView(GlitterCustomTextView textView) {
        this.textView = textView;
    }
}
