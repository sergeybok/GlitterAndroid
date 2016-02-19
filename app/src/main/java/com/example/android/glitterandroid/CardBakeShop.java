package com.example.android.glitterandroid;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.UUID;


/*
 * Fragment for the bake shop of a card. Goes on top of 'SecondFragment'
 */


public class CardBakeShop extends Fragment {
    private static final String TAG = "CardBakeShop";
    private static final String EXTRA_CARD_ID = "ExtraCardId";
    public static final int cardViewHeight = 1500;
    public static final int cardViewWidth = 900;
    private final boolean DEBUG = false;


    private Typeface font;

    private Card mCard;
    private View lastView;
    private CardViewFragment cardViewFragment;
    private Fragment backgroundIngredientFragment;
    private GlitterCardView cardView;
    private static CardLab sCardLab;
    private GiphyItem mLastGifItem;



    protected Button cancelButton;
    protected Button confirmButton;
    protected Fragment self = this;



    // Static method creates new instance of cardbakeshop with card id
    public static CardBakeShop newInstance(UUID uuid) {
        CardBakeShop fragment = new CardBakeShop();
        Bundle b = new Bundle();
        b.putSerializable(EXTRA_CARD_ID, uuid);
        fragment.setArguments(b);

        return fragment;
    }
    public Card getCard(UUID id) {
        if (id == null) {
            return new Card();
        }
        return CardLab.get(getActivity()).getCard(id);
    }

    @Override
    public void onPause() {
        super.onDestroy();
        GifImageView.stopAll();
    }

    @Override
    public void onResume() {
        super.onResume();
        for (GiphyItem gif: mCard.gifIngredients) {
            if (gif.getGif() != null) gif.getGif().play();
        }
    }

    protected ArrayList<GiphyItem> getGifIngredients(Card card) {
        if (card.getGifIngredients() == null)
            return new ArrayList<>();
        return card.getGifIngredients();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_bake_scroll, container, false);

        font = Typeface.createFromAsset(getActivity()
                .getAssets(), "fontawesome.ttf");

        //initialize card
        mCard = getCard((UUID) getArguments().getSerializable(EXTRA_CARD_ID));


        // initialize cardview on bottom half of screen
        cardViewFragment = new CardViewFragment();
        backgroundIngredientFragment = new BackgroundIngredientFragment();
        final FrameLayout cardContainer = (FrameLayout)v.findViewById(R.id.card_container);
        sCardLab = CardLab.get(getActivity());
        cardView = sCardLab.makeCardView(getActivity(), mCard);

        cardContainer.addView(cardView);


        // set all gifs to play
        for (GiphyItem gif: mCard.getGifIngredients()) {
            if (gif.getGif() != null) gif.getGif().play();
        }

