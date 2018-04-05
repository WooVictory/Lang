package com.sopt.lang.Alarm

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Chatting.ChatListAdapter
import com.sopt.lang.Home.HomeActivity
import com.sopt.lang.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by sec on 2018-01-08.
 */
class AlarmAdapter(var dataList: ArrayList<AlarmData>?, var ctx : Context?): RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    var mAlarmViewHolder : AlarmViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlarmViewHolder {
        val view : View = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_alarm, parent, false)

        mAlarmViewHolder = AlarmViewHolder(view)

        return AlarmViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.alarm_content.text = dataList!!.get(position).alarm_content
        //      holder.alarm_profile_img.setImageResource(dataList!!.get(position).alarm_profile_img)
        if(dataList!!.get(position).alarm_profile_img ==null){
            holder!!.alarm_profile_img.setImageResource(R.drawable.profile_default)
        }
        else
        {
            Glide.with(ctx).load(dataList!!.get(position).alarm_profile_img).apply(RequestOptions.circleCropTransform()).into(holder!!.alarm_profile_img)

        }


        val resultTime = dataList!!.get(position).alarm_time
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val date1 = df.parse(resultTime)

        holder.alarm_time.setText(ChatListAdapter.calculateTime(date1).toString())

        holder.alarm_profile_img.setOnClickListener{
            mAlarmViewHolder!!.toProfileActivity(dataList!!.get(position).alarm_profile_id, ctx)
        }
    }

    override fun getItemCount(): Int = dataList!!.size

    private var onItemClick : View.OnClickListener? = null

    fun setOnItemClickListener(l:View.OnClickListener){
        onItemClick = l
    }
    class AlarmViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var alarm_content : TextView = itemView!!.findViewById<View>(R.id.alarm_content) as TextView
        var alarm_profile_img : ImageView = itemView!!.findViewById<View>(R.id.alarm_profile_img) as ImageView
        var alarm_time : TextView = itemView!!.findViewById<View>(R.id.alarm_time) as TextView

        fun toProfileActivity(key : String?, ctx : Context?){
            val intent = Intent(ctx, HomeActivity::class.java)
            intent.putExtra("key", key)
            ctx!!.startActivity(intent)
        }
    }
}