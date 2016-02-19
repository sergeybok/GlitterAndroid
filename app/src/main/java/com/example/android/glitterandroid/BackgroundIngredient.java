package com.example.android.glitterandroid;

import android.graphics.Color;
import android.net.Uri;

/**
 * Created by sergey on 7/23/15.
 */
public class BackgroundIngredient {

    protected Color color;
    protected int clr;
    protected String url;
    protected Uri uri;

    public BackgroundIngredient() {
        super();
    }
    public BackgroundIngredient(Color clr) {
        color = clr;
    }
    public BackgroundIngredient(int color) {
        clr = color;
    }

    protected Color getColor() {
        return color;
    }
    protected void setColor(Color c) {
        color = c;
    }
    protected int getClr() {
        return clr;
    }
    protected void setClr(int c) {
        clr = c;
    }
    protected String getUrl() {
        return url;
    }
    protected void setUrl(String s) {
        url = s;
    }
    protected Uri getUri() {
        return uri;
    }
    protected void setUri(Uri u) {
        uri = u;
    }

}
