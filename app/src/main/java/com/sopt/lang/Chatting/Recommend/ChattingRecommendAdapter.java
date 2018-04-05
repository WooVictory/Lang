package com.sopt.lang.Chatting.Recommend;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity;
import com.sopt.lang.Network.Chatting.FriendRecommendDatas;

import com.sopt.lang.R;

import java.util.ArrayList;

/**
 * Created by johee on 2018-01-06.
 */

public class ChattingRecommendAdapter  extends RecyclerView.Adapter<ChattingRecommendHolder>{
    ArrayList<FriendRecommendDatas> recommendDatas;
    public static View itemView;


    public ChattingRecommendAdapter(ArrayList<FriendRecommendDatas> recommendData) {
        this.recommendDatas = recommendData;
    }


    @Override
    public ChattingRecommendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatting_recommend, parent, false);
        ChattingRecommendHolder viewHolder = new ChattingRecommendHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ChattingRecommendHolder holder, final int position) {
        // holder.profile.setImageResource(recommendDatas.get(position).user_image);
        Glide.with(itemView.getContext()).load(recommendDatas.get(position).user_image).into(holder.profile);
        holder.name.setText(recommendDatas.get(position).user_name);
        holder.myLang.setText(recommendDatas.get(position).native_lang);
        holder.wishLang.setText(recommendDatas.get(position).hope_lang);
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommendDatas.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        holder.profile_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("프로필자세히보기",recommendDatas.get(position).user_id);
                Intent more = new Intent(v.getContext(), YourProfileActivity.class);
                more.putExtra("key",recommendDatas.get(position).user_id);
                v.getContext().startActivity(more);

            }
        });
    }
    @Override
    public int getItemCount() {
        return recommendDatas.size();
    }
}