        // touching the card puts it in focus, replaces cardbakeshop fragment
        cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( ((ViewGroup)cardView.getParent()).getId() != R.id.card_container )
                    return false;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG,"cardView onTouch called");
                    //Call fragment to display CardView
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_bottom_cardview, R.anim.exit_to_bottom)
                            .replace(R.id.bake_shop_view, cardViewFragment)
                            .addToBackStack("CardViewFragment")
                            .commit();

                    return false;
                }
                return true;
            }
        });

        //turn off the side to side swiping after starting the bakeshop
        ((MainActivity) getActivity()).setEnableViewPager(false);

        //Turn off all clickability of the second fragment that's behind
        getActivity()
                .findViewById(R.id.layout_secondFragment)
                .setClickable(false);
        getActivity().findViewById(R.id.button_newCard).setClickable(false);


        //Make all icons draggable
        LinearLayout iconContainer =
                (LinearLayout)v.findViewById(R.id.container_of_icons);
        for (int i=0;i<iconContainer.getChildCount();i++) {
            for (int j = 0; j< ((LinearLayout)iconContainer.getChildAt(i)).getChildCount(); j++) {

                (((LinearLayout)(((LinearLayout)iconContainer
                        .getChildAt(i))
                        .getChildAt(j)))
                        .getChildAt(0))
                        .setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (cardContainer.getChildCount() < 1)
                                    return true;
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    ClipData data = ClipData.newPlainText("", "");
                                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                                    v.startDrag(data, shadowBuilder, v, 0);
                                    v.setVisibility(View.INVISIBLE);

                                    return true;
                                } else return false;
                            }
                        });
            }
        }

        //Set up a null catcher of dragged icons that doesn't start an activity
        iconContainer.setOnDragListener(new NullDragListener());

        //Set up the icon drop catcher to recognize icons and start proper activities
        // FrameLayout iconCatcher = (FrameLayout)v.findViewById(R.id.iconCatcher);
        cardView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    lastView = (View)event.getLocalState();

                    if (lastView.getId() == R.id.icon_background) {

                        getFragmentManager().beginTransaction()
                                .replace(R.id.bake_shop_view, backgroundIngredientFragment)
                                .addToBackStack("CardViewFragment")
                                .commit();
                        lastView.setVisibility(View.VISIBLE);
                        return false;
                    }
                    Intent i = new Intent(getActivity(),AddIngredientActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("ViewID", lastView.getId());
                    i.putExtras(b);
                    startActivityForResult(i, 0);
                    lastView.setVisibility(View.VISIBLE);
                    lastView.setX(48.0f);
                    lastView.setY(48.0f);
                    if (DEBUG) {
                        Log.d(TAG, "Should be VISIBLE: ID: "+ lastView.getId()
                                + "\n here is X: " + lastView.getX()
                                + "and here is Y: " + lastView.getY());
                    }
                    return false;
                }
                return true;
            }
        });

        confirmButton = (Button)v.findViewById(R.id.bake_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save card below
                (CardLab.get(getActivity()).getCards()).add(mCard);
                CardLab.get(getActivity()).saveCards();
                ((ViewGroup)cardView.getParent()).removeView(cardView);
                cardView.setScaleX(.4f);
                cardView.setScaleY(.4f);
                ((RelativeLayout)container.findViewById(R.id.secondFrag_cardContainer))
                        .addView(cardView);

                //remove self
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction()
                        .remove(self)
                        .commit();
            }
        });

        cancelButton = (Button)v.findViewById(R.id.bake_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Remove self without saving
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction()
                        .remove(self)
                        .commit();
            }
        });


        //Set background into transparent dark grey
        v.setBackgroundColor(0x984a4a5f);

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GifImageView.stopAll();
        ((MainActivity)getActivity()).setEnableViewPager(true);
        getActivity()
                .findViewById(R.id.layout_secondFragment)
                .setClickable(true);
        getActivity().findViewById(R.id.button_newCard).setClickable(true);
        getFragmentManager().beginTransaction()
                .remove(cardViewFragment).commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK) {
            lastView.setVisibility(View.VISIBLE);
            return;
        }
        if ((data.getBooleanExtra(AddIngredientActivity.EXTRA_UNIQUE,false))) {
            lastView.setVisibility(View.INVISIBLE);
        }
        if (data.getBooleanExtra(AddIngredientActivity.IS_GIF,false)) {
            mLastGifItem = new GiphyItem();
            mLastGifItem.setId(data.getStringExtra(AddIngredientActivity.GIF_ID));
            mLastGifItem.setGif(new GifImageView(getActivity()));
            new FetchGifTask().execute();
            return;
        }


        String addedText = data.getStringExtra(AddIngredientActivity.EXTRA_TEXT);
        if (data.getBooleanExtra(AddIngredientActivity.IS_PHONE, false)) {
            // Only because of this formatting is this app min API 21
            // Otherwise could've been 17 (if needs to be backward compatible)
            addedText = PhoneNumberUtils.formatNumber(addedText,"US");
            addedText = getString(R.string.phone_number) + " " + addedText;
        }
        TextItem text = new TextItem();
        GlitterCustomTextView textView = new GlitterCustomTextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(23);
        textView.setPaddingRelative(4, 4, 4, 4);
        textView.setText(addedText);
        textView.setTypeface(font);
        textView.setTextColor(0xff551a8b);
        textView.setGravity(Gravity.CENTER);
        ((RelativeLayout)cardView.getChildAt(0))
                .addView(textView);
        text.setTextView(textView);
        mCard.textIngredients.add(text);
    }


    // Makes the dragged icon snap back into place
    private class NullDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                View view = (View)event.getLocalState();
                view.setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.icon_gif).setVisibility(View.VISIBLE);
            }
            return true;
        }
    }


    // Fetches a gif from GIPHY based on mLastGifItem, adds it to the card and starts it
    private class FetchGifTask extends AsyncTask<Void,Void,GiphyItem> {
        @Override
        protected GiphyItem doInBackground(Void... params) {
            Activity activity = getActivity();
            Log.d(TAG, "Started doing in background");
            if (activity == null)
                return new GiphyItem();
            return new GiphyFetcher().searchGifId(mLastGifItem);

        }

        @Override
        protected void onPostExecute(GiphyItem item) {

            Log.d(TAG, "Post Execute");
            mCard.gifIngredients.add(item);
            ((RelativeLayout)cardView.getChildAt(0)).addView(item.getGif());
            item.getGif().play();


        }
    }
}