package com.nobodyknows.chatlistlayoutview.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewMediaAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    public ViewMediaAdapter(@NonNull FragmentManager fm,ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public static class Holder {
        private final ArrayList<Fragment> fragments = new ArrayList<>();
        private FragmentManager manager;
        public Holder(FragmentManager manager) {
            this.manager = manager;
        }

        public Holder add(Fragment f) {
            fragments.add(f);
            return this;
        }

        public Holder add(ArrayList<Fragment> list) {
            fragments.addAll(list);
            return this;
        }

        public ViewMediaAdapter set() {
            return new ViewMediaAdapter(manager, fragments);
        }
    }
}
