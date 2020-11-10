package com.nobodyknows.chatuserlistview.Listeners;

import com.nobodyknows.chatuserlistview.Model.User;

public interface ChatUserListViewListener {
    public void onUserSelect(User user);
    public void onClickAudioCall(User user);
    public void onClickVideoCall(User user);
    public void onClickInfoButton(User user);
}
