package com.sopt.lang.Chatting;

/**
 * Created by user on 2018-01-13.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sopt.lang.Chatting.Room.ChatDetail;
import com.sopt.lang.R;
import com.sopt.lang.SharedPreferencesService;

import java.util.Date;
import java.util.List;


/**
 * Created by jyoung on 2017. 12. 19..
 */

public class ChatListAdapter extends RecyclerView.Adapter {

    List<ChatDetail> chatDetailList;
    Context context;
    View.OnClickListener onClickListener;

    public static final int CHAT_TEXT = 101;
    public static final int CHAT_IMAGE = 102;
    public static final int CHAT_VIDEO = 103;
    public static final int CHAT_AUDIO = 104;
    public static final int CHAT_FILE = 105;

    public ChatListAdapter(List<ChatDetail> chatDetailList, Context context, View.OnClickListener onClickListener) {
        this.chatDetailList = chatDetailList;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    public void updateList(List<ChatDetail> chatDetailList){
        this.chatDetailList = chatDetailList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch(viewType){
            case CHAT_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting, parent, false);
                return new ChatTextViewHolder(view);
            case CHAT_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting, parent, false);
                view.setOnClickListener(onClickListener);
                return new ChatImageViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case CHAT_TEXT: ((ChatTextViewHolder)holder).bindView(chatDetailList.get(position)); break;
            case CHAT_IMAGE: ((ChatImageViewHolder)holder).bindView(chatDetailList.get(position)); break;
        }
    }

    @Override
    public int getItemCount() {
        return chatDetailList != null ? chatDetailList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return chatDetailList.get(position).getMediaType();
    }

    class ChatTextViewHolder extends RecyclerView.ViewHolder {
        TextView youContent;
        TextView meContent;
        ImageView youProfile;

        public ChatTextViewHolder(View itemView) {
            super(itemView);
            SharedPreferencesService.Companion.getInstance().load(context);

            youContent = (TextView) itemView.findViewById(R.id.your_content);
            meContent = (TextView) itemView.findViewById(R.id.my_content);
            youProfile = (ImageView) itemView.findViewById(R.id.your_profile);

        }

        public void bindView(ChatDetail chatItem){


            if(chatItem.getSenderToken().equals(SharedPreferencesService.Companion.getInstance().getPrefStringData("fcm_token"))) {
                youProfile.setVisibility(View.INVISIBLE);
                meContent.setVisibility(View.VISIBLE);
                youContent.setVisibility(View.GONE);
                meContent.setText(chatItem.getContent());
            }
            else {
                meContent.setVisibility(View.GONE);
                youContent.setVisibility(View.VISIBLE);
                youContent.setText(chatItem.getContent());
                if(getAdapterPosition() != 0) {
                    if (chatDetailList.get(getAdapterPosition() - 1).getSenderToken().equals(chatItem.getSenderToken()))
                        youProfile.setVisibility(View.INVISIBLE);

                    else
                        youProfile.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    class ChatImageViewHolder extends RecyclerView.ViewHolder {
        ImageView chatImage;

        public ChatImageViewHolder(View itemView) {
            super(itemView);
        }

        public void bindView(ChatDetail chatItem){

        }
    }

    public static String calculateTime(Date date) {

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        Log.d("asdf", curTime + "   " + regTime + "     " + diffTime);


        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            System.out.println(diffTime);

            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }

        return msg;
    }

    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
}