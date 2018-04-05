package com.sopt.lang.Chatting.Room;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sopt.lang.R;

import java.util.ArrayList;

/**
 * Created by johee on 2018-01-06.
 */

public class ChattingRoomAdapter extends RecyclerView.Adapter<ChattingRoomHolder> {

    ArrayList<ChattingRoomData> roomData;
    View.OnClickListener recyclerlistener;
    public static View itemView;

    public ChattingRoomAdapter(ArrayList<ChattingRoomData> roomData, View.OnClickListener recyclerroomClickListener) {
        this.roomData = roomData;
        this.recyclerlistener = recyclerroomClickListener;
    }

    @Override
    public ChattingRoomHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // 뷰홀더 패턴을 생성하는 메소드.
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting_room, parent, false);
        itemView.setOnClickListener(recyclerlistener);

        ChattingRoomHolder viewHolder = new ChattingRoomHolder(itemView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ChattingRoomHolder holder, int position) {

//        holder.room_profile.setImageResource(roomData.get(position).profile);
//        holder.room_name.setText(roomData.get(position).name);
//        holder.content.setText(roomData.get(position).content);
//        holder.count.setText(roomData.get(position).count);
//        holder.time.setText(roomData.get(position).time);
    }

    @Override
    public int getItemCount() {
        return roomData.size();
    }
}
