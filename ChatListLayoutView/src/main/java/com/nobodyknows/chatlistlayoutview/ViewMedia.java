package com.nobodyknows.chatlistlayoutview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.nobodyknows.chatlistlayoutview.Adapters.ViewMediaAdapter;
import com.nobodyknows.chatlistlayoutview.Fragments.ViewImageFragment;
import com.nobodyknows.chatlistlayoutview.Fragments.ViewVideoFragment;
import com.nobodyknows.commonhelper.CONSTANT.MessageType;

import java.util.ArrayList;

public class ViewMedia extends AppCompatActivity {

    private ArrayList<String> urls;
    private String type;
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_media);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        type = getIntent().getStringExtra("type");
        num = getIntent().getIntExtra("num",0);
        urls = getIntent().getStringArrayListExtra("urls");
        init();
    }

    private void init() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        if(type.equals(MessageType.IMAGE.name())) {
            for(String url:urls) {
                fragments.add(new ViewImageFragment(url));
            }
        } else if(type.equals(MessageType.VIDEO.name())) {
            for(String url:urls) {
                fragments.add(ViewVideoFragment.newInstance(url));
            }
        }
        ViewPager verticalViewPager = findViewById(R.id.viewpager);
        verticalViewPager.setAdapter(new ViewMediaAdapter.Holder(getSupportFragmentManager()).add(fragments).set());
        verticalViewPager.setCurrentItem(num);
    }
}