package com.nobodyknows.chatlistlayoutview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.nobodyknows.chatlistlayoutview.Adapters.ContactListViewAdapter;
import com.nobodyknows.commonhelper.Model.ContactParceable;

import java.util.ArrayList;

public class ViewContacts extends AppCompatActivity {

    ListView contactListview;
    ContactListViewAdapter contactListViewAdapter;
    ArrayList<ContactParceable> contacts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        getSupportActionBar().hide();
        contacts = getIntent().getParcelableArrayListExtra("contacts");
        contactListview = findViewById(R.id.contactlistview);
        contactListViewAdapter = new ContactListViewAdapter(getApplicationContext(),0,contacts);
        contactListview.setAdapter(contactListViewAdapter);

    }
}