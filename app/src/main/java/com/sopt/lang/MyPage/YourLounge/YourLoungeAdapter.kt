package com.sopt.lang.MyPage.YourLounge

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Chatting.ChatListAdapter
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Lounge.LoungeDetail.LoungeDetailActivity
import com.sopt.lang.Network.Lounge.LoungeLikeResponse
import com.sopt.lang.Network.MyPage.OtherLoungeListData
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kor on 2018-01-11.
 */


class YourLoungeAdapter (var dataList : List<OtherLoungeListData>?, var ctx : Context?) : RecyclerView.Adapter<YourLoungeAdapter.YourLoungeHolder>() {
    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"


    var mYourLoungeViewHolder: YourLoungeHolder? = null


    override fun getItemCount(): Int = dataList!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): YourLoungeHolder {
        val loungeView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_yourlounge, parent, false)

        networkService = GlobalApplication.instance!!.networkService

        loungeView.setOnClickListener(onItemClick)
        mYourLoungeViewHolder = YourLoungeHolder(loungeView)

        return YourLoungeHolder(loungeView)
    }


    override fun onBindViewHolder(holder: YourLoungeHolder?, position: Int) {
        //holder!!.writer_pic.setImageResource(dataList!!.get(position).writer_pic)
        //holder!!.writer_pic.setImageResource(dataList!!)
        Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.writer_pic)

        // holder!!.writer_pic.setImageResource(dataList!!.get(position).writer_pic)
        holder!!.writer_name.setText(dataList!!.get(position).user_name)
        holder!!.writer_language.setText(dataList!!.get(position).native_lang)
        holder!!.writer_interest.setText(dataList!!.get(position).hope_lang)

        val resultTime = dataList!!.get(position).lounge_time
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val date1 = df.parse(resultTime)

        holder!!.uploaded_time.setText(ChatListAdapter.calculateTime(date1).toString())
        holder!!.lounge_text.setText(dataList!!.get(position).lounge_content)
        holder!!.num_of_like.setText(dataList!!.get(position).like_count.toString())
        holder!!.num_of_comment.setText(dataList!!.get(position).comment_count.toString())

        if(dataList!!.get(position).isLike){
            holder.lounge_like.setImageResource(R.drawable.lounge_like_on)
        }else{
            holder.lounge_like.setImageResource(R.drawable.lounge_like_off)
        }

        holder.lounge_like.setOnClickListener {
            mLoungeLike(holder, position)
        }
        holder.lounge_text.setOnClickListener{
            mYourLoungeViewHolder!!.toDetailActivity(dataList!!.get(position).lounge_id, ctx)
        }
        holder!!.lounge_comment!!.setOnClickListener {
            mYourLoungeViewHolder!!.toDetailActivity(dataList!!.get(position).lounge_id, ctx)
        }

    }

    fun refreshAdapter(dataList : List<OtherLoungeListData>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun mLoungeLike(holder: YourLoungeHolder?, position : Int){
        Log.v("test633", "posiiton : " + position.toString())
        Log.v("test633", "loung id : " + dataList!!.get(position).lounge_id.toString())

        val loungeLikeResponge : Call<LoungeLikeResponse> = networkService!!.putLoungeLike((SharedPreferencesService.instance!!.getPrefStringData("token", ""))!!, dataList!!.get(position).lounge_id)
        loungeLikeResponge.enqueue(object : Callback<LoungeLikeResponse>
        {
            override fun onFailure(call: Call<LoungeLikeResponse>?, t: Throwable?) {
                Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoungeLikeResponse>?, response: Response<LoungeLikeResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    set_lounge_detail_like_btn(holder, position)
                }
            }

        })
    }


    fun set_lounge_detail_like_btn(holder: YourLoungeHolder?, position : Int) {
        if (dataList!!.get(position).isLike) {
            Log.v("테스트","on to off")
            dataList!!.get(position).isLike = false
            holder!!.lounge_like.setImageResource(R.drawable.lounge_like_off)
            dataList!!.get(position).like_count--
        } else {
            Log.v("테스트","off to on")
            dataList!!.get(position).isLike = true
            holder!!.lounge_like.setImageResource(R.drawable.lounge_like_on)
            dataList!!.get(position).like_count++
        }
        holder.num_of_like.setText(dataList!!.get(position).like_count.toString())
        //refreshAdapter(dataList!!)
    }

//    fun dpTopx(dp : Int) : Int{
//        var density : Float = ctx!!.getResources().getDisplayMetrics().density
//        return Math.round((dp.times(density)))
//    }

    private var onItemClick: View.OnClickListener? = null
    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    /**
     * Created by johee on 2018-01-04.
     */
    class YourLoungeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var writer_pic: ImageView = itemView!!.findViewById(R.id.your_lounge_writer_pic)
        var writer_name: TextView = itemView!!.findViewById(R.id.your_lounge_writer_name)
        var writer_language: TextView = itemView!!.findViewById(R.id.your_lounge_writer_lang)
        var writer_interest: TextView = itemView!!.findViewById(R.id.your_lounge_writer_interest)
        var uploaded_time: TextView = itemView!!.findViewById(R.id.your_lounge_uploded_time)
        var lounge_text: TextView = itemView!!.findViewById(R.id.your_lounge_expandable_text)
        // var expand_text_btn : Button = itemView!!.findViewById(R.id.lounge_) as Button
        var num_of_like: TextView = itemView!!.findViewById(R.id.your_lounge_num_of_like)
        var num_of_comment: TextView = itemView!!.findViewById(R.id.your_lounge_num_of_comment)

        var lounge_like: ImageView = itemView!!.findViewById(R.id.your_lounge_like)
        var lounge_comment: ImageView = itemView!!.findViewById(R.id.your_lounge_comment)
        // 동적으로 추가되는 이미지들은?
//        var index: Int? = null
//
//        var img_scroll_view : HorizontalScrollView = itemView!!.findViewById(R.id.horiozontal_scroll)
//        var img_box : LinearLayout = itemView!!.findViewById(R.id.lounge_update_image_box)
//
//        var img1 : ImageView = itemView!!.findViewById(R.id.img1)
//        var img2 : ImageView = itemView!!.findViewById(R.id.img2)
//        var img3 : ImageView = itemView!!.findViewById(R.id.img3)
//        var img4 : ImageView = itemView!!.findViewById(R.id.img4)
//        var img5 : ImageView = itemView!!.findViewById(R.id.img5)
//        var img6 : ImageView = itemView!!.findViewById(R.id.img6)
//        var img7 : ImageView = itemView!!.findViewById(R.id.img7)
//        var img8 : ImageView = itemView!!.findViewById(R.id.img8)
//        var img9 : ImageView = itemView!!.findViewById(R.id.img9)
//        var img10 : ImageView = itemView!!.findViewById(R.id.img10)
//
//        var imgs : Array<ImageView> = arrayOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10)
        fun toDetailActivity(key : Int?, ctx : Context?){
            val intent = Intent(ctx, LoungeDetailActivity::class.java)
            intent.putExtra("key", key)
            ctx!!.startActivity(intent)
        }

        override fun onClick(v: View?) {
            lounge_comment!!.setOnClickListener {
                //            val intent = Intent(v!!.getContext(), LoungeDetailActivity::class.java)
//            // putExtra로 글의 key 값 보내기zx
//            v!!.getContext().startActivity(intent)
            }

        }


    }
}