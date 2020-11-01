package com.nobodyknows.chatlayoutview.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nobodyknows.chatlayoutview.CONSTANT.MessageType;
import com.nobodyknows.chatlayoutview.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class viewmedia extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private String clicked = "",localPath;
    private MessageType type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmedia);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        urls = getIntent().getStringArrayListExtra("urls");
        names = getIntent().getStringArrayListExtra("names");
        clicked = getIntent().getStringExtra("clicked");
        localPath = getIntent().getStringExtra("localpath");
        type = MessageType.values()[getIntent().getIntExtra("type",0)];
        init();
    }

    private void init() {
        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        addFragments();
    }

    private void addFragments() {
        String url = "";
        for(int i=0;i<urls.size();i++) {
            if(new File(localPath+names.get(i)).exists()) {
                url = localPath+names.get(i);
            } else {
                url = urls.get(i);
            }
            Log.d("TAGURL", "addFragments: "+url);
            if(type == MessageType.IMAGE) {
                viewPagerAdapter.addFragment(new ImageViewFragment(url),"");
            } else if(type == MessageType.VIDEO) {
                viewPagerAdapter.addFragment(new VideoViewFragment(url),"");
            }
        }
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(urls.indexOf(clicked));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mList = new ArrayList<>();
        private final List<String> mTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }
        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mList.add(fragment);
            mTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}