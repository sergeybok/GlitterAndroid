package com.example.android.glitterandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


/*
 * Not implemented but was meant to emulate the hexagonal grid when
  * choosing background ingredient of Glitter for iPhone
 * Needs more work on drawing the hexagons and making them change cardView background upon touch
 */

public class HexGridView extends View {

    private final double mSin60 = 0.86602540378;
    private final int mNumRows = 2; //Idk how many needed
    private final int mNumHex = 10; // Ditto ^
    private final int mMarginX = 16;
    private final int mMarginY = 16;

    private float mSquareSide;
    private float mHexSide;
    private float mMiniSquareSide;
    private Path[][] mHexGrid;
    private Paint[] mPaintGrid;
    private Paint mBackground;


    public HexGridView(Context context) {
        super(context);
        init();
    }
    public HexGridView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();

    }
    public HexGridView(Context c, AttributeSet attrs, int defStyleAttr) {
        super(c,attrs,defStyleAttr);
        init();
    }



    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPaint(mBackground);
        int k=0;
        for (int i = 0; i< mNumRows; i++) {
            for (int j =0; j< mNumHex/mNumHex; j++) {
                canvas.drawPaint(mPaintGrid[k]);
                canvas.drawPath(mHexGrid[i][j],mPaintGrid[k]);
                k++;
            }
        }
        super.onDraw(canvas);
    }

    private void init() {

        mSquareSide = (this.getHeight()-(2*mMarginY))/mNumRows;
        mHexSide = (float) (mSquareSide/(2*mSin60));
        mMiniSquareSide = (mSquareSide - mHexSide)/2;

        mHexGrid = new Path[mNumRows][mNumHex/mNumRows];
        mPaintGrid = new Paint[mNumHex];
        mBackground = new Paint();
        mBackground.setColor(0xff888888);
        initPaintGrid();

        float startX = mMiniSquareSide + mMarginX;
        float startY = mMarginY;
        int i, j;
        for(i = 0; i<mNumRows; i++) {
            for (j = 0; j < mNumHex/mNumRows; j++) {
                mHexGrid[i][j] = drawHex(startX,startY);
                if (j%2 == 0) {
                    startX = (startX + mHexSide) + mMiniSquareSide;
                    startY = startY + mSquareSide/2;
                } else {
                    startX = (startX + mHexSide)+ mMiniSquareSide;
                    startY = startY - mSquareSide/2;
                }
            }
            startX = mMiniSquareSide + mMarginX;
            if (j%2 == 1 ) {
                startY = startY + mSquareSide;
            } else {
                startY = startY + mSquareSide/2;
            }
        }
    }

    public void initPaintGrid() {
        mPaintGrid = new Paint[mNumHex];
        int j,k,l;
        j = 0;
        k = 0;
        l = 0;
        for (int i =0; i < mNumHex;i++) {
            mPaintGrid[i] = new Paint();
            mPaintGrid[i].setColor(Color.argb(255,j,k,l));
            mPaintGrid[i].setStrokeWidth(6);
            if (i%3 == 0) {
                j = j + 255/4;
            }
            else if (i%3 == 1) {
                k = k + 255/3;
            }
            else {
                l = l + 255/3;
            }
        }
    }

    public Path drawHex(float startX, float startY) {
        float x = startX;
        float y = startY;
        Path path = new Path();
        path.moveTo(x,y);
        x = x + mHexSide;
        path.lineTo(x, y);

        x = x + mMiniSquareSide;
        y = y + mSquareSide/2;
        path.lineTo(x,y);

        x = x - mMiniSquareSide;
        y = y + mSquareSide/2;
        path.lineTo(x,y);

        x = x - mHexSide;
        path.lineTo(x,y);

        x = x - mMiniSquareSide;
        y = y - mSquareSide/2;
        path.lineTo(x,y);

        x = x + mMiniSquareSide;
        y = y - mSquareSide/2;
        path.lineTo(x,y);
        path.close();

        return path;
    }
}
