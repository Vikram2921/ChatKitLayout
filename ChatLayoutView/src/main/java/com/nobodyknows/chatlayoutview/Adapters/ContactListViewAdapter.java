package com.nobodyknows.chatlayoutview.Adapters;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.ChatMessageView;
import com.nobodyknows.chatlayoutview.Model.Contact;
import com.nobodyknows.chatlayoutview.Model.ContactParceable;
import com.nobodyknows.chatlayoutview.Model.Message;
import com.nobodyknows.chatlayoutview.Model.User;
import com.nobodyknows.chatlayoutview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nobodyknows.chatlayoutview.ChatLayoutView.downloadHelper;

public class ContactListViewAdapter extends ArrayAdapter {
    private ArrayList<ContactParceable> contacts;
    private Context context;
    private LayoutInflater layoutInflater;
    public ContactListViewAdapter(@NonNull Context context, int resource, ArrayList<ContactParceable> contacts) {
        super(context, resource,contacts);
        this.context = context;
        this.contacts = contacts;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View view, ViewGroup parent) {
        ContactParceable contact = contacts.get(position);
        View contactview = layoutInflater.inflate(R.layout.contactview,null,true);
        TextView name = contactview.findViewById(R.id.name);
        Button add = contactview.findViewById(R.id.add);
        LinearLayout numbers = contactview.findViewById(R.id.numbers);
        name.setText(contact.getName());
        String[] number = contact.getContactNumbers().split(",");
        for(String num:number) {
            numbers.addView(getNumberView(num));
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        addContacts(contact.getName(),number);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                    }
                };
                TedPermission.with(context)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                        .check();
            }
        });
       return contactview;
    }

    private View getNumberView(String num) {
        View view = layoutInflater.inflate(R.layout.number_view,null);
        TextView number = view.findViewById(R.id.number);
        number.setText(num);
        Button call = view.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        call(num);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                    }
                };
                TedPermission.with(context)
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CALL_PHONE)
                        .check();
            }
        });

        return view;
    }

    private void call(String number) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + number));
        phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(phoneIntent);
    }

    private void addContacts(String displayName,String[] numbers) {
        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        if (numbers.length == 3) {
            contactIntent
                    .putExtra(ContactsContract.Intents.Insert.NAME, displayName)
                    .putExtra(ContactsContract.Intents.Insert.PHONE, numbers[0])
                    .putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, numbers[1])
                    .putExtra(ContactsContract.Intents.Insert.TERTIARY_PHONE, numbers[2]);
        } else if (numbers.length == 2) {
            contactIntent
                    .putExtra(ContactsContract.Intents.Insert.NAME, displayName)
                    .putExtra(ContactsContract.Intents.Insert.PHONE, numbers[0])
                    .putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, numbers[1]);
        } else {
            contactIntent
                    .putExtra(ContactsContract.Intents.Insert.NAME, displayName)
                    .putExtra(ContactsContract.Intents.Insert.PHONE, numbers[0]);
        }
        contactIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(contactIntent);
    }
}
