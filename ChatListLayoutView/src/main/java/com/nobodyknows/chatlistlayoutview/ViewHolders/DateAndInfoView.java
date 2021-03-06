package com.nobodyknows.chatlistlayoutview.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nobodyknows.chatlistlayoutview.R;

public class DateAndInfoView extends RecyclerView.ViewHolder {
    View view;
    TextView textView;
    public DateAndInfoView(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void initalize(String info) {
        textView = view.findViewById(R.id.info);
        textView.setText(info);
    }
}
