package com.sopt.lang.Lounge.LoungeDetail

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sopt.lang.MyPage.MyProfile.MyProfileChangeActivity
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService

/**
 * Created by sec on 2018-01-07.
 */
class LoungeCommentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var writer_pic : ImageView = itemView!!.findViewById<ImageView>(R.id.lounge_comment_writer_pic) as ImageView
    var writer_name : TextView = itemView!!.findViewById<TextView>(R.id.lounge_comment_writer_name) as TextView
    var uploaded_time : TextView = itemView!!.findViewById<TextView>(R.id.lounge_comment_uploaded_time) as TextView
    var comment_text : TextView = itemView!!.findViewById<TextView>(R.id.lounge_comment_text) as TextView

    fun toProfileActivity(key : String?, ctx : Context?){
        var my_user_id : String? = SharedPreferencesService!!.instance!!.getPrefStringData("temp_id")
        if(my_user_id.equals(key)){ //내가 내 댓글의 이미지를 클릭했을 때
            val intent = Intent(ctx, MyProfileChangeActivity::class.java)   // 바꿔주세요
            ctx!!.startActivity(intent)
        }else{
            val intent = Intent(ctx, YourProfileActivity::class.java)
            intent.putExtra("key", key)
            ctx!!.startActivity(intent)
        }
    }
}