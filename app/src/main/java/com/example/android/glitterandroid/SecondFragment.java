package com.example.android.glitterandroid;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class SecondFragment extends Fragment {
    private static final String TAG = "SecondFragment";

    ImageButton newCardButton;
    View mainView;

    protected ArrayList<Card> mCards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCards = CardLab.get(getActivity()).getCards();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.second_frag, container, false);


        updateCards();


        newCardButton = (ImageButton)mainView.findViewById(R.id.button_newCard);
        newCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                CardBakeShop frag = CardBakeShop.newInstance(null);
                //Implement the bakeshop fragment here
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_bottom_bakeshop, R.anim.exit_to_bottom)
                        .replace(R.id.layout_secondFragment, frag, "CardBakeShop")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return mainView;
    }



    // Meant to iterate through mCards array and render each card as cardView
    // and add it to cardView's relativeLayout surface
    protected void updateCards() {
        RelativeLayout rl = (RelativeLayout)mainView.findViewById(R.id.secondFrag_cardContainer);
        View prevCard = null;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
                .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        for (Card card:mCards) {

            for (GiphyItem gif: card.getGifIngredients()){
                gif.setGif(new GifImageView(getActivity()));
                new FetchGifTask(gif).execute();
            }
            GlitterCardView cardView = GlitterCardView.initWithCard(getActivity(),card);
            rl.addView(cardView);
            cardView.setScaleX(.4f);
            cardView.setScaleY(.4f);
            ((ViewGroup)cardView.getParent()).removeView(cardView);
            if (prevCard != null) params.addRule(RelativeLayout.RIGHT_OF, prevCard.getId());
            rl.addView(cardView);
            //cardView.setOnTouchListener(new GlitterMultiTouchListener(mainView));
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getActivity(),R.string.card_deleted,Toast.LENGTH_SHORT).show();
                    GlitterCardView cv = (GlitterCardView)v;
                    CardLab.get(getActivity()).getCards().remove(cv.getCard());
                    ((ViewGroup) cv.getParent()).removeView(cv);
                    return true;
                }
            });
            prevCard = cardView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CardLab.get(getActivity()).saveCards();
    }



    public static SecondFragment newInstance() {
        SecondFragment f = new SecondFragment();
        return f;
    }
    private class FetchGifTask extends AsyncTask<Void,Void,GiphyItem> {

        GiphyItem gif;

        private FetchGifTask(GiphyItem item) {
            gif = item;
        }

        @Override
        protected GiphyItem doInBackground(Void... params) {
            Activity activity = getActivity();
            Log.d(TAG, "Started doing in background");
            if (activity == null)
                return new GiphyItem();
            return new GiphyFetcher().searchGifId(gif);

        }

        @Override
        protected void onPostExecute(GiphyItem item) {
            item.getGif().play();
            Log.d(TAG, "Gif location x,y : " + item.getGif().getX() + ", " + item.getGif().getY()
                 + "and it's parent id: " + item.getGif().getParent().toString());
            Log.d(TAG, "Post Execute");

        }
    }

}