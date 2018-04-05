package com.sopt.lang.OrganizerPage

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Meeting.MeetingParticipants.MeetingParticipantsData
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity
import com.sopt.lang.R

/**
 * Created by user on 2018-01-12.
 */
class OrganizerPageAdapter(var dataList: ArrayList<MeetingParticipantsData>?, var ctx: Context?) : RecyclerView.Adapter<OrganizerPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OrganizerPageViewHolder {
        val view: View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_meeting_participants, parent, false)

        return OrganizerPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganizerPageViewHolder, position: Int) {
        holder.p_name.text = dataList!!.get(position).user_name
        if (dataList!!.get(position).user_image.equals("0")) {
            holder!!.p_profile_img.setImageResource(R.drawable.profile_default)
        } else {
            Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform())
                    .into(holder!!.p_profile_img)
        }
        holder.master.setImageResource(dataList!!.get(position).approval_state)

        holder!!.itemView.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(ctx, YourProfileActivity::class.java)
            intent.putExtra("key",dataList!!.get(position).user_id)
            ctx!!.startActivity(intent)

        })
    }

    override fun getItemCount(): Int = dataList!!.size


}