package com.sopt.lang.Meeting.MeetingReview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.Network.MeetingReview.MeetingReviewList
import com.sopt.lang.Network.MeetingReview.MeetingReviewListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_meeting_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetingReviewActivity : AppCompatActivity() {

    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null

//    private var token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"

    //private var meeting_review_list: RecyclerView? = null
    private var meeting_review_datas: List<MeetingReviewList>? = null
    private var adapter: MeetingReviewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_review)

        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService

        var id : Int? = null
        id = getIntent().getIntExtra("key", 0)
        mMeetingReviewList(id)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.x)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        //meeting_review_list = findViewById<View>(R.id.recycler_view_review) as RecyclerView
        recycler_view_review!!.layoutManager = LinearLayoutManager(this)

        val review_write_btn = findViewById<View>(R.id.review_write) as ImageButton
        review_write_btn.setOnClickListener {
            val intent = Intent(
                    this,
                    ReviewWriteActivity::class.java)
            intent.putExtra("key", id!!)
            startActivity(intent)
        }
    }

    fun mMeetingReviewList(id : Int){
        val meetingReviewListResponse : Call<MeetingReviewListResponse> = networkService!!.getReviews(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, id)
        meetingReviewListResponse.enqueue(object : Callback<MeetingReviewListResponse>
        {
            override fun onFailure(call: Call<MeetingReviewListResponse>?, t: Throwable?) {
                Toast.makeText(this@MeetingReviewActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MeetingReviewListResponse>?, response: Response<MeetingReviewListResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    meeting_review_datas = response!!.body().data.reviewList
                    Log.v("test737", "num of review : " + response!!.body().data.reviewList.size.toString())
                    adapter = MeetingReviewAdapter(meeting_review_datas,this@MeetingReviewActivity)
                    recycler_view_review!!.adapter = adapter
                    setAvg(meeting_review_datas!!.size, response!!.body().data.average_rating)
                }else{
                    Toast.makeText(this@MeetingReviewActivity, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun setAvg(reviewSize : Int, average : String){
        review_rating_avg_text.setText("평균 평점(" + reviewSize.toString() + ")")
        review_rating_avg.setRating(average.toFloat())
        review_rating_avg_num.setText(average)
        review_rating_avg_toolbar.setRating(average.toFloat())
        review_rating_avg_num_toolbar.setText(average)
    }
}
