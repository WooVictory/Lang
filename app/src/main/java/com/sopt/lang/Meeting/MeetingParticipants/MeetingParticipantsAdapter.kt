package com.sopt.lang.Meeting.MeetingParticipants

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Network.Meeting.MeetingDetailParticipants
import com.sopt.lang.R

/**
 * Created by sec on 2018-01-06.
 */

class MeetingParticipantsAdapter(var dataList:List<MeetingDetailParticipants>?, var ctx : Context?, var organiser_name: String) : RecyclerView.Adapter<MeetingParticipantsAdapter.MeetingParticipantsViewHolder>() {
    override fun onBindViewHolder(holder: MeetingParticipantsViewHolder, position: Int) {
        holder.p_name.text = dataList!!.get(position).user_name
        Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.p_profile_img)
        if(dataList!!.get(position).user_id.equals(organiser_name)){
            holder.p_organizer_sign.setImageResource(R.drawable.meetinginformation_organizer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MeetingParticipantsViewHolder {
        val view : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_meeting_participants, parent, false)
        return MeetingParticipantsViewHolder(view)
    }

    override fun getItemCount(): Int = dataList!!.size
    class MeetingParticipantsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var p_profile_img : ImageView = itemView!!.findViewById<View>(R.id.participant_profile_img) as ImageView
        var p_name : TextView = itemView!!.findViewById<View>(R.id.participant_name) as TextView
        var p_organizer_sign : ImageView = itemView!!.findViewById<View>(R.id.meetinginfo_organizer) as ImageView
    }
}