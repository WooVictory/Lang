package com.sopt.lang.MyPage.Follow.YourFollow

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.sopt.lang.HomeFragment.LoungeFragment
import com.sopt.lang.Login.kakao.GlobalApplication
import com.sopt.lang.MyPage.YourProfile.YourProfileActivity
import com.sopt.lang.Network.MyPage.OtherFollowerListData
import com.sopt.lang.Network.MyPage.OtherFollowerListResponse
import com.sopt.lang.Network.NetworkService
import com.sopt.lang.R
import com.sopt.lang.SharedPreferencesService
import kotlinx.android.synthetic.main.activity_profile_follower.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourProfileFollowerActivity : AppCompatActivity(), View.OnClickListener {

    //private var follower_list : RecyclerView? = null
    private var follower_datas : List<OtherFollowerListData>? = null
    private var adapter : YourProfileFollowerAdapter? = null

    private var networkService: NetworkService? = null
    private var requestManager : RequestManager? = null
//    private var token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJrZXkxIjoiOE4rbHR3ZGpISFNETlA3WElmQjAzSHcxbWNVbnRVUUdTcE8ydWVvT21qdWY3Y0ZScGFxbHBTQS85djNYM2U2dlZmbnN1LzlvTDFKWGFoS0g1YURKT1E9PSIsImlhdCI6MTUxNTQ0MjA2MSwiZXhwIjoxNTE4MDM0MDYxfQ.KLCwFKDX3JutkqsJzkJf8ZMx_2xtWQBNE4RIYhBSq1w"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_follower)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.x)
        toolbar.setNavigationOnClickListener { finish() }

        SharedPreferencesService.instance!!.load(this)
        networkService = GlobalApplication.instance!!.networkService


        var otherUserId : String? = null
        otherUserId = getIntent().getStringExtra("key")

        if(otherUserId == YourProfileFollowingActivity.IS_ME){
        }else{
            mYourFollowerList(otherUserId)
        }

        follower_list!!.layoutManager = LinearLayoutManager(this)


        /*
        adapter = ProfileFollowerAdapter(follower_datas, this)
        follower_list!!.adapter = adapter*/
    }

    fun mYourFollowerList(otherUserId : String){
        val yourFollowerListResponse : Call<OtherFollowerListResponse> = networkService!!.postOtherFollowerList(SharedPreferencesService.instance!!.getPrefStringData("token", "")!!, otherUserId)
        yourFollowerListResponse.enqueue(object : Callback<OtherFollowerListResponse>
        {
            override fun onFailure(call: Call<OtherFollowerListResponse>?, t: Throwable?) {
                Toast.makeText(this@YourProfileFollowerActivity, "실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<OtherFollowerListResponse>?, response: Response<OtherFollowerListResponse>?) {
                if(response!!.body().status.equals(LoungeFragment.NETWORK_SUCCESS)){
                    follower_datas = response!!.body().data
                    adapter = YourProfileFollowerAdapter(follower_datas, this@YourProfileFollowerActivity)
                    follower_list!!.adapter = adapter
                }else{
                    Toast.makeText(this@YourProfileFollowerActivity, "실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        val idx : Int = follower_list!!.getChildAdapterPosition(v)
        val key : String? = follower_datas!!.get(idx).user_id

        //일단 각각의 리사이클러뷰 선택시 페이지로 이동하게 해놓았음
        val intent = Intent(this, YourProfileActivity::class.java)
        intent.putExtra("key", key)
        startActivity(intent)
    }
}
