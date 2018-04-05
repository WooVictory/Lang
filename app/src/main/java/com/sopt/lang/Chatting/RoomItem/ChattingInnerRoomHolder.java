package com.sopt.lang.Chatting.RoomItem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sopt.lang.R;

/**
 * Created by johee on 2018-01-07.
 */


public class ChattingInnerRoomHolder extends RecyclerView.ViewHolder {
    ImageView yourProfile;
    TextView date,yourContent,myContent;
    RelativeLayout dateLayout,contentLayout;
    public ChattingInnerRoomHolder(View itemView) {
        super(itemView);
        yourProfile = (ImageView)itemView.findViewById(R.id.your_profile);
        date = (TextView)itemView.findViewById(R.id.chatting_date_content);
        yourContent = (TextView)itemView.findViewById(R.id.your_content);
        myContent = (TextView)itemView.findViewById(R.id.my_content);
        dateLayout = (RelativeLayout)itemView.findViewById(R.id.chatting_room_date);
        contentLayout = (RelativeLayout)itemView.findViewById(R.id.chatting_room_content);
    }
}
