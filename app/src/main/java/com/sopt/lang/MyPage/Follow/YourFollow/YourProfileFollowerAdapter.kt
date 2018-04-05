package com.sopt.lang.MyPage.Follow.YourFollow

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Network.MyPage.OtherFollowerListData
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-08.
 */
class YourProfileFollowerAdapter(var dataList: List<OtherFollowerListData>?, var ctx : Context?) : RecyclerView.Adapter<YourProfileFollowerAdapter.YourProfileFollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): YourProfileFollowerViewHolder {
        val view : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_profile_follower, parent, false)
        return YourProfileFollowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: YourProfileFollowerViewHolder, position: Int) {
        holder.follower_name.text = dataList!!.get(position).user_name
        Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.follower_profile_img)
    }

    override fun getItemCount(): Int = dataList!!.size

    class YourProfileFollowerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var follower_profile_img : ImageView = itemView!!.findViewById<View>(R.id.follower_profile_img) as ImageView
        var follower_name : TextView = itemView!!.findViewById<View>(R.id.follower_name) as TextView
    }
}