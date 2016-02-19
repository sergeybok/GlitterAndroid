package com.example.android.glitterandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public class CardViewFragment extends Fragment {
    private static final String TAG = "CardViewFragment";

    GlitterCardView cardView;
    ViewGroup parentContainer;
    RelativeLayout frame;
    View thisView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_view_layout,container,false);
        parentContainer = container;


        //Get cardview from CardBakeShop
        frame = (RelativeLayout)v.findViewById(R.id.cardview_container);
        cardView = (GlitterCardView)(((FrameLayout)container
                .findViewById(R.id.card_container))
                .getChildAt(0));
        ((ViewGroup)cardView.getParent()).removeView(cardView);
        frame.addView(cardView);

        setEnableView(parentContainer, false);

        //Turn on Multi-touch listener for all cardview's children
        for (int i = 0; i< ((ViewGroup)cardView.getChildAt(0)).getChildCount(); i++) {
            if (((ViewGroup)cardView.getChildAt(0)).getChildAt(i) instanceof GifImageView)
                ((GifImageView) ((ViewGroup)cardView.getChildAt(0)).getChildAt(i)).play();
            ((ViewGroup) cardView.getChildAt(0)).getChildAt(i)
                    .setOnTouchListener( new GlitterMultiTouchListener(frame));
        }




        //Make relative frame relocate views based on drag & drop
        (cardView.getChildAt(0)).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                if (event.getAction() == DragEvent.ACTION_DROP) {
                    View view = (View) event.getLocalState();

                    view.setX(event.getX() - view.getWidth()/2);
                    view.setY(event.getY() - view.getHeight()/2);
                    view.setVisibility(View.VISIBLE);
                    view.bringToFront();

                    return false;
                }
                return true;
            }
        });


        return v;
    }


    @Override
    public void onDestroyView() {
        //Give cardview back to CardBakeShop
        frame.removeView(cardView);
        ((ViewGroup)parentContainer.findViewById(R.id.card_container))
                .addView(cardView);

        setEnableView(parentContainer, true);

        super.onDestroyView();

    }

    public void setEnableView(View v, boolean b) {
        if (v != thisView) {
            v.setEnabled(b);
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup)v;
                for (int i = 0; i< vg.getChildCount();i++) {
                    setEnableView(vg.getChildAt(i),b);
                }
            }
            if (v instanceof GifImageView) {
                ((GifImageView) v).play();
            }
        }
    }


}
