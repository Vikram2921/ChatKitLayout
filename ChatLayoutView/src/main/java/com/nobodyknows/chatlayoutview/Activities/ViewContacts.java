package com.nobodyknows.chatlayoutview.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nobodyknows.chatlayoutview.R;

public class ViewContacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        getSupportActionBar().hide();
    }
}