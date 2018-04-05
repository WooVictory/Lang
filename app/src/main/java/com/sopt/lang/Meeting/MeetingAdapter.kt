package com.sopt.lang.Meeting

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.Meeting.MeetingLikeResponse
import com.sopt.lang.Network.Meeting.MeetingListData
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by sec on 2018-01-01.
 */
class MeetingAdapter(var dataList : List<MeetingListData>, var ctx : Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var onItemClick : View.OnClickListener?=null
    private var networkService: NetworkService? = null
    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"
    private var context : Context?=this.ctx
    var flag : Boolean? = null
    val idx = Int
    override fun getItemCount(): Int {
        return dataList!!.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder!!.itemView.setOnClickListener(View.OnClickListener { v ->
            //내 모임 눌렀을 때 모임 상세보기
            val intent = Intent(ctx, MeetingDetailActivity::class.java)
            intent.putExtra("meeting_id",dataList!!.get(position).meeting_id)
            Log.v("meeting_id",dataList!!.get(position)!!.meeting_id!!.toString())
            ctx!!.startActivity(intent)

        })
        networkService = GlobalApplication.instance!!.networkService
        if(holder is MeetingViewHolder)
        {
            val itemHolder : MeetingViewHolder?=holder
            //itemHolder!!.groupImage.(dataList!!.get(position).groupImage ) // 몇번째가 들어갔는지 모르게 되는데 이럴 경우 position(구분하는 번호)이 구분해준다.
            // 각 포지션에 맞게 뷰홀더에 넣어줌
            Log.d("dataList size : ",dataList.size.toString())
            itemHolder!!.groupTitle.setText(dataList!!.get(position).meeting_title)
            Glide.with(ctx).load(dataList!!.get(position).meeting_image).into(itemHolder!!.groupImage)
//            itemHolder!!.groupTitle.setText(dataList!!.get(position).groupTitle)
            itemHolder!!.groupLanguage.setText(dataList!!.get(position).meeting_lang)
            itemHolder!!.groupHost.setText(dataList!!.get(position).user_name)

            flag = dataList!!.get(position).isLike

            if(flag!!){
                itemHolder!!.groupLike.setImageResource(R.drawable.home_like_on)
            }else{
                itemHolder!!.groupLike.setImageResource(R.drawable.home_like_off)
            }


            if(dataList!!.get(position).meeting_type == 100){
                itemHolder!!.groupPurpose.setText("전체")
            }
            if(dataList!!.get(position).meeting_type == 101){
                itemHolder!!.groupPurpose.setText("언어교환")
            }else if(dataList!!.get(position).meeting_type == 102)
            {
                itemHolder!!.groupPurpose.setText("파티")
            }
            itemHolder!!.groupLike.setOnClickListener {
                //set_lounge_like_btn(itemHolder, position)
                Log.v("색변경","11")
                mMeetingLike(itemHolder,position!!)!!

            }
            if(onItemClick !=null){

            }
        }


    }

    fun mMeetingLike(itemHolder : MeetingViewHolder,position : Int){
        Log.v("확인", dataList!!.get(position)!!.meeting_id.toString())
        val meetingLikeResponse = networkService!!.putMeetingLike(token, dataList!!.get(position)!!.meeting_id)
        meetingLikeResponse!!.enqueue(object : Callback<MeetingLikeResponse>
        {

            override fun onResponse(call: Call<MeetingLikeResponse>?, response: Response<MeetingLikeResponse>?) {
                if(response!!.body().status.equals("success"))
                {
                    Toast.makeText(context,"좋아요 누르셨습니다.", Toast.LENGTH_LONG).show()
                    Log.v("확인", "11")
                    set_lounge_like_btn(itemHolder, position)
                }

            }

            override fun onFailure(call: Call<MeetingLikeResponse>?, t: Throwable?) {
                Log.d("상태",t.toString())
                Toast.makeText(context,"통신 실패.", Toast.LENGTH_LONG).show()
                Log.v("실패", "22")
            }

        })

    }

    // var flag : Boolean =  true
    fun set_lounge_like_btn(holder: MeetingViewHolder?, position: Int) {
        Log.v("테스트","in set_lounge_like_btn")
        Log.v("flag:",flag.toString())
        if (flag!!) {
            Log.v("테스트","off to on")
            holder!!.groupLike.setImageResource(R.drawable.home_like_on)
            flag = false

        } else if(flag == false){
            Log.v("테스트","on to off")
            holder!!.groupLike.setImageResource(R.drawable.home_like_off)
            flag = true
            //Toast.makeText(context, "좋아요를 취소하셨습니다.",Toast.LENGTH_LONG).show()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        val mainView : View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_meeting,parent,false)
        // 부모로부터 inflate를 통해서 pokemon_items 뷰를 가져와서 mainView에 담는다.
        mainView.setOnClickListener(onItemClick)
        return MeetingViewHolder(mainView)
        // pokemon_items로부터 가지고 온 레이아웃 자체(큰 뷰)를 PokemonViewHolder에 넘긴다.
        // 넘기면 PokemonViewHolder가 pokemon_items 레이아웃에 있는 뷰들의 id를 가져온다.

    }
    fun setOnItemClickListener(l:View.OnClickListener){
        onItemClick = l
    }
}