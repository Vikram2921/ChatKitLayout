package com.nobodyknows.chatlayoutview.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.nobodyknows.chatlayoutview.R;

public class ImageViewFragment extends Fragment {

    private String url;
    public ImageViewFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.image, container, false);
        PhotoView imageView=v.findViewById(R.id.image);
        Glide.with(getContext()).load(url).into(imageView);
        return v;
    }
}
