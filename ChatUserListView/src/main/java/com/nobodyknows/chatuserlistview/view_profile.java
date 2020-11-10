package com.nobodyknows.chatuserlistview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class view_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("profile");
        TextView textViewname = findViewById(R.id.name);
        PhotoView photoView = findViewById(R.id.photoview);
        textViewname.setText(name);
        if(profile != null && profile.length() > 0) {
            Glide.with(getApplicationContext()).load(profile).placeholder(R.drawable.ic_baseline_person_24).into(photoView);
        }
    }
}