package com.example.android.glitterandroid;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;



public class BackgroundIngredientFragment extends Fragment {
    private static final String TAG = "BackgroundIngredientFragment";

    private Fragment self = this;

    GlitterCardView cardView;
    //HexGridView hexVView;
    ViewGroup container;
    ViewGroup view;
    ImageView currentBackground;
    int previousBackground = 0;
    Button confirmButton;
    Button cancelButton;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.background_add_fragment,container,false);
        view = (ViewGroup) v;
        this.container = container;
        cardView = (GlitterCardView)((ViewGroup)container
                .findViewById(R.id.card_container))
                .getChildAt(0);
        ColorDrawable cd = ((ColorDrawable)cardView.getChildAt(0)
                .getBackground());
        if (cd != null)
            previousBackground = cd.getColor();

        ((ViewGroup)cardView.getParent()).removeView(cardView);
        cardView.setScaleX(.5f);
        cardView.setScaleY(.8f);
        ((ViewGroup)v.findViewById(R.id.background_cardviewContainer)).addView(cardView);

        //Set up color detector
        ViewGroup ll = (ViewGroup)v.findViewById(R.id.hexView_container);
        for (int i = 0; i < ll.getChildCount();i++) {
            for (int j=0; j< ((ViewGroup)ll.getChildAt(i)).getChildCount();j++) {
                ImageView iv = (ImageView)((ViewGroup)ll.getChildAt(i)).getChildAt(j);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView imageView = (ImageView)v;
                        ColorDrawable cd = (ColorDrawable)imageView.getBackground();

                        // Important to change the background not of the CardView itself
                        // but of the relativeLayout that holds all of cardView's children
                        cardView.getChildAt(0).setBackgroundColor(cd.getColor());
                        imageView.setImageResource(R.drawable.checkmark);
                        if (currentBackground != null)
                            currentBackground.setImageDrawable(null);
                        currentBackground = imageView;
                        (cardView.getCard()).setBackgroundIngredient(
                                new BackgroundIngredient(cd.getColor()));
                    }
                });
            }
        }

        confirmButton = (Button)v.findViewById(R.id.backgroundFrag_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().remove(self).commit();
                //call save card method that's to come
            }
        });

        cancelButton = (Button)v.findViewById(R.id.backgroundFrag_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousBackground != 0)
                    cardView.getChildAt(0).setBackgroundColor(getResources()
                            .getColor(R.color.WHITE));
                else
                    cardView.getChildAt(0).setBackgroundColor(previousBackground);
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction().remove(self).commit();
            }
        });



        return v;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)cardView.getParent()).removeView(cardView);
        cardView.setScaleX(1.0f);
        cardView.setScaleY(1.0f);
        ((ViewGroup)container.findViewById(R.id.card_container)).addView(cardView);
    }
}
