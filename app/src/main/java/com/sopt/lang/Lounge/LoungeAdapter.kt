package com.sopt.lang.Lounge

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Lounge.LoungeDetail.LoungeDetailActivity
import com.sopt.lang.LoungeImageActivity
import com.sopt.lang.MyPage.MyProfile.MyProfileChangeActivity
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity
import com.sopt.lang.Network.Lounge.LoungeLikeResponse
import com.sopt.lang.Network.Lounge.LoungeListData
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.sopt.lang.Chatting.ChatListAdapter.calculateTime
import java.text.SimpleDateFormat




/**
 * Created by sec on 2018-01-07.
 */
class LoungeAdapter (var dataList : List<LoungeListData>?, var ctx : Context?) : RecyclerView.Adapter<LoungeAdapter.LoungeViewHolder>() {

    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null
    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"


    var mLoungeViewHolder: LoungeViewHolder? = null
    override fun onBindViewHolder(holder: LoungeViewHolder?, position: Int) {
        if(dataList!!.get(position).user_image == null){
            holder!!.writer_pic.setImageResource(R.drawable.profile_default)
        }else {
            Glide.with(ctx).load(dataList!!.get(position).user_image).apply(RequestOptions.circleCropTransform()).into(holder!!.writer_pic)
        }
        networkService = GlobalApplication.instance!!.networkService

        holder!!.writer_name.setText(dataList!!.get(position).user_name)
        holder!!.writer_language.setText(dataList!!.get(position).native_lang)
        holder!!.writer_interest.setText(dataList!!.get(position).hope_lang)

        val resultTime = dataList!!.get(position).lounge_time
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val date1 = df.parse(resultTime)

        holder!!.uploaded_time.setText(calculateTime(date1).toString())
        holder!!.lounge_text.setText(dataList!!.get(position).lounge_content)
        holder!!.num_of_like.setText(dataList!!.get(position).like_count.toString())
        holder!!.num_of_comment.setText(dataList!!.get(position).comment_count.toString())


        if(dataList!!.get(position).isLike){
            holder!!.lounge_like.setImageResource(R.drawable.lounge_like_on)
        }else{
            holder!!.lounge_like.setImageResource(R.drawable.lounge_like_off)
        }


        if (dataList!!.get(position).lounge_image != null) {
            var count: Int = 0
            var size: Int = dataList!!.get(position).lounge_image!!.size
            for (i in 1..size) {
                count++
                if (count == 1) {
                    holder.img_scroll_view.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    holder.img_box.setPadding(dpTopx(16), 0, 0, dpTopx(16))
                }

                holder.imgs!![count - 1]!!.layoutParams.width = dpTopx(90)
                holder.imgs!![count - 1]!!.layoutParams.height = dpTopx(90)
                holder.imgs!![count - 1]!!.setPadding(0, 0, dpTopx(8), 0)
                holder.imgs!![count - 1]!!.requestLayout()
                Glide.with(ctx).load(dataList!!.get(position).lounge_image!![count - 1]).apply(RequestOptions().centerCrop()).into(holder!!.imgs!![count - 1]);
            }
        }   // 왜 여러번 로드되지

        holder.lounge_like.setOnClickListener {
            mLoungeLike(holder, position)
        }

        holder.lounge_text.setOnClickListener{
            mLoungeViewHolder!!.toDetailActivity(dataList!!.get(position).lounge_id, ctx)
        }

        holder.lounge_more.setOnClickListener {
            val alertdialog = AlertDialog.Builder(ctx!!)
            //다이얼로그의 내용을 설정합니다.
            alertdialog.setTitle("신고")
            alertdialog.setMessage("신고 하시겠습니까?")

            //확인 버튼
            alertdialog.setPositiveButton("확인") { dialog, which ->
                Toast.makeText(ctx, "확인", Toast.LENGTH_SHORT).show()
            }

            //취소 버튼
            alertdialog.setNegativeButton("취소") { dialog, which ->
                Toast.makeText(ctx, "취소", Toast.LENGTH_SHORT).show()
            }

            val alert = alertdialog.create()
            alert.show()
        }


        holder!!.lounge_comment!!.setOnClickListener {
            mLoungeViewHolder!!.toDetailActivity(dataList!!.get(position).lounge_id, ctx)
        }

        holder!!.writer_pic!!.setOnClickListener {
            mLoungeViewHolder!!.toProfileActivity(dataList!!.get(position).user_id, ctx)
        }

        for(i in 0..9){
            holder!!.imgs!![i]!!.setOnClickListener{
                mLoungeViewHolder!!.toImageView(dataList!!.get(position).lounge_image as ArrayList<String>, ctx, i)
            }
        }

    }

    fun dpTopx(dp: Int): Int {
        var density: Float = ctx!!.getResources().getDisplayMetrics().density
        return Math.round((dp.times(density)))
    }

    fun refreshAdapter(dataList : List<LoungeListData>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(l:View.OnClickListener){
        onItemClick = l
    }

    fun set_lounge_like_btn(holder: LoungeViewHolder?, position: Int) {
        Log.v("테스트","in set_lounge_like_btn")
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

    }

    fun mLoungeLike(holder: LoungeViewHolder?,position : Int){
        val loungeLikeResponge : Call<LoungeLikeResponse> = networkService!!.putLoungeLike(token, dataList!!.get(position).lounge_id)
        loungeLikeResponge.enqueue(object : Callback<LoungeLikeResponse>
        {
            override fun onFailure(call: Call<LoungeLikeResponse>?, t: Throwable?) {
                Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoungeLikeResponse>?, response: Response<LoungeLikeResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    set_lounge_like_btn(holder, position)
                }
            }

        })
    }

    private var onItemClick: View.OnClickListener? = null

