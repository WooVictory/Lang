package com.sopt.lang.Lounge.LoungeDetail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity
import com.sopt.lang.Network.Lounge.*
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_lounge_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoungeDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var networkService: NetworkService? = null
    private var requestManager: RequestManager? = null

//    private var token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    var num_of_like : Int? = null
    var num_of_comment : Int? = null

    private var lounge_comment_datas: List<LoungeDetailComment>? = null
    private var adapter: LoungeCommentAdapter? = null

    var detail_isLike : Boolean? = null

    var lounge_writer_id : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lounge_detail)

        SharedPreferencesService.instance!!.load(this)

        networkService = GlobalApplication.instance!!.networkService

        var key: Int? = null    //lounge_id
        key = getIntent().getIntExtra("key", 0)

        Log.v("test704", "lounge id : " + key.toString())

        mLoungeDetail(key, this)

        Log.v("테스트1146","got out: " + key)

        lounge_detail_writer_pic!!.setOnClickListener{
            val intent = Intent(applicationContext, YourProfileActivity::class.java)
            intent.putExtra("key", lounge_writer_id)
            startActivity(intent)
        }

        lounge_detail_like!!.setOnClickListener {
            mLoungeLike(key!!, this)
        }

        lounge_detail_update_comment!!.setOnClickListener {
            // 댓글 서버에 올리기
            if (lounge_detail_type_comment!!.getText().toString().equals("")) {
                Toast.makeText(this, "댓글을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                mLoungeCommentPosting(key!!, lounge_detail_type_comment!!.getText().toString(), this)
            }
        }
    }

    fun mLoungeCommentPosting(id : Int, content : String, ctx: Context){
        val loungeCommentPostingResponse: Call<LoungeCommentPostingResponse> = networkService!!.postLoungeCommentPosting(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, id, content)

        loungeCommentPostingResponse.enqueue(object : Callback<LoungeCommentPostingResponse>
        {
            override fun onFailure(call: Call<LoungeCommentPostingResponse>?, t: Throwable?) {
                Log.v("test509", "in onFailure fail")
                Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<LoungeCommentPostingResponse>?, response: Response<LoungeCommentPostingResponse>?) {
                if (response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)) {
//                    val intent = Intent(applicationContext, LoungeDetailActivity::class.java)
//                    intent.putExtra("key", id)
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
                    Log.v("test509", "in onResponse")
                    mLoungeDetail(id!!, this@LoungeDetailActivity)
                    lounge_detail_type_comment!!.setText(null)
                    if(this != null){   // 키보드 내리기
                        var imm : InputMethodManager = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(lounge_detail_type_comment.getWindowToken(),0);
                    }
                } else {
                    Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("test509", "in onResponse fail")
                }
            }
        })
    }

    fun mLoungeDetail(key: Int, ctx: Context) {
        Log.v("test509", "mLoungeDetail key : " + key.toString())
        val loungeDetailResponse: Call<LoungeDetailResponse> = networkService!!.getLoungeDetail(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, key)

        loungeDetailResponse.enqueue(object : Callback<LoungeDetailResponse>
        {
            override fun onFailure(call: Call<LoungeDetailResponse>?, t: Throwable?) {
                Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
                Log.v("test509", "mLoungeDetail in onFailure" + t.toString())  //**
            }
            override fun onResponse(call: Call<LoungeDetailResponse>?, response: Response<LoungeDetailResponse>?) {
                if (response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)) {
                    lounge_writer_id = response!!.body().data!!.user_id
                    setLoungeDetailData(response!!.body().data!!)
                    Log.v("test509", "mLoungeDetail in onResponse")
                } else {
                    Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
                    Log.v("test509", "mLoungeDetail in onResponse fail")
                }
            }
        })
    }

    fun setLoungeDetailData(mLoungeDetailData : LoungeDetailData){
        Glide.with(this).load(mLoungeDetailData!!.user_image).apply(RequestOptions.circleCropTransform()).into(lounge_detail_writer_pic)

        lounge_detail_writer_name.setText(mLoungeDetailData!!.user_name)
        lounge_detail_uploded_time!!.setText(mLoungeDetailData!!.lounge_time)

        lounge_detail_writer_lang!!.setText(mLoungeDetailData!!.native_lang)
        lounge_detail_writer_interest!!.setText(mLoungeDetailData!!.hope_lang)

        lounge_detail_expandable_text!!.setText(mLoungeDetailData!!.lounge_content)


        if (mLoungeDetailData!!.lounge_image != null) {
            var imgs: Array<ImageView?>? = arrayOf(detail_img1, detail_img2, detail_img3, detail_img4, detail_img5, detail_img6, detail_img7, detail_img8, detail_img9, detail_img10)

            var count: Int = 0
            var size: Int = mLoungeDetailData!!.lounge_image!!.size
            for (i in 1..size) {
                count++
                if (count == 1) {
                    horiozontal_scroll.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    lounge_update_image_box.setPadding(dpTopx(16), 0, 0, dpTopx(16))
                }

                imgs!![count - 1]!!.layoutParams.width = dpTopx(90)
                imgs!![count - 1]!!.layoutParams.height = dpTopx(90)
                imgs!![count - 1]!!.setPadding(0, 0, dpTopx(8), 0)
                imgs!![count - 1]!!.requestLayout()
                Glide.with(this).load( mLoungeDetailData!!.lounge_image!![count - 1]).apply(RequestOptions().centerCrop()).into(imgs!![count - 1]);
            }
        }

        lounge_detail_num_of_like!!.setText(mLoungeDetailData!!.like_count.toString())

        if (mLoungeDetailData!!.isLike) {
            detail_isLike = true
            lounge_detail_like!!.setImageResource(R.drawable.lounge_like_on)
        } else {
            detail_isLike = false
            lounge_detail_like!!.setImageResource(R.drawable.lounge_like_off)
        }

        lounge_detail_num_of_comment!!.setText(mLoungeDetailData!!.comment_count.toString())
        lounge_detail_num_of_comment_s!!.setText(mLoungeDetailData!!.comment_count.toString())

        num_of_like = mLoungeDetailData!!.like_count
        num_of_comment = mLoungeDetailData!!.comment_count

        var size = mLoungeDetailData!!.comment_count
        Log.v("테스트0159","size : " + size)

        if(size!=0){
            Log.v("테스트0159","size != 0")
            lounge_detail_comment_list!!.layoutManager = LinearLayoutManager(this)


            Log.v("test449", "comment_count :" + mLoungeDetailData!!.comment_count.toString())
            Log.v("test449", "comment_size :" + mLoungeDetailData!!.comments!!.size.toString())
            lounge_comment_datas = mLoungeDetailData!!.comments

            lounge_detail_comment_list!!.setNestedScrollingEnabled(false)

            adapter = LoungeCommentAdapter(lounge_comment_datas!!,this@LoungeDetailActivity)
            lounge_detail_comment_list!!.adapter = adapter

            adapter!!.refreshAdapter(lounge_comment_datas!!)
        }
    }

    fun dpTopx(dp: Int): Int {
        var density: Float = this!!.getResources().getDisplayMetrics().density
        return Math.round((dp.times(density)))
    }

    fun mLoungeLike(key : Int, ctx : Context){
        val loungeLikeResponge : Call<LoungeLikeResponse> = networkService!!.putLoungeLike(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, key)
        loungeLikeResponge.enqueue(object : Callback<LoungeLikeResponse>
        {
            override fun onFailure(call: Call<LoungeLikeResponse>?, t: Throwable?) {
                Toast.makeText(ctx, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoungeLikeResponse>?, response: Response<LoungeLikeResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    set_lounge_detail_like_btn()
                }
            }

        })
    }


    fun set_lounge_detail_like_btn() {
        if (detail_isLike!!) {
            lounge_detail_like!!.setImageResource(R.drawable.lounge_like_off)
            num_of_like = num_of_like!!.minus(1)
            detail_isLike = false
            lounge_detail_num_of_like!!.setText(num_of_like.toString())
        } else {
            lounge_detail_like!!.setImageResource(R.drawable.lounge_like_on)
            num_of_like = num_of_like!!.plus(1)
            detail_isLike = true
            lounge_detail_num_of_like!!.setText(num_of_like.toString())
        }
    }

    override fun onClick(v: View?) {/*
        val idx: Int = lounge_comment_list!!.getChildAdapterPosition(v)
        val name: String? = lounge_comment_datas!!.get(idx).writer_name

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()*/
    }
}