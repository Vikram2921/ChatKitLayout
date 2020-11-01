package com.nobodyknows.chatlayoutview.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.nobodyknows.chatlayoutview.R;

public class VideoViewFragment extends Fragment {

    private String url;
    public VideoViewFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.video, container, false);
        VideoView videoView = v.findViewById(R.id.videoview);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
//        mediaController.show();
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.start();
        return v;
    }
}
