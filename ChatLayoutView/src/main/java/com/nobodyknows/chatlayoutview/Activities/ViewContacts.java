package com.nobodyknows.chatlayoutview.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.nobodyknows.chatlayoutview.Adapters.ContactListViewAdapter;
import com.nobodyknows.chatlayoutview.Model.Contact;
import com.nobodyknows.chatlayoutview.Model.ContactParceable;
import com.nobodyknows.chatlayoutview.R;

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
        contactListViewAdapter = new ContactListViewAdapter(getApplicationContext(),R.layout.contactsitem,contacts);
        contactListview.setAdapter(contactListViewAdapter);
    }
}