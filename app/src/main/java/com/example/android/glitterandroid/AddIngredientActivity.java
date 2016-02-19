package com.example.android.glitterandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;


public class AddIngredientActivity extends FragmentActivity {
    private static final String TAG = "AddIngredientActivity";
    public static final String EXTRA_UNIQUE = "unique";
    public static final String EXTRA_TEXT = "textField";
    public static final String GIF_ID = "gifId";
    public static final String IS_GIF = "isGif";
    public static final String IS_PHONE = "isPhone";


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        Bundle b = getIntent().getExtras();


        if (fragment == null) {
            fragment = getFragment(b.getInt("ViewID"));
        }
        fm.beginTransaction()
                .add(R.id.fragmentContainer,fragment)
                .commit();
    }

    private Fragment getFragment(int viewID) {

        switch (viewID){
            case R.id.icon_photo:
                //Call camera activity, needs implementation

                break;
            case R.id.icon_gif:
                //call gif activity
                Log.d(TAG,"Giphy Activity");
                return new GiphyGalleryFragment();
            default:
                Log.d(TAG, "Glitter drag listener default switch");
                return TextAddFragment.newInstance(viewID);
        }
        return new TextAddFragment();
    }



}
