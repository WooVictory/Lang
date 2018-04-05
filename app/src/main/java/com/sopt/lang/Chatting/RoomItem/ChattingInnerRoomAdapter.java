package com.sopt.lang.Chatting.RoomItem;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sopt.lang.R;

import java.util.ArrayList;

import static com.sopt.lang.Chatting.RoomItem.ChattingRoomActivity.isDate;
import static com.sopt.lang.Chatting.RoomItem.ChattingRoomActivity.ismyContent;
import static com.sopt.lang.Chatting.RoomItem.ChattingRoomActivity.isyourContent;

/**
 * Created by johee on 2018-01-07.
 */


public class ChattingInnerRoomAdapter extends RecyclerView.Adapter<ChattingInnerRoomHolder>  {

    ArrayList<ChattingInnerRoomData> innerRoomData;
    public static View itemView;

    public ChattingInnerRoomAdapter(ArrayList<ChattingInnerRoomData> innerRoomData) {
        this.innerRoomData =innerRoomData;
    }

    @Override
    public ChattingInnerRoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting, parent, false);

        ChattingInnerRoomHolder viewHolder = new ChattingInnerRoomHolder(itemView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ChattingInnerRoomHolder holder, int position) {
        //public RelativeLayout dateLayout,contentLayout;

        if(isDate){
            holder.date.setText(innerRoomData.get(position).date);
            holder.contentLayout.setVisibility(View.GONE);
            holder.dateLayout.setVisibility(View.VISIBLE);
            isDate =false;
            Log.d("isdata2",innerRoomData.get(position).date);
        }
        if(isyourContent){
            holder.yourProfile.setImageResource(innerRoomData.get(position).your_profile);
            holder.yourContent.setText(innerRoomData.get(position).your_content);
         //   holder.myContent.setText(innerRoomData.get(position).my_content);
            holder.myContent.setVisibility(View.GONE);
            holder.dateLayout.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
            isyourContent=false;
            Log.d("isdata2",innerRoomData.get(position).your_content);
        }
        if(ismyContent){
          //  holder.yourProfile.setImageResource(innerRoomData.get(position).your_profile);
         //   holder.yourContent.setText(innerRoomData.get(position).your_content);
            holder.myContent.setText(innerRoomData.get(position).my_content);
            holder.yourProfile.setVisibility(View.GONE);
            holder.yourContent.setVisibility(View.GONE);
            holder.dateLayout.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
            ismyContent = false;
            Log.d("isdata2",innerRoomData.get(position).my_content);
        }

    }

    @Override
    public int getItemCount() {
        return innerRoomData.size();
    }
}
