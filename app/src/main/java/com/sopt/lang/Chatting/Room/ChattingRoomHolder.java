package com.sopt.lang.Chatting.Room;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sopt.lang.R;

/**
 * Created by johee on 2018-01-06.
 */

public class ChattingRoomHolder extends RecyclerView.ViewHolder {
    ImageView room_profile;
    TextView room_name;
    TextView content;
    TextView count;
    TextView time;

    public ChattingRoomHolder(View itemView) {
        super(itemView);
        room_profile = (ImageView) itemView.findViewById(R.id.img);
        room_name = (TextView) itemView.findViewById(R.id.chatting_name);
        content = (TextView) itemView.findViewById(R.id.chatting_content);
        count = (TextView) itemView.findViewById(R.id.chatting_count);
        time = (TextView) itemView.findViewById(R.id.chatting_time);
    }
}
