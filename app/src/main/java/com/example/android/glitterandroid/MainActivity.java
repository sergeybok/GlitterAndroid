package com.example.android.glitterandroid;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int numPages = 3;

    FirstFragment frag1;
    SecondFragment frag2;
    ThirdFragment frag3;

    Bitmap backgroundSrc;

    Fragment currentFragment;

    GlitterViewPager vp;
    public static File mCacheDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCacheDir = GifUtils.getDir(GifUtils.getCacheDir(this), "gif-cache");

        vp = (GlitterViewPager)findViewById(R.id.viewPager);

        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        //
        backgroundSrc = BitmapFactory.decodeResource(getResources(), R.drawable.collection_scaled);

        vp.setCurrentItem(1);

        //vp.setBackgroundResource(R.drawable.collection_scaled);


        //Implement the parallax background
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                vp.setBackground(transitionBackground(backgroundSrc,positionOffset, position));
            }

            @Override
            public void onPageSelected(int position) {
                vp.setCurrentItem(position);
                vp.setBackground(afterTransition(backgroundSrc,position));
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }




    // Parallax background implemented in the two methods below
    // achieves parallax effect by cropping the picture a bunch of times as you swipe
    public Drawable afterTransition(Bitmap bmp, int current) {
        int start;
        int length;
        int w = bmp.getWidth();
        double height = bmp.getHeight();
        switch (current) {
            case 0:
                start = (int) (0.10*w);
                break;
            case 1:
                start = (w/4);
                break;
            case 2:
                start = (int)(0.40*w);
                break;
            default:
                start = 0;
        }
        length = w/3;
        Bitmap dest = Bitmap.createBitmap(bmp,
                start,
                (int)(0.15*bmp.getHeight()),
                length,
                (int)(height*0.7));
        return new BitmapDrawable(getResources(),dest);
    }
    public Drawable transitionBackground(Bitmap bmp, float pos, int curItem) {
        int w = bmp.getWidth();
        int height = bmp.getHeight();
        float tran = (w*0.15f)*(pos); //Has to equal the percentage
        // by which you are shifting the image over one swipe
        int start ;
        switch (curItem) {
            case 0:
                start = (int) (0.10*w);
                break;
            case 1:
                start = (w/4);
                break;
            case 2:
                start = (int)(0.40*w);
                break;
            default:
                start = 0;
        }
        int length = w/3;
        Bitmap dest = Bitmap.createBitmap(bmp,
                (int)(start + tran),
                (int)(0.15*height),
                length,
                (int)(0.7*height));
        return new BitmapDrawable(getResources(),dest);
    }



    public void setEnableViewPager(boolean bool) {
        vp.setPagingEnabled(bool);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0:
                    if (frag1 == null)
                        frag1 = FirstFragment.newInstance("First Fragment");
                    currentFragment = frag1;
                    return frag1;
                case 1:
                    if (frag2 == null)
                        frag2 = SecondFragment.newInstance();
                    currentFragment = frag2;
                    return frag2;
                case 2:
                    if (frag3 == null)
                        frag3 = ThirdFragment.newInstance("Third Fragment");
                    currentFragment = frag3;
                    return frag3;
                default: return frag2;
            }
        }

        @Override
        public int getCount() {
            return numPages;
        }
    }
}