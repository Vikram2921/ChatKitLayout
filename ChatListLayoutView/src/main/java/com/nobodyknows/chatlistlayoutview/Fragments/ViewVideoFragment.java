package com.nobodyknows.chatlistlayoutview.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nobodyknows.chatlistlayoutview.PlayVideo;
import com.nobodyknows.chatlistlayoutview.R;

public class ViewVideoFragment extends Fragment {

    private String url;
    public static ViewVideoFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        ViewVideoFragment f = new ViewVideoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_video, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        url = getArguments().getString("url");
        ImageView thumb = view.findViewById(R.id.thumbnail);
        Glide.with(getContext()).load(url).into(thumb);
        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlayVideo.class);
                intent.putExtra("url",url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }
}
