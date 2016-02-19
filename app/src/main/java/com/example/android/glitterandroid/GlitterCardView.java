package com.example.android.glitterandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/*
 * Created by Sergey
 * The class that is used to visually render the card
 * holds as children views all of the card's ingredients
 */

public class GlitterCardView extends CardView {

    private Card mCard;


    public GlitterCardView(Context context) {
        super(context);
    }
    public GlitterCardView(Context c, AttributeSet attrs) {
        super(c,attrs);
    }
    public GlitterCardView(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c, attrs, defStyleAttr);
    }


    //Initialize cardview with children derived from card's ingredients
    public static GlitterCardView initWithCard(Context c,Card card) {
        GlitterCardView cardView = new GlitterCardView(c);
        ViewGroup.LayoutParams params = new ViewGroup
                .LayoutParams(CardBakeShop.cardViewWidth,CardBakeShop.cardViewHeight);
        cardView.setLayoutParams(params);
        cardView.setRadius(50);
        RelativeLayout relativeLayout = new RelativeLayout(c);
        cardView.addView(relativeLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        cardView.setCard(card);
        ArrayList<GiphyItem> gifs = card.getGifIngredients();
        ArrayList<TextItem> texts = card.getTextIngredients();
        BackgroundIngredient backgroundIngredient = card.getBackgroundIngredient();

        for (GiphyItem gif: gifs) {
            GifImageView imageView = new GifImageView(c);
            imageView.setUrl(gif.getUrl());

            imageView.setX(gif.getmX());
            imageView.setY(gif.getmY());
            imageView.setRotation(gif.getmRotation());
            gif.setGif(imageView);
            relativeLayout.addView(imageView,
                    new ViewGroup.LayoutParams(gif.getmWidth(), gif.getmHeight()));
        }

        for (TextItem item:texts) {
            GlitterCustomTextView tv = new GlitterCustomTextView(c);
            tv.setText(item.getTextIngredient());
            relativeLayout.addView(tv, new ViewGroup.LayoutParams(item.getmWidth(),
                    item.getmHeight()));
            //relativeLayout.addView(tv);

            tv.setX(item.getmX());
            tv.setY(item.getmY());
            tv.setRotation(item.getmRotation());
            tv.setTypeface(Typeface.createFromAsset(c.getAssets(), "fontawesome.ttf"));
            tv.setTextSize(item.getmTextSize());
            item.setTextView(tv);
        }

        relativeLayout.setBackgroundColor(backgroundIngredient.getClr());

        //images and shit

        return cardView;
    }

    @Override
    public void addView(View v) {
        super.addView(v);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onMeasure(int i,int j) {
        super.onMeasure(i, j);
    }



    public void setCard(Card c) {
        this.mCard = c;
    }


    public Card getCard() {
        return mCard;
    }

}
