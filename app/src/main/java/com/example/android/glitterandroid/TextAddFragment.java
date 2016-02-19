package com.example.android.glitterandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*
 * Fragment for adding text ingredients to a cardView
 *
 */

public class TextAddFragment extends Fragment {
    private static final String TAG = "TextAddFragment";


    private EditText editText;
    protected String text;
    protected String header;
    protected String hint;
    protected String iconCode;
    protected int keyboardType;
    protected boolean uniqueField;
    protected boolean isPhone;

    public static TextAddFragment newInstance(int viewId) {
        TextAddFragment fragment = new TextAddFragment();
        Bundle b = new Bundle();
        b.putInt("ViewID",viewId);

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_add_text, container, false);

        uniqueField = true;
        isPhone = false;
        editText = (EditText)v.findViewById(R.id.edit_text);

        setHeaderHint(getArguments().getInt("ViewID"));

        TextView tv = (TextView)v.findViewById(R.id.textAdd_header);
        tv.setText(header);

        editText.setHint(hint);
        editText.setInputType(keyboardType);
        editText.requestFocus();

        //call keyboard
        ((InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Button confirmButton = (Button)v.findViewById(R.id.addText_confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = iconCode + " " + editText.getText().toString();


                Intent i = new Intent();
                i.putExtra(AddIngredientActivity.EXTRA_UNIQUE,uniqueField);
                i.putExtra(AddIngredientActivity.EXTRA_TEXT, text);
                i.putExtra(AddIngredientActivity.IS_GIF, false);
                i.putExtra(AddIngredientActivity.IS_PHONE, isPhone);
                onDestroy();
                getActivity().setResult(Activity.RESULT_OK,i);
                getActivity().finish();
            }
        });

        Button cancelButton = (Button)v.findViewById(R.id.addText_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });


        return v;
    }

    @Override
    public void onDestroy() {
        //Hide keyboard
        InputMethodManager imm = (InputMethodManager)(getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        super.onDestroy();
    }



    public void setHeaderHint(int id) {
        //compare all view ids
        switch (id) {
            case R.id.icon_name:
                //
                header = "Add Your Name";
                hint = "Enter your name here";
                keyboardType = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
                iconCode = "";
                break;
            case R.id.icon_instagram:
                //
                header = "Add Your Instagram";
                hint = "Enter your instagram here";
                keyboardType = InputType.TYPE_CLASS_TEXT;
                iconCode = getResources().getString(R.string.instagram);
                break;
            case R.id.icon_map:
                //
                header = "Add Your Address";
                hint = "Enter your address here";
                keyboardType = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
                iconCode = getResources().getString(R.string.location);
                break;
            case R.id.icon_email:
                //
                header = "Add Your Email";
                hint = "Enter your email here";
                keyboardType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                iconCode = getResources().getString(R.string.email);
                break;
            case R.id.icon_phone:
                //
                header = "Add Your Phone Number";
                hint = "Enter your phone number here";
                keyboardType = InputType.TYPE_CLASS_PHONE;
                isPhone = true;
                iconCode = getResources().getString(R.string.phone_number);
                break;
            case R.id.icon_url:
                //
                header = "Add Your URL";
                hint = "Enter your url here";
                keyboardType = InputType.TYPE_TEXT_VARIATION_URI;
                iconCode = getResources().getString(R.string.url);
                break;
            case R.id.icon_text:
                //
                header = "Add Your Text";
                hint = "Enter your text here";
                keyboardType = InputType.TYPE_CLASS_TEXT;
                iconCode = "";
                uniqueField = false;
                break;
            case R.id.icon_snapchat:
                //
                header = "Add Your Snapchat";
                hint = "Enter your snapchat here";
                keyboardType = InputType.TYPE_CLASS_TEXT;
                iconCode = getResources().getString(R.string.snapchat);
                break;
            case R.id.icon_facebook:
                //
                header = "Add Your Facebook";
                hint = "Enter your Facebook here";
                keyboardType = InputType.TYPE_CLASS_TEXT;
                iconCode = getResources().getString(R.string.facebook);
                break;
            case R.id.icon_twitter:
                //
                header = "Add Your Twitter";
                hint = "Enter your Twitter here";
                keyboardType = InputType.TYPE_CLASS_TEXT;
                iconCode = getResources().getString(R.string.twitter);
                break;
            default:
                Log.d(TAG, "Icon not recognized");
        }
    }
}
