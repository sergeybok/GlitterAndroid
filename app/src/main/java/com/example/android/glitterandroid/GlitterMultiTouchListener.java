package com.example.android.glitterandroid;

/*
 * Listener that allows for dragging (via single touch moving)
 * Scaling (via two finger expand/contract motion)
 * and Rotating (via two finger rotation motion)
 */


import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;


public class GlitterMultiTouchListener implements OnTouchListener {


    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOMROTATE = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;

    private View parentView;

    public GlitterMultiTouchListener() {
        super();
    }

    public GlitterMultiTouchListener(View parent) {
        parentView = parent;
    }


    public boolean onTouch(View v, MotionEvent event) {

        // Only invokes methods of listener if the view is within appropriate container
        // Otherwise does nothing upon touching of the view
        if (((ViewGroup)v.getParent().getParent().getParent()).getId() != parentView.getId())
            return false;


        v.bringToFront();

        // handle touch events here
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (event.getPointerCount() ==2) {
                    mode = ZOOMROTATE;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    Log.d("CardViewFragment","DRAG action started");
                    v.setTranslationX((event.getX() - start.x)
                            + v.getTranslationX() - (v.getWidth()/2));
                    v.setTranslationY((event.getY() - start.y)
                            + v.getTranslationY() - (v.getHeight()/2));

                    return true;
                }
                else if (mode == ZOOMROTATE) {
                    Log.d("CardViewFragment","ZOOMROTATE action started");
                    boolean isZoom = false;
                    if(!isRotate(event)){
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            float scale = newDist / oldDist * v.getScaleX();
                            v.setScaleX(scale);
                            v.setScaleY(scale);
                            isZoom = true;
                        }
                    }
                    else if(!isZoom){
                        newRot = rotation(event);
                        v.setRotation((float)(v.getRotation() + (newRot - d)));
                    }

                }
                break;
        }

        return true;
    }

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x*x+y*y);
    }

    private boolean isRotate(MotionEvent event){
        int dx1 = (int) (event.getX(0) - lastEvent[0]);
        int dy1 = (int) (event.getY(0) - lastEvent[2]);
        int dx2 = (int) (event.getX(1) - lastEvent[1]);
        int dy2 = (int) (event.getY(1) - lastEvent[3]);
        Log.d("dx1 ", ""+ dx1);
        Log.d("dx2 ", "" + dx2);
        Log.d("dy1 ", "" + dy1);
        Log.d("dy2 ", "" + dy2);
        //pointer 1
        if(Math.abs(dx1) > Math.abs(dy1) && Math.abs(dx2) > Math.abs(dy2)) {
            if(dx1 >= 2.0 && dx2 <=  -2.0){
                Log.d("first pointer ", "right");
                return true;
            }
            else if(dx1 <= -2.0 && dx2 >= 2.0){
                Log.d("first pointer ", "left");
                return true;
            }
        }
        else {
            if(dy1 >= 2.0 && dy2 <=  -2.0){
                Log.d("seccond pointer ", "top");
                return true;
            }
            else if(dy1 <= -2.0 && dy2 >= 2.0){
                Log.d("second pointer ", "bottom");
                return true;
            }

        }

        return false;
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}
