package com.sopt.lang.Chatting.Recommend;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sopt.lang.R;

/**
 * Created by johee on 2018-01-06.
 */

public class ChattingRecommendHolder extends RecyclerView.ViewHolder {
    ImageView profile,delet,profile_more;
    TextView name;
    TextView myLang;
    TextView wishLang;

    public ChattingRecommendHolder(View itemView){
        super(itemView);
        profile = (ImageView)itemView.findViewById(R.id.chatting_recommend_profile);
        name = (TextView)itemView.findViewById(R.id.chatting_recommend_name);
        myLang = (TextView)itemView.findViewById(R.id.chatting_my_lang);
        wishLang =(TextView)itemView.findViewById(R.id.chatting_wish_lang);
        delet = (ImageView)itemView.findViewById(R.id.recommend_delete);
        profile_more = (ImageView)itemView.findViewById(R.id.profile_more);

    }
}
