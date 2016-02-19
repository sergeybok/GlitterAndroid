package com.example.android.glitterandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;


public class GiphyGalleryFragment extends Fragment {
    private static final String TAG = "GiphyGalleryFragment";

    GridView mGridView;
    ArrayList<GiphyItem> mItems;
    EditText searchBar;
    Button gifButton;
    Button stickerButton;
    ImageButton searchButton;

    private boolean isSticker;
    private String mLastQuery;
    public static File mCacheDir;
    private int mNumGifs;
    public ArrayList<GifImageView> mGifImageViews;
    public String mSelectedGif;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    public void updateItems() {
        new FetchItemsTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gridview_layout, container,false);
        mGridView = (GridView)v.findViewById(R.id.gridView);

        mNumGifs = 24;
        mCacheDir = GifUtils.getDir(GifUtils.getCacheDir(getActivity()), "gif-cache");
        mGifImageViews = new ArrayList<>();
        for (int i=0; i< mNumGifs; i++) {
            GifImageView gif = new GifImageView(getActivity());
            GridView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, 260);
            gif.setLayoutParams(params);
            gif.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mGifImageViews.add(gif);
        }

        searchBar = (EditText)v.findViewById(R.id.gifGrid_searchBar);
        //setup a listener here or not

        searchButton = (ImageButton)v.findViewById(R.id.gifGrid_search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set up edittext puller here and call search method
                mLastQuery = searchBar.getText().toString();

                updateItems();
                InputMethodManager imm = (InputMethodManager)(getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE));
                imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);

            }
        });

        gifButton = (Button)v.findViewById(R.id.gifGrid_gif_button);
        gifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSticker = false;
                gifButton.setTextColor(0xffffffff);
                stickerButton.setTextColor(0xff000000);
                gifButton.setBackgroundColor(0xee000000);
                stickerButton.setBackgroundColor(0x00000000);
            }
        });

        stickerButton = (Button)v.findViewById(R.id.gifGrid_sticker_button);
        stickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSticker = true;
                stickerButton.setTextColor(0xffffffff);
                gifButton.setTextColor(0xff000000);
                stickerButton.setBackgroundColor(0xee000000);
                gifButton.setBackgroundColor(0x00000000);
            }
        });

        stickerButton.performClick();


        mGridView.setAdapter(new ImageAdapter(getActivity()));


        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,ArrayList<GiphyItem>> {
        @Override
        protected ArrayList<GiphyItem> doInBackground(Void... params) {
            Activity activity = getActivity();
            Log.d(TAG,"Started doing in background");
            if (activity == null)
                return new ArrayList<>();

            if (isSticker)
                return new GiphyFetcher().searchSticker(mLastQuery,mGifImageViews);
            else
                return new GiphyFetcher().searchGifs(mLastQuery,mGifImageViews);
        }

        @Override
        protected void onPostExecute(ArrayList<GiphyItem> items) {
            mItems = items;
            Log.d(TAG,"Post Execute");

            mGridView.setAdapter(new ImageAdapter(getActivity()));
            //setupAdapter();
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    mSelectedGif = mItems.get(position).getId();
                    Intent i = new Intent();
                    i.putExtra(AddIngredientActivity.IS_GIF, true);
                    i.putExtra(AddIngredientActivity.GIF_ID, mSelectedGif);
                    i.putExtra(AddIngredientActivity.EXTRA_UNIQUE,false);
                    onDestroy();
                    getActivity().setResult(Activity.RESULT_OK,i);
                    getActivity().finish();
                }
            });

            for (int i =0; i< mItems.size();i++) {
                (mItems.get(i)).getGif().play();
            }
        }
    }


    private class ImageAdapter extends BaseAdapter {
        private Context context;

        public ImageAdapter (Context c) {
            context =c;
        }
        public int getCount() {
            return mNumGifs;
        }
        public GiphyItem getItem(int position) {
            return mItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView,ViewGroup parent) {
            return mGifImageViews.get(position);
        }


    }
}