    override fun getItemCount(): Int = dataList!!.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LoungeViewHolder{
        val loungeView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_lounge, parent, false)

        loungeView.setOnClickListener(onItemClick)

        mLoungeViewHolder = LoungeViewHolder(loungeView)

        return LoungeViewHolder(loungeView)
    }

    class LoungeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var writer_pic: ImageView = itemView!!.findViewById<ImageView>(R.id.lounge_writer_pic) as ImageView
        var writer_name: TextView = itemView!!.findViewById<TextView>(R.id.lounge_writer_name) as TextView
        var writer_language: TextView = itemView!!.findViewById<TextView>(R.id.lounge_writer_lang) as TextView
        var writer_interest: TextView = itemView!!.findViewById<TextView>(R.id.lounge_writer_interest) as TextView
        var uploaded_time: TextView = itemView!!.findViewById<TextView>(R.id.lounge_uploded_time) as TextView
        var lounge_text: TextView = itemView!!.findViewById<TextView>(R.id.lounge_expandable_text) as TextView
        // var expand_text_btn : Button = itemView!!.findViewById(R.id.lounge_) as Button
        var num_of_like: TextView = itemView!!.findViewById<TextView>(R.id.lounge_num_of_like) as TextView
        var num_of_comment: TextView = itemView!!.findViewById<TextView>(R.id.lounge_num_of_comment) as TextView

        var lounge_like: ImageView = itemView!!.findViewById<ImageView>(R.id.lounge_like) as ImageView
        var lounge_comment: ImageView = itemView!!.findViewById<ImageView>(R.id.lounge_comment) as ImageView
        var lounge_more: ImageView = itemView!!.findViewById<ImageView>(R.id.lounge_more) as ImageView
        // 동적으로 추가되는 이미지들은?

        var img_scroll_view: HorizontalScrollView = itemView!!.findViewById<HorizontalScrollView>(R.id.horiozontal_scroll) as HorizontalScrollView
        var img_box: LinearLayout = itemView!!.findViewById<LinearLayout>(R.id.lounge_update_image_box) as LinearLayout

        var img1 = itemView!!.findViewById<ImageView>(R.id.img1) as ImageView
        var img2 = itemView!!.findViewById<ImageView>(R.id.img2) as ImageView
        var img3 = itemView!!.findViewById<ImageView>(R.id.img3) as ImageView
        var img4 = itemView!!.findViewById<ImageView>(R.id.img4) as ImageView
        var img5 = itemView!!.findViewById<ImageView>(R.id.img5) as ImageView
        var img6 = itemView!!.findViewById<ImageView>(R.id.img6) as ImageView
        var img7 = itemView!!.findViewById<ImageView>(R.id.img7) as ImageView
        var img8 = itemView!!.findViewById<ImageView>(R.id.img8) as ImageView
        var img9 = itemView!!.findViewById<ImageView>(R.id.img9) as ImageView
        var img10 = itemView!!.findViewById<ImageView>(R.id.img10) as ImageView

        var imgs: Array<ImageView?>? = arrayOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10)

        override fun onClick(v: View?) {
            /*lounge_comment!!.setOnClickListener{
                var key : String = mLoungeAdapter.getKey(1)
                val intent = Intent(v!!.getContext(), LoungeDetailActivity::class.java)
                // putExtra로 글의 key 값 보내기
                *//*intent.putExtra("key", key)*//*
                v!!.getContext().startActivity(intent)
            }*/
        }

        fun toDetailActivity(key : Int?, ctx : Context?){
            val intent = Intent(ctx, LoungeDetailActivity::class.java)
            intent.putExtra("key", key)
            ctx!!.startActivity(intent)
        }

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

        fun toImageView(img_array : ArrayList<String>, ctx : Context?,  n : Int?){
            val intent = Intent(ctx, LoungeImageActivity::class.java)
            Log.v("test1105", "img_array size : "+img_array.size.toString())
            Log.v("test1105", "n : "+ n)
            intent.putStringArrayListExtra("img_array", img_array)
            intent.putExtra("current_img_num", n.toString())
            ctx!!.startActivity(intent)
        }

        fun calculateTime(date: Date): String {

            val curTime = System.currentTimeMillis()
            val regTime = date.time
            var diffTime = (curTime - regTime) / 1000

            var msg: String? = null

            if (diffTime < TIME_MAXIMUM.SEC) {
                // sec
                msg = "방금 전"
            } else if ((diffTime / TIME_MAXIMUM.SEC.toLong()) < TIME_MAXIMUM.MIN) {
                // min
                diffTime = diffTime / TIME_MAXIMUM.SEC
                println(diffTime)

                msg = diffTime.toString() + "분 전"
            } else if ((diffTime / TIME_MAXIMUM.MIN.toLong()) < TIME_MAXIMUM.HOUR) {
                // hour
                diffTime = diffTime / TIME_MAXIMUM.MIN
                msg = diffTime.toString() + "시간 전"
            } else if ((diffTime / TIME_MAXIMUM.HOUR.toLong()) < TIME_MAXIMUM.DAY) {
                // day
                diffTime = diffTime / TIME_MAXIMUM.HOUR

                msg = diffTime.toString() + "일 전"
            } else if ((diffTime / TIME_MAXIMUM.DAY.toLong()) < TIME_MAXIMUM.MONTH) {
                // day
                diffTime = diffTime / TIME_MAXIMUM.DAY

                msg = diffTime.toString() + "달 전"
            } else {
                msg = diffTime.toString() + "년 전"
            }

            return msg
        }

        private object TIME_MAXIMUM {
            val SEC = 60
            val MIN = 60
            val HOUR = 24
            val DAY = 30
            val MONTH = 12
        }

    }
}