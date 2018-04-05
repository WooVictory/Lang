package com.sopt.lang.Meeting.MeetingReview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Network.MeetingReview.MeetingReviewList
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-07.
 */
class MeetingReviewAdapter(var dataList: List<MeetingReviewList>?, var ctx : Context?) : RecyclerView.Adapter<MeetingReviewAdapter.MeetingReviewViewHolder>()
{
    override fun onBindViewHolder(holder: MeetingReviewViewHolder?, position: Int) {
        Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.review_profile_img)
        holder.review_name.text = dataList!!.get(position).user_name
        Log.v("test737", dataList!!.get(position).review_id.toString()+dataList!!.get(position).user_name)
        holder.review_rating.setRating(dataList!!.get(position).review_rating!!.toFloat())
        holder.review_rating.rating = dataList!!.get(position).review_rating!!.toFloatOrNull()!!
        holder.review_rating_num.text = dataList!!.get(position).review_rating
        holder.review_context.text = dataList!!.get(position).review_content
    }

    override fun getItemCount(): Int = dataList!!.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MeetingReviewViewHolder {
        val view : View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_meeting_review, parent, false)
        return MeetingReviewViewHolder(view)
    }
    class MeetingReviewViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var review_profile_img : ImageView = itemView!!.findViewById<View>(R.id.review_profile_img) as ImageView
        var review_name : TextView = itemView!!.findViewById<View>(R.id.review_name) as TextView
        var review_rating : RatingBar = itemView!!.findViewById<View>(R.id.review_rating) as RatingBar
        var review_rating_num : TextView = itemView!!.findViewById<View>(R.id.review_rating_num) as TextView
        var review_context : TextView = itemView!!.findViewById<View>(R.id.review_context) as TextView
    }
}