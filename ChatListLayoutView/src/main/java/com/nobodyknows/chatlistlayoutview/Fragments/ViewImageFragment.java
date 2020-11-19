package com.nobodyknows.chatlistlayoutview.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.nobodyknows.chatlistlayoutview.R;

public class ViewImageFragment extends Fragment {

    private String url;

    public ViewImageFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_image, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        PhotoView photoView  = view.findViewById(R.id.photoView);
        Glide.with(getContext()).load(url).into(photoView);
    }
}
