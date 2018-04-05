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
class ProfileFollowerAdapter(var dataList: ArrayList<ProfileFollowerData>?, var ctx : Context?) : RecyclerView.Adapter<ProfileFollowerAdapter.ProfileFollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProfileFollowerViewHolder {
        val view : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_profile_follower, parent, false)
        return ProfileFollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileFollowerViewHolder, position: Int) {
        holder.follower_name.text = dataList!!.get(position).follower_name

        // holder.follower_profile_img.setImageResource(dataList!!.get(position).follower_profile_img)
        Glide.with(ctx).load(dataList!!.get(position).follower_profile_img).apply(RequestOptions.circleCropTransform()).into(holder!!.follower_profile_img)
    }

    override fun getItemCount(): Int = dataList!!.size

    class ProfileFollowerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var follower_profile_img : ImageView = itemView!!.findViewById<View>(R.id.follower_profile_img) as ImageView
        var follower_name : TextView = itemView!!.findViewById<View>(R.id.follower_name) as TextView
    }
}