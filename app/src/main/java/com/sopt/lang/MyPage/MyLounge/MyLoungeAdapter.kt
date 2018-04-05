package com.sopt.lang.MyPage.MyLounge

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.Chatting.ChatListAdapter
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Lounge.LoungeDetail.LoungeDetailActivity
import com.sopt.lang.Lounge.LoungeWrite.LoungeWriteActivity
import com.sopt.lang.Network.Lounge.LoungeLikeResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by sec on 2018-01-08.
 */
class MyLoungeAdapter (var dataList : ArrayList<MyLoungeData>?, var ctx : Context?) : RecyclerView.Adapter<MyLoungeAdapter.MyLoungeHolder>() {
    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null
//    private var token : String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"
    var mMyLoungeViewHolder : MyLoungeHolder? = null

    override fun getItemCount(): Int = dataList!!.size

    fun refreshAdapter(dataList : ArrayList<MyLoungeData>){
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyLoungeHolder {
        val loungeView: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_mylounge, parent, false)

        networkService = GlobalApplication.instance!!.networkService
        SharedPreferencesService.instance!!.load(ctx!!)
        mMyLoungeViewHolder = MyLoungeHolder(loungeView)

        loungeView.setOnClickListener(onItemClick)
        return MyLoungeHolder(loungeView)
    }


    override fun onBindViewHolder(holder: MyLoungeHolder?, position: Int) {
        Glide.with(ctx).load(dataList!!.get(position).writer_pic).apply(RequestOptions.circleCropTransform()).into(holder!!.writer_pic)

        // holder!!.writer_pic.setImageResource(dataList!!.get(position).writer_pic)
        holder!!.writer_name.setText(dataList!!.get(position).writer_name)
        holder!!.writer_language.setText(dataList!!.get(position).writer_language)
        holder!!.writer_interest.setText(dataList!!.get(position).writer_interest)

        val resultTime = dataList!!.get(position).uploaded_time
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val date1 = df.parse(resultTime)

        holder!!.uploaded_time.setText(ChatListAdapter.calculateTime(date1).toString())

        holder!!.lounge_text.setText(dataList!!.get(position).lounge_text)
        holder!!.num_of_like.setText(dataList!!.get(position).num_of_like.toString())
        holder!!.num_of_comment.setText(dataList!!.get(position).num_of_comments.toString())

        if (dataList!!.get(position).uploaded_images != null) {
            var count: Int = 0
            var size: Int = dataList!!.get(position).uploaded_images!!.size
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
                Glide.with(ctx).load(dataList!!.get(position).uploaded_images!![count - 1]).apply(RequestOptions().centerCrop()).into(holder!!.imgs!![count - 1]);
            }
        }

        if(dataList!!.get(position).is_like_on){
            holder.lounge_like.setImageResource(R.drawable.lounge_like_on)
        }else{
            holder.lounge_like.setImageResource(R.drawable.lounge_like_off)
        }


        holder!!.lounge_like!!.setOnClickListener {
            mLoungeLike(holder, position)
        }
        holder!!.lounge_comment!!.setOnClickListener{
            mMyLoungeViewHolder!!.toDetailActivity(dataList!!.get(position).key, ctx!!)
        }
        holder!!.lounge_text!!.setOnClickListener{
            mMyLoungeViewHolder!!.toDetailActivity(dataList!!.get(position).key, ctx!!)
        }

        holder!!.lounge_more!!.setOnClickListener {
            dataList!!.removeAt(position)
            //holder!!.itemView.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
        holder!!.lounge_change.setOnClickListener{
            //라운지 수정하기, 수정할 때 정보 다 가져가서 뿌려주기
            val intent = Intent(ctx!!, LoungeWriteActivity::class.java)
            // putExtra로 글의 key 값 보내기zx
            ctx!!.startActivity(intent)

        }

    }
    fun mLoungeLike(holder: MyLoungeHolder?, position : Int){
        val loungeLikeResponge : Call<LoungeLikeResponse> = networkService!!.putLoungeLike(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, dataList!!.get(position).key)
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

    fun set_lounge_like_btn(holder: MyLoungeHolder?, position: Int) {
        Log.v("테스트", "in set_lounge_like_btn")
        if (dataList!!.get(position).is_like_on) {
            Log.v("테스트", "on to off")
            dataList!!.get(position).is_like_on = false
            holder!!.lounge_like.setImageResource(R.drawable.lounge_like_off)
            dataList!!.get(position).num_of_like--
        } else {
            Log.v("테스트", "off to on")
            dataList!!.get(position).is_like_on = true
            holder!!.lounge_like.setImageResource(R.drawable.lounge_like_on)
            dataList!!.get(position).num_of_like++
        }
        holder.num_of_like.setText(dataList!!.get(position).num_of_like.toString())
    }

    fun dpTopx(dp : Int) : Int{
        var density : Float = ctx!!.getResources().getDisplayMetrics().density
        return Math.round((dp.times(density)))
    }

    //여기 주석도 필요합니다.
//    fun mLoungeLike(holder: LoungeAdapter.LoungeViewHolder?, position: Int) {
//        val loungeLikeResponge: Call<LoungeLikeResponse> = networkService!!.putLoungeLike(token, dataList!!.get(position).key)
//        loungeLikeResponge.enqueue(object : Callback<LoungeLikeResponse> {
//            override fun onFailure(call: Call<LoungeLikeResponse>?, t: Throwable?) {
//                Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(call: Call<LoungeLikeResponse>?, response: Response<LoungeLikeResponse>?) {
//                if (response!!.body().status.equals("sucess")) {
//                    set_lounge_like_btn(holder, position)
//                }
//            }
//
//        })
//    }

    private var onItemClick: View.OnClickListener? = null
    fun setOnItemClickListener(l: View.OnClickListener) {
        onItemClick = l
    }

    fun delete(position: Int) { //removes the row
        dataList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Created by johee on 2018-01-04.
     */
    class MyLoungeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var writer_pic: ImageView = itemView!!.findViewById(R.id.lounge_writer_pic)
        var writer_name: TextView = itemView!!.findViewById(R.id.lounge_writer_name)
        var writer_language: TextView = itemView!!.findViewById(R.id.lounge_writer_lang)
        var writer_interest: TextView = itemView!!.findViewById(R.id.lounge_writer_interest)
        var uploaded_time: TextView = itemView!!.findViewById(R.id.lounge_uploded_time)
        var lounge_text: TextView = itemView!!.findViewById(R.id.lounge_expandable_text)
        // var expand_text_btn : Button = itemView!!.findViewById(R.id.lounge_) as Button
        var num_of_like: TextView = itemView!!.findViewById(R.id.lounge_num_of_like)
        var num_of_comment: TextView = itemView!!.findViewById(R.id.lounge_num_of_comment)

        var lounge_like: ImageView = itemView!!.findViewById(R.id.lounge_like)
        var lounge_comment: ImageView = itemView!!.findViewById(R.id.lounge_comment)
        var lounge_more: ImageView = itemView!!.findViewById(R.id.lounge_delete)
        var lounge_change :ImageView = itemView!!.findViewById(R.id.lounge_change)

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
        }

        fun toDetailActivity(key: Int?, ctx: Context?) {
            val intent = Intent(ctx, LoungeDetailActivity::class.java)
            intent.putExtra("key", key)
            ctx!!.startActivity(intent)
        }

        fun toProfileActivity(key: Int?, ctx: Context?) {
/*            val intent = Intent(ctx, TestActivity::class.java)
            intent.putExtra("key", key)
            ctx!!.startActivity(intent)*/
        }
    }
}