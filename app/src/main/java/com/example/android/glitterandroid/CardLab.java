package com.example.android.glitterandroid;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;


// Helper class for cards
public class CardLab  {

    private static final String TAG = "CardLab";
    private static final String FILENAME = "gc.json";

    private static CardLab sCardLab;
    private Context mAppContext;

    private ArrayList<Card> mCards;
    private GlitterJSONSerializer mSerializer;

    private CardLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new GlitterJSONSerializer(mAppContext,FILENAME);

        try {
            mCards = mSerializer.loadCards();
        } catch (Exception e) {
            mCards = new ArrayList<>();
            Log.e(TAG, "Error loading crimes", e);
        }
    }

    public static CardLab get(Context c) {
        if (sCardLab == null) {
            sCardLab = new CardLab(c.getApplicationContext());
        }
        return sCardLab;
    }

    public ArrayList<Card> getCards() {
        if (mCards == null) return new ArrayList<>();
        return mCards;
    }


    public Card getCard(UUID uuid) {
        for (Card c:mCards) {
            if (c.getUuid().equals(uuid)) return c;
        }
        return null;
    }

    public void addCard(Card c) {
        mCards.add(c);
    }

    public boolean saveCards() {
        //Need to implement
        try {
            mSerializer.saveCrimes(mCards);
            Log.d(TAG, "Cards saved to file: " + FILENAME);
            return true;
        } catch (Exception e){
            Log.e(TAG, "Error saving crimes: ", e);
        }
        return false;
    }

    public void deleteCard(Card c) {
        mCards.remove(c);
    }

    public int getNumCards() {
        return mCards.size();
    }

    public GlitterCardView makeCardView(Context context,Card card) {

        if (card == null)
            return new GlitterCardView(context);
        else {
            return GlitterCardView.initWithCard(context, card);
        }
    }


}
