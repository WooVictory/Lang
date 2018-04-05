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
import com.sopt.lang.Network.MyPage.OtherFollowingListData
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-08.
 */
class YourProfileFollowingAdapter(var dataList: List<OtherFollowingListData>?, var ctx : Context?) : RecyclerView.Adapter<YourProfileFollowingAdapter.YourProfileFollowingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): YourProfileFollowingViewHolder {
        val view : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_profile_following, parent, false)
        return YourProfileFollowingViewHolder(view)
    }

    override fun onBindViewHolder(holder: YourProfileFollowingViewHolder, position: Int) {
        holder.following_name.text = dataList!!.get(position).user_name
        Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.following_profile_img)
    }

    override fun getItemCount(): Int = dataList!!.size

    class YourProfileFollowingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var following_profile_img : ImageView = itemView!!.findViewById<View>(R.id.following_profile_img) as ImageView
        var following_name : TextView = itemView!!.findViewById<View>(R.id.following_name) as TextView
    }
}