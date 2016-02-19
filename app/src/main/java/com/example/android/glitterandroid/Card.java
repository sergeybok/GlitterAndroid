package com.example.android.glitterandroid;

import android.graphics.Matrix;
import android.graphics.PointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sergey on 7/20/15.
 */
public class Card {

    // Constants for saving/loading JSON objects
    public static final String JSON_ID = "id";
    public static final String JSON_SHORT_ID = "shortId";
    public static final String JSON_USER = "userId";
    //
    public static final String JSON_CREATED_DATE = "dateCreated";
    public static final String JSON_GIF_ITEMS = "gifItems";
    public static final String JSON_TEXT_ITEMS = "textItems";
    public static final String JSON_IMAGE_ITEMS = "imageItems";
    public static final String JSON_BACKGROUND_INGREDIENT = "background";
    // and so on...


    protected UUID uuid;
    protected String shortID;
    protected String userID;
    protected String username;
    protected String avatarURL;

    //CardType cardType;
    protected ArrayList<TextItem> textIngredients;
    protected ArrayList imageIngredients;
    protected ArrayList<GiphyItem> gifIngredients;
    protected BackgroundIngredient backgroundIngredient;
    protected int collects;
    protected int views;
    protected ArrayList likers;
    protected Date createdOn;
    protected Date updatedOn;
    protected Date userViewDate;
    protected boolean hasRemoteUpdates;
    protected boolean hasPositionData;
    protected int index;
    protected PointF miniCardCenter;
    protected Matrix miniCardTransform;



    public Card(){
        uuid = UUID.randomUUID();
        createdOn = new Date();
        textIngredients = new ArrayList<>();
        gifIngredients = new ArrayList<>();
        imageIngredients = new ArrayList();
        backgroundIngredient = new BackgroundIngredient(0xffffffff);
    }

    // Constructors/methods for for saving/loading

    public Card(JSONObject json) throws JSONException {
        //
        uuid = UUID.fromString(json.getString(JSON_ID));
        //shortID = json.getString(JSON_SHORT_ID);
        //userID = json.getString(JSON_USER);
        //createdOn = new Date(json.getLong(JSON_CREATED_DATE));
        JSONArray gifs = (JSONArray)json.get(JSON_GIF_ITEMS);
        gifIngredients = new ArrayList<>();
        for (int i = 0; i<gifs.length();i++) {
            gifIngredients.add( new GiphyItem(gifs.getJSONObject(i)));
        }
        JSONArray texts = (JSONArray)json.get(JSON_TEXT_ITEMS);
        textIngredients = new ArrayList<>();
        for (int i=0; i< texts.length();i++) {
            textIngredients.add(new TextItem(texts.getJSONObject(i)));
        }
        backgroundIngredient = new BackgroundIngredient(json.getInt(JSON_BACKGROUND_INGREDIENT));

        //images and other things
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, uuid.toString());
        //json.put(JSON_SHORT_ID, shortID);
        //json.put(JSON_USER, userID);
        //json.put(JSON_CREATED_DATE, createdOn);

        JSONArray gifs = new JSONArray();
        for (GiphyItem gif: gifIngredients) {
            gifs.put(gif.toJSON());
        }
        json.put(JSON_GIF_ITEMS, gifs);

        JSONArray texts = new JSONArray();
        for (TextItem item: textIngredients) {
            texts.put(item.toJSON());
        }
        json.put(JSON_TEXT_ITEMS, texts);

        json.put(JSON_BACKGROUND_INGREDIENT, backgroundIngredient.getClr());

        // Implement image and other json conversions

        return json;
    }

    //


    protected UUID getUuid() { return uuid; }
    protected void setUuid(UUID id) {
        uuid = id;
    }
    protected String getShortID() {
        return shortID;
    }
    protected void setShortID(String id) {
        shortID = id;
    }
    protected String getUserID() {
        return userID;
    }
    protected void setUserID(String id) {
        userID = id;
    }
    protected String getUsername() {
        return username;
    }
    protected void setUsername(String u) {
        username = u;
    }
    protected String getAvatarURL() {
        return avatarURL;
    }
    protected void setAvatarURL(String url) {
        avatarURL = url;
    }
    protected int getCollects() {
        return collects;
    }
    protected void setCollects(int c) {
        collects = c;
    }
    protected int getViews() {
        return views;
    }
    protected void setViews(int v) {
        views = v;
    }

    //Need to implement these after create proper object classes
    protected ArrayList<TextItem> getTextIngredients() {
        return textIngredients;
    }
    protected void addTextIngredient(TextItem text) {
        textIngredients.add(text);
    }
    protected ArrayList getImageIngredients() {
        return imageIngredients;
    }
    protected void addImageIngredient(Object s) {
        imageIngredients.add(s);
    }
    protected ArrayList<GiphyItem> getGifIngredients() {
        return gifIngredients;
    }
    protected void addGifIngredient(GiphyItem gif) {
        gifIngredients.add(gif);
    }

    protected ArrayList getLikers() {
        return likers;
    }
    protected void addLiker(Object o) {
        likers.add(o);
    }
    protected Date getCreatedOn() {
        return createdOn;
    }
    protected void setCreatedOn(Date d) {
        createdOn = d;
    }
    protected Date getUpdatedOn() {
        return updatedOn;
    }
    protected void setUpdatedOn(Date d) {
        updatedOn = d;
    }
    protected Date getUserViewDate() {
        return userViewDate;
    }
    protected void setUserViewDate(Date d) {
        userViewDate = d;
    }
    protected boolean getHasRemoteUpdates() {
        return hasRemoteUpdates;
    }
    protected void setHasRemoteUpdates(boolean b) {
        hasRemoteUpdates = b;
    }
    protected boolean getHasPositionData() {
        return hasPositionData;
    }
    protected void setHasPositionData(boolean b) {
        hasPositionData = b;
    }
    protected int getIndex() {
        return index;
    }
    protected void setIndex(int i) {
        index = i;
    }
    protected PointF getMiniCardCenter() {
        return miniCardCenter;
    }
    protected void setMiniCardCenter(PointF p) {
        miniCardCenter = p;
    }
    protected Matrix getMiniCardTransform() {
        return miniCardTransform;
    }
    protected void setMiniCardTransform(Matrix m) {
        miniCardTransform = m;
    }
    protected BackgroundIngredient getBackgroundIngredient() {
        return backgroundIngredient;
    }
    protected void setBackgroundIngredient(BackgroundIngredient bi) {
        backgroundIngredient = bi;
    }

}
