package com.sopt.lang.MyPage.Follow

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-08.
 */
class ProfileFollowingAdapter(var dataList: ArrayList<ProfileFollowingData>?, var ctx : Context?) : RecyclerView.Adapter<ProfileFollowingAdapter.ProfileFollowingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProfileFollowingViewHolder {
        val view : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_profile_following, parent, false)
        return ProfileFollowingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileFollowingViewHolder, position: Int) {
        holder.following_name.text = dataList!!.get(position).following_name

      //  holder.following_profile_img.setImageResource(dataList!!.get(position).following_profile_img)
        Glide.with(ctx).load(dataList!!.get(position).following_profile_img).apply(RequestOptions.circleCropTransform()).into(holder!!.following_profile_img)
    }

    override fun getItemCount(): Int = dataList!!.size

    class ProfileFollowingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var following_profile_img : ImageView = itemView!!.findViewById<View>(R.id.following_profile_img) as ImageView
        var following_name : TextView = itemView!!.findViewById<View>(R.id.following_name) as TextView
    }
}