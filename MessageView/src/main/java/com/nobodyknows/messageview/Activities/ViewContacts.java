package com.nobodyknows.messageview.Activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.nobodyknows.commonhelper.Model.ContactParceable;
import com.nobodyknows.messageview.Adapters.ContactListViewAdapter;
import com.nobodyknows.messageview.R;

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