package com.nobodyknows.chatlayoutview.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.nobodyknows.chatlayoutview.R;

public class VideoViewFragment extends Fragment {

    private String url;
    private VideoView videoView;
    public VideoViewFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v= inflater.inflate(R.layout.video, container, false);
        videoView = v.findViewById(R.id.videoview);
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
//        mediaController.show();
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        return v;
    }

    public VideoView getVideoView() {
        return videoView;
    }
}
