package com.sopt.lang.Chatting;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sopt.lang.Chatting.Room.ChattingRoomData;
import com.sopt.lang.R;
import com.sopt.lang.SharedPreferencesService;

import java.util.List;

/**
 * Created by jyoung on 2017. 12. 18..
 */

public class RoomListAdapter extends RecyclerView.Adapter {

    List<ChattingRoomData> roomList;
    View.OnClickListener onClickListener;

    public RoomListAdapter(List<ChattingRoomData> roomList, View.OnClickListener onClickListener) {
        this.roomList = roomList;
        this.onClickListener = onClickListener;
    }

    public void updateList(List<ChattingRoomData> roomList){
        this.roomList = roomList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting_room, parent, false);
        view.setOnClickListener(onClickListener);
        return new RoomListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RoomListViewHolder)holder).bindView(roomList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomList!=null?roomList.size():0;
    }

    class RoomListViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;
        TextView recentChatTime;
        TextView recentChatContent;
        TextView chatCount;
        ImageView roomImage;
        int count =0;

        public RoomListViewHolder(View itemView) {
            super(itemView);
            roomName = (TextView) itemView.findViewById(R.id.chatting_name);
            recentChatTime = (TextView) itemView.findViewById(R.id.chatting_time);
            recentChatContent = (TextView) itemView.findViewById(R.id.chatting_content);
            chatCount = (TextView) itemView.findViewById(R.id.chatting_count);
            roomImage = (ImageView) itemView.findViewById(R.id.img);
        }

        public void bindView(ChattingRoomData roomItem){
            /*roomName.setText("임수정");
            roomImage.setImageResource(R.drawable.sujung);*/

            Log.d("sf", roomItem.getRecentMessage());
            recentChatContent.setText(roomItem.getRecentMessage());
            recentChatTime.setText(roomItem.getRecentMessageTime());

            if(SharedPreferencesService.Companion.getInstance().getPrefIntegerData(roomItem.getRoomName()) != 0) {
                count = SharedPreferencesService.Companion.getInstance().getPrefIntegerData(roomItem.getRoomName());
                chatCount.setText(String.valueOf(roomItem.getTotalCount() - count+1));
                Log.d("count1", count+"");
            }
            else {
                SharedPreferencesService.Companion.getInstance().setPrefData(roomItem.getRoomName(), count);
                chatCount.setText("0");
                Log.d("count3", count+"");

            }

        }
    }
}
