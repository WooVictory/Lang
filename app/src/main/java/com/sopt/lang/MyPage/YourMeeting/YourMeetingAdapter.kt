package com.sopt.lang.MyPage.YourMeeting

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sopt.lang.Meeting.MeetingDetailActivity
import com.sopt.lang.Network.MyPage.OtherMeetingListData
import com.sopt.lang.R

/**
 * Created by kor on 2018-01-11.
 */

/**
 * Created by sec on 2018-01-08.
 */
class YourMeetingAdapter(var dataList : List<OtherMeetingListData>, var ctx : Context?) : RecyclerView.Adapter<YourMeetingViewHolder>() {
    private  var onItemClick : View.OnClickListener ?=null

    override fun onBindViewHolder(holder: YourMeetingViewHolder?, position: Int) {

      //  Log.v("널?",dataList!!.get(position).meeting_title)
        if(dataList==null){

        }else{
            holder!!.meeting_title.setText(dataList!!.get(position).meeting_title)
            holder!!.meeting_master.setText(dataList!!.get(position).user_name)
            holder!!.meeting_lang.setText(dataList!!.get(position).meeting_lang)
            holder!!.meeting_purpose.setText(dataList!!.get(position).meeting_type)

        }

        holder!!.itemView.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(ctx, MeetingDetailActivity::class.java)
            intent.putExtra("key",dataList!!.get(position).meeting_id)
            ctx!!.startActivity(intent)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): YourMeetingViewHolder {
        val mainView : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_mymeeting, parent, false)

        mainView.setOnClickListener ( onItemClick )

        return YourMeetingViewHolder(mainView)
    }

    override fun getItemCount(): Int  = dataList!!.size


}